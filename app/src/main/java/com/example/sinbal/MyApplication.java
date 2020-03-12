package com.example.sinbal;

import android.app.Application;

public class MyApplication extends Application {

    private int turn = 1;

    public int getGlobalValue() {

        return turn;
    }

    public void setGlobalValue(int mValue){

        this.turn = mValue;
    }

}
