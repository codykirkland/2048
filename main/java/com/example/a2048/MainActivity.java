package com.example.a2048;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * this is a sliding block puzzle game. the object of the game is to slide the numbered tiles on
 * the grid to combine them to create a tile with the number 2048 by swiping on the screen
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



}
