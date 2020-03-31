package com.example.sinbal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;


public class initial extends AppCompatActivity {
    Button button;
    EditText userSize;
    CheckBox checkSize;
    SharedPreferences UserInfo;
    SharedPreferences.Editor editor;


    public static final int sub = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        button = (Button) findViewById(R.id.alert);


        userSize = (EditText) findViewById(R.id.inputSize);
        checkSize = (CheckBox) findViewById(R.id.checkSize);

        checkSize.setChecked(true);
        UserInfo = getSharedPreferences("UserInfo", 0);
        editor = UserInfo.edit();


        final int inputSizeValue = UserInfo.getInt("inputSize", 0);

        if (inputSizeValue > 0) {
            userSize.setText(Integer.toString(inputSizeValue));
        }


        Button button = findViewById(R.id.start); /*페이지 전환버튼*/

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class); //변수 받아오기 추가 부분
                String SizeValue = userSize.getText().toString();

                if (!SizeValue.isEmpty()) {//Empty 아닐떄만 실행
                    int inputSizeValue = Integer.parseInt(SizeValue);
                    intent.putExtra("size", inputSizeValue);
                    startActivity(intent);//액티비티 띄우기
                }
            }
        });
    }

    public void onPause() {
        super.onPause();

        if (checkSize.isChecked()) {
            String sSize = userSize.getText().toString();
            if (!sSize.isEmpty()) {
                editor.putInt("inputSize", Integer.parseInt(sSize));
            }
        } else {
            editor.putInt("inputSize", 0);
        }

        editor.commit();

    }


    public void onStart(){
        super.onStart();
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(initial.this, PopupActivity.class);
                startActivity(intent);
            }


        });
    }


}




