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
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = ActivityRegisterBinding.inflate(getLayoutInflater());

        setContentView(registerBinding.getRoot());

        AppDB db = AppDB.getDBInstance(getApplicationContext());
        userDao = db.userDao();

        registerBinding.btnRegister.setOnClickListener(v -> {
            String password = registerBinding.etRegisterPassword.getText().toString();
            String username = registerBinding.etRegisterUsername.getText().toString();

            TextView tvError = registerBinding.tvRegisterError;
            tvError.setText("");

            if (!isValidInput(username, password, tvError)) {
                tvError.setVisibility(View.VISIBLE);
                return;
            }
            User user = new User(username, password);
            userDao.insert(user);
            finish();
        });
    }

    private boolean isValidInput(String username, String password, TextView tvError) {
        if (!password.equals(registerBinding.etRegisterAgain.getText().toString())) {
            tvError.setText(R.string.the_passwords_are_different);
            return false;
        }
        if (password.length() < 8) {
            tvError.setText(R.string.tv_password_length);
            return false;
        }
        if (!Pattern.matches(".*[0-9].*", password)) {
            tvError.setText(R.string.tv_password_numeric);
            return false;
        }
        if (!Pattern.matches(".*[a-z].*", password)) {
            tvError.setText(R.string.tv_password_lowercase);
            return false;
        }
        if (!Pattern.matches(".*[A-Z].*", password)) {
            tvError.setText(R.string.tv_password_uppercase);
            return false;
        }
        if (password.length() > 20) {
            tvError.setText("Password is too long! Please enter a password with 20 characters or fewer.");
            return false;
        }
        if (username.length() < 3) {
            tvError.setText(R.string.tv_username_length);
            return false;
        }
        if (username.length() > 20) {
            tvError.setText("Username is too long! Please enter a name with 20 characters or fewer.");
            return false;
        }
        if (!Pattern.matches("[A-Za-z0-9]+", username)) {
            tvError.setText(R.string.invalid_username);
            return false;
        }
        if (userDao.getUsername(username) > 0) {
            tvError.setText(R.string.username_taken);
            return false;
        }
        return true;
    }
}