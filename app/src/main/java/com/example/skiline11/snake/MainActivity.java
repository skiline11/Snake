package com.example.skiline11.snake;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skiline11.snake.data.MyDataBaseHandler;
import com.example.skiline11.snake.engine.GameEngine;
import com.example.skiline11.snake.enums.Direction;
import com.example.skiline11.snake.enums.GameState;
import com.example.skiline11.snake.views.ResultView;
import com.example.skiline11.snake.views.SnakeView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    private GameEngine gameEngine;
    private SnakeView snakeView;
    private ResultView resultView;
    private TextView resultTextView, maxResultTextView;
    private final Handler handler = new Handler();
    private final long updateDelay = 500;
    private float prevX, prevY;
    private MyDataBaseHandler myDataBaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = (TextView) findViewById(R.id.Result);
        maxResultTextView = (TextView) findViewById(R.id.MaxResult);

        myDataBaseHandler = new MyDataBaseHandler(this, null, null, 1);

        resultView = (ResultView) findViewById(R.id.resultView);
        resultView.addComponents(resultTextView, maxResultTextView, myDataBaseHandler);

        final Button restartButton = (Button) findViewById(R.id.RestartButton);
        resultView.setRestartButton(restartButton);
        resultView.resetResult();

        gameEngine = new GameEngine(resultView, myDataBaseHandler);
        gameEngine.initGame();

        restartButton.setOnClickListener(new View.OnClickListener() {
            /* When clicked then we need to
            - set currentGameState to Running
            - make restartButton invisible
            - reset result to 0
            - init game again, to render new map
             */
            @Override
            public void onClick(View v) {
                gameEngine.setCurrentGameStateRunning();
                restartButton.setVisibility(View.INVISIBLE);
                resultView.resetResult();
                gameEngine.initGame();
                startUpdateHandler();
            }
        });

        snakeView = (SnakeView) findViewById(R.id.snakeView);
        snakeView.setOnTouchListener(this);

        startUpdateHandler();
    }

    private void startUpdateHandler() {
        // This function, after updateDelay milisecond, execute one move, and if game doesn't end up, recursivly execute next move
        // otherwise it shows end game notification, and give possibility to restart game
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameEngine.update();
                if(gameEngine.getCurrentGameState() == GameState.Running) {
                    // Notice recursion
                    handler.postDelayed(this, updateDelay);
                }
                if(gameEngine.getCurrentGameState() == GameState.Lost) {
                    onGameLost();
                }

                snakeView.setSnakeViewMap(gameEngine.getMap());
                snakeView.invalidate();
                resultView.invalidate();
            }
        }, updateDelay);
    }

    private void onGameLost() {
        // This function shows end game notification, and give possibility to restart game
        Button restartButton = (Button) findViewById(R.id.RestartButton);
        restartButton.setVisibility(View.VISIBLE);
        Toast.makeText(this, "You lost", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // This function handle touch events
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                prevX = event.getX();
                prevY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float newX = event.getX();
                float newY = event.getY();

                if(Math.abs(newX - prevX) > Math.abs(newY - prevY)) { // horizontal move (left - right)
                    if(newX > prevX) gameEngine.updateDirection(Direction.East);
                    else gameEngine.updateDirection(Direction.West);
                } else { // vertival move (up - down)
                    if(newY > prevY) gameEngine.updateDirection(Direction.South);
                    else gameEngine.updateDirection(Direction.North);
                }
                break;
        }
        return true;
    }
}
