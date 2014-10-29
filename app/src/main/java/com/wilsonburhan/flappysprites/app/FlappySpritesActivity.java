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


public class FlappySpritesActivity extends Activity {

    FlappySpriteSprite mView;
    static TextView mScoreBoard;
    static Button mRestart;
    static TextView mHighScore;
    static TextView mNewHighScore;
    static Button mShare;

    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;

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

        sharedPreferences = getSharedPreferences("high_score", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        mHighScore.setText("Best -" + Integer.toString(FlappySpritesActivity.sharedPreferences.getInt("high_score", 0)));
        mRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FlappySpritesActivity.class);
                mRestart.setVisibility(View.GONE);
                mHighScore.setVisibility(View.GONE);
                mNewHighScore.setVisibility(View.GONE);
                mShare.setVisibility(View.GONE);
                startActivity(intent);
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
            FlappySpriteSprite.posY -= 50;
            FlappySpriteSprite.mVelocity = -10;
        }
        return super.onTouchEvent(event);
    }
}
