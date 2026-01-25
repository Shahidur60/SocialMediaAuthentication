package com.example.socialmediaattack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class mainInstructions extends AppCompatActivity {

    Button btnNextToBob;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_instructions);

        btnNextToBob = findViewById(R.id.btnFriends1);

        btnNextToBob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mainInstructions.this,bobAuthenticationCeremony.class));
            }
        });

    }
}
