
package com.snakegame.net.server;

import com.snakegame.model.Direction;
import com.snakegame.net.SocketWrapper;

import java.io.IOException;

public class ClientHandler implements Runnable {
    private final SocketWrapper socketWrapper;
    private final GameServer server;

    public ClientHandler(SocketWrapper socketWrapper, GameServer server) {
        this.socketWrapper = socketWrapper;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            server.addPlayer(socketWrapper);
            String line;
            while ((line = socketWrapper.readLine()) != null) {
                try {
                    Direction dir = Direction.valueOf(line.toUpperCase());
                    server.setPlayerDirection(socketWrapper.getId(), dir);
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid direction from client " + socketWrapper.getId() + ": " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Client " + socketWrapper.getId() + " disconnected.");
        } finally {
            server.removePlayer(socketWrapper.getId());
        }
    }
}
