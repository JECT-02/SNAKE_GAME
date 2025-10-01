
package com.snakegame.net.server;

import com.snakegame.model.Direction;
import com.snakegame.model.Game;
import com.snakegame.model.GameConfig;
import com.snakegame.net.SocketWrapper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameServer {
    private final int port;
    private final Game game;
    private final Map<Integer, SocketWrapper> clients = new ConcurrentHashMap<>();
    private final ScheduledExecutorService gameLoop;
    private int nextPlayerId = 1;

    public GameServer(int port) {
        this.port = port;
        GameConfig config = new GameConfig(40, 30, 5, 150);
        this.game = new Game(config);
        this.gameLoop = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() throws IOException {
        gameLoop.scheduleAtFixedRate(this::updateAndBroadcast, 0, 150, TimeUnit.MILLISECONDS);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                int playerId = nextPlayerId++;
                SocketWrapper wrapper = new SocketWrapper(playerId, clientSocket);
                ClientHandler handler = new ClientHandler(wrapper, this);
                new Thread(handler).start();
            }
        }
    }

    void addPlayer(SocketWrapper client) {
        clients.put(client.getId(), client);
        game.addPlayer(client.getId(), "Player " + client.getId());
        System.out.println("Player " + client.getId() + " connected.");
    }

    void removePlayer(int playerId) {
        clients.remove(playerId);
        game.removePlayer(playerId);
        System.out.println("Player " + playerId + " removed.");
    }

    void setPlayerDirection(int playerId, Direction direction) {
        game.setDirection(playerId, direction);
    }

    private void updateAndBroadcast() {
        game.update();
        String gameStateString = game.getGameState().toString();
        for (SocketWrapper client : clients.values()) {
            client.println(gameStateString);
        }
    }
}
