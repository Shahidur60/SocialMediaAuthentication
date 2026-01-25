package com.example.socialmediaattack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;

public class exampleForBob extends AppCompatActivity {

    ImageView imageViewTwitterExample;
    ImageView imageViewFacebookExample;
    ImageView imageViewPinteresExample;
    ImageView imageViewInstagramExample;

    Button exBtnAccept;
    Button exBtnReject;
    Button exBtnNext;

    TextView textViewBobExample;
    TextView textViewDescriptionExample;

    int pickedImageExampl = 0, lastPickedExampl = 0;

    private String linkTextUpperImageExampl;
    private String linkTextLowerImageExampl;

    private String currentImageExampl;

    private int counter = 0;

    private int counterRealProfile = 0;
    private int counterClearFakeProfile = 0;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bob_authentication_example);

        imageViewTwitterExample = findViewById(R.id.imageTwitterViewExample);
        imageViewFacebookExample = findViewById(R.id.imageFacebookViewExample);
        imageViewPinteresExample = findViewById(R.id.imagePinteresViewExample);
        imageViewInstagramExample = findViewById(R.id.imageInstagramViewExample);

        exBtnAccept = findViewById(R.id.exBtnAccept);
        exBtnReject = findViewById(R.id.exBtnReject);
        exBtnNext = findViewById(R.id.exBtnNext);

        textViewBobExample = findViewById(R.id.textViewBobExample);
        textViewDescriptionExample = findViewById(R.id.descriptionExample);

        showRandomBobLinks();

        exBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter += 1;
                exBtnNext.setEnabled(true);
                exBtnAccept.setEnabled(false);
                exBtnReject.setEnabled(false);
            }
        });

        exBtnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter += 1;
                exBtnNext.setEnabled(true);
                exBtnAccept.setEnabled(false);
                exBtnReject.setEnabled(false);
            }
        });

        imageViewTwitterExample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initialize link
                String sWebLink = linkTextUpperImageExampl;
                openLink(sWebLink);
            }
        });

        imageViewInstagramExample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initialize link
                String sWeblink = linkTextLowerImageExampl;
                openLink(sWeblink);
            }
        });

        exBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter != 6){
                    showRandomBobLinks();
                    exBtnNext.setEnabled(false);
                    exBtnAccept.setEnabled(true);
                    exBtnReject.setEnabled(true);
                }
                else {
                    startActivity(new Intent(exampleForBob.this,mainInstructions.class));
                }
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

    public void showRandomBobLinks(){

        //remove duplicate
        do {
            shuffleBobLinks();
            pickedImageExampl = bobLinksArray[0].getmImage();

            if (counterRealProfile == 3){
                do {
                    shuffleBobLinks();
                    pickedImageExampl = bobLinksArray[0].getmImage();
                } while (pickedImageExampl == R.drawable.bob_real_img_x || pickedImageExampl == R.drawable.bob_real_img_instagram);
            }

            if (counterClearFakeProfile == 3){
                do {
                    shuffleBobLinks();
                    pickedImageExampl = bobLinksArray[0].getmImage();
                } while (pickedImageExampl == R.drawable.clear_bob_fake_img_x || pickedImageExampl == R.drawable.clear_bob_fake_img_instagram);
            }

        } while (pickedImageExampl == lastPickedExampl);

        lastPickedExampl = pickedImageExampl;

        imageViewTwitterExample.setImageResource(bobLinksArray[0].getmImage());
        linkTextUpperImageExampl = bobLinksArray[0].getmLink();
        imageViewTwitterExample.setTag(bobLinksArray[0].getmImage());
        Integer resource = (Integer) imageViewTwitterExample.getTag();

        if (resource == R.drawable.bob_real_img_x){
            imageViewInstagramExample.setImageResource(R.drawable.bob_real_img_instagram);
            linkTextLowerImageExampl = "https://www.instagram.com/bob253784738/";
            currentImageExampl = "Bob's real X and instagram accounts.";
            counterRealProfile += 1;
        }

        if (resource == R.drawable.bob_real_img_instagram){
            imageViewInstagramExample.setImageResource(R.drawable.bob_real_img_x);
            linkTextLowerImageExampl = "https://twitter.com/Bob253784738";
            currentImageExampl = "Bob's real X and instagram accounts.";
            counterRealProfile += 1;
        }

        if (resource == R.drawable.clear_bob_fake_img_x){
            imageViewInstagramExample.setImageResource(R.drawable.clear_bob_fake_img_instagram);
            linkTextLowerImageExampl = "https://www.instagram.com/tombobnyc/?hl=en";
            currentImageExampl = "Bob's clear fake X and instagram accounts.";
            counterClearFakeProfile += 1;
        }

        if (resource == R.drawable.clear_bob_fake_img_instagram){
            imageViewInstagramExample.setImageResource(R.drawable.clear_bob_fake_img_x);
            linkTextLowerImageExampl = "https://twitter.com/Bob";
            currentImageExampl = "Bob's clear fake X and instagram accounts.";
            counterClearFakeProfile += 1;
        }


    }


    BobLinks b01 = new BobLinks(R.drawable.bob_real_img_instagram, "https://www.instagram.com/bob253784738/");
    BobLinks b02 = new BobLinks(R.drawable.bob_real_img_x, "https://twitter.com/Bob253784738");
    BobLinks b03 = new BobLinks(R.drawable.clear_bob_fake_img_x, "https://twitter.com/Bob");
    BobLinks b04 = new BobLinks(R.drawable.clear_bob_fake_img_instagram, "https://www.instagram.com/tombobnyc/?hl=en");

    BobLinks [] bobLinksArray = new BobLinks[]{
            b01, b02, b03, b04
    };

    public void shuffleBobLinks(){
        Collections.shuffle(Arrays.asList(bobLinksArray));
    }


}
