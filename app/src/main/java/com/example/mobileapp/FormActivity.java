package com.example.mobileapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class FormActivity extends AppCompatActivity {

    private TextInputEditText editTextNama, editTextEmail, editTextAlamat, editTextNomorHP, editTextKelas, editTextCatatan;
    private Button buttonSubmit;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_activity);

        // Initialize views
        editTextNama = findViewById(R.id.editTextNama);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextAlamat = findViewById(R.id.editTextAlamat);
        editTextNomorHP = findViewById(R.id.editTextNomorHP);
        editTextKelas = findViewById(R.id.editTextKelas);
        editTextCatatan = findViewById(R.id.editTextCatatan);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // Set click listener for submit button
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }

    private void submitForm() {
        // Get values from input fields
        final String nama = editTextNama.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String alamat = editTextAlamat.getText().toString().trim();
        final String nomorHP = editTextNomorHP.getText().toString().trim();
        final String kelas = editTextKelas.getText().toString().trim();
        final String catatan = editTextCatatan.getText().toString().trim();

        // Validate input fields
        if (nama.isEmpty()) {
            editTextNama.setError("Nama harus diisi");
            editTextNama.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email harus diisi");
            editTextEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Format email tidak valid");
            editTextEmail.requestFocus();
            return;
        }

        // Prepare data map
        final Map<String, String> params = new HashMap<>();
        params.put("entry.479274716", nama);
        params.put("entry.1096621200", email);
        params.put("entry.622641120", alamat);
        params.put("entry.810302634", nomorHP);
        params.put("entry.1188280880", kelas);
        params.put("entry.1270704674", catatan);

        // Base URL Google Form
        String url = "https://docs.google.com/forms/d/e/1FAIpQLScAf7G5x8Z5SO0UzICnKpV9PnVkl-k6uez-htkeftIfQfLinA/formResponse";

        // --- DEBUGGING: Generate & Log Full URL ---
        // Copy URL dari Logcat (tag: FormSubmit) dan coba buka di browser jika error 400 berlanjut
        try {
            StringBuilder debugUrl = new StringBuilder(url + "?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                debugUrl.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                        .append("&");
            }
            Log.d("FormSubmit", "URL UNTUK TESTING: " + debugUrl.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // ------------------------------------------

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FormSubmit", "Response: " + response);
                        Toast.makeText(FormActivity.this, "Data berhasil dikirim!", Toast.LENGTH_SHORT).show();
                        clearFormFields();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("FormSubmit", "Error: " + error.toString());
                        String message = "Gagal mengirim data.";

                        if (error.networkResponse != null) {
                            message += " Kode: " + error.networkResponse.statusCode;
                            Log.e("FormSubmit", "Status Code: " + error.networkResponse.statusCode);
                            // Log response body from server for more details
                            if (error.networkResponse.data != null) {
                                try {
                                    String body = new String(error.networkResponse.data, "UTF-8");
                                    Log.e("FormSubmit", "Error Body: " + body);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else if (error.getMessage() != null) {
                            message += " " + error.getMessage();
                        }

                        Toast.makeText(FormActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }

    private void clearFormFields() {
        editTextNama.setText("");
        editTextEmail.setText("");
        editTextAlamat.setText("");
        editTextNomorHP.setText("");
        editTextKelas.setText("");
        editTextCatatan.setText("");
    }
}
