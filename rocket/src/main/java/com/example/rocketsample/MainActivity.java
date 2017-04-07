package com.example.rocketsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.rocketsample.service.RocketService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void open(View view) {
        startService(new Intent(this, RocketService.class));
    }

    public void close(View view) {
        stopService(new Intent(this, RocketService.class));
    }
}
