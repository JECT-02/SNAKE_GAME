
package com.snakegame.gui;

import com.snakegame.model.GameState;
import com.snakegame.model.Snake;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private GameState gameState;
    private static final int CELL_SIZE = 20;

    public GamePanel() {
        setBackground(Color.BLACK);
    }

    public void updateState(GameState state) {
        this.gameState = state;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameState == null) return;

        // Draw snakes
        for (Snake snake : gameState.getSnakes().values()) {
            g.setColor(Color.GREEN); // Default snake color
            for (Point p : snake.getBody()) {
                g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        // Draw fruit
        Point fruit = gameState.getFruit();
        if (fruit != null) {
            g.setColor(Color.RED);
            g.fillOval(fruit.x * CELL_SIZE, fruit.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        if (gameState.isGameOver()) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("Game Over", getWidth() / 2 - 150, getHeight() / 2);
        }
    }
}
