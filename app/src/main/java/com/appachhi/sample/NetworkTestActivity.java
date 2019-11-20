package com.appachhi.sample;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.appachhi.sdk.instrument.trace.Trace;
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
        final EditText editText = findViewById(R.id.url);
        findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchAsyncTask().execute(editText.getText().toString().isEmpty() ? Uri.parse("https://jsonplaceholder.typicode.com/posts") : Uri.parse(editText.getText().toString()));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenTransitionManager.getInstance().endTransition(this, "SecondScreen");
    }

    private static class FetchAsyncTask extends AsyncTask<Uri, Void, String> {

        @Trace(name = "Network Usage Fetch")
        @Override
        protected String doInBackground(Uri... uris) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Uri uri : uris) {
                try {
                    for (int i = 0; i < 20; i++) {
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
