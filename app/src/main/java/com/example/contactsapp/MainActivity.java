package com.example.contactsapp;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Button btnSignup;
    private Button btnLogin;
    private TextView loginError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignup = findViewById(R.id.btn_gotoRegister);
        btnLogin = findViewById(R.id.btnLogin);
        loginError = findViewById(R.id.login_tvError);

        btnSignup.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(view -> {
            String username = findViewById(R.id.login_etUsername).toString();
            String password = findViewById(R.id.login_etPassword).toString();
            if (!Pattern.matches("[ A-Za-z0-9/-]{3,30}$", username) ||
                    !Pattern.matches("[ A-Za-z0-9/-]{4,30}$", password)) {
                loginError.setVisibility(View.VISIBLE);
                return;
            }
            Log.d("", username + " " + password);
//            new Thread(() -> {
//                return;
//            }

        });
    }


}