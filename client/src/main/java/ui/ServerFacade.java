package ui;
import requestandresponse.*;
import com.google.gson.*;
import java.io.IOException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
public class ServerFacade {
    private final String serverUrl = "http://localhost:8080";

    public RegResponse register(RegRequest req) throws IOException {
        URL url = new URL(serverUrl + "/user");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.connect();
        try (var outputStream = conn.getOutputStream()) {
            outputStream.write(new Gson().toJson(req).getBytes());
        }

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            RegResponse resp = null;
            try (InputStream stream = conn.getErrorStream()) {
                InputStreamReader streamReader = new InputStreamReader(stream);
                resp = new Gson().fromJson(streamReader, RegResponse.class);
            }
            return resp;
        } else {
            RegResponse resp = null;
            try (InputStream stream = conn.getInputStream()) {
                InputStreamReader streamReader = new InputStreamReader(stream);
                resp = new Gson().fromJson(streamReader, RegResponse.class);
            }
            return resp;
        }
    }
    public CreateGameResponse createGame(CreateGameRequest request) throws IOException{
        URL url = new URL(serverUrl+"/game");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.addRequestProperty("Authorization", TokenPlaceholder.token);
        conn.connect();
        try (var outStream = conn.getOutputStream()) {
            outStream.write(new Gson().toJson(request).getBytes());
        }
        if (HttpURLConnection.HTTP_OK != conn.getResponseCode()) {
            CreateGameResponse resp = null;
            try (InputStream stream = conn.getErrorStream()) {
                InputStreamReader streamReader = new InputStreamReader(stream);
                resp = new Gson().fromJson(streamReader, CreateGameResponse.class);
            }
            return resp;
        }

        else {
            CreateGameResponse resp = null;
            try (InputStream stream = conn.getInputStream()) {
                InputStreamReader streamReader = new InputStreamReader(stream);
                resp = new Gson().fromJson(streamReader, CreateGameResponse.class);
            }
            return resp;
        }
    }
    public LoginResponse login(LoginRequest request) throws IOException{
        URL url = new URL(serverUrl+"/session");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.connect();
        try (var outStream = conn.getOutputStream()) {
            outStream.write(new Gson().toJson(request).getBytes());
        }
        if (HttpURLConnection.HTTP_OK != conn.getResponseCode()) {
            LoginResponse resp = null;
            try (InputStream stream = conn.getErrorStream()) {
                InputStreamReader streamReader = new InputStreamReader(stream);
                resp = new Gson().fromJson(streamReader, LoginResponse.class);
            }
            return resp;
        }

        else {
            LoginResponse resp = null;
            try (InputStream stream = conn.getInputStream()) {
                InputStreamReader streamReader = new InputStreamReader(stream);
                resp = new Gson().fromJson(streamReader, LoginResponse.class);
            }
            return resp;
        }
    }
    public LogoutResponse logout() throws IOException{
        URL url = new URL(serverUrl+"/session");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);
        conn.setRequestMethod("DELETE");
        conn.addRequestProperty("Authorization", TokenPlaceholder.token);
        conn.connect();
        if (HttpURLConnection.HTTP_OK != conn.getResponseCode()) {
            LogoutResponse resp = null;
            try (InputStream stream = conn.getErrorStream()) {
                InputStreamReader streamReader = new InputStreamReader(stream);
                resp = new Gson().fromJson(streamReader, LogoutResponse.class);
            }
            return resp;
        }

        else {
            LogoutResponse resp = null;
            try (InputStream stream = conn.getInputStream()) {
                InputStreamReader streamReader = new InputStreamReader(stream);
                resp = new Gson().fromJson(streamReader, LogoutResponse.class);
            }
            return resp;
        }
    }
}