package io.github.dsckiet.dsc_event_attendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static io.github.dsckiet.dsc_event_attendance.Constant.BASE_URL;
import static io.github.dsckiet.dsc_event_attendance.Constant.GOOGLE_ID;

public class MainActivity extends AppCompatActivity {

    private static String url = BASE_URL +  "attendees/" + GOOGLE_ID;
    RecyclerView recyclerView;
    JSONObject jsonObj;
    CardView btnMarkAtt;
    JSONArray jsonArray;
    String jsonStr;
    int len;
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> emailList = new ArrayList<>();
    ArrayList<String> attendeeIdList = new ArrayList<>();
    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private RecyclerView.Adapter adapter;

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

        new GetAttendees().execute();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView = findViewById(R.id.rec_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.INVISIBLE);
    }

    public void sendBundle() {
        adapter = new AttendeesAdapter(getApplicationContext(), nameList, emailList, attendeeIdList);
        recyclerView.setAdapter(adapter);
    }

    class GetAttendees extends AsyncTask<Void, Void, Void> {
        GetAttendees() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("<loading>...</loading>");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            jsonStr = sh.makeServiceCall(url);
            if (jsonStr != null) {
                try {
                    jsonObj = new JSONObject(jsonStr);

                    jsonArray = jsonObj.getJSONArray("attendees");
                    len = jsonArray.length();

                    for (int i = 0; i < len; i++) {
                        JSONObject attendeeObj = jsonArray.getJSONObject(i);
                        nameList.add(attendeeObj.getString("name"));
                        emailList.add(attendeeObj.getString("email"));
                        attendeeIdList.add(attendeeObj.getString("attendeeId"));
                    }
                    return null;
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error Loading data!!\nTry Again.", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            recyclerView.setVisibility(View.VISIBLE);
            sendBundle();
        }
    }
}
