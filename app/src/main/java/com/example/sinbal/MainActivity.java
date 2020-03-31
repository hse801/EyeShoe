package com.example.sinbal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

import android.media.MediaPlayer;


public class MainActivity extends AppCompatActivity {

    private BluetoothSPP bt;
    int size;
    boolean blockDetected = false;
    boolean wallDetected = false;
    boolean downhillDetected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent(); //변수 받아오기 추가 부분
        size = intent.getIntExtra("size",0);
        bt = new BluetoothSPP(this); //Initializing


        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            TextView distance22 = findViewById(R.id.distance2); //텍스트뷰를 통해 초음파 센서 값 받아오기
            TextView distance33 = findViewById(R.id.distance3);
            TextView distance44 = findViewById(R.id.distance4);


            double finalSize = MainActivity.this.size/10*2.5*3; // final size를 사용자의 보폭 기준 3걸음으로 설정


            public void onDataReceived(byte[] data, String message) { //데이터 수신용 코드 추가


                String[] array = message.split(",");

                distance22.setText(array[0].concat("cm"));
                distance33.setText(array[1].concat("cm"));
                distance44.setText(array[2].concat("cm"));


                double distance2 = Double.parseDouble(array[0]); //초음파센서값 3개 array 형식으로 안드로이드 스튜디오에 받아오기
                double distance3 = Double.parseDouble(array[1]);
                double distance4 = Double.parseDouble(array[2]);

                double hypotenuse = distance2 / 0.93969; //오르막센서 각도를 20도로 설정. 밑변 길이를 cos20으로 나눈 값, 대각선 길이

                //벽 & 오르막
                if (distance4 < 400) {
                    if (distance4 > hypotenuse) { //오르막 센서값과 전방센서값이 직각삼각형 형성하지 않을 경우 오르막으로 인식
                        //오르막
                        final MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.uphill);
                        mp.start();
                    } else { // 오르막 센서값과 전방센서값이 직각삼각형 형성할 경우 벽으로 인식
                        //벽
                        if (distance2 < finalSize) {
                            if (!wallDetected) { //벽 중복 알람 방지
                                wallDetected = true;
                                final MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.wall);
                                mp.start();
                            }
                            if (distance2 < 15) { //근접 경보
                                final MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.close);
                                mp.start();
                            }
                        } else {
                            wallDetected = false;
                        }
                    }
                }
                //장애물
                else { // 오르막 센서가 감지되지 않을 경우, 전방센서값은 측정되는 경우.
                    if (distance2 < finalSize) {
                        if (!blockDetected) { //장애물 중복 알람 방지
                            blockDetected = true;

                            final MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.block);
                            mp.start();
                        }
                        if (distance2 < 15) { //근접 경보
                            final MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.close);
                            mp.start();
                        }
                    } else {
                        blockDetected = false;
                    }
                }

                //내리막센서가 일정 값 이상이 나올 경우
                //내리막
                if (distance3 > 100) {
                    if (!downhillDetected) {
                        downhillDetected = true; //내리막길 중복 알람 방지

                        final MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.downhill);
                        mp.start();
                    } else {
                        downhillDetected = false;
                    }
                }
            }

            });


        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때

            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() { //연결해제
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnConnect = findViewById(R.id.btnConnect); //연결시도
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });
    }


    public void onDestroy() {
        super.onDestroy();
        bt.stopService(); //블루투스 중지
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) { //
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
//                setup();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
//                setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    } }
