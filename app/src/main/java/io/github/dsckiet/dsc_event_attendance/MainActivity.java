package io.github.dsckiet.dsc_event_attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    CardView btnMarkAtt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMarkAtt = findViewById(R.id.btnMarkAtt);

        btnMarkAtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AttendanceActivity.class));
            }
        });
    }
}
