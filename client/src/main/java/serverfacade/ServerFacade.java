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

    public ServerFacade(int port) {
        this.serverURL = String.format("http://localhost:%d", port);
    }

    public RegisterResult register(RegisterRequest request) throws HttpResponseException {
        var httpRequest = buildRequest("POST", "/user", request, null);
        var httpResponse = sendRequest(httpRequest);
        return handleResponse(httpResponse, RegisterResult.class);
    }

    public LoginResult login(LoginRequest request) throws HttpResponseException {
        var httpRequest = buildRequest("POST", "/session", request, null);
        var httpResponse = sendRequest(httpRequest);
        return handleResponse(httpResponse, LoginResult.class);
    }

    public void logout(String authToken) throws HttpResponseException {
        var httpRequest = buildRequest("DELETE", "/session", null, authToken);
        sendRequest(httpRequest);
    }

    public ListGamesResult listGames(String authToken) throws HttpResponseException {
        var httpRequest = buildRequest("GET", "/game", null, authToken);
        var httpResponse = sendRequest(httpRequest);
        return handleResponse(httpResponse, ListGamesResult.class);
    }

    public CreateGameResult createGame(CreateGameRequest request, String authToken) throws HttpResponseException {
        var httpRequest = buildRequest("POST", "/game", request, authToken);
        var httpResponse = sendRequest(httpRequest);
        return handleResponse(httpResponse, CreateGameResult.class);
    }

    public void joinGame(JoinGameRequest request, String authToken) throws HttpResponseException {
        var httpRequest = buildRequest("PUT", "/game", request, authToken);
        sendRequest(httpRequest);
    }

    public void clear() throws HttpResponseException {
        var httpRequest = buildRequest("DELETE", "/db", null, null);
        sendRequest(httpRequest);
    }


    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL + path))
                .method(method, makeRequestBody(body));
        if(body != null) {
    request.setHeader("Content-Type", "application/json");
        }
        if(authToken != null) {
            request.header("authorization", authToken);
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

    private HttpResponse<String> sendRequest(HttpRequest request) throws HttpResponseException {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new HttpResponseException(ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws HttpResponseException {
        var status = response.statusCode();
        if(!isSuccessful(status)) {
            var body = response.body();
            if(body != null) {
                HashMap<String, String> errorMap = new Gson().fromJson(body, HashMap.class);
                throw new HttpResponseException(errorMap.get("message"));
            }

            throw new HttpResponseException(String.format("other failure: %d", status));
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
