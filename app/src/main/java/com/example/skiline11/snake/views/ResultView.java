package com.example.skiline11.snake.views;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.skiline11.snake.data.MyDataBaseHandler;

public class ResultView extends View {
    private int result = 0;
    private boolean firstCall_onDrawMethod = true;
    private boolean inc_result = false;
    private TextView resultTextView, maxResultTextView;
    private MyDataBaseHandler myDataBaseHandler;
    private Button restartButton;


    public ResultView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void addComponents(TextView resultTextView, TextView maxResultTextView, MyDataBaseHandler myDataBaseHandler)
    {
        this.resultTextView = resultTextView;
        this.maxResultTextView = maxResultTextView;
        this.myDataBaseHandler = myDataBaseHandler;
    }

    public void setRestartButton(Button restartButton) {
        this.restartButton = restartButton;
    }

    private void setComponents(Canvas canvas) {
        // this function setComponents sizes and positions, at first call of onDraw methodc
        float result_view_height = (float)canvas.getHeight() * 0.05f;
        float result_view_width = (float)canvas.getWidth();

        resultTextView.setText("Result : 0");
        resultTextView.setWidth((int) (result_view_width / 2));
        resultTextView.setHeight((int) result_view_height);
        resultTextView.setX(0);
        resultTextView.setY(0);

        maxResultTextView.setText("Max Result : " + myDataBaseHandler.getMaxResult());
        maxResultTextView.setWidth((int) (result_view_width / 2));
        maxResultTextView.setHeight((int) result_view_height);
        maxResultTextView.setX(result_view_width / 2f);
        maxResultTextView.setY(0);

        restartButton.setHeight((int)(canvas.getWidth() * 0.3));
        restartButton.setWidth((int)(canvas.getWidth() * 0.6));
        restartButton.setX((float)(canvas.getWidth() / 2f - canvas.getWidth() * 0.3));
        restartButton.setY((float)(canvas.getHeight() / 2f - canvas.getWidth() * 0.15));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // ATTENTION : this function execute at the end of every move
        super.onDraw(canvas);
        if(firstCall_onDrawMethod) {
            // setComponents at first call of onDraw method
            setComponents(canvas);
            firstCall_onDrawMethod = false;
        }
        if(this.inc_result) {
            // if result changed we need to change value in TextView, and increase maxResult if result > maxResult
            this.inc_result = false;
            this.result++;
            if(this.result > myDataBaseHandler.getMaxResult()) {
                Log.i("*", "Nowy rezultat jest większy");
                myDataBaseHandler.setMaxResult(this.result);
            }
            this.resultTextView.setText("Result : " + this.result);
            this.maxResultTextView.setText("Max Result : " + myDataBaseHandler.getMaxResult());
            Log.i("*", "Max result = " + myDataBaseHandler.getMaxResult());
            this.maxResultTextView.invalidate();
        }
    }

    public void increaseResult() {
        // Thanks to this, next calling onDraw method will cause increase result on screen
        this.inc_result = true;
    }

    public void resetResult() {
        // This reset result value to 0, to prepare for next round
        this.result = 0;
        this.resultTextView.setText("Result : " + this.result);
    }
}
