package com.example.sinbal;

import android.app.Application;

public class MyApplication extends Application {
    private int turn = 1;


// 타 class에서 MyApplication class를 통해 해당 variable 값을 참조

    public int getGlobalValue(){
        return turn;
    }



// 타 class에서 변경한 valuable을 MyApplication 에 저장

    public void setGlobalValue(int mValue){
        this.turn = mValue;
    }
}

