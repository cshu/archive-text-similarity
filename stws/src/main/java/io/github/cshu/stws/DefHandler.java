package io.github.cshu.stws;

import org.springframework.web.socket.handler.*;
import org.springframework.web.socket.*;

import org.springframework.kafka.core.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.messaging.simp.*;

import java.security.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.io.*;
import java.util.*;

import com.google.gson.*;

public class DefHandler extends TextWebSocketHandler {

  // @Autowired
  // private SimpMessagingTemplate msgSender;
  @Autowired private KafkaTemplate<String, String> kafkaTemplate;

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    // var sid = session.getId();
    // session.sendMessage(new TextMessage(sid));
  }

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    try {
      var sbforid = new StringBuilder();
      for (byte octet : session.getId().getBytes(StandardCharsets.UTF_8))
        sbforid.append(String.format("%02x", octet));
      var idinhex = sbforid.toString();
      var datafilename = Paths.get(partialPathPrefix + idinhex);
      switch (message.getPayload()) {
        case "BEGIN":
          Files.deleteIfExists(datafilename);
          session.sendMessage(new TextMessage("CONT."));
          break;
        case "END":
          var blob = Files.readAllBytes(datafilename);
          MessageDigest digest = MessageDigest.getInstance("SHA-256");
          byte[] hash = digest.digest(blob);
          var sb = new StringBuilder();
          for (byte octet : hash) sb.append(String.format("%02x", octet));
          var hashinhex = sb.toString();
          var hashinhexfnm = textPathPrefix + hashinhex;
          var hashdir = new File(hashinhexfnm);
          // if (hashdir.exists())
          if (hashdir.mkdirs()) {
            // (Paths.get(hashinhexfnm+"/sids"), sid.getBytes(StandardCharsets.UTF_8));
            datafilename
                .toFile()
                .renameTo(new File(hashinhexfnm + "/data")); // ?use Files.move is better?
            // Files.write(Paths.get(hashinhexfnm + "/data"), blob);
            // Files.write(Paths.get(hashinhexfnm+"/fnm"),
            // filename.getBytes(StandardCharsets.UTF_8));
          }
          session.sendMessage(new TextMessage(hashinhex));
          break;
      }
    } catch (Exception e) {
      e.printStackTrace(System.err);
      // fixme warn user
      return;
    }
  }

  @Override
  public void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
    var bytebuf = message.getPayload();
    var blob = new byte[bytebuf.remaining()];
    bytebuf.get(blob);
    try {
      var sbforid = new StringBuilder();
      for (byte octet : session.getId().getBytes(StandardCharsets.UTF_8))
        sbforid.append(String.format("%02x", octet));
      var idinhex = sbforid.toString();
      (new File(partialPathPrefix)).mkdirs();
      var datafilename = Paths.get(partialPathPrefix + idinhex);
      Files.write(datafilename, blob, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
      session.sendMessage(new TextMessage("CONT."));
    } catch (Exception e) {
      e.printStackTrace(System.err);
      // fixme warn user
      return;
    }
  }
  static final String textPathPrefix = "/tmp/st/text/";
  static final String partialPathPrefix = "/tmp/st/partial/";
}
