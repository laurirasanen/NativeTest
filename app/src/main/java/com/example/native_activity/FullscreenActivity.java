package com.example.native_activity;

import android.annotation.SuppressLint;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NativeActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;

import com.example.native_activity.databinding.ActivityFullscreenBinding;

import java.io.IOException;

public class FullscreenActivity extends AppCompatActivity {

    private ActivityFullscreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFullscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Unpack assets to internal storage
        boolean success = false;
        try {
            AssetHelper.copyDirorfileFromAssetManager(
                    getApplicationContext().getFilesDir(),
                    getApplicationContext().getAssets(),
                    "",
                    "assets"
            );
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
            finish();
            success = false;
        }

        // Start native activity
        if (success)
        {
            Intent intent = new Intent(this, NativeActivity.class);
            startActivity(intent);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            hideSystemUI();
        }

        finish();
    }

    private void hideSystemUI() {
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}