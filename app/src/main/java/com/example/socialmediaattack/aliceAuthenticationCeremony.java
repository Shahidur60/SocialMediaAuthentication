package com.example.socialmediaattack;

import android.annotation.SuppressLint;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class aliceAuthenticationCeremony extends AppCompatActivity {

    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/usersData";

    ImageView imageViewTwitter;
    ImageView imageViewFacebook;
    ImageView imageViewPinteres;
    ImageView imageViewInstagram;

    Button btnAccept;
    Button btnReject;
    Button btnNext;

    TextView textViewAlice;
    TextView textViewDescription;

    Random r;

    int pickedImage = 0, lastPicked = 0;

    private String linkTextUpperImage;
    private String linkTextLowerImage;

    SharedPreferences sp;

    String fName, lName;

    private String currentImage;

    private String oldFileText = "File starts for authenticating Alice: \n\n";

    private int counter = 0;

    private int counterRealProfile = 0;
    private int counterFakeProfile = 0;
    private int counterClearFakeProfile = 0;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alice_authentication_ceremony);

        imageViewTwitter = findViewById(R.id.imageTwitterView);
        imageViewFacebook = findViewById(R.id.imageFacebookView);
        imageViewPinteres = findViewById(R.id.imagePinteresView);
        imageViewInstagram = findViewById(R.id.imageInstagramView);

        btnAccept = findViewById(R.id.btnAccept);
        btnReject = findViewById(R.id.btnReject);
        btnNext = findViewById(R.id.btnNext);

        textViewAlice = findViewById(R.id.textViewAlice);
        textViewDescription = findViewById(R.id.description);

        showRandomAliceLinks();

        sp = getApplicationContext().getSharedPreferences("myUserPrefs", Context.MODE_PRIVATE);
        fName = sp.getString("firstName", "");
        lName = sp.getString("lastName", "");

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //write user's decision into a file
                String myFile = path + "/" + fName + "_" + lName + "_Auth4Alice" + ".txt";
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
                //Toast.makeText(aliceAuthenticationCeremony.this, "You marked these accounts verified!", Toast.LENGTH_SHORT).show();
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //write user's decision into a file
                String myFile = path + "/" + fName + "_" + lName + "_Auth4Alice" + ".txt";
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
                //Toast.makeText(aliceAuthenticationCeremony.this, "You marked these accounts unverified!", Toast.LENGTH_SHORT).show();
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
                if (counter != 30){
                    showRandomAliceLinks();
                    btnNext.setEnabled(false);
                    btnAccept.setEnabled(true);
                    btnReject.setEnabled(true);
                }
                else {
                    startActivity(new Intent(aliceAuthenticationCeremony.this,FinalActivity.class));
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

    public void showRandomAliceLinks(){

        //remove duplicate
        do {
            shuffleAliceLinks();
            pickedImage = aliceLinksArray[0].getmImage();

            if (counterRealProfile == 10){
                do {
                    shuffleAliceLinks();
                    pickedImage = aliceLinksArray[0].getmImage();
                } while (pickedImage == R.drawable.alice_real_img_x || pickedImage == R.drawable.alice_real_img_instagram);
            }

            if (counterFakeProfile == 10){
                do {
                    shuffleAliceLinks();
                    pickedImage = aliceLinksArray[0].getmImage();
                } while (pickedImage == R.drawable.alice_fake_img_x || pickedImage == R.drawable.alice_fake_img_instagram);
            }

            if (counterClearFakeProfile == 10){
                do {
                    shuffleAliceLinks();
                    pickedImage = aliceLinksArray[0].getmImage();
                } while (pickedImage == R.drawable.clear_alice_fake_img_x || pickedImage == R.drawable.clear_alice_fake_img_instagram);
            }

            if (counterRealProfile == 10 && counterFakeProfile == 10){
                do {
                    shuffleAliceLinks();
                    pickedImage = aliceLinksArray[0].getmImage();
                } while (pickedImage == R.drawable.alice_real_img_x || pickedImage == R.drawable.alice_real_img_instagram || pickedImage == R.drawable.alice_fake_img_x || pickedImage == R.drawable.alice_fake_img_instagram);
            }

            if (counterRealProfile == 10 && counterClearFakeProfile == 10){
                do {
                    shuffleAliceLinks();
                    pickedImage = aliceLinksArray[0].getmImage();
                } while (pickedImage == R.drawable.alice_real_img_x || pickedImage == R.drawable.alice_real_img_instagram || pickedImage == R.drawable.clear_alice_fake_img_x || pickedImage == R.drawable.clear_alice_fake_img_instagram);
            }

            if (counterFakeProfile == 10 && counterClearFakeProfile == 10){
                do {
                    shuffleAliceLinks();
                    pickedImage = aliceLinksArray[0].getmImage();
                } while (pickedImage == R.drawable.alice_fake_img_x || pickedImage == R.drawable.alice_fake_img_instagram || pickedImage == R.drawable.clear_alice_fake_img_x || pickedImage == R.drawable.clear_alice_fake_img_instagram);
            }

        } while (pickedImage == lastPicked);

        lastPicked = pickedImage;

        imageViewTwitter.setImageResource(aliceLinksArray[0].getmImage());
        linkTextUpperImage = aliceLinksArray[0].getmLink();
        imageViewTwitter.setTag(aliceLinksArray[0].getmImage());
        Integer resource = (Integer) imageViewTwitter.getTag();

        if (resource == R.drawable.alice_fake_img_x){
            imageViewInstagram.setImageResource(R.drawable.alice_fake_img_instagram);
            linkTextLowerImage = "https://www.instagram.com/alice1000100000/";
            currentImage = "Alice's fake X and instagram accounts.";
            counterFakeProfile += 1;
        }

        if (resource == R.drawable.alice_real_img_x){
            imageViewInstagram.setImageResource(R.drawable.alice_real_img_instagram);
            linkTextLowerImage = "https://www.instagram.com/alice100010000/";
            currentImage = "Alice's real X and instagram accounts.";
            counterRealProfile += 1;
        }

        if (resource == R.drawable.alice_fake_img_instagram){
            imageViewInstagram.setImageResource(R.drawable.alice_fake_img_x);
            linkTextLowerImage = "https://twitter.com/Alice1000100000";
            currentImage = "Alice's fake X and instagram accounts.";
            counterFakeProfile += 1;
        }

        if (resource == R.drawable.alice_real_img_instagram){
            imageViewInstagram.setImageResource(R.drawable.alice_real_img_x);
            linkTextLowerImage = "https://twitter.com/Alice100010000";
            currentImage = "Alice's real X and instagram accounts.";
            counterRealProfile += 1;
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


    }

    AliceLinks a01 = new AliceLinks(R.drawable.alice_fake_img_x, "https://twitter.com/Alice1000100000");
    AliceLinks a02 = new AliceLinks(R.drawable.alice_real_img_instagram, "https://www.instagram.com/alice100010000/");
    AliceLinks a03 = new AliceLinks(R.drawable.alice_fake_img_instagram, "https://www.instagram.com/alice1000100000/");
    AliceLinks a04 = new AliceLinks(R.drawable.alice_real_img_x, "https://twitter.com/Alice100010000");
    AliceLinks a05 = new AliceLinks(R.drawable.clear_alice_fake_img_x, "https://twitter.com/doekis_");
    AliceLinks a06 = new AliceLinks(R.drawable.clear_alice_fake_img_instagram, "https://www.instagram.com/alicelk/?hl=en");

    AliceLinks [] aliceLinksArray = new AliceLinks[]{
            a01, a02, a03, a04, a05, a06
    };

    public void shuffleAliceLinks(){
        Collections.shuffle(Arrays.asList(aliceLinksArray));
    }

}
