package io.github.cshu.stwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.*;
import org.springframework.kafka.config.*;
import org.springframework.kafka.core.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Bean;
import org.apache.kafka.clients.admin.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.common.primitives.*;
import info.debatty.java.stringsimilarity.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.io.*;
import java.util.*;

@SpringBootApplication
public class StworkApplication {

  @Autowired private KafkaTemplate<String, String> kafkaTemplate;

  public static void main(String[] args) {
    SpringApplication.run(StworkApplication.class, args);
  }

  @Bean
  public NewTopic topic() {
    return TopicBuilder.name("new").partitions(10).replicas(1).build();
  }

  @KafkaListener(id = "newListen", topics = "new")
  public void listen(String in) {
    System.out.println(in);
    Gson gson = new Gson();
    Similarity sim = gson.fromJson(in, Similarity.class);
    try {
      final String textPathPrefix = "/tmp/st/text/";
      var files = (new File(textPathPrefix)).listFiles();
      var hashinhexfnm = textPathPrefix + sim.hash();
      (new File(hashinhexfnm + "/result")).mkdirs();
      byte[] blob = Files.readAllBytes(Paths.get(hashinhexfnm + "/data"));
      Random rand = new Random();
      final int haystackLen = 500; // fixme magic number
      final int needleLen = 50; // fixme magic number
      final int thresholdNeedleMatch = 2; // fixme magic number
      final double thresholdForSim = 0.5; // fixme magic number
      for (var otherfile : files) {
        if (otherfile.getName().equals(sim.hash())) continue;
        int simCount = 0;
        var other = Files.readAllBytes(otherfile.toPath().resolve("data"));
        double strsim;
        if (other.length <= haystackLen || blob.length <= haystackLen) {
          strsim = strsimilarity(blob, other);
        } else {
          strsim = 0D;
          for (int off = 0; off < other.length; off += haystackLen) {
            int end = Math.min(haystackLen, other.length - off) - needleLen;
            if (end <= 0) break;
            int idx = off + rand.nextInt(end);
            if (-1 == Bytes.indexOf(blob, Arrays.copyOfRange(other, idx, idx + needleLen)))
              continue;
            simCount++;
            if (simCount == thresholdNeedleMatch) {
              strsim = strsimilarity(blob, other);
              break;
            }
          }
        }
        if (strsim > thresholdForSim) {
          var strsimText = String.valueOf(strsim).getBytes(StandardCharsets.UTF_8);
          Files.write(Paths.get(hashinhexfnm + "/result/" + otherfile.getName()), strsimText);
          Files.write(
              Paths.get(textPathPrefix + otherfile.getName() + "/result/" + sim.hash()),
              strsimText);
        }
      }
      kafkaTemplate.send("finished", gson.toJson(sim));
    } catch (Exception e) {
      e.printStackTrace(System.err);
      // fixme notify user?
      return;
    }
  }

  double strsimilarity(byte[] s1, byte[] s2) {
    JaroWinkler jw = new JaroWinkler();
    return jw.similarity(
        new String(s1, StandardCharsets.UTF_8), new String(s2, StandardCharsets.UTF_8));
  }
}
