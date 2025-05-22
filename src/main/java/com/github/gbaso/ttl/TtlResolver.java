package com.github.gbaso.ttl;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("resolver")
public class TtlResolver {

  private final Duration ttl;

  public TtlResolver(@Value("${ttl}") Duration ttl) {
    this.ttl = ttl;
  }

  public Duration resolve() {
    return ttl;
  }
}
