package com.outstandingboy.vertxexample.verticle;

import com.outstandingboy.vertxexample.RequestMapping;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

@RequestMapping("/test")
public class HelloVerticle extends HttpServerVerticle {
    @RequestMapping("/")
    public Handler<RoutingContext> root() {
        return (context) -> {
            HttpServerResponse response = context.response();
            response.putHeader("content-type", "text/plain");
            response.end("Hello world!\n\n" +
                    context.pathParams() + "\n\n" +
                    context.queryParams());
        };
    }

    @RequestMapping("/hello/:name")
    public Handler<RoutingContext> hello() {
        return context -> {
            context.pathParams();
            HttpServerResponse response = context.response();
            response.putHeader("content-type", "text/plain");
            response.end("Hello " + context.pathParam("name"));
        };
    }

    @Override
    public void start() {
        runServer(8089, res -> {
            if (res.succeeded()) {
                System.out.println("server running at http://localhost:8089/");
            } else {
                System.out.println("server run failed!!");
            }
        });
    }

    @Override
    public void stop() {
        System.out.println("server stopped");
    }
}
