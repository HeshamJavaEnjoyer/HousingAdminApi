package org.school.housing.views;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import org.school.housing.Prefs.AppSharedPreferences;
import org.school.housing.R;

/**
 * @author  Hesham AbuShaban
 * @version 1.7
 * @since   2023-01-20
 */

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        controlSplashActivity();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private void controlSplashActivity() {
        //3000ms - 3s
        new Handler().postDelayed(() -> {
            //Logged in ?
            boolean isLogged_In = AppSharedPreferences.getInstance().isLoggedIn();
            Intent intent = new Intent(getApplicationContext(), isLogged_In ? MainActivity.class : LoginActivity.class);
            startActivity(intent);
        }, 1500);
    }
}