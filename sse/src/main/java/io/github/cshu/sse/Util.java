package io.github.cshu.sse;

import java.nio.charset.*;
import java.nio.file.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class Util {
  public static final java.util.Base64.Encoder base64Encoder = java.util.Base64.getEncoder();
  public static final java.security.SecureRandom secureRandom = new java.security.SecureRandom();
  public static final ConcurrentHashMap<String, EmitterWithSeq> liveSse = new ConcurrentHashMap<>();

  public static String mkToken() {
    byte[] randomBytes = new byte[32];
    secureRandom.nextBytes(randomBytes);
    return base64Encoder.encodeToString(randomBytes);
  }

  // note here I am using Factory Pattern
  public static SseEmitter mkSseEmitter(String sseid) {
    // Gson gson = new Gson();
    // var sseid = mkToken();
    // var newsse = new NewSseid();
    // newsse.id = sseid;
    // var msg = gson.toJson(newsse);
    SseEmitter emitter = new SseEmitter(900_000L);
    var ews = new EmitterWithSeq(emitter, System.currentTimeMillis());
    emitter.onCompletion(
        () -> {
          liveSse.remove(sseid, ews);
        });
    emitter.onTimeout(
        () -> {
          liveSse.remove(sseid, ews);
        });
    // try{
    // emitter.send(SseEmitter.event().name("message").data(msg));
    // }catch(IOException e){
    // emitter.completeWithError(e);
    // }
    liveSse.put(sseid, ews);
    return emitter;
  }

  public static String readNameOfText(String hash) throws IOException {
    var namefile = Paths.get("/tmp/st/text/" + hash + "/name");
    return new String(Files.readAllBytes(namefile), StandardCharsets.UTF_8);
  }

  public static String readSimResult(String hash, String name, Gson gson) {
    try {
      var resFiles = new File("/tmp/st/text/" + hash + "/result").listFiles();
      var lst = new ArrayList<SimPair>();
      for (var resFile : resFiles) {
        var similar =
            Double.parseDouble(
                new String(Files.readAllBytes(resFile.toPath()), StandardCharsets.UTF_8));
        var other = resFile.getName();
        var namefile = Paths.get("/tmp/st/text/" + other + "/name");
        if (!namefile.toFile().isFile()) continue;
        var fnm = new String(Files.readAllBytes(namefile), StandardCharsets.UTF_8);
        lst.add(new SimPair(other, fnm, similar));
      }
      var retval = new SimResult("", hash, name, lst, "sim");
      return gson.toJson(retval);
    } catch (IOException e) {
      e.printStackTrace(System.err);
      return mkErrorSimResult(
          "Unexpected error occurred during file comparison.", hash, name, gson);
    }
  }

  public static void sendResultToUser(String in) {
    Gson gson = new Gson();
    Similarity sim = gson.fromJson(in, Similarity.class);
    sendResultToUser(sim, gson);
  }

  public static void sendResultToUser(Similarity sim, Gson gson) {
    final String simresult = readSimResult(sim.hash(), sim.name(), gson);
    var ews = liveSse.get(sim.id());
    if (null == ews) {
      Thread thread =
          Thread.ofVirtual()
              .start(
                  () -> {
                    // note Virtual Threads don’t block when sleep()!!! OS thread is still avail
                    // like goroutine!!!
                    try {
                      Thread.sleep(
                          5000); // give it a second chance due to possible intermittent connection
                      // of user
                    } catch (InterruptedException e) {
                      e.printStackTrace(System.err);
                    }
                    var ewsretry = liveSse.get(sim.id());
                    if (null == ewsretry) return;
                    sendResultToUser(ewsretry, simresult);
                  });
      return;
    }
    sendResultToUser(ews, simresult);
  }

  public static void sendResultToUser(EmitterWithSeq ews, String simresult) {
    SseEmitter emitter = ews.emitter;
    try {
      emitter.send(SseEmitter.event().name("message").data(simresult));
    } catch (IOException e) {
      e.printStackTrace(System.err);
      // todo write to db for user to view later
    }
  }

  public static String mkErrorSimResult(String msg, String hash, String name, Gson gson) {
    return gson.toJson(new SimResult(msg, hash, name, new ArrayList<SimPair>(0), "sim"));
  }
}
