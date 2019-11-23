package co.devbeerloper.myicecreamgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Coin {
    public static final int SPRITE_SIZE_WIDTH =50;
    public static final int SPRITE_SIZE_HEIGTH=50;
    public static final float GRAVITY_FORCE=10;
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;
    private final int image = R.drawable.coin;
    private float maxY;
    private float maxX;
    private float width;
    private float heigth;


    private float speed = 0;
    private float positionX;
    private float positionY;
    private Bitmap spriteCoin;
    private boolean dead = false;

    public Coin(Context context, float screenWidth, float screenHeigth, float i){
        speed = 5;
        positionX = screenWidth;
        positionY = i;

        this.width = SPRITE_SIZE_WIDTH;
        this.heigth = SPRITE_SIZE_HEIGTH;
        //Getting bitmap from resource
        Bitmap originalBitmap= BitmapFactory.decodeResource(context.getResources(), image);
        spriteCoin  = Bitmap.createScaledBitmap(originalBitmap, SPRITE_SIZE_WIDTH , SPRITE_SIZE_HEIGTH, false);

        this.maxX = screenWidth - (spriteCoin.getWidth()/2);
        this.maxY = screenHeigth - spriteCoin.getHeight();
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

    public Bitmap getSprite() {
        return spriteCoin;
    }

    public void setSprite(Bitmap spriteIcecreamCar) {
        this.spriteCoin= spriteIcecreamCar;
    }

    public Rectangle getRectangle(){
        return new Rectangle(this.positionX, this.positionY, this.width, this.heigth);
    }

    /**
     * Control the position and behaviour of the icecream car
     * @param nextY
     */
    public void updateInfo(float nextY) {
        this.positionX -= speed;
        if (positionX < 0) {
            positionX = this.maxX;
            positionY = nextY;
        }
    }

    public void kill(float y){
        this.positionX = this.maxX;
        this.positionY = y;
    }

    public boolean isDead() {
        return this.dead;
    }

    public void setDead(boolean b) {
        dead = b;
    }
}

