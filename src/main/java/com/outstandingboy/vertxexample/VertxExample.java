package com.outstandingboy.vertxexample;

import com.outstandingboy.vertxexample.verticle.HelloVerticle;
import io.vertx.core.*;
import io.vertx.core.eventbus.*;

public class VertxExample {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        Verticle helloVerticle = new HelloVerticle();
//        vertx.deployVerticle("com.outstandingboy.vertxexample.verticle.HelloVerticle");
        vertx.deployVerticle(helloVerticle, res -> {
            if (res.succeeded()) {
                System.out.println("Deployment id = " + res.result());
            } else {
                throw new RuntimeException("Deployment failed!");
            }
        });

        Context context = vertx.getOrCreateContext();
        if (context.isEventLoopContext()) {
            System.out.println("Context attached to Event Loop");
        } else if (context.isWorkerContext()) {
            System.out.println("Context attached to Worker Thread");
        } else if (! Context.isOnVertxThread()) {
            System.out.println("Context not attached to a thread managed by vert.x");
        }

        EventBus eventBus = vertx.eventBus();
        eventBus.consumer("some.subject", message -> {
            System.out.println("message received: \"" + message.body() + "\", with option: " + message.headers());
            message.reply("그렇게 됐습니다...");
        });
        eventBus.consumer("some.subject", message -> {
            System.out.println("message received22: \"" + message.body() + "\", with option: " + message.headers());
            message.reply("그렇게 됐습니다...");
        });

        MessageProducer<String> sender = eventBus.sender("some.subject");
        sender.<String>send("안녕하세요~", event -> {
            System.out.println("event.result().body() = " + event.result().body());
        });
    }
}
