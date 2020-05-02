package com.example.myinstagram;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    ConstraintLayout backgroundLayout;
    ImageView logoImageView;
    TextView switchTextView;
    TextView messageTextView;
    EditText usernameEditText;
    EditText passwordEditText;

    ParseUser currentUser;
    Boolean signUpMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Instagram");

        parseServerSetup();
        layoutSetup(); // find Views and set onClickListeners

        currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            String message = "Hi, " + currentUser.getUsername() + "!";
            messageTextView.setText(message);
            showUserList();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.switchTextView) {
            Button button = findViewById(R.id.button);
            if (signUpMode) {
                button.setText("Login");
                switchTextView.setText("or, Sign Up");
            } else {
                button.setText("Sign Up");
                switchTextView.setText("or, Login");
            }
            signUpMode = !signUpMode;
        }

        // error occurred
//        if (view.getId() == R.id.logoImageView || view.getId() == R.id.backgroundLayout) {
//            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0); // close keyboard
//        }
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            signUpClicked(view);
        }

        return false;
    }

    public void showUserList() {
        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);
    }

    public void signUpClicked(View view) {

        final String username = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        if (username.equals("") || password.equals("")) {
            Toast.makeText(this, "A username and password is required", Toast.LENGTH_SHORT).show();
        } else {
            if (signUpMode) {
                accountSignUp(username, password);
            } else {
                accountLogIn(username, password);
            }
        }
    }

    private void accountSignUp(final String username, final String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("Account Sign Up", username + " signed up successfully.");
                    Toast.makeText(MainActivity.this, username + " signed up successfully", Toast.LENGTH_SHORT).show();
                    showUserList();
                } else {
                    Log.i("Account Sign Up", "Sign up failed");
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void accountLogIn(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    String message = "Hi, " + user.getUsername() + "!";
                    Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                    Log.i("Log in Success", user.getUsername() + " logged in successfully.");
                    messageTextView.setText(message);
                    showUserList();
                } else {
                    Toast.makeText(MainActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                    Log.i("Log in Failed", "Log in failed");
                }
            }
        });
    }

    private void parseServerSetup() {

        final String APP_ID = "myappID";
        final String CLIENT_KEY = "ksDOgKSSQr4B"; // masterKey
        final String IP_ADDRESS = "3.133.104.214";

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId(APP_ID)
                .clientKey(CLIENT_KEY)
                .server("http://" + IP_ADDRESS + "/parse/")
                .build()
        );

        // ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        TextView switchTextView = findViewById(R.id.switchTextView);
        switchTextView.setOnClickListener(this);
    }

    private void layoutSetup() {
        backgroundLayout = findViewById(R.id.backgroundLayout);
        logoImageView = findViewById(R.id.logoImageView);
        switchTextView = findViewById(R.id.switchTextView);
        messageTextView = findViewById(R.id.messageTextView);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        backgroundLayout.setOnClickListener(this);
        logoImageView.setOnClickListener(this);
        switchTextView.setOnClickListener(this);
    }
}