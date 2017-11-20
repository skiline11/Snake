package com.example.skiline11.snake.data;

/**
 * Created by skiline11 on 11.11.17.
 */

public class MaxResults {
    private int id;
    private int maxResult;

    public MaxResults(int id) {
        this.id = id;
        this.maxResult = 0;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }

    public int getId() {
        return id;
    }
}
