package com.softtech.stevekamau.nickmuthumbi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class EditProfile extends ActionBarActivity {
    TextView textSavedMem1;
    SharedPreferences pref;
    EditText t3, t4;
    ImageView img1, img2;
    Button btn;
    String getmovie, getactor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        t3 = (EditText) findViewById(R.id.name);
        t4 = (EditText) findViewById(R.id.number);
        pref = getSharedPreferences("MoviePref", MODE_PRIVATE);

        getmovie = pref.getString("Movie", "");
        getactor = pref.getString("Actor", "");
        t3.setText(getmovie);
        t4.setText(getactor);

        String fontPath = "fonts/MadrasExtraLight.otf";

        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        t3.setTypeface(tf);
        t4.setTypeface(tf);

        img1 = (ImageView) findViewById(R.id.close);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        img2 = (ImageView) findViewById(R.id.back);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn = (Button) findViewById(R.id.save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (t3 != null || !t3.getText().equals(""))

                    if (t4 != null || !t4.getText().equals("")) {
                        String movie, actor;
                        movie = t3.getText().toString();
                        actor = t4.getText().toString();
                        SharedPreferences.Editor edit = pref.edit();
                        //Storing Data using SharedPreferences
                        edit.putString("Movie", movie);
                        edit.putString("Actor", actor);
                        edit.commit();
                        Toast.makeText(getApplicationContext(), "Saved successfully!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        t3.setError("Please enter your full name");
                        t4.setError("Please enter phone number");

                    }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
