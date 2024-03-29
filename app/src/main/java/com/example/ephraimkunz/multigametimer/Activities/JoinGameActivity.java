package com.example.ephraimkunz.multigametimer.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ephraimkunz.multigametimer.Fragments.JoinGameFragment;
import com.example.ephraimkunz.multigametimer.GamePeripheral;
import com.example.ephraimkunz.multigametimer.GameSetupPeripheralDelegate;
import com.example.ephraimkunz.multigametimer.R;
import com.example.ephraimkunz.multigametimer.Fragments.WaitFragment;

public class JoinGameActivity extends AppCompatActivity implements JoinGameFragment.JoinGameFragmentListener, GameSetupPeripheralDelegate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        FragmentManager fragmentManager = getSupportFragmentManager();
        JoinGameFragment fragment = JoinGameFragment.newInstance();
        fragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onGameJoinTapped(String gameId) {
        if (gameId == null || gameId.length() != 3) {
            return;
        }

        // Start advertising services and characteristics
        GamePeripheral.sharedInstance().setGameSetupDelegate(this);
        GamePeripheral.sharedInstance().advertiseForGameId(gameId, this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        WaitFragment fragment = WaitFragment.newInstance();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    // GameSetupPeripheralDelegate
    @Override
    public void gameDidStart(int start, int increment) {
        Intent intent = new Intent(this, PlayGameActivity.class);
        intent.putExtra(PlayGameActivity.START_TIME, start);
        intent.putExtra(PlayGameActivity.INCREMENT, increment);
        intent.putExtra(PlayGameActivity.IS_CENTRAL, false);

        startActivity(intent);
    }
}
