package com.example.mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    private Button buttonViewProfile;
    private Button buttonLogout;
    private Button buttonForm;
    private SessionManager sessionManager;
    private RequestQueue requestQueue;

    private static final String LOGOUT_URL = "https://react-router-hono-fullstack-template.blodunet149.workers.dev/api/logout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sessionManager = new SessionManager(this);
        
        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            redirectToLogin();
            return;
        }

        buttonViewProfile = findViewById(R.id.buttonViewProfile);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonForm = findViewById(R.id.buttonForm);
        
        requestQueue = Volley.newRequestQueue(this);

        buttonForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, FormActivity.class);
                startActivity(intent);
            }
        });

        buttonViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    private void logoutUser() {
        final String token = sessionManager.getToken();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGOUT_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        sessionManager.logoutUser();
                        Toast.makeText(DashboardActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                        redirectToLogin();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Even if API fails, we should probably log the user out locally
                        sessionManager.logoutUser();
                        Toast.makeText(DashboardActivity.this, "Logged out locally", Toast.LENGTH_SHORT).show();
                        redirectToLogin();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    private void redirectToLogin() {
        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
