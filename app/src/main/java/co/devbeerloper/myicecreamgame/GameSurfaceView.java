package co.devbeerloper.myicecreamgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GameSurfaceView extends SurfaceView implements Runnable {

    private boolean isPlaying;
    private Ship ship;
    private ArrayList<Asteroid> asteroids;
    private ArrayList<Coin> coins;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder holder;
    private Thread gameplayThread = null;

    private long timeCoin;
    private long timeAsteroid;

    private Context context;
    private float screenWith;
    private float screenHeight;
    private boolean firstTime;


    private final int numberAsteroids = 8;
    private final int numberCoins = 10;

    private int limitAsteroids = 3;
    private int limitCoins = 10;

    private int currentAsteroids;
    private int currentCoins;


    private boolean coinLoaded;
    private boolean asteroidLoaded;

    private float randomSet[];

    private Random random;

    private Snow snow;
    private boolean freeze;
    private long freezeTime;

    private int score;
    private int level;

    private int randomSize = 10000;
    private int randomIndex = 0;

    private double posibilitySnow = 0.99;

    /**
     * Contructor
     *
     * @param context
     */
    public GameSurfaceView(Context context, float screenWith, float screenHeight) {
        super(context);

        // Start the game in pause
        firstTime = true;
        isPlaying = false;
        random = new Random(System.currentTimeMillis());

        snow = null;

        this.asteroids = new ArrayList<Asteroid>();
        this.coins = new ArrayList<Coin>();

        asteroidLoaded = false;
        coinLoaded = false;

        currentAsteroids = 0;
        currentCoins = 0;

        this.context = context;
        this.screenWith = screenWith;
        this.screenHeight = screenHeight;

        randomSet = new float[randomSize];
        for (int i = 0; i < randomSet.length; i++)
            randomSet[i] = generateH();

        fillArrays();

        level = 0;
        score = 0;

        ship = new Ship(context, screenWith, screenHeight);
        paint = new Paint();
        holder = getHolder();
    }

    private void fillArrays() {
        for (int i = 0; i < numberAsteroids; i++)
            asteroids.add(new Asteroid(context, screenWith, screenHeight, random.nextBoolean(), 5, getNextY()));
        for (int i = 0; i < numberCoins; i++)
            coins.add(new Coin(this.context, this.screenWith, this.screenHeight, getNextY()));
    }


    /**
     * Method implemented from runnable interface
     */
    @Override
    public void run() {
        while (isPlaying) {
            inflateObjects();
            updateInfo();
            paintFrame();
        }
    }

    private void inflateObjects() {
        if (snow == null && Math.random() > posibilitySnow) {
            snow = new Snow(context, screenWith, screenHeight, getNextY());
        }
        if (!coinLoaded || !asteroidLoaded) {
            long currentTime = System.currentTimeMillis();
            if (!coinLoaded && currentTime - timeCoin > 1000) {
                currentCoins++;
                timeCoin = currentTime;
                if (currentCoins == limitCoins)
                    coinLoaded = true;

            }
            print(currentTime - timeAsteroid + "");
            if (!asteroidLoaded && currentTime - timeAsteroid > 1500) {
                timeAsteroid = currentTime;
                currentAsteroids++;
                if (currentAsteroids == limitAsteroids)
                    asteroidLoaded = true;
            }
        }
    }

    private void print(String s) {
        Log.i("DEBUG", s);
    }

    private void updateInfo() {
        ship.updateInfo();

        // SNOW
        if (snow != null) {
            snow.updateInfo();
            if (snow.isDead()) snow = null;
            else {
                if (isIntersect(ship.getRectangle(), snow.getRectangle())) { // SET POWER-UP
                    snow = null;
                    freeze = true;
                    freezeTime = System.currentTimeMillis();
                }
            }
        }

        if (System.currentTimeMillis() - freezeTime > 5000) {
            freeze = false;
        }

        // ASTEROIDS
        for (int i = 0; i < currentAsteroids; i++) {
            Asteroid e = asteroids.get(i);
            e.updateInfo((float) (1.0 + level * 0.5), getNextY(), freeze);
            if (e.isDead() || isIntersect(ship.getRectangle(), e.getRectangle())) {
                e.kill(getNextY());
            }

        }

        // COINS
        for (int i = 0; i < currentCoins; i++) {
            Coin e = coins.get(i);
            if (!e.isDead()) {
                e.updateInfo(getNextY());
                if (isIntersect(ship.getRectangle(), e.getRectangle())) { // ADD SCORE
                    score += 5;
                    if (score % 20 == 0) {
                        e.setDead(true);
                        level++;
                        limitAsteroids++;
                        asteroidLoaded = false;
                    }
                    e.kill(getNextY());
                }
            }
        }
    }

    private float getNextY() {
        randomIndex %= randomSize;
        return randomSet[randomIndex++];
    }

    private float generateH() {
        return random.nextInt((int) screenHeight - 100);
    }

    private boolean isIntersect(Rectangle a, Rectangle b) {
        return a.intersect(b) || b.intersect(a);
    }

    private void paintFrame() {
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            canvas.drawBitmap(ship.getSpriteIcecreamCar(), ship.getPositionX(), ship.getPositionY(), paint);
            for (int i = 0; i < currentAsteroids; i++) {
                Asteroid e = asteroids.get(i);
                canvas.drawBitmap(e.getSprite(), e.getPositionX(), e.getPositionY(), paint);
            }
            for (int i = 0; i < currentCoins; i++) {
                Coin e = coins.get(i);
                if (!e.isDead())
                    canvas.drawBitmap(e.getSprite(), e.getPositionX(), e.getPositionY(), paint);
            }

            if (snow != null)
                canvas.drawBitmap(snow.getSprite(), snow.getPositionX(), snow.getPositionY(), paint);
            if (freeze) {
                drawFreezeText(canvas);
                drawFreezeTimeText(canvas);
            }

            drawScoreText(canvas);
            drawLevelText(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawLevelText(Canvas canvas) {
        float x = screenWith - 500;
        float y = 50;

        Paint paint = new Paint();
        paint.setTextSize(50);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);

        canvas.drawText("Nivel: " + level, x, y, paint);
    }

    private void drawFreezeTimeText(Canvas canvas) {
        float x = screenWith / 2;
        float y = 160;

        Paint paint = new Paint();
        paint.setTextSize(80);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);

        double res = (5000.0 - (System.currentTimeMillis() - freezeTime)) / 1000.0;
        if (res < 0) res = 0;
        canvas.drawText(String.format("%.2f", res), x, y, paint);
    }

    private void drawFreezeText(Canvas canvas) {
        float x = screenWith / 2;
        float y = 80;

        Paint paint = new Paint();
        paint.setTextSize(80);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);

        canvas.drawText("FREEZE", x, y, paint);
    }

    private void drawScoreText(Canvas canvas) {
        float x = 150;
        float y = 50;

        Paint paint = new Paint();
        paint.setTextSize(50);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);

        canvas.drawText("Puntaje: " + score, x, y, paint);
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

        isPlaying = true && !firstTime;
        gameplayThread = new Thread(this);
        gameplayThread.start();
    }

    /**
     * Detect the action of the touch event
     *
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                System.out.println("TOUCH UP - STOP JUMPING");
                ship.setJumping(false);
                break;
            case MotionEvent.ACTION_DOWN:
                System.out.println("TOUCH DOWN - JUMP");
                ship.setJumping(true);
                break;
        }
        if (firstTime) {
            firstTime = false;
            timeCoin = System.currentTimeMillis();
            timeAsteroid = timeCoin;
            resume();
        }
        return true;
    }
}
