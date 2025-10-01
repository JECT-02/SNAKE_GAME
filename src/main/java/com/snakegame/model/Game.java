
package com.snakegame.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    private final GameConfig config;
    private final Map<Integer, Player> players;
    private final Map<Integer, Snake> snakes;
    private Point fruit;
    private final Random random = new Random();
    private boolean isGameOver = false;

    public Game(GameConfig config) {
        this.config = config;
        this.players = new ConcurrentHashMap<>();
        this.snakes = new ConcurrentHashMap<>();
        spawnFruit();
    }

    public void addPlayer(int playerId, String name) {
        Player player = new Player(playerId, name);
        players.put(playerId, player);

        Point startPoint = new Point(config.getBoardWidth() / 2, config.getBoardHeight() / 2);
        Snake snake = new Snake(playerId, startPoint, config.getSnakeInitialSize());
        snakes.put(playerId, snake);
    }

    public void removePlayer(int playerId) {
        players.remove(playerId);
        snakes.remove(playerId);
    }

    public void setDirection(int playerId, Direction direction) {
        Snake snake = snakes.get(playerId);
        if (snake != null) {
            snake.setDirection(direction);
        }
    }

    public void update() {
        if (isGameOver) return;

        for (Snake snake : snakes.values()) {
            snake.move();
            checkCollisions(snake);
            checkFruitEaten(snake);
        }
    }

    private void spawnFruit() {
        int x = random.nextInt(config.getBoardWidth());
        int y = random.nextInt(config.getBoardHeight());
        fruit = new Point(x, y);
    }

    private void checkFruitEaten(Snake snake) {
        if (snake.getHead().equals(fruit)) {
            snake.grow();
            players.get(snake.getPlayerId()).incrementScore(10);
            spawnFruit();
        }
    }

    private void checkCollisions(Snake snake) {
        Point head = snake.getHead();

        // Wall collision
        if (head.x < 0 || head.x >= config.getBoardWidth() || head.y < 0 || head.y >= config.getBoardHeight()) {
            isGameOver = true;
            return;
        }

        // Self collision
        for (int i = 1; i < snake.getBody().size(); i++) {
            if (head.equals(snake.getBody().get(i))) {
                isGameOver = true;
                return;
            }
        }

        // Other snake collision
        for (Snake otherSnake : snakes.values()) {
            if (snake.getPlayerId() == otherSnake.getPlayerId()) continue;
            for (Point bodyPart : otherSnake.getBody()) {
                if (head.equals(bodyPart)) {
                    isGameOver = true;
                    return;
                }
            }
        }
    }

    public GameState getGameState() {
        return new GameState(new ConcurrentHashMap<>(snakes), new Point(fruit), isGameOver);
    }
}
