
package com.snakegame.main;

import com.snakegame.gui.GameFrame;

import javax.swing.*;

public class ClientMain {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 8080;

        if (args.length >= 2) {
            host = args[0];
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number. Using default 8080.");
            }
        } else if (args.length == 1) {
            host = args[0];
        }

        String finalHost = host;
        int finalPort = port;
        SwingUtilities.invokeLater(() -> {
            GameFrame frame = new GameFrame();
            frame.setVisible(true);
            frame.connectToServer(finalHost, finalPort);
        });
    }
}
