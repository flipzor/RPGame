package catzor.rpgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by steurjor on 2-3-2016.
 */
public class Character {
    private MainThread thread;
    private Bitmap image;
    private int x = 400, y = 400; // Start position character
    boolean walking = false;
    boolean doXPath = false;
    int walkingSpeed = 9;
    int newX, newY;
    int diffX, diffY;
    int speedX, speedY;
    int centerCharacter = 61;
    int angle;


    public Character (Bitmap res) {
        image = res;
    }

    public void update(int positionX, int positionY){
        if ((newX != positionX)&(newY != positionY)) {
            walking = true;
            diffX = positionX - x;
            diffY = positionY - y;
            double dX = Math.signum(diffX);
            double dY = Math.signum(diffY);
            int iX = (int) dX;
            int iY = (int) dY;
            // Take route in 2 sec / 60 fps
            speedX = iX * walkingSpeed;
            speedY = iY * walkingSpeed;
            if (Math.abs(diffX) < Math.abs(diffY)) {
                doXPath = true;
            }
        }
        newX = positionX;
        newY = positionY;

        // Reached destination
        if ((positionX == x)&(positionY == y)) {
            walking = false;
        }

        // Start walking to destination
        if (walking) {
            // Check if X rout is shorter than Y route
            diffX = positionX - x;
            diffY = positionY - y;
            if (doXPath){
                setAngle(doXPath, positionX, positionY);
                if (diffX != 0) {
                    if (Math.abs(diffX) < Math.abs(speedX)) {
                        x = positionX;
                    } else {
                        x = x + speedX;
                    }
                } else {
                    doXPath = false;
                }
            }
            if (!doXPath) {
                setAngle(doXPath, positionX, positionY);
                if (diffY != 0) {
                    if (Math.abs(diffY) < Math.abs(speedY)) {
                        y = positionY;
                    } else {
                        y = y + speedY;
                    }
                } else {
                    doXPath = true;
                }
            }
        }
        //System.out.println("speedX: " + speedX);
    }

    public void draw(Canvas canvas, int positionX, int positionY) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(70);
        paint.setAntiAlias(true);

        Matrix matrix = new Matrix();
        matrix.postRotate(getAngle(), centerCharacter,centerCharacter);
        matrix.postTranslate((x-centerCharacter), (y-centerCharacter));
        // Draw X line
        //canvas.drawLine(x, y, x, positionY, paint);
        // Draw y line
        //canvas.drawLine(x, positionY, positionX, positionY, paint);
        canvas.drawText(getAngle()+ " deg", 10 , 230, paint);
        canvas.drawBitmap(image, matrix, null);
    }


    public void setAngle(boolean doXPath, int positionX, int positionY) {
        if (doXPath) { // If start walking via X
            if (x < positionX) {
                angle = 90;
            } else {
                angle = 270;
            }
        }
        if (!doXPath) { // If start walking via Y
            if (y < positionY) {
                angle = 180;
            } else {
                angle = 0;
            }
        }
    }

    public int getAngle(){
        return angle;
    }

}
