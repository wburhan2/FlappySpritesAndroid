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
public class FlappySpriteSprite extends View{

    // Fields
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
    public int score = 0, mSubScore = 0;
    public int velocity = 0;
    public int posY = 300;
    private int posX = 0;
    private int mGroundMovement = 0;
    private Rect mTopBlock, mBottomBlock, mGround, mMovingGround;
    private int height = 300;
    private Bitmap mBottomPipe, mTopPipe, mGroundImage, mSprite;
    private IGameListener mGameListener;
    public boolean isRunning;

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
        posY = 600;
        velocity = 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        sprite.set(canvas.getWidth() / 2 - 130,
                posY - 130,
                canvas.getWidth() / 2,
                posY);
        canvas.drawBitmap(mSprite, null, sprite, null);

        mGround.set(-mGroundMovement,
                canvas.getHeight() - 200,
                canvas.getWidth() - mGroundMovement,
                canvas.getHeight());
        canvas.drawBitmap(mGroundImage, null, mGround, null);

        if (isRunning) {
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

            mMovingGround.set(-mGroundMovement,
                    canvas.getHeight() - 200,
                    2 * canvas.getWidth() - mGroundMovement,
                    canvas.getHeight());
            canvas.drawBitmap(mGroundImage, null, mMovingGround, null);

            posX += X_VELOCITY;
            mGroundMovement++;

            if (mGroundMovement == canvas.getWidth())
                mGroundMovement = 0;

            if (posX == canvas.getWidth() + BLOCK_WIDTH) {
                posX = 0;
                height = rand.nextInt(canvas.getHeight() - GAP - GROUND_HEIGHT - 100);
            }

            if (sprite.bottom > canvas.getHeight() - GROUND_HEIGHT)
                mLose = true;


            if (sprite.right >= mBottomBlock.left &&
                    sprite.left <= mBottomBlock.right) {
                if (sprite.bottom < mBottomBlock.top &&
                        sprite.top > mTopBlock.bottom) {
                    mSubScore++;
                    if (mSubScore == SUBPOINT) {
                        score++;
                        if (mGameListener != null) {
                            mGameListener.onScoreUpdate();
                        }
                        mSubScore = 0;
                    }
                } else {
                    mLose = true;
                }
            }

            if (mLose) {
                if (mGameListener != null)
                    mGameListener.onGameEnd();
            } else
                invalidate();
        }
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

    public void setGameListener(IGameListener gameListener){
        mGameListener = gameListener;
    }
}
