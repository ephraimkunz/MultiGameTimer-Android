package com.example.ephraimkunz.multigametimer.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ephraimkunz.multigametimer.GamePeripheral;
import com.example.ephraimkunz.multigametimer.GamePlayPeripheralDelegate;
import com.example.ephraimkunz.multigametimer.GameTimer;
import com.example.ephraimkunz.multigametimer.R;

public class PlayGameActivity extends AppCompatActivity implements GamePlayPeripheralDelegate {
    public final static String START_TIME = "StartTime";
    public final static String INCREMENT = "Increment";
    public final static String IS_CENTRAL = "IsCentral";

    private Button countdownButton;
    private GameTimer timer;
    private int startTime;
    private int increment;
    private boolean isCentral; // Denotes whether this instance is a master or central

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        Intent intent = getIntent();
        startTime = intent.getExtras().getInt(START_TIME);
        increment = intent.getExtras().getInt(INCREMENT);
        isCentral = intent.getExtras().getBoolean(IS_CENTRAL);

        countdownButton = (Button) findViewById(R.id.countdownButton);
        countdownButton.setEnabled(true);
        countdownButton.setText(formatButtonText(startTime));

        if(isCentral) {

        } else {
            GamePeripheral.sharedInstance().setGamePlayDelegate(this);
        }

        timer = new GameTimer(startTime * 1000, 1 * 1000, increment * 1000) {
            @Override
            public void onFinish() {

            }

            @Override
            public void onTick(long millisUntilFinished) {
                countdownButton.setText(formatButtonText((int)(millisUntilFinished / 1000)));

            }
        };

        countdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timer.isPaused()) {
                    timer.start();
                } else {
                    timer.pause();
                }
            }
        });
    }

    private String formatButtonText(int timeInSec) {
        int hours = timeInSec / 3600;
        int minutes = timeInSec / 60 % 60;
        int seconds = timeInSec % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
