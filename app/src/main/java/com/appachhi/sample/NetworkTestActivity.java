package com.appachhi.sample;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.appachhi.sdk.instrument.transition.ScreenTransitionManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkTestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ScreenTransitionManager.getInstance().beginTransition(this, "SecondScreen");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_test);
        EditText editText = findViewById(R.id.url);
        findViewById(R.id.action).setOnClickListener(v -> {
            new FetchAsyncTask().execute(editText.getText().toString().isEmpty() ? Uri.parse("https://jsonplaceholder.typicode.com/posts") : Uri.parse(editText.getText().toString()));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenTransitionManager.getInstance().endTransition(this, "SecondScreen");
    }

    private static class FetchAsyncTask extends AsyncTask<Uri, Void, String> {

        @Override
        protected String doInBackground(Uri... uris) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Uri uri : uris) {
                try {
                    for (int i = 0; i < 2000; i++) {
                        if (!isCancelled()) {
                            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(uri.toString()).openConnection();
                            String message = httpURLConnection.getResponseMessage();
                            stringBuilder.append(message);
                            stringBuilder.append("\n\n\n");
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return stringBuilder.toString();
        }
    }
}
