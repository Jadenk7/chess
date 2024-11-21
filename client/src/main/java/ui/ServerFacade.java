package ui;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;
import requestandresponse.*;
import com.google.gson.*;

import java.io.IOException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerFacade {
    private String serverUrl;
    private static Map<Integer, Integer> gameIdMap = new HashMap<>();
    private static int nextSequentialId = 1;

    public ServerFacade(int port) {
        this.serverUrl = "http://localhost:" + port;
    }

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
    public void preloadGames() throws IOException {
        URL url = new URL(serverUrl + "/game");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);
        conn.setRequestMethod("GET");
        conn.addRequestProperty("Authorization", TokenPlaceholder.token);
        conn.connect();
        InputStream stream;
        if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
            stream = conn.getInputStream();
        } else {
            stream = conn.getErrorStream();
        }
        try (InputStreamReader streamReader = new InputStreamReader(stream)){
            ListGamesResponse listGamesResponse = new Gson().fromJson(streamReader, ListGamesResponse.class);
            if (listGamesResponse != null && listGamesResponse.getGames() != null) {
                synchronized (gameIdMap) {
                    for (GameData game : listGamesResponse.getGames()) {
                        int dbId = game.getID();
                        if (!gameIdMap.containsKey(dbId)) {
                            gameIdMap.put(dbId, nextSequentialId++);
                        }
                    }
                }
            }
        }
    }


    public CreateGameResponse createGame(CreateGameRequest request) throws IOException {
        preloadGames();
        URL url = new URL(serverUrl + "/game");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.addRequestProperty("Authorization", TokenPlaceholder.token);
        conn.connect();
        try (var outStream = conn.getOutputStream()) {
            outStream.write(new Gson().toJson(request).getBytes());
        }
        CreateGameResponse resp;
        try (InputStream stream = (HttpURLConnection.HTTP_OK == conn.getResponseCode())
                ? conn.getInputStream()
                : conn.getErrorStream()) {
            InputStreamReader streamReader = new InputStreamReader(stream);
            resp = new Gson().fromJson(streamReader, CreateGameResponse.class);
        }
        if (resp != null && resp.getID() != null) {
            int dbId = resp.getID();
            synchronized (gameIdMap) {
                if (!gameIdMap.containsKey(dbId)) {
                    gameIdMap.put(dbId, nextSequentialId++);
                }
                resp.setSequentialId(gameIdMap.get(dbId));
            }
        }
        return resp;
    }

    public LoginResponse login(LoginRequest request) throws IOException {
        URL url = new URL(serverUrl + "/session");
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
        } else {
            LoginResponse resp = null;
            try (InputStream stream = conn.getInputStream()) {
                InputStreamReader streamReader = new InputStreamReader(stream);
                resp = new Gson().fromJson(streamReader, LoginResponse.class);
            }
            return resp;
        }
    }

    public LogoutResponse logout() throws IOException {
        URL url = new URL(serverUrl + "/session");
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
        } else {
            LogoutResponse resp = null;
            try (InputStream stream = conn.getInputStream()) {
                InputStreamReader streamReader = new InputStreamReader(stream);
                resp = new Gson().fromJson(streamReader, LogoutResponse.class);
            }
            return resp;
        }
    }

    public JoinGameResponse joinGame(JoinGameRequest request) throws IOException {
        URL url = new URL(serverUrl + "/game");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);
        conn.setRequestMethod("PUT");
        conn.addRequestProperty("Authorization", TokenPlaceholder.token);
        conn.setDoOutput(true);
        conn.connect();
        try (var outStream = conn.getOutputStream()) {
            outStream.write(new Gson().toJson(request).getBytes());
        }
        if (HttpURLConnection.HTTP_OK != conn.getResponseCode()) {
            JoinGameResponse resp = null;
            try (InputStream stream = conn.getErrorStream()) {
                InputStreamReader streamReader = new InputStreamReader(stream);
                resp = new Gson().fromJson(streamReader, JoinGameResponse.class);
            }
            return resp;
        } else {
            JoinGameResponse resp = null;
            try (InputStream stream = conn.getInputStream()) {
                InputStreamReader streamReader = new InputStreamReader(stream);
                resp = new Gson().fromJson(streamReader, JoinGameResponse.class);
            }
            return resp;
        }
    }

    public ListGamesResponse listgames() throws IOException {
        URL url = new URL(serverUrl + "/game");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);
        conn.setRequestMethod("GET");
        conn.addRequestProperty("Authorization", TokenPlaceholder.token);
        conn.connect();
        ListGamesResponse resp = null;
        try (InputStream stream = (HttpURLConnection.HTTP_OK == conn.getResponseCode())
                ? conn.getInputStream()
                : conn.getErrorStream()) {
            InputStreamReader streamReader = new InputStreamReader(stream);
            resp = new Gson().fromJson(streamReader, ListGamesResponse.class);
        }

        // If the response contains a list of games
        if (resp != null && resp.getGames() != null) {
            List<GameData> games = (List<GameData>) resp.getGames(); // Assuming ListGamesResponse has a `getGames()` method returning a List<Game>.
            Map<Integer, Integer> gameIdMap = new HashMap<>(); // Mapping actual game IDs to sequential numbers
            int sequentialId = 1;

            for (GameData game : games) {
                int dbId = game.getID(); // Assuming Game has a `getId()` method returning the database number
                gameIdMap.put(dbId, sequentialId);
                game.setID(sequentialId); // Overwrite the ID for client response
                sequentialId++;
            }
        }
        return resp;
    }
}
