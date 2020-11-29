package com.voicelock.voicelockv3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.KeyEventDispatcher;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.voicelock.voicelockv3.services.BackgroundManager;
import com.voicelock.voicelockv3.utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import io.paperdb.Paper;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class sec_lock extends AppCompatActivity {
    ImageView passwordmic;
    ImageView regmic;
    EditText regtxt;
    Button regbtn;
    private SpeechRecognizer speechRecognizer;
    private Intent intentRecognizer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec_lock);

        BackgroundManager.getInstance().init(this).startService();
        initIconeApp();

        passwordmic=findViewById(R.id.passwordmic);
        regmic=findViewById(R.id.regmic);
        regtxt=findViewById(R.id.regtxt);
        regbtn=findViewById(R.id.regbtn);

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

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches=results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String string = "";
                string=matches.get(0);
                File f = getFileStreamPath("config.txt");

                if (matches!=null){
                    if (f.length() == 0) {
                        regtxt.setText(string);
                    }else {
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

                        if (resultstr.equals(string)){

                            startAct();

                            //startActivity(new Intent(sec_lock.this, MainActivity.class));
                            //remove this if ever
                            //finish();
                            //startActivity(onNewIntent(););
                        }
                        else{

                            Toast.makeText(sec_lock.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{

                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
        try {
            File f = getFileStreamPath("config.txt");
            if (f.length()== 0) {
                passwordmic.setVisibility(View.GONE);
            } else {
                regmic.setVisibility(View.GONE);
                regtxt.setVisibility(View.GONE);
                regbtn.setVisibility(View.GONE);
            }
        }catch (Exception ex){
            Toast.makeText(this, "Error From catch in sec_lock", Toast.LENGTH_SHORT).show();
        }
    }

    private void initIconeApp() {
        if (getIntent().getStringExtra("broadcast_reciever")!= null){

            ImageView icone = findViewById(R.id.app_icone);
            String current_app = new Utils(this).getLastApp();
            ApplicationInfo applicationInfo = null;

            try {
                applicationInfo = getPackageManager().getApplicationInfo(current_app,0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "message "+ e, Toast.LENGTH_SHORT).show();
            }

            icone.setImageDrawable(applicationInfo.loadIcon(getPackageManager()));

        }
    }

    public void reord_password(View view) {
        speechRecognizer.startListening(intentRecognizer);

    }
    public void save_password(View view){
        try {
            String password;

            password=regtxt.getText().toString();
            //Paper.book().write(password)
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(password);
            outputStreamWriter.close();

            startActivity(new Intent(sec_lock.this,MainActivity.class));
            Toast.makeText(this, "Password has been set", Toast.LENGTH_SHORT).show();

        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            Toast.makeText(sec_lock.this, "Error saving password", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        startCurrentHomepage();
        finish();
    }
    private void  startAct(){
        if (getIntent().getStringExtra("broadcast_reciever")== null){
            startActivity(new Intent(sec_lock.this, MainActivity.class));
        }
    }

    private void startCurrentHomepage() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent,PackageManager.MATCH_DEFAULT_ONLY);

        ActivityInfo activityInfo = resolveInfo.activityInfo;
        ComponentName componentName = new ComponentName(activityInfo.applicationInfo.packageName,activityInfo.name);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(intent);
    }
}
