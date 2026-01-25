package com.example.socialmediaattack;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class bobAuthenticationCeremony extends AppCompatActivity {

    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/usersData";

    ImageView imageViewTwitter;
    ImageView imageViewFacebook;
    ImageView imageViewPinteres;
    ImageView imageViewInstagram;

    Button btnAccept;
    Button btnReject;
    Button btnNext;

    TextView textViewBob;
    TextView textViewDescription;

    Random r;

    int pickedImage = 0, lastPicked = 0;

    private String linkTextUpperImage;
    private String linkTextLowerImage;

    SharedPreferences sp;

    String fName, lName;

    private String currentImage;

    private String oldFileText = "File starts for authenticating Bob: \n\n";

    private int counter = 0;

    private int counterRealProfile = 0;
    private int counterFakeProfile = 0;
    private int counterClearFakeProfile = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bob_authentication_ceremony);

        imageViewTwitter = findViewById(R.id.imageTwitterView);
        imageViewFacebook = findViewById(R.id.imageFacebookView);
        imageViewPinteres = findViewById(R.id.imagePinteresView);
        imageViewInstagram = findViewById(R.id.imageInstagramView);

        btnAccept = findViewById(R.id.btnAccept);
        btnReject = findViewById(R.id.btnReject);
        btnNext = findViewById(R.id.btnNext);

        textViewBob = findViewById(R.id.textViewBob);
        textViewDescription = findViewById(R.id.description);

        showRandomBobLinks();

        sp = getApplicationContext().getSharedPreferences("myUserPrefs", Context.MODE_PRIVATE);
        fName = sp.getString("firstName", "");
        lName = sp.getString("lastName", "");

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //write user's decision into a file
                String myFile = path + "/" + fName + "_" + lName + "_Auth4Bob" + ".txt";
                //load(view, myFile);
                try {
                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                    counter += 1;
                    String userDecision = counter+"- "+fName+" "+lName+" "+"accepted"+" "+currentImage+"\n\n";
                    oldFileText += userDecision;
                    myOutWriter.append(oldFileText);
                    myOutWriter.close();
                    fOut.close();
                }
                catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
                btnNext.setEnabled(true);
                btnAccept.setEnabled(false);
                btnReject.setEnabled(false);
                //Toast.makeText(bobAuthenticationCeremony.this, "You marked these accounts verified!", Toast.LENGTH_SHORT).show();
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //write user's decision into a file
                String myFile = path + "/" + fName + "_" + lName + "_Auth4Bob" + ".txt";
                //load(view, myFile);
                try {
                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                    counter += 1;
                    String userDecision = counter+"- "+fName+" "+lName+" "+"rejected"+" "+currentImage+"\n\n";
                    oldFileText += userDecision;
                    myOutWriter.append(oldFileText);
                    myOutWriter.close();
                    fOut.close();
                }
                catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
                btnNext.setEnabled(true);
                btnAccept.setEnabled(false);
                btnReject.setEnabled(false);
                //Toast.makeText(bobAuthenticationCeremony.this, "You marked these accounts unverified!", Toast.LENGTH_SHORT).show();
            }
        });

        imageViewTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initialize link
                String sWebLink = linkTextUpperImage;
                openLink(sWebLink);
            }
        });

        imageViewInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initialize link
                String sWeblink = linkTextLowerImage;
                openLink(sWeblink);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter != 40){
                    showRandomBobLinks();
                    btnNext.setEnabled(false);
                    btnAccept.setEnabled(true);
                    btnReject.setEnabled(true);
                }
                else {
                    startActivity(new Intent(bobAuthenticationCeremony.this,FinalActivity.class));
                }
            }
        });

    }

    public void load(View view, String fileName){
        FileInputStream fis = null;
        try {
            fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null){
                sb.append(text).append("\n");
            }

            oldFileText = sb.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
            pickedImage = bobLinksArray[0].getmImage();

            if (counterRealProfile == 10){
                do {
                    shuffleBobLinks();
                    pickedImage = bobLinksArray[0].getmImage();
                } while (pickedImage == R.drawable.bob_real_img_x || pickedImage == R.drawable.bob_real_img_instagram);
            }

            if (counterFakeProfile == 10){
                do {
                    shuffleBobLinks();
                    pickedImage = bobLinksArray[0].getmImage();
                } while (pickedImage == R.drawable.bob_fake_img_x || pickedImage == R.drawable.bob_fake_img_instagram || pickedImage == R.drawable.bob_fake_img_x_2 || pickedImage == R.drawable.bob_fake_img_instagram_2);
            }

            if (counterClearFakeProfile == 20){
                do {
                    shuffleBobLinks();
                    pickedImage = bobLinksArray[0].getmImage();
                } while (pickedImage == R.drawable.clear_bob_fake_img_x || pickedImage == R.drawable.clear_bob_fake_img_instagram || pickedImage == R.drawable.clear_alice_fake_img_x || pickedImage == R.drawable.clear_alice_fake_img_instagram || pickedImage == R.drawable.clear_bob_fake_img_x_2 || pickedImage == R.drawable.clear_bob_fake_img_instagram_2 || pickedImage == R.drawable.alice_fake_img_x || pickedImage == R.drawable.alice_fake_img_instagram);
            }

            if (counterRealProfile == 10 && counterFakeProfile == 10){
                do {
                    shuffleBobLinks();
                    pickedImage = bobLinksArray[0].getmImage();
                } while (pickedImage == R.drawable.bob_real_img_x || pickedImage == R.drawable.bob_real_img_instagram || pickedImage == R.drawable.bob_fake_img_x || pickedImage == R.drawable.bob_fake_img_instagram || pickedImage == R.drawable.bob_fake_img_x_2 || pickedImage == R.drawable.bob_fake_img_instagram_2);
            }

            if (counterRealProfile == 10 && counterClearFakeProfile == 20){
                do {
                    shuffleBobLinks();
                    pickedImage = bobLinksArray[0].getmImage();
                } while (pickedImage == R.drawable.bob_real_img_x || pickedImage == R.drawable.bob_real_img_instagram || pickedImage == R.drawable.clear_bob_fake_img_x || pickedImage == R.drawable.clear_bob_fake_img_instagram || pickedImage == R.drawable.clear_alice_fake_img_x || pickedImage == R.drawable.clear_alice_fake_img_instagram || pickedImage == R.drawable.clear_bob_fake_img_x_2 || pickedImage == R.drawable.clear_bob_fake_img_instagram_2 || pickedImage == R.drawable.alice_fake_img_x || pickedImage == R.drawable.alice_fake_img_instagram);
            }

            if (counterFakeProfile == 10 && counterClearFakeProfile == 20){
                do {
                    shuffleBobLinks();
                    pickedImage = bobLinksArray[0].getmImage();
                } while (pickedImage == R.drawable.bob_fake_img_x || pickedImage == R.drawable.bob_fake_img_instagram || pickedImage == R.drawable.clear_bob_fake_img_x || pickedImage == R.drawable.clear_bob_fake_img_instagram || pickedImage == R.drawable.clear_alice_fake_img_x || pickedImage == R.drawable.clear_alice_fake_img_instagram || pickedImage == R.drawable.bob_fake_img_x_2 || pickedImage == R.drawable.bob_fake_img_instagram_2 || pickedImage == R.drawable.clear_bob_fake_img_x_2 || pickedImage == R.drawable.clear_bob_fake_img_instagram_2 || pickedImage == R.drawable.alice_fake_img_x || pickedImage == R.drawable.alice_fake_img_instagram);
            }

        } while (pickedImage == lastPicked);

        lastPicked = pickedImage;

        imageViewTwitter.setImageResource(bobLinksArray[0].getmImage());
        linkTextUpperImage = bobLinksArray[0].getmLink();
        imageViewTwitter.setTag(bobLinksArray[0].getmImage());
        Integer resource = (Integer) imageViewTwitter.getTag();

        if (resource == R.drawable.bob_fake_img_x){
            imageViewInstagram.setImageResource(R.drawable.bob_fake_img_instagram);
            linkTextLowerImage = "https://www.instagram.com/bob253874738/";
            currentImage = "Bob's fake X and instagram accounts. Hidden Attack Case";
            counterFakeProfile += 1;
        }

        if (resource == R.drawable.bob_real_img_x){
            imageViewInstagram.setImageResource(R.drawable.bob_real_img_instagram);
            linkTextLowerImage = "https://www.instagram.com/bob253784738/";
            currentImage = "Bob's real X and instagram accounts.";
            counterRealProfile += 1;
        }

        if (resource == R.drawable.bob_fake_img_instagram){
            imageViewInstagram.setImageResource(R.drawable.bob_fake_img_x);
            linkTextLowerImage = "https://twitter.com/Bob253874738";
            currentImage = "Bob's fake X and instagram accounts. Hidden Attack Case";
            counterFakeProfile += 1;
        }

        if (resource == R.drawable.bob_real_img_instagram){
            imageViewInstagram.setImageResource(R.drawable.bob_real_img_x);
            linkTextLowerImage = "https://twitter.com/Bob253784738";
            currentImage = "Bob's real X and instagram accounts.";
            counterRealProfile += 1;
        }

        if (resource == R.drawable.clear_bob_fake_img_x){
            imageViewInstagram.setImageResource(R.drawable.clear_bob_fake_img_instagram);
            linkTextLowerImage = "https://www.instagram.com/tombobnyc/?hl=en";
            currentImage = "Bob's clear fake X and instagram accounts.";
            counterClearFakeProfile += 1;
        }

        if (resource == R.drawable.clear_bob_fake_img_instagram){
            imageViewInstagram.setImageResource(R.drawable.clear_bob_fake_img_x);
            linkTextLowerImage = "https://twitter.com/Bob";
            currentImage = "Bob's clear fake X and instagram accounts.";
            counterClearFakeProfile += 1;
        }

        if (resource == R.drawable.clear_alice_fake_img_x){
            imageViewInstagram.setImageResource(R.drawable.clear_alice_fake_img_instagram);
            linkTextLowerImage = "https://www.instagram.com/alicelk/?hl=en";
            currentImage = "Alice's clear fake X and instagram accounts.";
            counterClearFakeProfile += 1;
        }

        if (resource == R.drawable.clear_alice_fake_img_instagram){
            imageViewInstagram.setImageResource(R.drawable.clear_alice_fake_img_x);
            linkTextLowerImage = "https://twitter.com/doekis_";
            currentImage = "Alice's clear fake X and instagram accounts.";
            counterClearFakeProfile += 1;
        }

        if (resource == R.drawable.bob_fake_img_x_2){
            imageViewInstagram.setImageResource(R.drawable.bob_fake_img_instagram_2);
            linkTextLowerImage = "https://www.instagram.com/bob253748738/";
            currentImage = "Bob's fake X and instagram accounts. Hidden Attack Case";
            counterFakeProfile += 1;
        }

        if (resource == R.drawable.bob_fake_img_instagram_2){
            imageViewInstagram.setImageResource(R.drawable.bob_fake_img_x_2);
            linkTextLowerImage = "https://twitter.com/Bob253748738";
            currentImage = "Bob's fake X and instagram accounts. Hidden Attack Case";
            counterFakeProfile += 1;
        }

        if (resource == R.drawable.clear_bob_fake_img_x_2){
            imageViewInstagram.setImageResource(R.drawable.clear_bob_fake_img_instagram_2);
            linkTextLowerImage = "https://www.instagram.com/boboiboy/";
            currentImage = "Bob's clear fake X and instagram accounts.";
            counterClearFakeProfile += 1;
        }

        if (resource == R.drawable.clear_bob_fake_img_instagram_2){
            imageViewInstagram.setImageResource(R.drawable.clear_bob_fake_img_x_2);
            linkTextLowerImage = "https://twitter.com/bobbyjonc";
            currentImage = "Bob's clear fake X and instagram accounts.";
            counterClearFakeProfile += 1;
        }

        if (resource == R.drawable.alice_fake_img_x){
            imageViewInstagram.setImageResource(R.drawable.alice_fake_img_instagram);
            linkTextLowerImage = "https://www.instagram.com/bobbyleelive/";
            currentImage = "Bob's clear fake X and instagram accounts.";
            counterClearFakeProfile += 1;
        }

        if (resource == R.drawable.alice_fake_img_instagram){
            imageViewInstagram.setImageResource(R.drawable.alice_fake_img_x);
            linkTextLowerImage = "https://twitter.com/Bee_Bob";
            currentImage = "Bob's clear fake X and instagram accounts.";
            counterClearFakeProfile += 1;
        }


    }

    BobLinks b01 = new BobLinks(R.drawable.bob_fake_img_x, "https://twitter.com/Bob253874738");
    BobLinks b02 = new BobLinks(R.drawable.bob_real_img_instagram, "https://www.instagram.com/bob253784738/");
    BobLinks b03 = new BobLinks(R.drawable.bob_fake_img_instagram, "https://www.instagram.com/bob253874738/");
    BobLinks b04 = new BobLinks(R.drawable.bob_real_img_x, "https://twitter.com/Bob253784738");
    BobLinks b05 = new BobLinks(R.drawable.clear_bob_fake_img_x, "https://twitter.com/Bob");
    BobLinks b06 = new BobLinks(R.drawable.clear_bob_fake_img_instagram, "https://www.instagram.com/tombobnyc/?hl=en");
    BobLinks b07 = new BobLinks(R.drawable.clear_alice_fake_img_x, "https://twitter.com/doekis_");
    BobLinks b08 = new BobLinks(R.drawable.clear_alice_fake_img_instagram, "https://www.instagram.com/alicelk/?hl=en");
    BobLinks b09 = new BobLinks(R.drawable.bob_fake_img_x_2, "https://twitter.com/Bob253748738");
    BobLinks b10 = new BobLinks(R.drawable.bob_fake_img_instagram_2, "https://www.instagram.com/bob253748738/");
    //BobLinks b11 = new BobLinks(R.drawable.bob_fake_img_x_3, "https://twitter.com/B48253478469738");
    //BobLinks b12 = new BobLinks(R.drawable.bob_fake_img_instagram_3, "https://www.instagram.com/a48253478469738/");
    BobLinks b11 = new BobLinks(R.drawable.clear_bob_fake_img_x_2, "https://twitter.com/bobbyjonc");
    BobLinks b12 = new BobLinks(R.drawable.clear_bob_fake_img_instagram_2, "https://www.instagram.com/boboiboy/");
    BobLinks b13 = new BobLinks(R.drawable.alice_fake_img_x, "https://twitter.com/Bee_Bob");
    BobLinks b14 = new BobLinks(R.drawable.alice_fake_img_instagram, "https://www.instagram.com/bobbyleelive/");

    BobLinks [] bobLinksArray = new BobLinks[]{
            b01, b02, b03, b04, b05, b06, b07, b08, b09, b10, b11, b12, b13, b14
    };

    public void shuffleBobLinks(){
        Collections.shuffle(Arrays.asList(bobLinksArray));
    }

}
