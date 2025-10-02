
package com.snakegame.net.server;

import com.snakegame.model.Direction;
import com.snakegame.model.Game;
import com.snakegame.model.GameConfig;
import com.snakegame.model.GameState;
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
    private ScheduledExecutorService gameLoopExecutor;
    private int nextPlayerId = 1;
    private long initialSpeed = 200;
    private long speedDecrement = 25;
    private int currentLevel = 1;

    public GameServer(int port) {
        this.port = port;
        GameConfig config = new GameConfig(40, 30, 5, (int) initialSpeed);
        this.game = new Game(config);
        this.gameLoopExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() throws IOException {
        startGameLoop(initialSpeed);

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

    private void startGameLoop(long speed) {
        gameLoopExecutor.scheduleAtFixedRate(this::tick, 0, speed, TimeUnit.MILLISECONDS);
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

    private void tick() {
        game.update();
        broadcastState(game.getGameState());

        if (game.getLevel() > currentLevel) {
            currentLevel = game.getLevel();
            long newSpeed = Math.max(50, initialSpeed - (currentLevel - 1) * speedDecrement); // Ensure speed doesn't go below 50ms
            System.out.println("Level up! New speed: " + newSpeed + "ms");
            gameLoopExecutor.shutdown();
            gameLoopExecutor = Executors.newSingleThreadScheduledExecutor();
            startGameLoop(newSpeed);
        }
    }

    private void broadcastState(GameState gameState) {
        String gameStateString = gameState.toString();
        for (SocketWrapper client : clients.values()) {
            client.println(gameStateString);
        }
    }
}
