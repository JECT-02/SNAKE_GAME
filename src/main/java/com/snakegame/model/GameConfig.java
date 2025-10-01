
package com.snakegame.model;

public class GameConfig {
    private final int boardWidth;
    private final int boardHeight;
    private final int snakeInitialSize;
    private final int gameSpeedMillis;

    public GameConfig(int boardWidth, int boardHeight, int snakeInitialSize, int gameSpeedMillis) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.snakeInitialSize = snakeInitialSize;
        this.gameSpeedMillis = gameSpeedMillis;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public int getSnakeInitialSize() {
        return snakeInitialSize;
    }

    public int getGameSpeedMillis() {
        return gameSpeedMillis;
    }
}
