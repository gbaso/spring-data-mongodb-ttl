package com.github.gbaso.ttl;

import org.springframework.boot.SpringApplication;

public class TestSpringDataMongodbTtlSpelApplication {

  public static void main(String[] args) {
    SpringApplication
        .from(SpringDataMongodbTtlSpelApplication::main)
        .with(TestcontainersConfiguration.class)
        .run(args);
  }

}
