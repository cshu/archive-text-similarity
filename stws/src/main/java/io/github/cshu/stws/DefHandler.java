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

import com.google.gson.*;

public class DefHandler extends TextWebSocketHandler {

    //@Autowired
    //private SimpMessagingTemplate msgSender;
  @Autowired private KafkaTemplate<String, String> kafkaTemplate;

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    // var sid = session.getId();
    // session.sendMessage(new TextMessage(sid));
  }

  //@Override
  //public void handleTextMessage(WebSocketSession session, TextMessage message) {
  //  // System.out.println(message.getPayload());
  //  Gson gson = new Gson();
  //  DefMsg msg = gson.fromJson(message.getPayload(), DefMsg.class);
  //  if ("similarity".equals(msg.action)) {
  //    if (!msg.hash.matches("[A-Za-z0-9]+")) {
  //      // fixme do something about malicious request?
  //      //session.close();
  //      return;
  //    }
  //    var hashinhexfnm = "/tmp/st/text/" + msg.hash;
  //    // var hashdir = new File(hashinhexfnm);
  //    if ((new File(hashinhexfnm + "/result")).exists()) {
  //      // undone send result to user
  //    } else {
  //      //kafkaTemplate.send("new", msg.hash);
  //    }
  //      kafkaTemplate.send("finished", session.getId());
  //    // System.out.println(msg.name);
  //    // System.out.println(msg.hash);
  //    // fixme msg.name should be saved to some kind of data store
  //  }
  //}

  @Override
  public void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
    // System.out.println(message.getPayload());
    var bytebuf = message.getPayload();
    var blob = new byte[bytebuf.remaining()];
    bytebuf.get(blob);
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(blob);
      var sb = new StringBuilder();
      for (byte octet : hash) sb.append(String.format("%02x", octet));
      var hashinhex = sb.toString();
      var hashinhexfnm = "/tmp/st/text/" + hashinhex;
      var hashdir = new File(hashinhexfnm);
      // if (hashdir.exists())
      if (hashdir.mkdirs()) {
        // (Paths.get(hashinhexfnm+"/sids"), sid.getBytes(StandardCharsets.UTF_8));
        Files.write(Paths.get(hashinhexfnm + "/data"), blob);
        // Files.write(Paths.get(hashinhexfnm+"/fnm"), filename.getBytes(StandardCharsets.UTF_8));
      }
      session.sendMessage(new TextMessage(hashinhex));
    } catch (Exception e) {
      e.printStackTrace(System.err);
      // fixme warn user
    }
  }
}
