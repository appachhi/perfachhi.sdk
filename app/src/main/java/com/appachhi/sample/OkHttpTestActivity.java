package com.appachhi.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.appachhi.sdk.instrument.transition.ScreenTransitionManager;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpTestActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;
    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ScreenTransitionManager.getInstance().beginTransition(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http_test);
        Spinner spinner = findViewById(R.id.request_type);
        spinner.setSelection(0);
        final EditText url = findViewById(R.id.url);
        final Button enqueue = findViewById(R.id.enqueue);
        final Button execute = findViewById(R.id.execute);
        final TextView status = findViewById(R.id.status);
        if (url.getText().length() <= 0) {
            enqueue.setEnabled(false);
            execute.setEnabled(false);
        }
        url.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && URLUtil.isValidUrl(s.toString())) {
                    enqueue.setEnabled(true);
                    execute.setEnabled(true);

                } else {
                    enqueue.setEnabled(false);
                    execute.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        okHttpClient = new OkHttpClient.Builder().build();
        enqueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Request request = new Request.Builder().get().url(url.getText().toString()).build();
                Callback callback = new Callback() {
                    @Override
                    public void onFailure(final Call call, final IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                status.setText(e.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                status.setText(String.format(Locale.ENGLISH,"%d", response.code()));
                            }
                        });
                    }
                };
                Log.d("OKHTTPTESTACTIVITY", "onCreate: Inside click handler");
                okHttpClient.newCall(request).enqueue(callback);
            }
        });
        execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EXECUTOR.execute(new Runnable() {
                    @Override
                    public void run() {
                        Request request = new Request.Builder().get().url(url.getText().toString()).build();
                        try {
                            final Response response = okHttpClient.newCall(request).execute();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    status.setText(String.format(Locale.ENGLISH,"%d", response.code()));
                                }
                            });
                        } catch (final IOException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    status.setText(e.getMessage());
                                }
                            });
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenTransitionManager.getInstance().beginTransition(this);
    }
}
