package com.example.myinstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class UserFeedActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        setTitle("Images of " + username);
        linearLayout = findViewById(R.id.linearLayout);

        ParseQuery<ParseObject> query = new ParseQuery<>("Image");
        query.whereEqualTo("username", username);
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseObject object : objects) {
                        ParseFile file = object.getParseFile("image");
                        if (file != null) {
                            addImageToLayout(file);
                        }
                    }
                }
            }
        });
    }

    private void addImageToLayout(ParseFile file) {
        file.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e == null && data != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    ImageView imageView = new ImageView(getApplicationContext());
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                    ));

                    imageView.setImageBitmap(bitmap);
                    linearLayout.addView(imageView);
                }
            }
        });
    }
}
