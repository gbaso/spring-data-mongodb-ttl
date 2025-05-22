package com.github.gbaso.ttl;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
record WithTtlFromProperties(
    @Id
    String id,
    @Indexed(expireAfter = "${ttl}")
    Instant timestamp
) {}
