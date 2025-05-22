package com.github.gbaso.ttl;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.IndexDefinition;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;

@DataMongoTest
@Import({
    TtlResolver.class,
    TestcontainersConfiguration.class
})
class TtlTests {

  @Autowired
  MongoOperations mongoOperations;

  @AfterEach
  void tearDown() {
    mongoOperations.dropCollection(WithTtlFromBean.class);
    mongoOperations.dropCollection(WithTtlFromProperties.class);
  }

  @Test
  @DisplayName("TTL should be derived from bean invocation")
  void fromBean() {
    ensureIndexes(WithTtlFromBean.class);
    IndexOperations indexOperations = mongoOperations.indexOps(WithTtlFromBean.class);
    Optional<Duration> ttl = indexOperations
        .getIndexInfo()
        .stream()
        .map(IndexInfo::getExpireAfter)
        .flatMap(Optional::stream)
        .findAny();
    assertThat(ttl).hasValue(Duration.ofDays(7));
  }

  @Test
  @DisplayName("TTL should be derived from properties")
  void fromProperties() {
    ensureIndexes(WithTtlFromProperties.class); // fails with message: '${ttl}' is not a valid duration, cannot detect any known style
    IndexOperations indexOperations = mongoOperations.indexOps(WithTtlFromProperties.class);
    Optional<Duration> ttl = indexOperations
        .getIndexInfo()
        .stream()
        .map(IndexInfo::getExpireAfter)
        .flatMap(Optional::stream)
        .findAny();
    assertThat(ttl).hasValue(Duration.ofDays(7));
  }

  private void ensureIndexes(Class<?> documentClass) {
    IndexOperations indexOperations = mongoOperations.indexOps(documentClass);
    var context = mongoOperations.getConverter().getMappingContext();
    var indexResolver = new MongoPersistentEntityIndexResolver(context);
    Iterable<? extends IndexDefinition> definitions = indexResolver.resolveIndexFor(documentClass);
    definitions.forEach(indexOperations::ensureIndex);
  }

}
