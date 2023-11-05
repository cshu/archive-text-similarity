package io.github.cshu.sse;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;

import org.springframework.kafka.core.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.*;

@RestController
@RequestMapping(value = "/download",
                produces = "text/plain")
public class DownloadController {

  @GetMapping("/{hash}")
  public ResponseEntity<byte[]> sim(@PathVariable String hash)  throws IOException{
    //try {
      if (!hash.matches("[A-Za-z0-9]+")) {
        // fixme do something about malicious request?
        return new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
      }
      var hashinhexfnm = "/tmp/st/text/" + hash;
      var blob = Files.readAllBytes(Paths.get(hashinhexfnm +"/data"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        //headers.setContentDispositionFormData("attachment", "w.txt");
        headers.setContentLength(blob.length);
	return new ResponseEntity<byte[]>(blob, headers, HttpStatus.OK);
    //} catch (Exception e) {
    //  e.printStackTrace(System.err);
    //  return new SimResp("Unexpected error occurred");
    //}
  }
}
