
package com.snakegame.gui;

import com.snakegame.model.GameState;
import com.snakegame.model.Player;
import com.snakegame.model.Snake;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class GamePanel extends JPanel {
    private GameState gameState;
    private static final int CELL_SIZE = 20;

    public GamePanel() {
        setBackground(Color.BLACK);
    }

    public void updateState(GameState state) {
        this.gameState = state;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameState == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw obstacles
        g.setColor(Color.GRAY);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, CELL_SIZE));
        if (gameState.getObstacles() != null) {
            for (Point p : gameState.getObstacles()) {
                g.drawString("#", p.x * CELL_SIZE, (p.y + 1) * CELL_SIZE);
            }
        }

        // Draw snakes
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, CELL_SIZE));
        for (Snake snake : gameState.getSnakes().values()) {
            g.setColor(Color.GREEN);
            // Draw body
            for (int i = 1; i < snake.getBody().size(); i++) {
                Point p = snake.getBody().get(i);
                g.drawString("o", p.x * CELL_SIZE, (p.y + 1) * CELL_SIZE);
            }
            // Draw head
            Point head = snake.getHead();
            char headChar = (char) ('A' + snake.getPlayerId() - 1);
            g.drawString(String.valueOf(headChar), head.x * CELL_SIZE, (head.y + 1) * CELL_SIZE);
        }

        // Draw fruits
        g.setColor(Color.RED);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, CELL_SIZE));
        for (Map.Entry<Point, Integer> entry : gameState.getFruits().entrySet()) {
            Point p = entry.getKey();
            int value = entry.getValue();
            g.drawString(String.valueOf(value), p.x * CELL_SIZE, (p.y + 1) * CELL_SIZE);
        }

        // Draw scores and level
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Level: " + gameState.getLevel(), 10, 20);
        int y = 40;
        for (Player player : gameState.getPlayers().values()) {
            g.drawString(player.getName() + ": " + player.getScore(), 10, y);
            y += 20;
        }

        if (gameState.isGameOver()) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("Game Over", getWidth() / 2 - 150, getHeight() / 2);
        }
    }
}
