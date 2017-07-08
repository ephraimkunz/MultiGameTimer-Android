package com.example.ephraimkunz.multigametimer.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.ephraimkunz.multigametimer.Constants;
import com.example.ephraimkunz.multigametimer.GameCentral;
import com.example.ephraimkunz.multigametimer.R;

import java.util.Random;
import java.util.UUID;

public class CreateGameActivity extends AppCompatActivity {

    int gameId;
    GameCentral central;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        Random rand = new Random();
        gameId = rand.nextInt(900) + 100;
        UUID gameUuid = Constants.uuidFromGameId(Integer.toString(gameId));

        TextView gameIdView = (TextView)findViewById(R.id.gameIdTextView);
        gameIdView.setText(generateNewGameIdLabel(gameId));

        // Start bluetooth central
        central = GameCentral.sharedInstance();
        central.setupGame(gameUuid, this);
    }

    private String generateNewGameIdLabel(int gameId) {
        return "Game Id: " + gameId;
    }
}
