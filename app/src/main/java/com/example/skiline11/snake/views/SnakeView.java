package com.example.skiline11.snake.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.skiline11.snake.enums.TileType;

/**
 * Created by skiline11 on 11.11.17.
 */

public class SnakeView extends View {

    private Paint mPaint = new Paint();
    private TileType snakeViewMap[][];

    public SnakeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSnakeViewMap(TileType[][] map) {
        this.snakeViewMap = map;
    }

//    public int getStatusBarHeight() {
//        int result = 0;
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            result = getResources().getDimensionPixelSize(resourceId);
//        }
//        return result;
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(snakeViewMap != null) {
            float result_view_height = (float)canvas.getHeight() * 0.05f;
            float tileSizeX = (float)canvas.getWidth() / (float)snakeViewMap.length;
            float tileSizeY = ((float)canvas.getHeight() - result_view_height) / (float)snakeViewMap[0].length;

            float circleSize = Math.min(tileSizeX, tileSizeY) / 2;
            float sizeX = 0, sizeY = 0;

            for(int x = 0; x < snakeViewMap.length; x++) {
                for(int y = 0; y < snakeViewMap[0].length; y++) {
                    switch (snakeViewMap[x][y]) {
                        case Nothing:
                            mPaint.setColor(Color.WHITE);
                            break;
                        case Wall:
                            mPaint.setColor(Color.BLACK);
                            break;
                        case SnakeHead:
                            mPaint.setColor(Color.RED);
                            break;
                        case SnakeTail:
                            mPaint.setColor(Color.GREEN);
                            break;
                        case Apple:
                            mPaint.setColor(Color.RED);
                            break;
                    }

                    sizeX = x * tileSizeX + tileSizeX / 2f;
                    sizeY = y * tileSizeY + tileSizeY / 2f + result_view_height;

                    canvas.drawCircle(sizeX, sizeY, circleSize, mPaint);
                }
            }
        }
    }
}
