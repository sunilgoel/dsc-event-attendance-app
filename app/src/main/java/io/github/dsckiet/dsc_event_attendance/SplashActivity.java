package io.github.dsckiet.dsc_event_attendance;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        ImageView logolayout = findViewById(R.id.logo);
        logolayout.animate().alpha(1.0f).scaleX(1.1f).scaleY(1.1f).setDuration(2500);

        Boolean isFirstRun = getSharedPreferences("PREFERENCES", MODE_PRIVATE)
                .getBoolean("isfirstrun", true);

        if (isFirstRun) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(getApplicationContext(), AuthActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 3000);

            getSharedPreferences("PREFERENCES", MODE_PRIVATE).edit()
                    .putBoolean("isfirstrun", false).commit();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    FirebaseUser user = mAuth.getCurrentUser();

                    if (user == null) {
                        Intent i = new Intent(getApplicationContext(), AuthActivity.class);
                        startActivity(i);
                        finish();
                        finishActivity(0);
                    } else {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                        finishActivity(0);
                    }
                }
            }, 2600);
        }

    }
}
