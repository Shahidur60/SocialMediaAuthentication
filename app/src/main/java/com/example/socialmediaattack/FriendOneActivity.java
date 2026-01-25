package com.example.socialmediaattack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FriendOneActivity extends AppCompatActivity {

    Button btnNextToBob;

    ImageView imageViewTwitter;

    ImageView imageViewInstagram;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_one);

        btnNextToBob = findViewById(R.id.btnFriends1);
        imageViewTwitter = findViewById(R.id.imageViewForBobX);
        imageViewInstagram = findViewById(R.id.imageViewForBobInstagram);

        imageViewTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initialize link
                String sWebLink = "https://twitter.com/Bob253784738";
                openLink(sWebLink);
            }
        });


        imageViewInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initialize link
                String sWeblink = "https://www.instagram.com/bob253784738/";
                openLink(sWeblink);
            }
        });


        btnNextToBob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FriendOneActivity.this,exampleForBob.class));
            }
        });

    }


    private void openLink(String sWebLink) {
        //open link in browser
        //initialize uri
        Uri uri = Uri.parse(sWebLink);
        //initialize intent
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //set data
        intent.setData(uri);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //start activity
        startActivity(intent);
    }


}
