package com.example.maldeeplink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class deeplink extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Uri data = getIntent().getData();
            if (data != null && "koko".equals(data.getScheme()) && "auth".equals(data.getHost())) {
                handleAuthResponse(data);
                return;
            }
            showErrorAndFinish("Invalid authentication response");
        } catch (Exception e) {
            showErrorAndFinish("Error processing authentication");
        }
    }

    private void handleAuthResponse(Uri data) throws UnsupportedEncodingException, JSONException {
        String token = data.getQueryParameter("token");
        if (token == null) {
            showErrorAndFinish("Missing authentication data");
            return;
        }
        saveAuthToken(token);
        Toast.makeText(this, "Login Successfully", 1).show();
        GetData(token);
    }

    private void GetData(String token) {
        OkHttpClient client = ApiClient.getInstance();
        JSONObject json = new JSONObject();
        try {
            json.put("token", token);
            RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
            Request request = new Request.Builder().url("http://84.247.132.220:8000/profile.php").post(body).build();
            client.newCall(request).enqueue(new AnonymousClass1());
        } catch (JSONException e) {
            showErrorAndFinish("Error creating verification request");
        }
    }

    class AnonymousClass1 implements Callback {
        AnonymousClass1() {
        }

        @Override // okhttp3.Callback
        public void onFailure(Call call, final IOException e) {
            deeplink.this.runOnUiThread(new Runnable() { // from class: com.koko.deeplink.AuthCallbackActivity$1$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    deeplink.AnonymousClass1.this.m157lambda$onFailure$0$comkokodeeplinkAuthCallbackActivity$1(e);
                }
            });
        }
        void m157lambda$onFailure$0$comkokodeeplinkAuthCallbackActivity$1(IOException e) {
            deeplink.this.showErrorAndFinish("Network error: " + e.getMessage());
        }

        @Override // okhttp3.Callback
        public void onResponse(Call call, Response response) throws IOException {
            try {
                String responseData = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseData);
                if (response.isSuccessful()) {
                    JSONObject user = jsonResponse.getJSONObject("user");
                    final String userId = user.getString("id");
                    final String username = user.getString("username");
                    String email = user.optString(NotificationCompat.CATEGORY_EMAIL, "");
                    SharedPreferences prefs = deeplink.this.getSharedPreferences("auth", 0);
                    prefs.edit().putString("user_id", userId).putString("username", username).putString(NotificationCompat.CATEGORY_EMAIL, email).apply();
                    deeplink.this.runOnUiThread(new Runnable() { // from class: com.koko.deeplink.AuthCallbackActivity$1$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            deeplink.AnonymousClass1.this.m158lambda$onResponse$1$comkokodeeplinkAuthCallbackActivity$1(userId, username);
                        }
                    });
                } else {
                    final String errorMessage = jsonResponse.optString("message", "Failed to fetch user data");
                    deeplink.this.runOnUiThread(new Runnable() { // from class: com.koko.deeplink.AuthCallbackActivity$1$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            deeplink.AnonymousClass1.this.m159lambda$onResponse$2$comkokodeeplinkAuthCallbackActivity$1(errorMessage);
                        }
                    });
                }
            } catch (Exception e) {
                deeplink.this.runOnUiThread(new Runnable() { // from class: com.koko.deeplink.AuthCallbackActivity$1$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        deeplink.AnonymousClass1.this.m160lambda$onResponse$3$comkokodeeplinkAuthCallbackActivity$1();
                    }
                });
            }
        }

        void m158lambda$onResponse$1$comkokodeeplinkAuthCallbackActivity$1(String userId, String username) {
            deeplink.this.navigateToProfileActivity(userId, username);
            deeplink.this.finish();
        }

        void m159lambda$onResponse$2$comkokodeeplinkAuthCallbackActivity$1(String errorMessage) {
            deeplink.this.showErrorAndFinish(errorMessage);
        }

         void m160lambda$onResponse$3$comkokodeeplinkAuthCallbackActivity$1() {
            deeplink.this.showErrorAndFinish("Error processing user data");
        }
    }

    private void saveAuthToken(String token) {
        SharedPreferences prefs = getSharedPreferences("auth", 0);
        prefs.edit().putString("token", token).apply();
    }


    public void navigateToProfileActivity(String userId, String username) {
        Intent intent = new Intent(this, (Class<?>) profile.class).putExtra("user_id", userId).putExtra("username", username).addFlags(335544320);
        startActivity(intent);
    }

    public void showErrorAndFinish(String message) {
        Toast.makeText(this, message, 1).show();
        finish();
    }
}