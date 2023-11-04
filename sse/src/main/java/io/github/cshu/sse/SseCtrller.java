package io.github.cshu.sse;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.bind.annotation.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

@Controller
public class SseCtrller {

  @RequestMapping("/sse")
  public @ResponseBody SseEmitter handle(@RequestParam String id) {
    //System.out.println(id);
    var emitter= Util.mkSseEmitter(id);
		try{
		var msg = new SseMsg();
		msg.type = "established";
		Gson gson = new Gson();
		emitter.send(SseEmitter.event().name("message").data(gson.toJson(msg)));
		}catch(IOException e){
		emitter.completeWithError(e);
		}
		return emitter;
  }
}
