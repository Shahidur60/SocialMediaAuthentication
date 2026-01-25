package com.example.socialmediaattack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class MainActivity extends AppCompatActivity {

    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/usersData";

    EditText firstName;
    EditText lastName;
    //EditText email;
    Button btnRegister;

    SharedPreferences sp;

    String fName, lName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstName = findViewById(R.id.inputFirstName);
        lastName = findViewById(R.id.inputLastName);
        //email = findViewById(R.id.inputEmail);
        btnRegister = findViewById(R.id.btnRegister);

        sp = getSharedPreferences("myUserPrefs", Context.MODE_PRIVATE);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userFirstName = firstName.getText().toString();
                String userLastName = lastName.getText().toString();
                if(TextUtils.isEmpty(userFirstName)){
                    firstName.setError("This field cannot be empty!");
                }

                if(TextUtils.isEmpty(userLastName)){
                    lastName.setError("This field cannot be empty!");
                }

                else {
                    String fileName1 = firstName.getText().toString().trim() +"_"+ lastName.getText().toString().trim()+"_Auth4Bob";
                    //String fileName2 = firstName.getText().toString().trim() +"_"+ lastName.getText().toString().trim()+"_Auth4Alice";
                    fName = firstName.getText().toString().trim();
                    lName = lastName.getText().toString().trim();

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("firstName", fName);
                    editor.putString("lastName", lName);
                    editor.apply();

                    writeToFile(fileName1);
                    //writeToFile(fileName2);
                    startActivity(new Intent(MainActivity.this,FriendOneActivity.class));
                }
            }
        });

    }


    private void writeToFile(String fileName) {
        try {

            File dir = new File(path);
            if(!dir.exists()){
                dir.mkdirs();
            }

            File statText = new File(path+ "/"+fileName+".txt");
            FileOutputStream is = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
            w.write(fileName+"\n");
            w.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


}