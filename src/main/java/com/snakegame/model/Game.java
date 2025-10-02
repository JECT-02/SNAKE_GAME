
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
    private final Map<Point, Integer> fruits;
    private final List<Point> obstacles;
    private final Random random = new Random();
    private boolean isGameOver = false;

    private int level = 1;
    private int totalFruitsEaten = 0;
    private static final int FRUITS_PER_LEVEL = 5;
    private static final int MAX_FRUITS = 5;
    private static final int[] FRUIT_VALUES = {1, 3, 5, 7};

    public Game(GameConfig config) {
        this.config = config;
        this.players = new ConcurrentHashMap<>();
        this.snakes = new ConcurrentHashMap<>();
        this.fruits = new ConcurrentHashMap<>();
        this.obstacles = new ArrayList<>();
        for (int i = 0; i < MAX_FRUITS; i++) {
            spawnFruit();
        }
    }

    public int getLevel() {
        return level;
    }

    private void nextLevel() {
        level++;
        totalFruitsEaten = 0;
        obstacles.clear();
        generateObstacles();
        System.out.println("Advanced to Level " + level);
    }

    private void generateObstacles() {
        if (level > 1) {
            for (int i = 0; i < level * 2; i++) {
                obstacles.add(new Point(random.nextInt(config.getBoardWidth()), random.nextInt(config.getBoardHeight())));
            }
        }
    }

    public void addPlayer(int playerId, String name) {
        Player player = new Player(playerId, name);
        players.put(playerId, player);

        Point startPoint = new Point(random.nextInt(config.getBoardWidth()), random.nextInt(config.getBoardHeight()));
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
        if (fruits.size() >= MAX_FRUITS) return;

        Point fruitPoint;
        do {
            int x = random.nextInt(config.getBoardWidth());
            int y = random.nextInt(config.getBoardHeight());
            fruitPoint = new Point(x, y);
        } while (fruits.containsKey(fruitPoint) || obstacles.contains(fruitPoint));

        int value = FRUIT_VALUES[random.nextInt(FRUIT_VALUES.length)];
        fruits.put(fruitPoint, value);
    }

    private void checkFruitEaten(Snake snake) {
        Point head = snake.getHead();
        if (fruits.containsKey(head)) {
            int value = fruits.get(head);
            snake.grow(value);
            players.get(snake.getPlayerId()).incrementScore(value);
            fruits.remove(head);
            spawnFruit();

            totalFruitsEaten++;
            if (totalFruitsEaten >= FRUITS_PER_LEVEL) {
                nextLevel();
            }
        }
    }

    private void checkCollisions(Snake snake) {
        Point head = snake.getHead();

        // Wall collision
        if (head.x < 0 || head.x >= config.getBoardWidth() || head.y < 0 || head.y >= config.getBoardHeight()) {
            isGameOver = true;
            return;
        }

        // Obstacle collision
        if (obstacles.contains(head)) {
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
        return new GameState(new ConcurrentHashMap<>(players), new ConcurrentHashMap<>(snakes), new ConcurrentHashMap<>(fruits), new ArrayList<>(obstacles), level, isGameOver);
    }
}
