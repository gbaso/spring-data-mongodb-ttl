package com.github.gbaso.ttl;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
record WithTtlFromBean(
    @Id
    String id,
    @Indexed(expireAfter = "#{@resolver.resolve()}")
    Instant timestamp
) {}
