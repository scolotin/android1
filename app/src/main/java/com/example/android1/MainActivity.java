package com.example.android1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAddCity = findViewById(R.id.btn_add_city);
        btnAddCity.setOnClickListener(v -> setContentView(R.layout.add_city));
    }
}