package middleware;

import httpServer.Request;
import httpServer.Response;

public class CorsMiddleware implements Middleware{
    @Override
    public void execute(Request request, Response response, MiddlewareChain middlewareChain) {
        middlewareChain.next(request, response);

        response.addResponseHeader("Access-Control-Allow-Origin", "*");
    }
}
