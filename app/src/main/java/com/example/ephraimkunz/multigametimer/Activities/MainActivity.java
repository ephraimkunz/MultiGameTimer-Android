package com.example.ephraimkunz.multigametimer.Activities;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ephraimkunz.multigametimer.Activities.CreateGameActivity;
import com.example.ephraimkunz.multigametimer.Activities.JoinGameActivity;
import com.example.ephraimkunz.multigametimer.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Turn on bluetooth if not already on
        if(!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            BluetoothAdapter.getDefaultAdapter().enable();
        }

        Button createGame = (Button)findViewById(R.id.createGameButton);
        createGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateGameActivity.class);
                startActivity(intent);
            }
        });

        Button joinGame = (Button)findViewById(R.id.joinGameButton);
        joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinGameActivity.class);
                startActivity(intent);
            }
        });
    }
}
