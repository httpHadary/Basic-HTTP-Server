package middleware;
import webServer.Request;
import webServer.Response;

public interface Middleware {
    void execute(Request request, Response response, MiddlewareChain chain);
}
