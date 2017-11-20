package com.example.skiline11.snake.engine;

import android.util.Log;

import com.example.skiline11.snake.classes.Coordinate;
import com.example.skiline11.snake.data.MyDataBaseHandler;
import com.example.skiline11.snake.enums.Direction;
import com.example.skiline11.snake.enums.GameState;
import com.example.skiline11.snake.enums.TileType;
import com.example.skiline11.snake.views.ResultView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine {
    public static final int GameWidth = 20, GameHeight = 34;

    private ResultView resultView;
    private MyDataBaseHandler myDataBaseHandler;

    private List<Coordinate> walls;
    private List<Coordinate> snake;
    private List<Coordinate> apples;

    private Direction currentDirection = Direction.East;
    private GameState currentGameState = GameState.Running;

    private boolean increaseTail = false;

    private Random rand = new Random();

    private Coordinate getSnakeHead() {
        return snake.get(0);
    }

    public GameEngine(ResultView resultView, MyDataBaseHandler myDataBaseHandler) {
        this.resultView = resultView;
        this.myDataBaseHandler = myDataBaseHandler;
    }

    public void initGame() {
        walls = new ArrayList<>();
        snake = new ArrayList<>();
        addSnake();
        addWalls();
        apples = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            addApple();
        }
    }

    public void updateDirection(Direction newDirection) {
        if(Math.abs(newDirection.ordinal() - currentDirection.ordinal()) % 2 == 1) {
            currentDirection = newDirection;
        }
    }

    private void check_if_snake_hit_the_wall() {
        for(Coordinate wall : walls) {
            if(snake.get(0).equals(wall)) {
                currentGameState = GameState.Lost;
            }
        }
    }

    private void check_if_snake_bit_himself() {
        int parts_on_snakeHeadPosition = 0;
        for(Coordinate snakePart : snake) {
            if(snakePart.equals(getSnakeHead())) {
                parts_on_snakeHeadPosition++;
            }
        }
        if(parts_on_snakeHeadPosition == 2) {
            currentGameState = GameState.Lost;
        }
    }

    private void check_if_snake_ate_some_apple() {
        Coordinate appleToRemove = null;
        for(Coordinate apple : apples) {
            if(getSnakeHead().equals(apple)) {
                appleToRemove = apple;
            }
        }
        if(appleToRemove != null) {
            resultView.increaseResult();
            apples.remove(appleToRemove);
            increaseTail = true;
            addApple();
        }
    }

    public void update() {
        switch (currentDirection) {
            case North: updateSnake(0, -1); break;
            case East: updateSnake(1, 0); break;
            case South: updateSnake(0, 1); break;
            case West: updateSnake(-1, 0); break;
        }

        check_if_snake_hit_the_wall();
        check_if_snake_bit_himself();
        check_if_snake_ate_some_apple();
    }

    public TileType[][] getMap() {
        TileType[][] map = new TileType[GameWidth][GameHeight];

        for(int x = 0; x < GameWidth; x++) {
            for(int y = 0; y < GameHeight; y++) {
                map[x][y] = TileType.Nothing;
            }
        }

        for(Coordinate s : snake) {
            map[s.getX()][s.getY()] = TileType.SnakeTail;
        }
        map[snake.get(0).getX()][snake.get(0).getY()] = TileType.SnakeHead;

        for(Coordinate wall : walls) {
            map[wall.getX()][wall.getY()] = TileType.Wall;
        }

        for(Coordinate apple : apples) {
            map[apple.getX()][apple.getY()] = TileType.Apple;
        }

        return map;
    }

    private void updateSnake(int x, int y) {
        Coordinate newCoordinate = snake.get(snake.size() - 1);
        int newX = newCoordinate.getX();
        int newY = newCoordinate.getY();


        for(int i = snake.size() - 1; i > 0; i--) {
            snake.get(i).setX(snake.get(i - 1).getX());
            snake.get(i).setY(snake.get(i - 1).getY());
        }

        if(increaseTail) {
            snake.add(new Coordinate(newX, newY));
            increaseTail = false;
        }

        snake.get(0).setX(snake.get(0).getX() + x);
        snake.get(0).setY(snake.get(0).getY() + y);
    }

    private void addSnake() {
        snake.clear();
        for(int x = 7; x >= 2; x--) {
            snake.add(new Coordinate(x, 7));
        }
    }

    private void addWalls() {
        for(int x = 0; x < GameWidth; x++) {
            walls.add(new Coordinate(x, 0));
            walls.add(new Coordinate(x,GameHeight - 1));
        }

        for(int y = 0; y < GameHeight; y++) {
            walls.add(new Coordinate(0, y));
            walls.add(new Coordinate(GameWidth - 1, y));
        }
    }

    private void addApple() {
        boolean collision = true;
        int x = 0, y = 0;
        while(collision) {
            collision = false;
            x = 1 + rand.nextInt(GameWidth - 2);
            y = 1 + rand.nextInt(GameHeight - 2);
            Coordinate newCoordinate = new Coordinate(x, y);
            for(Coordinate c : snake) {
                if(newCoordinate.equals(c)) {
                    collision = true;
                    break;
                }
            }
            for(Coordinate c : apples) {
                if(newCoordinate.equals(c)) {
                    collision = true;
                    break;
                }
            }
        }
        apples.add(new Coordinate(x, y));
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }

    public void setCurrentGameStateRunning() { this.currentGameState = GameState.Running;}

}
