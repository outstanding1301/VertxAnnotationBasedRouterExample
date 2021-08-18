package com.outstandingboy.vertxexample;

import io.vertx.core.Vertx;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


class VertxExampleTest {
    @Test
    void vertxTest() {
        Vertx vertx1 = Vertx.vertx();
        Vertx vertx2 = Vertx.vertx();

        Assertions.assertThat(vertx1).isEqualTo(vertx2);
    }
}