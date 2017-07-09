package com.example.ephraimkunz.multigametimer;

import android.os.CountDownTimer;

/**
 * Created by ephraimkunz on 7/9/17.
 */

public abstract class GameTimer {

    private CountDownTimer cdt;
    private long millisInFuture;
    private long countDownInterval;
    private long incrementMillis;
    private boolean isPaused;

    public GameTimer(long millisInFuture, long countDownInterval, long incrementMillis) {
        this.millisInFuture = millisInFuture;
        this.countDownInterval = countDownInterval;
        this.incrementMillis = incrementMillis;
        isPaused = true;

        recreateCounter(millisInFuture, countDownInterval);
    }

    public abstract void onFinish();

    public abstract void onTick(long millisUntilFinished);

    public void start(){
        isPaused = false;
        onIncrement(incrementMillis);
        cdt.start();
    }

    public void pause() {
        isPaused = true;
        cdt.cancel();
    }

    public boolean isPaused() {
        return this.isPaused;
    }

    private void setMillisInFuture(long millisInFuture){
        this.millisInFuture = millisInFuture;
    }

    public void onIncrement(long millis){
        millisInFuture += millis;
        recreateCounter(millisInFuture, countDownInterval);
    }

    private void recreateCounter(long millisInFuture, long countDownInterval){
        if(cdt != null){
            try {
                cdt.cancel();
            } catch (Exception e) {
            }
        }

        cdt = new CountDownTimer(millisInFuture, countDownInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                setMillisInFuture(millisUntilFinished);
                GameTimer.this.onTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                GameTimer.this.onFinish();
            }
        };
    }
}
