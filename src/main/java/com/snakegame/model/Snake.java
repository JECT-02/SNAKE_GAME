
package com.snakegame.model;

import java.awt.Point;
import java.util.LinkedList;

public class Snake {
    private final int playerId;
    private final LinkedList<Point> body;
    private Direction direction;

    public Snake(int playerId, Point start, int initialSize) {
        this.playerId = playerId;
        this.body = new LinkedList<>();
        for (int i = 0; i < initialSize; i++) {
            body.add(new Point(start.x - i, start.y));
        }
        this.direction = Direction.RIGHT;
    }

    public void move() {
        Point head = body.getFirst();
        Point newHead = new Point(head);

        switch (direction) {
            case UP: newHead.y--; break;
            case DOWN: newHead.y++; break;
            case LEFT: newHead.x--; break;
            case RIGHT: newHead.x++; break;
        }

        body.addFirst(newHead);
        body.removeLast();
    }

    public void grow() {
        Point tail = body.getLast();
        body.addLast(new Point(tail));
    }

    public LinkedList<Point> getBody() {
        return body;
    }

    public Point getHead() {
        return body.getFirst();
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        // Prevent the snake from reversing on itself
        if (this.direction == Direction.UP && direction == Direction.DOWN) return;
        if (this.direction == Direction.DOWN && direction == Direction.UP) return;
        if (this.direction == Direction.LEFT && direction == Direction.RIGHT) return;
        if (this.direction == Direction.RIGHT && direction == Direction.LEFT) return;
        this.direction = direction;
    }

    public int getPlayerId() {
        return playerId;
    }
}
