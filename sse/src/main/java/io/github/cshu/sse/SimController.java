package io.github.cshu.sse;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;

import org.springframework.kafka.core.*;
import org.springframework.beans.factory.annotation.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.*;

@RestController
public class SimController {

  @Autowired private KafkaTemplate<String, String> kafkaTemplate;

  @PostMapping("/sim")
  public SimResp sim(@RequestBody Similarity similarity) {
    try{
    System.out.println(similarity.id());
    System.out.println(similarity.hash());
      if (!similarity.hash().matches("[A-Za-z0-9]+")) {
      	// fixme do something about malicious request?
	return new SimResp("ERROR");
      }
    var hashinhexfnm = "/tmp/st/text/" + similarity.hash();
    Files.write(Paths.get(hashinhexfnm+"/name"), similarity.name().getBytes(StandardCharsets.UTF_8));
    // var hashdir = new File(hashinhexfnm);
    Gson gson = new Gson();
    if ((new File(hashinhexfnm + "/result")).exists()) {
      Util.sendResultToUser(similarity, gson);
      return new SimResp("OK");
    }
    kafkaTemplate.send("new", gson.toJson(similarity));
    return new SimResp("OK");
    }catch(Exception e){
    	e.printStackTrace(System.err);
	return new SimResp("Unexpected error occurred");
    }
  }
}
