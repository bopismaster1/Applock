package com.voicelock.voicelockv3;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class Set_password extends AppCompatActivity {
    private SpeechRecognizer speechRecognizer;
    private Intent intentRecognizer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO}, PERMISSION_GRANTED);

        intentRecognizer= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);



        speechRecognizer= SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches=results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String string = "";
                /*if (matches!=null){
                    string=matches.get(0);
                    textView.setText(string);
                }*/
                if (matches!=null){
                    string=matches.get(0);

                    File f = getFileStreamPath("config.txt");

                    File fileEvents = new File(getFilesDir()+"/config.txt");
                    StringBuilder text = new StringBuilder();
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(fileEvents));
                        String line;
                        while ((line = br.readLine()) != null) {
                            text.append(line);
                        }
                        br.close();
                    } catch (IOException e) { }
                    String resultstr = text.toString();

                    if (f.length() == 0) {
                        try {
                            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
                            outputStreamWriter.write(string);
                            outputStreamWriter.close();
                            Toast.makeText(Set_password.this, "asd "+string, Toast.LENGTH_SHORT).show();
                            Toast.makeText(Set_password.this, "Password has been set. please verify", Toast.LENGTH_SHORT).show();
                        }
                        catch (IOException e) {
                            Log.e("Exception", "File write failed: " + e.toString());
                            Toast.makeText(Set_password.this, "Error creating password", Toast.LENGTH_SHORT).show();
                        }
                    }
                    Toast.makeText(Set_password.this, string+"From speech", Toast.LENGTH_SHORT).show();
                    Toast.makeText(Set_password.this, resultstr+" from file", Toast.LENGTH_SHORT).show();
                    if (resultstr.equals(string)){
                        Toast.makeText(Set_password.this, "contecnt "+resultstr+"="+string, Toast.LENGTH_SHORT).show();

                        System.exit(0);


                    }
                    else{
                        Toast.makeText(Set_password.this, "contecnt "+resultstr+"!="+string, Toast.LENGTH_SHORT).show();
                    }



                }
                }


            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
    }

    public void reord_password(View view) {
        speechRecognizer.startListening(intentRecognizer);

    }
    public void verify_password(View view) {
        speechRecognizer.startListening(intentRecognizer);

    }


}
