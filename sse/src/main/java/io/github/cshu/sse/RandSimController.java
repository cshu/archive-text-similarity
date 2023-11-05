package io.github.cshu.sse;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

import org.springframework.kafka.core.*;
import org.springframework.beans.factory.annotation.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.*;

@RestController
public class RandSimController {

  @PostMapping("/rsim")
  public SimResp sim(@RequestBody Token id) {
    try {
      if (!new File("/tmp/st/text/").exists()) return new SimResp("END");
      var files = (new File("/tmp/st/text/")).listFiles();
      if (files.length == 0) {
        return new SimResp("END");
      }
      var selection = new HashSet<Integer>();
      Random rand = new Random();
      for (var count = 0; count < 100; ++count) { // fixme magic number
        var idx = rand.nextInt(files.length);
        if (!files[idx].toPath().resolve("result").toFile().exists()) continue;
        var added = selection.add(idx);
        if (selection.size() == files.length || selection.size() >= 5) break; // fixme magic number
      }
      if (selection.size() == 0) {
        return new SimResp("END");
      }
      for (var idx : selection) {
        var hash = files[idx].getName();
        var filename = Util.readNameOfText(hash);
        Util.sendResultToUser(new Similarity(id.id(), hash, filename), new Gson());
      }
      return new SimResp("OK");
    } catch (Exception e) {
      e.printStackTrace(System.err);
      return new SimResp("Unexpected error occurred");
    }
  }
}
