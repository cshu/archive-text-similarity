package io.github.cshu.similartext;

import org.springframework.core.io.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import org.springframework.kafka.core.*;
import org.springframework.beans.factory.annotation.*;

import reactor.core.publisher.Mono;

import java.security.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.io.*;

@Component
public class SimHandler {
  private KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  SimHandler(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public Mono<ServerResponse> newtext(ServerRequest request) {
    var hdrs = request.headers();
    String sid = hdrs.firstHeader("X-arg-sid");
    String filename = hdrs.firstHeader("X-arg-filename");
Thread.ofVirtual().start(() -> {
    ByteArrayResource bar = request.bodyToMono(ByteArrayResource.class).block();
try{
kafkaTemplate.send("finished", ""+sid+""+filename+"");//

byte[] blob = bar.getContentAsByteArray();
MessageDigest digest = MessageDigest.getInstance("SHA-256");
byte[] hash = digest.digest(blob);
var sb = new StringBuilder();
for (byte octet : hash) sb.append(String.format("%02x", octet));
var hashinhex = sb.toString();
var hashinhexfnm = "/tmp/st/text/"+hashinhex;
var hashdir = new File(hashinhexfnm);
//if (hashdir.exists())
if(!hashdir.mkdirs())
{
    //undone send event to kafka
    if ((new File(hashinhexfnm+"/result")).exists()) {
    } else {
    }
    ////return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON)
    ////  .body(BodyInserters.fromValue("{}"));
    //return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
    //  .body(BodyInserters.fromValue("{}"));
}
//(Paths.get(hashinhexfnm+"/sids"), sid.getBytes(StandardCharsets.UTF_8));
Files.write(Paths.get(hashinhexfnm+"/data"), blob);
//Files.write(Paths.get(hashinhexfnm+"/fnm"), filename.getBytes(StandardCharsets.UTF_8));
    //return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
    //  .body(BodyInserters.fromValue("{}"));
}catch(Exception ex) {
    System.err.println(ex.toString());
    //return ServerResponse.status(500).contentType(MediaType.APPLICATION_JSON)
    //  .body(BodyInserters.fromValue("{}"));
}
});
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue("{}"));
  }
}
