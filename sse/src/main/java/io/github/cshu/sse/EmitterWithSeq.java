package io.github.cshu.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class EmitterWithSeq {

  public SseEmitter emitter;
  public long seq;

  public EmitterWithSeq(SseEmitter emitter, long seq) {
    this.emitter = emitter;
    this.seq = seq;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof EmitterWithSeq ews) {
      return ews.seq == this.seq;
    }

    return false;
  }
}
