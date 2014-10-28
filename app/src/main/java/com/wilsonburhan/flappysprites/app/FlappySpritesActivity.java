package com.wilsonburhan.flappysprites.app;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class FlappySpritesActivity extends Activity {

    FlappySpriteSprite mView;
    RelativeLayout mLinearLayout;
    static TextView mScoreBoard;
    RelativeLayout.LayoutParams lp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = new FlappySpriteSprite(this);
        mLinearLayout = new RelativeLayout(this);
        mScoreBoard = new TextView(this);
        mScoreBoard.setTextSize(25);
        mScoreBoard.setGravity(Gravity.CENTER);
        lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 100;
        mScoreBoard.setLayoutParams(lp);
        mLinearLayout.addView(mView);
        mLinearLayout.addView(mScoreBoard);
        setContentView(mLinearLayout);
        mScoreBoard.setText("0");
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
            mView.posY -= 50;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
