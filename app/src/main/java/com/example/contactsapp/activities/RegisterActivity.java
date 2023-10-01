package com.example.contactsapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.contactsapp.AppDB;
import com.example.contactsapp.R;
import com.example.contactsapp.daos.UserDao;
import com.example.contactsapp.databinding.ActivityRegisterBinding;
import com.example.contactsapp.entities.User;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding registerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        registerBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_register);

        AppDB db = AppDB.getDBInstance(getApplicationContext());
        UserDao userDao = db.userDao();

        registerBinding.btnRegister.setOnClickListener(v -> {
            String password = registerBinding.etRegisterPassword.getText().toString();
            String username = registerBinding.etRegisterUsername.getText().toString();

            TextView tvError = registerBinding.tvRegisterError;
            tvError.setText("");

            if (checkInput(username, password, tvError)) {
                return;
            }

            tvError.setVisibility(View.INVISIBLE);

            if (userDao.getUsername(username) > 0) {
                tvError.setText(R.string.username_taken);
                tvError.setVisibility(View.VISIBLE);
                return;
            }

            User user = new User(username, password);
            userDao.insert(user);
            finish();
        });
    }

    private boolean checkInput(String username, String password, TextView tvError) {
        if (!password.equals(registerBinding.etRegisterAgain.getText().toString())) {
            tvError.setText(R.string.the_passwords_are_different);
            tvError.setVisibility(View.VISIBLE);
            return true;
        }

        if (password.length() < 8) {
            tvError.setText(R.string.tv_password_length);
            tvError.setVisibility(View.VISIBLE);
            return true;
        }

        if (!Pattern.matches(".*[0-9].*", password)) {
            tvError.setText(R.string.tv_password_numeric);
            tvError.setVisibility(View.VISIBLE);
            return true;
        }

        if (!Pattern.matches(".*[a-z].*", password)) {
            tvError.setText(R.string.tv_password_lowercase);
            tvError.setVisibility(View.VISIBLE);
            return true;
        }

        if (!Pattern.matches(".*[A-Z].*", password)) {
            tvError.setText(R.string.tv_password_uppercase);
            tvError.setVisibility(View.VISIBLE);
            return true;
        }

        if (username.length() < 3) {
            tvError.setText(R.string.tv_username_length);
            tvError.setVisibility(View.VISIBLE);
            return true;
        }

        if (!Pattern.matches("[A-Za-z0-9]+", username)) {
            tvError.setText(R.string.invalid_username);
            tvError.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }
}