package co.devbeerloper.myicecreamgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Kid {

    public static final float INIT_X = 2100;
    public static final float INIT_Y = 0;
    public static final int SPRITE_SIZE_WIDTH = 150;
    public static final int SPRITE_SIZE_HEIGTH = 200;
    private final int MIN_SPEED = 4;
    private final int MAX_SPEED = 20;

    private float maxY;
    private float maxX;

    private float speed = 0;
    private float positionX;
    private float positionY;
    private Bitmap spriteKid;

    public Kid (Context context, float screenWidth, float screenHeigth) {
        this.speed = 15;
        positionX = this.INIT_X;
        positionY = this.INIT_Y;
        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.kid);
        spriteKid = Bitmap.createScaledBitmap(originalBitmap, SPRITE_SIZE_WIDTH, SPRITE_SIZE_HEIGTH, false);

        this.maxX = screenWidth - (spriteKid.getWidth()/2);
        this.maxY = screenHeigth - spriteKid.getHeight();
    }

    public Kid (Context context, float initialX, float initialY, float screenWidth, float screenHeigth) {

        speed = 1;
        positionX = initialX;
        positionY = initialY;

        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.kid);
        spriteKid = Bitmap.createScaledBitmap(originalBitmap, SPRITE_SIZE_WIDTH, SPRITE_SIZE_HEIGTH, false);

        this.maxX = screenWidth - (spriteKid.getWidth() / 2);
        this.maxY = screenHeigth - spriteKid.getHeight();

    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public Bitmap getSpriteKid() {
        return spriteKid;
    }

    public void updateInfo () {
        this.positionX -= speed;
        Log.i("Edward", this.positionX + "");
        if (positionX < 0) {
            positionX = this.maxX;
        }
    }
}
