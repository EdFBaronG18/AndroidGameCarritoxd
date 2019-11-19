# Android_2DGame - ICE CREAM GAME
This is an example of a simple 2D game creation with android (java)

The rules of the game are:
1. The player (IceCream Car) has to give icecreams to the kids by collide them
2. The player has to avoid to collide with the parents that appear in the game, if a parent collide with the player the game is over 


## Tutorial - Step by step
Follow the next steps to replicate this game
### 1. Integrate the assets to the project
Put the assets (images) inside the folder drawable 
### 2. Add the general theme of the game
Configure a new style inside the res/values/styles.xml file. 

    <!-- General Theme of the app -->
    <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
        <item name="windowActionBar">false</item>
        <item name="android:windowFullscreen">true</item>
        <item name="android:windowContentOverlay">@null</item>
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
        <item name="windowNoTitle">true</item>
        
    </style>

### 3. Configure the colors in the res/values/colors.xml

    <color name="colorPrimary">#DC22EB</color>
    <color name="colorPrimaryDark">#00574B</color>
    <color name="colorAccent">#D81B60</color>

### 4. Create your menu

In the main activity place an image and the play button, create a method to manage the action of the button, this method has to redirect to a new activity.

### 5. Create the Gameplay activity

### 6. Create a SurfaceView to paint the game

- Create a new class that inherits from SurfaceView and implements the Runnable interface. 
- Add the methods to manage and control the execution (pause, resume, paint, updateInfo)
- Create an attribute to paint in (Canvas) and a painter (Paint) and initilize them in the constructor
- Create a holder to manage the SurfaceView
- Inside this class implement the run method and a thread to manage the execution of the game.
- Add a boolean attribute to control the execution
- Add an attribute for your player
 `
package co.devbeerloper.myicecreamgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurfaceView extends SurfaceView implements Runnable {

    private boolean isPlaying;
    private IceCreamCar icecreamCar;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder holder;
    private Thread gameplayThread = null;

    /**
     * Contructor
     * @param context
     */
    public GameSurfaceView(Context context) {
        super(context);
        icecreamCar = new IceCreamCar(context);
        paint = new Paint();
        holder = getHolder();
        isPlaying = true;
    }

    /**
     * Method implemented from runnable interface
     */
    @Override
    public void run() {
        while (isPlaying) {
            updateInfo();
            paintFrame();

        }

    }

    private void updateInfo() {
        // icecreamCar.updateInfo ();
    }

    private void paintFrame() {
        if (holder.getSurface().isValid()){
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.CYAN);
            canvas.drawBitmap(icecreamCar.getSpriteIcecreamCar(),icecreamCar.getPositionX(),icecreamCar.getPositionY(),paint);
            holder.unlockCanvasAndPost(canvas);
        }

    }


    public void pause() {
        isPlaying = false;
        try {
            gameplayThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public void resume() {

        isPlaying = true;
        gameplayThread = new Thread(this);
        gameplayThread.start();
    }
}

`

### 7.Create a class for our main character (the icecream car)

It class has the attributes of the position where the character is and the image that is going to be rendered in the canvas
 `
package co.devbeerloper.myicecreamgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class IceCreamCar {

    public static final float INIT_X =100;
    public static final float INIT_Y =100;
    public static final int SPRITE_SIZE_WIDTH =600;
    public static final int SPRITE_SIZE_HEIGTH=500;


    private float speed = 0;
    private float positionX;
    private float positionY;
    private Bitmap spriteIcecreamCar;

    public IceCreamCar (Context context){

        speed = 1;
        positionX = this.INIT_X;
        positionY = this.INIT_Y;

        //Getting bitmap from drawable resource
        Bitmap originalBitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.icecreamcar);
        spriteIcecreamCar  = Bitmap.createScaledBitmap(originalBitmap, SPRITE_SIZE_WIDTH, SPRITE_SIZE_HEIGTH, false);

    }

    public IceCreamCar (Context context, float initialX, float initialY){

        speed = 1;
        positionX = initialX;
        positionY = initialY;

        //Getting bitmap from drawable resource
        spriteIcecreamCar = BitmapFactory.decodeResource(context.getResources(), R.drawable.icecreamcar);

    }

    public static float getInitX() {
        return INIT_X;
    }

    public static float getInitY() {
        return INIT_Y;
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

    public Bitmap getSpriteIcecreamCar() {
        return spriteIcecreamCar;
    }

    public void setSpriteIcecreamCar(Bitmap spriteIcecreamCar) {
        this.spriteIcecreamCar = spriteIcecreamCar;
    }

    public void updateInfo () {

    }
}

 `
### 8. Add the surface created to the gameplay activity
 `
public class GamePlay extends AppCompatActivity {

    private GameSurfaceView gameSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        gameSurfaceView = new GameSurfaceView(this);
        setContentView(gameSurfaceView);

    }


    @Override
    protected void onPause() {
        super.onPause();
        gameSurfaceView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameSurfaceView.resume();
    }
}
 `

### 9. Add the controls of the game in the GameSurfaceView


 `
 /**
     * Detect the action of the touch event
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                System.out.println("TOUCH UP - STOP JUMPING");
                icecreamCar.setJumping(false);
                break;
            case MotionEvent.ACTION_DOWN:
                System.out.println("TOUCH DOWN - JUMP");
                icecreamCar.setJumping(true);
                break;
        }
        return true;
    }


 `

### 10. Implement the method updateInfo in the IcecreamCar class

- Create the needed variables and constats 

 /**
     * Control the position and behaviour of the icecream car
     */
    public void updateInfo () {

        if (isJumping) {
            speed += 5;
        } else {
            speed -= 5;
        }

        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }
        this.positionY -= speed - GRAVITY_FORCE;

        if (positionY < 0) {
            positionY = 0;
        }
        if (positionY > maxY) {
            positionY = maxY;
        }




    }

## Assets taken from
CAR SPRITE:  www.lonegames.net https://opengameart.org/content/ice-cream-car
KID https://opengameart.org/content/pixel-kid
PARENT 
