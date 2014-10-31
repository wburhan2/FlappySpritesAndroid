package com.wilsonburhan.flappysprites.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FlappySpritesActivity extends Activity{

    private FlappySpriteSprite mView;
    private TextView mScoreBoard;
    private Button mRestart;
    private TextView mHighScore;
    private TextView mNewHighScore;
    private Button mShare;
    private ImageView mTapToStart;

    private SharedPreferences sharedPreferences;
    private SoundPool sp;
    private int[] soundPoolIds= new int[5];
    private boolean isRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flappy_sprites_activity);

        mView = (FlappySpriteSprite)findViewById(R.id.sprite);
        mScoreBoard = (TextView)findViewById(R.id.score_board);

        mHighScore = (TextView)findViewById(R.id.high_score);
        mNewHighScore = (TextView)findViewById(R.id.new_high_score);
        mRestart = (Button)findViewById(R.id.restart_button);
        mShare = (Button)findViewById(R.id.share);
        mTapToStart = (ImageView)findViewById(R.id.tap_to_start);

        init();

        /* Only for API 21+
        sp = new SoundPool.Builder().
                setMaxStreams(5).
                setAudioAttributes(
                        new AudioAttributes.Builder().
                                setLegacyStreamType(AudioManager.STREAM_MUSIC).build()).build();*/
        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundPoolIds[0] = sp.load(this, R.raw.sfx_hit, 1);
        soundPoolIds[1] = sp.load(this, R.raw.sfx_point, 1);
        soundPoolIds[2] = sp.load(this, R.raw.sfx_wing, 1);
        soundPoolIds[3] = sp.load(this, R.raw.sfx_die, 1);

        sharedPreferences = getSharedPreferences("high_score", MODE_PRIVATE);
        mHighScore.setText("Best -" + Integer.toString(sharedPreferences.getInt("high_score", 0)));

        helper();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isRunning) {
                mView.isRunning = true;
                mTapToStart.setVisibility(View.GONE);
                mView.invalidate();
                mView.setPosY(mView.getPosY() - 50);
                mView.setVelocity(-10);
                sp.play(soundPoolIds[2], 1, 1, 1, 0, 1.0f);
            }
        }
        return super.onTouchEvent(event);
    }

    private void init(){
        mHighScore.setVisibility(View.GONE);
        mNewHighScore.setVisibility(View.GONE);
        mRestart.setVisibility(View.GONE);
        mShare.setVisibility(View.GONE);
    }

    private void helper(){
        // Restart button
        mRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FlappySpritesActivity.class);
                mRestart.setVisibility(View.GONE);
                mHighScore.setVisibility(View.GONE);
                mNewHighScore.setVisibility(View.GONE);
                mShare.setVisibility(View.GONE);
                startActivity(intent);
                sp.play(soundPoolIds[3], 1, 1, 1, 0, 1.0f);
                finish();
            }
        });

        // Share button
        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "Check out my high score! You jelly bro? " + Integer.toString(sharedPreferences.getInt("high_score", 0)));
                startActivity(Intent.createChooser(share, "Share your score!"));
            }
        });

        // Listener
        mView.setGameListener(new IGameListener() {
            @Override
            public void onScoreUpdate() {
                mScoreBoard.setText(Integer.toString(mView.score));
                sp.play(soundPoolIds[1], 1, 1, 1, 0, 1.0f);
            }

            @Override
            public void onGameEnd() {
                mRestart.setVisibility(View.VISIBLE);
                mHighScore.setVisibility(View.VISIBLE);
                mShare.setVisibility(View.VISIBLE);
                sp.play(soundPoolIds[0], 1, 1, 1, 0, 1.0f);

                isRunning = false;

                if (sharedPreferences.getInt("high_score", 0) < mView.score) {
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putInt("high_score", mView.score);
                    edit.commit();
                    mNewHighScore.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
