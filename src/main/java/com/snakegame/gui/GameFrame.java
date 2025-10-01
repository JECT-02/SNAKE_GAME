
package com.snakegame.gui;

import com.snakegame.net.client.GameClient;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class GameFrame extends JFrame {
    private final GamePanel gamePanel;
    private GameClient gameClient;

    public GameFrame() {
        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        gamePanel = new GamePanel();
        add(gamePanel);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameClient == null) return;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP: gameClient.sendDirection("UP"); break;
                    case KeyEvent.VK_DOWN: gameClient.sendDirection("DOWN"); break;
                    case KeyEvent.VK_LEFT: gameClient.sendDirection("LEFT"); break;
                    case KeyEvent.VK_RIGHT: gameClient.sendDirection("RIGHT"); break;
                }
            }
        });
    }

    public void connectToServer(String host, int port) {
        try {
            gameClient = new GameClient(host, port, gamePanel);
            gameClient.connect();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to server: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}
