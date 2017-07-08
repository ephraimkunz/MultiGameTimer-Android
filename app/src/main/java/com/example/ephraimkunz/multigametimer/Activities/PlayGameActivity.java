package com.example.ephraimkunz.multigametimer.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.ephraimkunz.multigametimer.GamePeripheral;
import com.example.ephraimkunz.multigametimer.GamePlayPeripheralDelegate;
import com.example.ephraimkunz.multigametimer.R;

public class PlayGameActivity extends AppCompatActivity implements GamePlayPeripheralDelegate {
    public final static String START_TIME = "StartTime";
    public final static String INCREMENT = "Increment";
    public final static String IS_CENTRAL = "IsCentral";

    private Button countdownButton;
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
        countdownButton.setEnabled(false);
        countdownButton.setText(formatButtonText(startTime));

        if(isCentral) {

        } else {
            GamePeripheral.sharedInstance().setGamePlayDelegate(this);
        }
    }

    private String formatButtonText(int timeInSec) {
        int hours = timeInSec / 3600;
        int minutes = timeInSec / 60 % 60;
        int seconds = timeInSec % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
