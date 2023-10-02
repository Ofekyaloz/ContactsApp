package com.example.contactsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.contactsapp.AppDB;
import com.example.contactsapp.Converters;
import com.example.contactsapp.R;
import com.example.contactsapp.daos.UserDao;
import com.example.contactsapp.databinding.ActivityMainBinding;
import com.example.contactsapp.entities.User;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private TextView loginError;
    private EditText etUsername;
    private EditText etPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.contactsapp.databinding.ActivityMainBinding activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        loginError = activityMainBinding.loginTvError;
        etUsername = activityMainBinding.loginEtUsername;
        etPassword = activityMainBinding.loginEtPassword;


        AppDB db = AppDB.getDBInstance(getApplicationContext());
        UserDao userDao = db.userDao();

        Button btnSignup = activityMainBinding.btnGotoRegister;
        btnSignup.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            loginError.setVisibility(View.INVISIBLE);
            etUsername.setText("");
            etPassword.setText("");
            startActivity(intent);
        });

        Button btnLogin = activityMainBinding.btnLogin;
        btnLogin.setOnClickListener(view -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            if (!Pattern.matches(getString(R.string.username_regex), username) ||
                    !Pattern.matches(getString(R.string.password_regex), password)) {
                loginError.setVisibility(View.VISIBLE);
                return;
            }

            User user = userDao.get(username);
            if (user != null && password.equals(user.getPassword())) {
                Intent intent = new Intent(this, ContactListActivity.class);
                intent.putExtra("userid", user.getUserid());
                intent.putExtra("username", user.getUsername());
                intent.putExtra("settings", Converters.toBooleanArray(user.getSettings()));

                loginError.setVisibility(View.INVISIBLE);
                etUsername.setText("");
                etPassword.setText("");
                startActivity(intent);
            } else {
                loginError.setVisibility(View.VISIBLE);
            }
        });
    }


}