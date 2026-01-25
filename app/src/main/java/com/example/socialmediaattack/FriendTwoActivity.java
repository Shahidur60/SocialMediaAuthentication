package com.example.socialmediaattack;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FriendTwoActivity extends AppCompatActivity {

    Button getBtnNextToAlice;

    ImageView imageViewTwitter;

    ImageView imageViewInstagram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_two);

        getBtnNextToAlice = findViewById(R.id.btnFriends1);
        imageViewTwitter = findViewById(R.id.imageViewForAliceX);
        imageViewInstagram = findViewById(R.id.imageViewForAliceInstagram);

        imageViewTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initialize link
                String sWebLink = "https://twitter.com/Alice100010000";
                openLink(sWebLink);
            }
        });


        imageViewInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initialize link
                String sWeblink = "https://www.instagram.com/alice100010000/";
                openLink(sWeblink);
            }
        });

        getBtnNextToAlice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FriendTwoActivity.this,aliceAuthenticationCeremony.class));
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
