
package com.snakegame.main;

import com.snakegame.net.server.GameServer;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) {
        int port = 8080;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number. Using default 8080.");
            }
        }

        try {
            new GameServer(port).start();
        } catch (IOException e) {
            System.err.println("Could not start server: " + e.getMessage());
        }
    }
}
