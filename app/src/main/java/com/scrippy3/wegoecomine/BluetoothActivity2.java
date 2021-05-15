package com.scrippy3.wegoecomine;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BluetoothActivity2 extends AppCompatActivity {
    public TextView textView;
    public Button startKnap;
    public Button endKnap;
    public Datastream2 datastream2 = new Datastream2();
    private Trip trip = new Trip();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth2);

        //Hide action bar
        getSupportActionBar().hide();

        textView = findViewById(R.id.text);
        startKnap = findViewById(R.id.btn_start);
        endKnap = findViewById(R.id.btn_end);
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!bluetoothAdapter.isEnabled()){
            bluetoothAdapter.enable();
        }

        startKnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startKnap.setVisibility(View.GONE);
                endKnap.setVisibility(View.VISIBLE);

                datastream2.readData(bluetoothAdapter);
                shift();
                //startKnap.setEnabled(false);
            }
        });

        endKnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datastream2.setStartData(false);
                datastream2.setReading(true);
            }
        });

        endKnap.setVisibility(View.GONE);
    }

    public void shift(){
        Thread thread = new Thread(){
            public void run(){
                while(true){
                    if (datastream2.slut) {
                        finish();
                    }
                }
            }
        };
        thread.start();
    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}