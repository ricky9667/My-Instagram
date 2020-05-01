package com.example.myinstagram;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    final String APP_ID = "myappID";
    final String CLIENT_KEY = "ksDOgKSSQr4B"; // masterKey
    final String IP_ADDRESS = "3.133.104.214";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId(APP_ID)
                .clientKey(CLIENT_KEY)
                .server("http://" + IP_ADDRESS + "/parse/")
                .build()
        );

        /*ParseUser.logInInBackground("ricky9667", "password", new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Log.i("Log in Success", user.getUsername() + " logged in successfully.");
                } else {
                    Log.i("Log in Failed", "Check your username or password.");

                }
            }
        });*/

        ParseUser.logOut();

        if (ParseUser.getCurrentUser() != null) {
            Log.i("Signed In", "Current user: " + ParseUser.getCurrentUser().getUsername());
        } else {
            Log.i("Not Signed In", "No current user");
        }

        // ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }

    private void accountSignUp(final String username, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("Account Sign Up", username + " signed up successfully.");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}

