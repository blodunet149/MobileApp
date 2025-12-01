package com.example.mobileapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

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
        String nama = editTextNama.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String alamat = editTextAlamat.getText().toString().trim();
        String nomorHP = editTextNomorHP.getText().toString().trim();
        String kelas = editTextKelas.getText().toString().trim();
        String catatan = editTextCatatan.getText().toString().trim();

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

        // Prepare data for Google Form submission
        String url = "https://docs.google.com/forms/d/e/1FAIpQLScAf7G5x8Z5SO0UzICnKpV9PnVkl-k6uez-htkeftIfQfLinA/formResponse";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FormSubmit", "Response: " + response);
                        Toast.makeText(FormActivity.this, "Data berhasil dikirim!", Toast.LENGTH_SHORT).show();
                        
                        // Clear form fields after successful submission
                        clearFormFields();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("FormSubmit", "Error: " + error.toString());
                        Toast.makeText(FormActivity.this, "Gagal mengirim data. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("entry.479274716", nama);
                params.put("entry.1096621200", email);
                params.put("entry.622641120", alamat);
                params.put("entry.810302634", nomorHP);
                params.put("entry.1188280880", kelas);
                params.put("entry.1270704674", catatan);
                return params;
            }
        };

        // Add request to queue
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