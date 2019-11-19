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
    private IceCreamCar icecreamCar;
    private ArrayList<Kid> kids;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder holder;
    private Thread gameplayThread = null;
    private Long time = System.currentTimeMillis();
    private Context context;
    private float screenWith;
    private float screenHeight;

    /**
     * Contructor
     *
     * @param context
     */
    public GameSurfaceView(Context context, float screenWith, float screenHeight) {
        super(context);
        this.kids = new ArrayList<Kid>();
        this.context = context;
        this.screenWith = screenWith;
        this.screenHeight = screenHeight;
        icecreamCar = new IceCreamCar(context, screenWith, screenHeight);
        kids.add(new Kid(context, screenWith, screenHeight));
        Log.i("Edward", screenHeight + "" + screenWith);
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
            long time2 = System.currentTimeMillis();
            if (time2 - this.time > 3000) {
                Random z = new Random(time2);
                this.time = System.currentTimeMillis();
                Kid kid2 = new Kid(this.context, this.screenWith, this.screenHeight);
                kid2.setPositionY(z.nextInt(1000));
                kids.add(kid2);
            }
            updateInfo();
            paintFrame();
        }
    }

    private void updateInfo() {
        icecreamCar.updateInfo();
        for (Kid kid : kids) {
            kid.updateInfo();
        }
    }

    private void paintFrame() {
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.CYAN);
            canvas.drawBitmap(icecreamCar.getSpriteIcecreamCar(), icecreamCar.getPositionX(), icecreamCar.getPositionY(), paint);
            for (Kid kid : kids) {
                float a = icecreamCar.getPositionY();
                float b = a;
                float kh = kid.getPositionX();
                Log.i("Edward", kh + "     [" + a+ " - " + b + " ]");

                if((kid.getPositionX()>a && kid.getPositionX()< b) || (kh >a && kh<b)){
                    kids.remove(kid);
                }
                canvas.drawBitmap(kid.getSpriteKid(), kid.getPositionX(), kid.getPositionY(), paint);
            }
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
                icecreamCar.setJumping(false);
                break;
            case MotionEvent.ACTION_DOWN:
                System.out.println("TOUCH DOWN - JUMP");
                icecreamCar.setJumping(true);
                break;
        }
        return true;
    }

}
