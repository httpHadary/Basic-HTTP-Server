package webServer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class Handlers {

    public static void homeHandler(Request request, Response response) {
        response.json(Map.of("message","Hello From Elhaddour's Server."));
    }

    public static void helloHandler(Request request, Response response) {
        response.text("Hello World");
    }

    public static void userAgentHandler(Request request, Response response) {
        response.text(request.getRequestHeaders().getOrDefault("User-Agent" , "null"));
    }

    public static void echoHandler(Request request, Response response) {
        response.text(request.getPathParameter("message"));
    }

    public static void downloadFileHandler(Request request, Response response) throws IOException {
        String fileName = request.getPathParameter("filename");
        File file = new File(WebServer.ROOT_DIRECTORY, fileName);

        if (file.exists()) {
            byte[] body = Files.readAllBytes(file.toPath());
            response.setStatusCode("200");
            response.addResponseHeader("Content-Type", WebServer.getMimeType(fileName));
            response.setResponseBody(body);
        } else {
            response.setStatusCode("404");
            response.text("File Was Not Found");
        }
    }

    public static void createFileHandler(Request request, Response response) throws IOException {
        String fileName = request.getPathParameter("filename");
        File file = new File(WebServer.ROOT_DIRECTORY, fileName);

        Files.write(file.toPath(), request.getRequestBody());
        response.setStatusCode("201");
        response.setResponseBody(new byte[0]);
    }

    public static void updateFileHandler(Request request, Response response) throws IOException {
        String fileName = request.getPathParameter("filename");
        File file = new File(WebServer.ROOT_DIRECTORY, fileName);

        Files.write(file.toPath(), request.getRequestBody());

        response.text("File Updated Successfully");
    }

    public static void deleteFileHandler(Request request, Response response) throws IOException {
        String fileName = request.getPathParameter("filename");
        File file = new File(WebServer.ROOT_DIRECTORY, fileName);

        boolean isDeleted = Files.deleteIfExists(file.toPath());

        String body;

        if (isDeleted) {
            response.setStatusCode("200");
            body = "File Deleted Successfully";
        } else {
            response.setStatusCode("404");
            body = "File Not Found";
        }

        response.text(body);
    }

    public static void headHandler(Request request, Response response) throws IOException {
        downloadFileHandler(request, response);
    }

    public static void searchHandler(Request request, Response response) {
        Map<String, String> parameters = request.getQueryParameters();

        response.json(Map.of(
                "query", parameters.getOrDefault("query", "java"),
                "page", parameters.getOrDefault("page", "1")
        ));
    }
}
