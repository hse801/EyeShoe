package com.example.sinbal;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class initial extends AppCompatActivity {

    EditText userSize;
    EditText userHeight;
    CheckBox checkSize;
    SharedPreferences UserInfo;
    SharedPreferences.Editor editor;

    public static final int sub = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        userSize = (EditText) findViewById(R.id.inputSize);
        userHeight = (EditText) findViewById(R.id.inputHeight);
        checkSize = (CheckBox) findViewById(R.id.checkSize);

        checkSize.setChecked(true);
        UserInfo = getSharedPreferences("UserInfo", 0);
        editor = UserInfo.edit();


        final int inputSizeValue = UserInfo.getInt("inputSize", 0);

        if(inputSizeValue > 0){
            userSize.setText(Integer.toString(inputSizeValue));
        }

        int inputHeightValue = UserInfo.getInt("inputHeight", 0);
        if(inputHeightValue > 0){
            userHeight.setText(Integer.toString(inputHeightValue));
        }

        //userHeight.setText(UserInfo.getString("inputHeight", ""));
        //userSize.setText(ShoeSize.getInt("InputSize", 0));
        //setPreference(ShoeSize, Integer.parseInt(input InputSize.getText().toString()));


        Button button = findViewById(R.id.start); /*페이지 전환버튼*/

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class); //변수 받아오기 추가 부분
//        Intent intent = new Intent(this, MainActivity.class);
                String SizeValue = userSize.getText().toString();
                if(!SizeValue.isEmpty()) {//Empty 아닐떄만 실행
                    int inputSizeValue = Integer.parseInt(SizeValue);
                    intent.putExtra("size", inputSizeValue);
                    startActivity(intent);//액티비티 띄우기
                }
            }
        });
    }
    public void onPause(){
        super.onPause();

        if(checkSize.isChecked()){
            String sSize = userSize.getText().toString();
            if(!sSize.isEmpty()){
                editor.putInt("inputSize", Integer.parseInt(sSize));
            }
        } else{
            editor.putInt("inputSize",0);
        }
        if(checkSize.isChecked()){
            String sHeight = userHeight.getText().toString();
            if(!sHeight.isEmpty()){
                editor.putInt("inputHeight", Integer.parseInt(sHeight));
            }
        } else{
            editor.putInt("inputHeight",0);
        }
        editor.commit();
//        if(UserInfo.getBoolean("checkSize_enabled", false)){
//            userSize.setText(UserInfo.getString("inputSize", ""));
//            checkSize.setChecked(true);}
//
//            if(UserInfo.getBoolean("checkSize_enabled", false)){
//                userHeight.setText(UserInfo.getString("inputHeight", ""));
//                checkSize.setChecked(true);}
}
}