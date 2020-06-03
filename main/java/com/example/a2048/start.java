package com.example.a2048;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * this is the start screen of the game
 */
public class start extends Fragment implements View.OnClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_start, container, false);
        ((Button) v.findViewById(R.id.button)).setOnClickListener(this);

        return v;

    }

    /**
     * this is what is called when the player taps start of the screen
     * @param v
     */
    public void onClick(View v) {

        v.findViewById(R.id.button).setVisibility(View.GONE);//makes the button invisible
        //switches this fragment with the game fragment
        game fragment2 = new game();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.actgame, fragment2);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

}
