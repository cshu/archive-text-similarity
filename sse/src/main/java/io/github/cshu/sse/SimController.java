package io.github.cshu.sse;

import java.io.*;

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
		System.out.println(similarity.id());
		System.out.println(similarity.hash());
      var hashinhexfnm = "/tmp/st/text/" + similarity.hash();
      //var hashdir = new File(hashinhexfnm);
      if ((new File(hashinhexfnm + "/result")).exists()) {
		//undone make a response with content
		return new SimResp("OK");
      }
	Gson gson = new Gson();
      	kafkaTemplate.send("new", gson.toJson(similarity));
		return new SimResp("OK");
	}
}
