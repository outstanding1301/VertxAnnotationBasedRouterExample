package com.outstandingboy.vertxexample.verticle;

import com.outstandingboy.vertxexample.RequestMapping;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public abstract class HttpServerVerticle extends AbstractVerticle {
    private HttpServer server;

    private Router getRouter() {
        String rootPath = getClass().getAnnotation(RequestMapping.class).value();
        Router router = Router.router(vertx);
        Arrays.stream(getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> {
                    try {
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        HttpMethod httpMethod = requestMapping.method().getMethod();
                        String path = rootPath + requestMapping.value();
                        Object handler = method.invoke(this);
                        if (path.endsWith("/")) path = path.substring(0, path.length() - 1);
                        router.route(httpMethod, path).handler((Handler<RoutingContext>) handler);
                        router.route(httpMethod, path + "/").handler((Handler<RoutingContext>) handler);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
        return router;
    }

    public void runServer(int port, Handler<AsyncResult<HttpServer>> asyncResultHandler) {
        server = vertx.createHttpServer().requestHandler(getRouter());
        server.listen(port, asyncResultHandler);
    }
}
