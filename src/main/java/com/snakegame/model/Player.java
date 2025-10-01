
package com.snakegame.model;

public class Player {
    private final int id;
    private final String name;
    private int score;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
        this.score = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore(int points) {
        this.score += points;
    }
}
