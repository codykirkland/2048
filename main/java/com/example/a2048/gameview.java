package com.example.a2048;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

import java.util.Random;

/**
 * this is the game
 */
public class gameview extends View implements Runnable
{
    int rowsandcols = 6;//this is the number of rows and columns the grid has
    tile[][] mTiles = new tile[rowsandcols][rowsandcols];//this is the board
    float size;//size of the tile
    float xmargin, ymargin;//the margins of the view

    Paint paint1, paint2,paint3,paint4,paint5,paint6,scorepaint,endpaint,endpaint2;

    //this is the time stuff
    long nextFrameTime = System.currentTimeMillis();//makes an update be triggered
    private final long FPS = 8;//this is the fps you want
    private final long MILLIS_PER_SECOND = 1000;//get the milliseconds in a second
    boolean isPlaying = true;//this will indicate that the game if playing

    Thread drawThread = null;//this creates a thread

    public int d =0;//this is the direction the tiles will move
    public boolean cmove = true;//this tells the program if any of the tiles have moves
    public int moves = 1;//this is the number of times the tiles have moved on that turn
    public int score = 0;//this is the score of the player
    private GestureDetectorCompat mDetector;//this is to detect a gesture
    boolean lost = false;//this tell the game the player has lost
    boolean won = false;//this tell the game the player has won

    Random random = new Random();

    /**
     * this is used when you are creating the view
     * @param context
     * @param attrs
     */
    public gameview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //the paint stuff
        paint1 = new Paint();
        paint1.setColor(Color.RED);
        paint2 = new Paint();
        paint2.setColor(Color.WHITE);
        paint3 = new Paint();
        paint3.setColor(Color.GREEN);
        paint4 = new Paint();
        paint4.setColor(Color.BLUE);
        paint5 = new Paint();
        paint5.setColor(Color.YELLOW);
        paint6 = new Paint();
        paint6.setColor(Color.LTGRAY);
        scorepaint = new Paint();
        scorepaint.setColor(Color.BLACK);
        scorepaint.setTextAlign(Paint.Align.CENTER);
        scorepaint.setTextSize(100);
        endpaint = new Paint();
        endpaint.setColor(Color.BLACK);
        endpaint.setTextAlign(Paint.Align.LEFT);
        endpaint.setTextSize(200);
        endpaint2 = new Paint();
        endpaint2.setColor(Color.BLACK);
        endpaint2.setTextAlign(Paint.Align.CENTER);
        endpaint2.setTextSize(75);

        setLongClickable(true);
        mDetector=new GestureDetectorCompat(context,new MyGestureListener() );//creates the gesture object
        newgame();//this starts a new game
        start();//starts the loop
    }

    /**
     * this draws everthing
     * @param canvas
     */
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        size = getHeight() / (rowsandcols + 1);//the size of the tiles

        xmargin = (getHeight() - rowsandcols * size) / 2;//the margin at the bottom and top of screen
        ymargin = (getWidth() - rowsandcols * size) / 2;//the margin at the left and right of the maze


        canvas.translate(ymargin, xmargin);

        //this will draw the lost screen
        if(lost){

            canvas.drawText("you lost !!", 2 * size+.5f, 3 * size+0.5f, endpaint);
            canvas.drawText("tap the screen to reset the game", 3 * size+.5f, 5 * size+0.5f, endpaint2);
        }
        else if(won){
            //this will draw the won screen
            canvas.drawText("you won !!", 2 * size+.5f, 3 * size+0.5f, endpaint);
            canvas.drawText("tap the screen to reset the game", 3 * size+.5f, 5 * size+0.5f, endpaint2);


        }else{
            //draw the board
        for (int y = 0; y < rowsandcols; y++) {
            for (int x = 0; x < rowsandcols; x++) {

                //checks to see of the tile will be a wall or not
                if (mTiles[y][x].getNum()!=1) {

                //this draws all of the tiles
                if (mTiles[y][x].getNum()==2||mTiles[y][x].getNum()==32||mTiles[y][x].getNum()==512) {
                    canvas.drawRect(
                            y * size+5,
                            x * size+5,
                            (y + 1) * size-5,
                            (x + 1) * size-5,
                            paint1);
                }
                if (mTiles[y][x].getNum()==4||mTiles[y][x].getNum()==64||mTiles[y][x].getNum()==1024) {
                    canvas.drawRect(
                            y * size+5,
                            x * size+5,
                            (y + 1) * size-5,
                            (x + 1) * size-5,
                            paint3);
                }
                if (mTiles[y][x].getNum()==8||mTiles[y][x].getNum()==128||mTiles[y][x].getNum()==2048) {
                    canvas.drawRect(
                            y * size+5,
                            x * size+5,
                            (y + 1) * size-5,
                            (x + 1) * size-5,
                            paint4);
                }
                if (mTiles[y][x].getNum()==16||mTiles[y][x].getNum()==256) {
                    canvas.drawRect(
                            y * size+5,
                            x * size+5,
                            (y + 1) * size-5,
                            (x + 1) * size-5,
                            paint5);
                }

                if (mTiles[y][x].getNum()==0) {
                    canvas.drawRect(
                            y * size+5,
                            x * size+5,
                            (y + 1) * size-5,
                            (x + 1) * size-5,
                            paint6);
                }
                //this draws what number the tile is on
                    canvas.drawText(String.valueOf(mTiles[y][x].getNum()), (y+.5f) * size, (x+.5f) * size, endpaint2);


                }

            }
        }}
        //this draws the score
        canvas.drawText("score: "+score, 3 * size, 0 * size+0.5f, scorepaint);



    }
    /**
     * this makes a new board
     */
    public void newgame() {

        for (int y = 0; y < rowsandcols; y++) {

            for (int x = 0; x < rowsandcols; x++) {

                mTiles[y][x] = new tile(y, x);//create a new tile
                //this sets the walls of the board
                if (y == 0 || y == rowsandcols - 1 || x == 0 || x == rowsandcols - 1) {
                    mTiles[y][x].setNum(1);
                }
                else{
                    mTiles[y][x].setNum(0);
                }

            }
        }


        //places 2 tiles on the board to start
        newtile();
        newtile();
    }

    /**
     * this will add a new tile to the board
     */
    public void newtile(){
       boolean placed = false;//this will turn true when the tile has been placed

        //this will loop until a new tile is placed
        do {
            //gets 2 random numbers
            int col = random.nextInt(rowsandcols - 2) + 1;
            int row = random.nextInt(rowsandcols - 2) + 1;
            //this checks to see of the tile is empty
            if(mTiles[col][row].getNum()==0){
                mTiles[col][row].setNum(2);
                placed = true;

            }


        }while (!placed);


    }

    /**
     * this will get a motions event and give it to the gesture controller
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

       this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);

    }

    /**
     * this will move the tiles
     */
    public void move(){

        tile next, current;//this is the current and the next tile

        switch (d) {
            //this moves the tiles right
            case 1:
                for (int y = 4; y >= 1; y--) {

                    for (int x = 4; x >= 1; x--) {
                        //gets the tiles
                        current = mTiles[x][y];
                        next = mTiles[x+1][y];
                        //checks to see if the tile can move
                        if(next.getNum()==0&& current.getNum()!=0){
                            //moves the tile
                            next.setNum(current.getNum());
                            current.setNum(0);//this makes the current tile number zero
                            cmove = false;//this will tell the game it can add a new tile
                        }
                        //checks to see if the tile will add up
                        else if(next.getNum()==current.getNum()&& current.getNum()!=0){
                            //add the next and current together
                            next.addnum(current.getNum());
                            current.setNum(0);//this makes the current tile number zero
                            score+=next.getNum();//the the number added together to the score
                            cmove=false;//this will tell the game it can add a new tile
                        }
                    }
                }
                moves++;
                break;

            //this moves the tile left
            case 2:
                for (int y = 1; y <= 4; y++) {

                    for (int x = 1; x <= 4; x++) {
                        //gets the tiles
                        current = mTiles[x][y];
                        next = mTiles[x-1][y];
                        //checks to see if the tile can move
                        if(next.getNum()==0&& current.getNum()!=0&& current.getNum()!=0) {
                            //moves the tile

                            next.setNum(current.getNum());
                            current.setNum(0);//this makes the current tile number zero
                            cmove=false;//this will tell the game it can add a new tile
                        }
                        //checks to see if the tile will add up

                        else if(next.getNum()==current.getNum()&& current.getNum()!=0){
                            //add the next and current together
                            next.addnum(current.getNum());
                            current.setNum(0);//this makes the current tile number zero
                            score+=next.getNum();//the the number added together to the score
                            cmove=false; //this will tell the game it can add a new tile

                    }}
                }
                moves++;

                break;
            case 3:
                //this moves the tiles up
                for (int y = 1; y < rowsandcols-1; y++) {

                    for (int x = 1; x < rowsandcols-1; x++) {

                        //gets the tiles
                        current = mTiles[x][y];
                        next = mTiles[x][y-1];
                        //checks to see if the tile can move
                        if(next.getNum()==0&& current.getNum()!=0){
                            //moves the tile
                            next.setNum(current.getNum());
                            current.setNum(0);//this makes the current tile number zero
                            cmove=false;//this will tell the game it can add a new tile
                        }
                        //checks to see if the tile will add up
                        else if(next.getNum()==current.getNum()&& current.getNum()!=0){
                            //add the next and current together
                            next.addnum(current.getNum());
                            current.setNum(0);//this makes the current tile number zero
                            score+=next.getNum();//the the number added together to the score
                            cmove=false;//this will tell the game it can add a new tile
                             }

                    }
                }
                moves++;
                break;

            //this moves the tile down
            case 4:
                for (int y = 4; y >= 1; y--) {

                    for (int x = 4; x >= 1; x--) {
                        //gets the tiles
                        current = mTiles[x][y];
                        next = mTiles[x][y+1];
                        //checks to see if the tile can move
                        if(next.getNum()==0 && current.getNum()!=0){
                            //moves the tile

                            next.setNum(current.getNum());
                            current.setNum(0);//this makes the current tile number zero
                            cmove=false;//this will tell the game it can add a new tile

                            }
                        //checks to see if the tile will add up
                       else if(next.getNum()==current.getNum()&& current.getNum()!=0){
                            //add the next and current together
                            next.addnum(current.getNum());
                            current.setNum(0);//this makes the current tile number zero
                            score+=next.getNum();//the the number added together to the score
                                cmove=false;//this will tell the game it can add a new tile
                            }


                    }
                }
                moves++;
                break;


        }
//if the is 4 the tiles will stop moving
        if(moves ==4){

            moves = 1;//changes moves to 1

            d = 0;//resets the direction

            check();//checks to see if the player has won or lost

            //if the tiles can not move in the direction that is picked it will not add a new tile
            if(!cmove) {
                cmove = true;
                newtile();//add a new tile
            }
        }
    }

    /**
     * this check to see if the player has won or lost
     */
    public void check(){

        boolean done = true;//this will tell the game the player has no moves left

        for (int y = 1; y <= 4; y++) {

            for (int x = 1; x <= 4; x++) {

                //this will see if the player can move the tile
                if (mTiles[y][x].getNum() ==0||mTiles[x][y].getNum()==mTiles[x][y+1].getNum()||mTiles[x][y].getNum()==mTiles[x+1][y].getNum()||mTiles[x][y].getNum()==mTiles[x][y-1].getNum()||mTiles[x][y].getNum()==mTiles[x-1][y].getNum()) {
                    done = false;

                }
                //this will check if the player has won the game
                if (mTiles[y][x].getNum() == 2048) {
                    won = true;//sets won to true
                }
            }
        }

        //this tells the game the player has lost
        if (done){
            lost = true;//sets the lost to true
        }
    }


    /**
     * this starts the loop
     */
    public void start() {
        //this creates a new thread if there was not one
        if (drawThread == null) drawThread = new Thread(this);
        drawThread.start();// this starts the loop
    }

    @Override
    public void run() {

        while (isPlaying) {
            //this updates 5 time a second
            if (updateRequired()) {
                    move();
                postInvalidate();
            }

        }


    }

    /**
     * this gets the timeframe of the game
     * @return*/

    public boolean updateRequired() {

        //checks if an update is needed
        if (nextFrameTime <= System.currentTimeMillis()) {
            //sets when the next update will be needed
            nextFrameTime = System.currentTimeMillis() + MILLIS_PER_SECOND / FPS;

            return true;
        }
        return false;
    }


    /**
     * this is the gesture listener of the game
     */
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final String DEBUG_TAG = "Gestures";

        /**
         * this will be triggered when the player puts a finger on the screen
         * @param event
         * @return
         */
    @Override
    public boolean onDown(MotionEvent event) {
        //if the payer has won or lost the game will start a new game
        if(lost||won){
            lost = false;
            won = false;
            score = 0;
            newgame();}
        return true;
    }

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        /**
         * this will be how the game will decide what direction the tiles will move by swipes
         * @param event1
         * @param event2
         * @param velocityX
         * @param velocityY
         * @return
         */
    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        boolean result = false;

        try {
            //gets the differences of the 2 events
            float diffY = event2.getY() - event1.getY();
            float diffX = event2.getX() - event1.getX();
            //determines what axis the tile will move
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    //determines the direction
                    if (diffX > 0) {//right
                        d= 1;

                    } else {//left
                        d= 2;
                    }
                    result = true;
                }
            }
            else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                //determines the direction
                if (diffY < 0) {//up
                    d = 3;
                } else {//down
                   d= 4;
                }
                result = true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }
}
}




