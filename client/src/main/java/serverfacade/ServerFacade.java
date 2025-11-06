package serverfacade;

import com.google.gson.Gson;
import model.requests.*;
import model.results.*;

import java.net.URI;
import java.net.http.*;
import java.net.http.HttpRequest.*;
import java.net.http.HttpResponse.*;
import java.util.HashMap;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverURL;

    public ServerFacade(String url) {
        this.serverURL = url;
    }

    public RegisterResult register(RegisterRequest request) {
        var httpRequest = buildRequest("POST", "/user", request);
        var httpResponse = sendRequest(httpRequest);
        return handleResponse(httpResponse, RegisterResult.class);
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL + path))
                .method(method, makeRequestBody(body));
        if(body != null) {
    request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ServerConnectionException(ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) {
        var status = response.statusCode();
        if(!isSuccessful(status)) {
            var body = response.body();
            if(body != null) {
                HashMap<String, String> errorMap = new Gson().fromJson(body, HashMap.class);
                throw new ServerConnectionException(errorMap.get("message"));
            }

            throw new ServerConnectionException(String.format("other failure: %d", status));
        }
        if(responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }
        return null;
    }
    private boolean isSuccessful(int status) {
        return status >= 200 && status < 300;
    }
}
