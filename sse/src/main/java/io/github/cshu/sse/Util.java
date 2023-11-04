package io.github.cshu.sse;

import java.io.*;
import java.util.concurrent.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class Util {
	public final static java.util.Base64.Encoder base64Encoder = java.util.Base64.getEncoder();
	public final static java.security.SecureRandom secureRandom = new java.security.SecureRandom();
	public final static ConcurrentHashMap<String, EmitterWithSeq> liveSse = new ConcurrentHashMap<>();

public static String mkToken() {
    byte[] randomBytes = new byte[32];
	secureRandom.nextBytes(randomBytes);
	return base64Encoder.encodeToString(randomBytes);
}
	public static SseEmitter mkSseEmitter(String sseid) {
		//Gson gson = new Gson();
		//var sseid = mkToken();
		//var newsse = new NewSseid();
		//newsse.id = sseid;
		//var msg = gson.toJson(newsse);
    		SseEmitter emitter = new SseEmitter(20_000L);
		var ews = new EmitterWithSeq(emitter, System.currentTimeMillis());
		emitter.onCompletion(()->{liveSse.remove(sseid, ews);});
		emitter.onTimeout(()->{liveSse.remove(sseid, ews);});
		//try{
		//emitter.send(SseEmitter.event().name("message").data(msg));
		//}catch(IOException e){
		//emitter.completeWithError(e);
		//}
		liveSse.put(sseid, ews);
		return emitter;
	}
	public static void sendResultToUser(String in) {
		var ews = liveSse.get(in);
		if (null == ews) {
			Thread thread = Thread.ofVirtual().start(() -> {
				//note Virtual Threads don’t block when sleep()!!! OS thread is still avail like goroutine!!!
				try{
				Thread.sleep(5000);//give it a second chance due to possible intermittent connection of user
				}catch(InterruptedException e){
					e.printStackTrace(System.err);
				}
				var ewsretry = liveSse.get(in);
				if (null == ewsretry) return;
				sendResultToUser(ewsretry);
			});
			return;
		}
		sendResultToUser(ews);
	}
	public static void sendResultToUser(EmitterWithSeq ews) {
		//undone
	}
}
