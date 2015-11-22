package com.softtech.stevekamau.nickmuthumbi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class RegisterActivity extends ActionBarActivity {
    EditText editText1, editText2;
    Button btn;
    SharedPreferences pref;
    String movie, actor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editText1 = (EditText) findViewById(R.id.ed1);
        editText2 = (EditText) findViewById(R.id.ed2);
        pref = getSharedPreferences("MoviePref", MODE_PRIVATE);
        btn = (Button) findViewById(R.id.sign_in);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText1.getText().length() == 0) ;
                if (editText2.getText().length() == 0) {
                    editText1.setError("Please enter your full name");
                    editText2.setError("Please enter phone number");

                } else {
                    movie = editText1.getText().toString();
                    actor = editText2.getText().toString();
                    SharedPreferences.Editor edit = pref.edit();
                    //Storing Data using SharedPreferences
                    edit.putString("Movie", movie);
                    edit.putString("Actor", actor);
                    edit.commit();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
