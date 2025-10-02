package com.snakegame.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameState {
    private final Map<Integer, Player> players;
    private final Map<Integer, Snake> snakes;
    private final Map<Point, Integer> fruits;
    private final List<Point> obstacles;
    private final int level;
    private final boolean isGameOver;

    public GameState(Map<Integer, Player> players, Map<Integer, Snake> snakes, Map<Point, Integer> fruits, List<Point> obstacles, int level, boolean isGameOver) {
        this.players = players;
        this.snakes = snakes;
        this.fruits = fruits;
        this.obstacles = obstacles;
        this.level = level;
        this.isGameOver = isGameOver;
    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }

    public Map<Integer, Snake> getSnakes() {
        return snakes;
    }

    public Map<Point, Integer> getFruits() {
        return fruits;
    }

    public List<Point> getObstacles() {
        return obstacles;
    }

    public int getLevel() {
        return level;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public static GameState fromString(String data) {
        Map<Integer, Player> players = new ConcurrentHashMap<>();
        Map<Integer, Snake> snakes = new ConcurrentHashMap<>();
        Map<Point, Integer> fruits = new ConcurrentHashMap<>();
        List<Point> obstacles = new ArrayList<>();
        int level = 1;
        boolean isGameOver = false;

        if (data == null || data.isEmpty()) {
            return new GameState(players, snakes, fruits, obstacles, level, true);
        }

        String[] parts = data.split(";");
        for (String part : parts) {
            String[] keyValue = part.split(":");
            if (keyValue.length < 2) continue;

            String key = keyValue[0];
            String value = keyValue[1];

            switch (key) {
                case "LEVEL":
                    level = Integer.parseInt(value);
                    break;
                case "PLAYER": {
                    String[] playerData = value.split(",");
                    int playerId = Integer.parseInt(playerData[0]);
                    int score = Integer.parseInt(playerData[1]);
                    Player player = new Player(playerId, "Player " + playerId);
                    player.incrementScore(score); // Sets the score
                    players.put(playerId, player);
                    break;
                }
                case "SNAKE": {
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
                }
                case "FRUIT": {
                    String[] fruitData = value.split(",");
                    Point fruitPoint = new Point(Integer.parseInt(fruitData[0]), Integer.parseInt(fruitData[1]));
                    int fruitValue = Integer.parseInt(fruitData[2]);
                    fruits.put(fruitPoint, fruitValue);
                    break;
                }
                case "OBSTACLE": {
                    String[] obsData = value.split(",");
                    obstacles.add(new Point(Integer.parseInt(obsData[0]), Integer.parseInt(obsData[1])));
                    break;
                }
                case "GAMEOVER":
                    isGameOver = Boolean.parseBoolean(value);
                    break;
            }
        }
        return new GameState(players, snakes, fruits, obstacles, level, isGameOver);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LEVEL:").append(level).append(";");
        for (Player player : players.values()) {
            sb.append("PLAYER:").append(player.getId()).append(",").append(player.getScore()).append(";");
        }
        for (Snake snake : snakes.values()) {
            sb.append("SNAKE:").append(snake.getPlayerId());
            for (Point p : snake.getBody()) {
                sb.append(",").append(p.x).append(",").append(p.y);
            }
            sb.append(";");
        }
        for (Map.Entry<Point, Integer> entry : fruits.entrySet()) {
            Point p = entry.getKey();
            Integer val = entry.getValue();
            sb.append("FRUIT:").append(p.x).append(",").append(p.y).append(",").append(val).append(";");
        }
        for (Point p : obstacles) {
            sb.append("OBSTACLE:").append(p.x).append(",").append(p.y).append(";");
        }
        sb.append("GAMEOVER:").append(isGameOver).append(";");
        return sb.toString();
    }
}