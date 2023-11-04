package io.github.cshu.sse;

import java.io.IOException;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {


	@PostMapping("/token")
	public RandomToken token() {
		//var retval = new RandomToken();
		//retval.token = Util.mkToken();
		//return retval;
		return new RandomToken(Util.mkToken());
	}
}
