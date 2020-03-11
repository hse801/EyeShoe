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
    CheckBox checkSize;
    SharedPreferences ShoeSize;
    SharedPreferences.Editor editor;

    public static final int sub = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        userSize = (EditText) findViewById(R.id.inputSize);
        checkSize = (CheckBox) findViewById(R.id.checkSize);

        ShoeSize = getSharedPreferences("ShoeSize", 0);
        editor = ShoeSize.edit();

        //userSize.setText(ShoeSize.getInt("InputSize", 0));
        //setPreference(ShoeSize, Integer.parseInt(input InputSize.getText().toString()));


        Button button = findViewById(R.id.start); /*페이지 전환버튼*/

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivityForResult(intent,sub);//액티비티 띄우기
            }
        });
    }
    public void onPause(){
        super.onPause();

        if(checkSize.isChecked() == true){
            editor.putInt("InputSize",250);
        } else{
            editor.putInt("InputSize",0);
        }
        editor.commit();
    }
}