package com.wilsonburhan.flappysprites.app;

import android.content.Context;
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

    final int BLOCK_WIDTH = 200;
    final int GROUND_HEIGHT = 200;
    final int SUBPOINT = 61;
    final int IMAGE_HEIGHT = 1500;
    final int ACCELERATION = 1;
    final int X_VELOCITY = 5;
    final int GAP = 300;
    boolean mLose = false;
    RectF oval;
    Random rand;
    int mScore = 0, mSubScore = 0;
    static int mVelocity = 0;
    static int posY = 300;
    int posX = 0;
    int mGroundMovement = 0;
    Rect mTopBlock, mBottomBlock, mGround, mMovingGround;
    int height = 300;
    Bitmap mBottomPipe, mTopPipe, mGroundImage, mSprite;

    public FlappySpriteSprite(Context context, AttributeSet attribute) {
        super(context);
        oval = new RectF();
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
        mVelocity = 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        oval.set(canvas.getWidth() / 2 - 100,
                posY - 100,
                canvas.getWidth() / 2,
                posY);
        canvas.drawBitmap(mSprite, null, oval, null);
        /* ROTATE
        if (mVelocity < 0)
            canvas.rotate(-25, -50, -50);
        else
            canvas.rotate(25, -50, -50);

        canvas.drawBitmap(mSprite, null, oval, null);
        canvas.restore();
        */
        mVelocity += ACCELERATION;
        posY += mVelocity;

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

        if (oval.bottom > canvas.getHeight() - GROUND_HEIGHT)
            mLose = true;


        if (oval.right < mBottomBlock.left ||
            oval.left > mBottomBlock.right) {

        }
        else {
            if (oval.bottom < mBottomBlock.top &&
                oval.top > mTopBlock.bottom) {
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
                FlappySpritesActivity.editor.putInt("high_score", mScore);
                FlappySpritesActivity.editor.commit();
                FlappySpritesActivity.mNewHighScore.setVisibility(View.VISIBLE);
            }
            FlappySpritesActivity.sp.play(FlappySpritesActivity.soundPoolIds[0], 1, 1, 1, 0, 1.0f);
        }
        else
            invalidate();
    }
}
