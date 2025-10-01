
package com.snakegame.net.client;

import com.snakegame.gui.GamePanel;
import com.snakegame.model.GameState;
import com.snakegame.net.SocketWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient {
    private final String host;
    private final int port;
    private SocketWrapper socketWrapper;
    private final GamePanel gamePanel;

    public GameClient(String host, int port, GamePanel gamePanel) {
        this.host = host;
        this.port = port;
        this.gamePanel = gamePanel;
    }

    public void connect() throws IOException {
        Socket socket = new Socket(host, port);
        this.socketWrapper = new SocketWrapper(0, socket); // Client ID is assigned by server

        Thread listenerThread = new Thread(() -> {
            try {
                String fromServer;
                while ((fromServer = socketWrapper.readLine()) != null) {
                    GameState gameState = GameState.fromString(fromServer);
                    if (gameState != null) {
                        gamePanel.updateState(gameState);
                        gamePanel.repaint();
                    }
                }
            } catch (IOException e) {
                System.out.println("Disconnected from server.");
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public void sendDirection(String direction) {
        if (socketWrapper != null) {
            socketWrapper.println(direction);
        }
    }
}
