package com.example.a2048;

/**
 * this is the tile of the board
 */
public class tile {

    int tiley,tilex;//this is the x and y of the tile on the board

    /**
     * this will get the number the tile is
     * @return
     */
    public int getNum() {
        return num;
    }

    /**
     * this will set the tile number
     * @param num
     */
    public void setNum(int num) {
        this.num = num;
    }


    int num = 0;//this is the tile number

    public tile(int tiley, int tilex){
        this.tiley = tiley;
        this.tilex = tilex;
    }

    /**
     * this will add the num2 to num
     * @param num2
     */
    public void addnum(int num2){
        num+= num2;
    }

}
