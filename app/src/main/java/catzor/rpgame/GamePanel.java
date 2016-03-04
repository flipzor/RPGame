package catzor.rpgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by steurjor on 29-2-2016.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private Background bg;
    private Character ch;

    private int positionX;
    private int positionY;


    public GamePanel(Context context) {
        super(context);

        // Add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        // Make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background_green));
        ch = new Character(BitmapFactory.decodeResource(getResources(), R.drawable.character));
        thread.setRunning(true);
        // We can safely start the game loop
        if (thread.getState() == Thread.State.NEW) {
            //thread.setRunning(true);
            thread.start();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        positionX = (int)event.getX();
        positionY = (int)event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
        }
        return super.onTouchEvent(event);
    }

    public void update() {
        bg.update();
        ch.update(positionX, positionY);
    }

    @Override
    public void draw(Canvas canvas){
        String stringFPS = getFPS();
        bg.draw(canvas, stringFPS, positionX, positionY);
        ch.draw(canvas, positionX, positionY);
    }



    public String getFPS() {
        String avFPS = String.valueOf(thread.averageFPS);
        return avFPS;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

}
