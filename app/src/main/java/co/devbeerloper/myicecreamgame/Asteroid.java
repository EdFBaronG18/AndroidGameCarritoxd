package co.devbeerloper.myicecreamgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
public class Asteroid {

    public static final int SPRITE_SIZE_WIDTH =100;
    public static final int SPRITE_SIZE_HEIGTH=100;
    public static final float GRAVITY_FORCE=10;
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;
    private final int image1 = R.drawable.asteroide1;
    private final int image2 = R.drawable.asteroide2;
    private float maxY;
    private float maxX;
    private float width;
    private float heigth;


    private float speed = 0;
    private float positionX;
    private float positionY;
    private Bitmap spriteAsteroid;
    private boolean dead = false;

    public Asteroid(Context context, float screenWidth, float screenHeigth, boolean ver, int speed, float y){
        this.speed = speed;
        positionX = screenWidth;
        positionY = y;

        this.width = SPRITE_SIZE_WIDTH *(ver? 2: 1);
        this.heigth = SPRITE_SIZE_HEIGTH *(ver? 2: 1);
        //Getting bitmap from resource
        Bitmap originalBitmap= BitmapFactory.decodeResource(context.getResources(), ver? image1: image2);
        spriteAsteroid  = Bitmap.createScaledBitmap(originalBitmap, SPRITE_SIZE_WIDTH *(ver? 2: 1), SPRITE_SIZE_HEIGTH *(ver? 2: 1), false);

        this.maxX = screenWidth - (spriteAsteroid.getWidth()/2.0f);
        this.maxY = screenHeigth - spriteAsteroid.getHeight();
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
        return spriteAsteroid;
    }

    public void setSprite(Bitmap spriteIcecreamCar) {
        this.spriteAsteroid = spriteIcecreamCar;
    }

    public Rectangle getRectangle(){
        return new Rectangle(this.positionX, this.positionY, this.width, this.heigth);
    }

    /**
     * Control the position and behaviour of the icecream car
     * @param level
     * @param h
     * @param freeze
     */
    public void updateInfo(float level, float h, boolean freeze) {
        this.positionX -= (freeze ? 5: speed*level);
        if (positionX < 0) {
            positionX = this.maxX;
            positionY = h;
        }
    }

    public void kill(float y) {
        this.positionX = this.maxX;
        this.positionY = y;
    }

    public boolean isDead() {
        return dead;
    }
}
