package com.wilsonburhan.flappysprites.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * Created by Wilson on 10/27/14.
 */
public class FlappySpriteSprite extends View {

    private final int BLOCK_WIDTH = 200;
    private final int GROUND_HEIGHT = 200;
    private final int SUBPOINT = 61;
    private final int IMAGE_HEIGHT = 1500;
    private final int ACCELERATION = 1;
    private final int X_VELOCITY = 5;
    private final int GAP = 300;
    private boolean mLose = false;
    private RectF sprite;
    private Random rand;
    private int mScore = 0, mSubScore = 0;
    public int velocity = 0;
    public int posY = 300;
    private int posX = 0;
    private int mGroundMovement = 0;
    private Rect mTopBlock, mBottomBlock, mGround, mMovingGround;
    private int height = 300;
    private Bitmap mBottomPipe, mTopPipe, mGroundImage, mSprite;

    public FlappySpriteSprite(Context context, AttributeSet attribute) {
        super(context, attribute);
        sprite = new RectF();
        mTopBlock = new Rect();
        mBottomBlock = new Rect();
        mGround = new Rect();
        mMovingGround = new Rect();
        rand = new Random();
        mBottomPipe = BitmapFactory.decodeResource(getResources(), R.drawable.pipe_up);
        mTopPipe = BitmapFactory.decodeResource(getResources(), R.drawable.pipe_down);
        mGroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        mSprite = BitmapFactory.decodeResource(getResources(), R.drawable.sprite);
        posY = 300;
        velocity = 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        sprite.set(canvas.getWidth() / 2 - 150,
                posY - 150,
                canvas.getWidth() / 2,
                posY);
        canvas.drawBitmap(mSprite, null, sprite, null);
        /* ROTATE
        if (mVelocity < 0)
            canvas.rotate(-25, -50, -50);
        else
            canvas.rotate(25, -50, -50);

        canvas.drawBitmap(mSprite, null, oval, null);
        canvas.restore();
        */
        velocity += ACCELERATION;
        posY += velocity;

        mBottomBlock.set(canvas.getWidth() - posX,
                        height + GAP,
                        canvas.getWidth() - posX + BLOCK_WIDTH,
                        height + GAP + IMAGE_HEIGHT);

        canvas.drawBitmap(mBottomPipe, null, mBottomBlock, null);

        mTopBlock.set(canvas.getWidth() - posX,
                     height - IMAGE_HEIGHT,
                     canvas.getWidth() - posX + BLOCK_WIDTH,
                     height);
        canvas.drawBitmap(mTopPipe, null, mTopBlock, null);

        mGround.set(-mGroundMovement,
                canvas.getHeight() - 200,
                canvas.getWidth() - mGroundMovement,
                canvas.getHeight());
        canvas.drawBitmap(mGroundImage, null, mGround, null);
        mMovingGround.set(-mGroundMovement,
                canvas.getHeight() - 200,
                2 * canvas.getWidth() - mGroundMovement,
                canvas.getHeight());
        canvas.drawBitmap(mGroundImage, null, mMovingGround, null);

        posX+= X_VELOCITY;
        mGroundMovement++;

        if (mGroundMovement == canvas.getWidth())
            mGroundMovement = 0;

        if (posX == canvas.getWidth() + BLOCK_WIDTH){
            posX = 0;
            height = rand.nextInt(canvas.getHeight() - GAP - GROUND_HEIGHT);
        }

        if (sprite.bottom > canvas.getHeight() - GROUND_HEIGHT)
            mLose = true;


        if (sprite.right >= mBottomBlock.left &&
            sprite.left <= mBottomBlock.right) {
            if (sprite.bottom < mBottomBlock.top &&
                sprite.top > mTopBlock.bottom) {
                mSubScore++;
                if (mSubScore == SUBPOINT) {
                    mScore++;
                    FlappySpritesActivity.mScoreBoard.setText(Integer.toString(mScore));
                    mSubScore = 0;
                    FlappySpritesActivity.sp.play(FlappySpritesActivity.soundPoolIds[1], 1, 1, 1, 0, 1.0f);
                }
            }else {
                mLose = true;
            }
        }

        if (mLose) {
            FlappySpritesActivity.mRestart.setVisibility(View.VISIBLE);
            FlappySpritesActivity.mHighScore.setVisibility(View.VISIBLE);
            FlappySpritesActivity.mShare.setVisibility(View.VISIBLE);
            if (FlappySpritesActivity.sharedPreferences.getInt("high_score", 0) < mScore) {
                SharedPreferences.Editor edit = FlappySpritesActivity.sharedPreferences.edit();
                edit.putInt("high_score", mScore);
                edit.commit();
                FlappySpritesActivity.mNewHighScore.setVisibility(View.VISIBLE);
            }
            FlappySpritesActivity.sp.play(FlappySpritesActivity.soundPoolIds[0], 1, 1, 1, 0, 1.0f);
        }
        else
            invalidate();
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getPosY(){
        return this.posY;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }
}
