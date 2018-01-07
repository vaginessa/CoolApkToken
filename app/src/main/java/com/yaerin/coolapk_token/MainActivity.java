package com.yaerin.coolapk_token;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.coolapk.market.util.AuthUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.UUID;

@SuppressLint("SetTextI18n")
public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private EditText callbackUrl;
    private TextView tokenView;
    private TextView responseView;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        callbackUrl = findViewById(R.id.url);
        tokenView = findViewById(R.id.token);
        responseView = findViewById(R.id.response);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        token = AuthUtils.getAS(new ApplicationProxy(), UUID.randomUUID().toString());
        if (TextUtils.isEmpty(token)) {
            tokenView.setText("Failed");
            return true;
        } else {
            tokenView.setText(token + "\n");
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(callbackUrl.getText().toString())
                .post(RequestBody.create(MediaType.parse("text/*"), token))
                .addHeader("Accept", "*/*")
                .addHeader("Cache-Control", "no-cache")
                .build();
        responseView.setText("Waiting response...");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(() -> responseView.setText(e.toString()));
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Headers headers = response.headers();
                StringBuilder data = new StringBuilder("Response Headers:\n\nHTTP/1.1 " + response.code() + " " + response.message() + "\n");
                for (int i = 0; i < headers.size(); i++) {
                    if (!headers.name(i).startsWith("OkHttp")) {
                        data.append(headers.name(i)).append(": ").append(headers.value(i)).append("\n");
                    }
                }
                data.append("\nResponse Body:\n\n").append(response.body().string());
                runOnUiThread(() -> responseView.setText(data.toString()));
            }
        });
        return true;
    }

}
