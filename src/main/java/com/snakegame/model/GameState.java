
package com.snakegame.model;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameState {
    private final Map<Integer, Snake> snakes;
    private final Point fruit;
    private final boolean isGameOver;

    public GameState(Map<Integer, Snake> snakes, Point fruit, boolean isGameOver) {
        this.snakes = snakes;
        this.fruit = fruit;
        this.isGameOver = isGameOver;
    }

    public Map<Integer, Snake> getSnakes() {
        return snakes;
    }

    public Point getFruit() {
        return fruit;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public static GameState fromString(String data) {
        Map<Integer, Snake> snakes = new ConcurrentHashMap<>();
        Point fruit = null;
        boolean isGameOver = false;

        String[] parts = data.split(";");
        for (String part : parts) {
            String[] keyValue = part.split(":");
            if (keyValue.length < 2) continue;

            String key = keyValue[0];
            String value = keyValue[1];

            switch (key) {
                case "SNAKE":
                    String[] snakeData = value.split(",");
                    int playerId = Integer.parseInt(snakeData[0]);
                    Snake snake = new Snake(playerId, new Point(0, 0), 0);
                    snake.getBody().clear();
                    for (int i = 1; i < snakeData.length; i += 2) {
                        int x = Integer.parseInt(snakeData[i]);
                        int y = Integer.parseInt(snakeData[i + 1]);
                        snake.getBody().add(new Point(x, y));
                    }
                    snakes.put(playerId, snake);
                    break;
                case "FRUIT":
                    String[] fruitData = value.split(",");
                    fruit = new Point(Integer.parseInt(fruitData[0]), Integer.parseInt(fruitData[1]));
                    break;
                case "GAMEOVER":
                    isGameOver = Boolean.parseBoolean(value);
                    break;
            }
        }
        return new GameState(snakes, fruit, isGameOver);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Snake snake : snakes.values()) {
            sb.append("SNAKE:").append(snake.getPlayerId());
            for (Point p : snake.getBody()) {
                sb.append(",").append(p.x).append(",").append(p.y);
            }
            sb.append(";");
        }
        if (fruit != null) {
            sb.append("FRUIT:").append(fruit.x).append(",").append(fruit.y).append(";");
        }
        sb.append("GAMEOVER:").append(isGameOver).append(";");
        return sb.toString();
    }
}
