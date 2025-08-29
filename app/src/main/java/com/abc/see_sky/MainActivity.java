package com.abc.see_sky;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView logo = findViewById(R.id.imageView);
        TextView appName = findViewById(R.id.appNameTextView);

        // Fade-in Animation
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1500);
        fadeIn.setFillAfter(true);

        logo.startAnimation(fadeIn);
        appName.startAnimation(fadeIn);

        // Navigate to Home after 2.5 seconds
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(MainActivity.this, Home.class));
            finish();
        }, 2500);
    }
}
