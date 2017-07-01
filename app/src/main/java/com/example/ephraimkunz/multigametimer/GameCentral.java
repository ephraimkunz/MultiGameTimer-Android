package com.example.ephraimkunz.multigametimer;

/**
 * Created by ephraimkunz on 7/1/17.
 */

public class GameCentral {
    private static GameCentral instance;

    private GameCentral() {

    }

    public static GameCentral sharedInstance() {
        if (instance == null) {
            instance = new GameCentral();
        }
        return instance;
    }
}
