package com.wilsonburhan.flappysprites.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class FlappySpritesActivity extends Activity{

    private FlappySpriteSprite mView;
    public static TextView mScoreBoard;
    public static Button mRestart;
    public static TextView mHighScore;
    public static TextView mNewHighScore;
    public static Button mShare;

    public static SharedPreferences sharedPreferences;
    public static SoundPool sp;
    public static int[] soundPoolIds= new int[5];

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
        mHighScore.setVisibility(View.GONE);
        mNewHighScore.setVisibility(View.GONE);
        mRestart.setVisibility(View.GONE);
        mShare.setVisibility(View.GONE);

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

        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "Check out my high score!" + Integer.toString(sharedPreferences.getInt("high_score", 0)));
                startActivity(Intent.createChooser(share, "Share your score!"));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.flappy_sprites, menu);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mView.setPosY(mView.getPosY() - 50);
            mView.setVelocity(-10);
            sp.play(soundPoolIds[2], 1, 1, 1, 0, 1.0f);
        }
        return super.onTouchEvent(event);
    }
}
