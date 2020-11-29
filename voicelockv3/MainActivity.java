package com.voicelock.voicelockv3;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.voicelock.voicelockv3.Adabters.AppAdabters;
import com.voicelock.voicelockv3.model.AppItem;
import com.voicelock.voicelockv3.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    LinearLayout layout_permision;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ceheck if password is set or not.
        try {


                initToolbar();
                initView();

        }
        catch (Exception ex){
            Toast.makeText(MainActivity.this,"Error From Catch",Toast.LENGTH_LONG).show();
            Log.e("Exception", "error here:" + ex.toString());
        }

    }

    private void initView() {

        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppAdabters appAdabters = new AppAdabters(this,getAllapps());
        recyclerView.setAdapter(appAdabters);

        layout_permision=findViewById(R.id.layout_permision);
    }

    private List<AppItem> getAllapps() {
        List<AppItem> results = new ArrayList<>();

        PackageManager pk = getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN,null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfoList =pk.queryIntentActivities(intent,0);

        for (ResolveInfo resolveInfo : resolveInfoList){
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            results.add(new AppItem(activityInfo.loadIcon(pk),activityInfo.loadLabel(pk).toString(),activityInfo.packageName));

        }
        return  results;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initToolbar() {

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().setTitle("AppList");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home)
            finish();

        return true;
    }

    public void set_permision(View view) {
        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onResume() {


        if (Utils.checkPermision(this)) {
            layout_permision.setVisibility(View.GONE);
        } else {
            layout_permision.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }
}
