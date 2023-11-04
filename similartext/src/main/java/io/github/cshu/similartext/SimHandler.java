package io.github.cshu.similartext;

import org.springframework.core.io.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.*;

import org.springframework.kafka.core.*;
import org.springframework.beans.factory.annotation.*;

import reactor.core.publisher.Mono;
import reactor.core.publisher.*;

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

        // Extract the request body as a Flux of DataBuffer
        var requestBody = request.body(BodyExtractors.toDataBuffers());

        // Create a Flux of bytes by reading the DataBuffer
        Flux<byte[]> bytesFlux = requestBody
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    return bytes;
                });

        // You can process the bytes here, for example, log them
        bytesFlux
                .doOnNext(bytes -> {
                    String bodyPart = new String(bytes);
                    System.out.println("Received bytes: " + bodyPart);
                })
                .subscribe(); // Subscribe to start processing

////var fluxdatabuf = request.body(BodyExtractors.toDataBuffers());
////fluxdatabuf.subscribe(value -> {
////try (var instre = value.asInputStream(true)){
////var blob = instre.readAllBytes();
////System.out.println(blob.length);
////}catch(Exception ex){}
////});
//fluxdatabuf.subscribe()
////if (false)
////Thread.ofVirtual().start(() -> {
////var coll = fluxdatabuf.buffer();
////    var databuf = coll.blockFirst().get(0);//.block();
////try{
////kafkaTemplate.send("finished", ""+sid+""+filename+"");//
////
//////byte[] blob = bar.getContentAsByteArray();
////byte[] blob;
////try (var instre = databuf.asInputStream(true)){
////blob = instre.readAllBytes();
////}
////MessageDigest digest = MessageDigest.getInstance("SHA-256");
////byte[] hash = digest.digest(blob);
////var sb = new StringBuilder();
////for (byte octet : hash) sb.append(String.format("%02x", octet));
////var hashinhex = sb.toString();
////var hashinhexfnm = "/tmp/st/text/"+hashinhex;
////var hashdir = new File(hashinhexfnm);
//////if (hashdir.exists())
////if(!hashdir.mkdirs())
////{
////    //undone send event to kafka
////    if ((new File(hashinhexfnm+"/result")).exists()) {
////    } else {
////    }
////    ////return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON)
////    ////  .body(BodyInserters.fromValue("{}"));
////    //return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
////    //  .body(BodyInserters.fromValue("{}"));
////}
//////(Paths.get(hashinhexfnm+"/sids"), sid.getBytes(StandardCharsets.UTF_8));
////Files.write(Paths.get(hashinhexfnm+"/data"), blob);
//////Files.write(Paths.get(hashinhexfnm+"/fnm"), filename.getBytes(StandardCharsets.UTF_8));
////    //return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
////    //  .body(BodyInserters.fromValue("{}"));
////}catch(Exception ex) {
////    System.err.println(ex.toString());
////    //return ServerResponse.status(500).contentType(MediaType.APPLICATION_JSON)
////    //  .body(BodyInserters.fromValue("{}"));
////}
////});
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue("{}"));
  }
}
