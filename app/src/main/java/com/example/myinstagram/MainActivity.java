package com.example.myinstagram;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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

//        addParseObject("ricky9667", 85);
//        addParseObject("txya900619", 100);
//        addParseObject("xanonymous", 100);
//        addParseObject("cathychen", 60);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");

//        query.whereEqualTo("score", 100); // only shows data that requires the query
//        query.setLimit(1); // limit the amount of data to show

        query.whereLessThan("score", 80);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseObject object: objects) {
                            String username = object.getString("username");
                            int score = object.getInt("score");

                            score += 20;
                            object.put("score", score);
                            object.saveInBackground();

                            Log.i("username", username);
                            Log.i("score", Integer.toString(score));
                        }
                    }
                }
            }
        });

        ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }

    private void addParseObject(final String username, final int score) {
        ParseObject object = new ParseObject("Score");
        object.put("username", username);
        object.put("score", score);

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("Success", "data of " + username + " with score " + score + " saved.");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}

