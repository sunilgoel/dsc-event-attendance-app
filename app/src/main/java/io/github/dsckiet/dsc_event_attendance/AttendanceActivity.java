package io.github.dsckiet.dsc_event_attendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AttendanceActivity extends AppCompatActivity {
    private static String url = "http://dsckiet.herokuapp.com/" + "api/mark_attendance";
    String attendeeId = "";
    CardView button;
    EditText attendeeIDet;
    private String TAG = AttendanceActivity.class.getSimpleName();
    private ProgressDialog progressDialog;

    public static String POST(String url, String attendeeId) {
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("attendeeId", attendeeId);
            json = jsonObject.toString();
            Log.e("JSON DATA", json);
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        attendeeIDet = findViewById(R.id.editText_attendeeId);


        button = findViewById(R.id.btnMarkAtt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(AttendanceActivity.this);
                progressDialog.setMessage("<loading>...</loading>");
                progressDialog.setCancelable(false);
                progressDialog.show();

                attendeeId = String.valueOf(attendeeIDet.getText());

                new HttpAsyncTask().execute(url);

            }
        });


    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String attId = attendeeId;
            return POST(urls[0], "DSC-" + attId);
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Log.e("Result: ", result);
            Toast.makeText(getBaseContext(), "" + result, Toast.LENGTH_LONG).show();
        }
    }
}
