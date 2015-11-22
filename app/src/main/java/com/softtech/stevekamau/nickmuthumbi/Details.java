package com.softtech.stevekamau.nickmuthumbi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softtech.stevekamau.nickmuthumbi.app.AppController;


public class Details extends ActionBarActivity {
    private static String Title = "title";
    private static String Rate = "rating";
    private static String Genre = "genre";
    private static String Year = "year";
    TextView title, rating, genre, rYear;
    ImageView img1, img2, img3, img4;
    Button btn;
    String url = "http://softtech.comuv.com/nick/nick_backend/take_order.php";
    String data, item_name,time;
    ProgressDialog PD;
    SharedPreferences pref;
    String getmovie, getactor,formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent i = getIntent();

        final String name = i.getStringExtra(Title);
        String rate = i.getStringExtra(Rate);
        String genres = i.getStringExtra(Genre);
        String year = i.getStringExtra(Year);

        String fontPath = "fonts/Lato-Semibold.ttf";
        TextView txtGhost = (TextView) findViewById(R.id.header);
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        txtGhost.setTypeface(tf);

        title = (TextView) findViewById(R.id.name);
        rating = (TextView) findViewById(R.id.rate);
        genre = (TextView) findViewById(R.id.genres);
        rYear = (TextView) findViewById(R.id.year);
        title.setText(name);
        rating.setText(rate);
        genre.setText(genres);
        rYear.setText(year);
        rYear.setVisibility(View.INVISIBLE);

        img1 = (ImageView) findViewById(R.id.air_back);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        img2 = (ImageView) findViewById(R.id.saf_back);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        img3 = (ImageView) findViewById(R.id.air_share);
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Get " + title.getText() + " of " + rYear.getText() + " at only " + rating.getText() + ". Call 0711219490 now and enjoy data bundles at affordable prices!!");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Data Bundles");
                startActivity(Intent.createChooser(shareIntent, "Tell a friend..."));
            }
        });
        img4 = (ImageView) findViewById(R.id.saf_share);
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Get " + title.getText() + " of " + rYear.getText() + " at only " + rating.getText() + ". Call 0711219490 now and enjoy data bundles at affordable prices!!");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Data Bundles");
                startActivity(Intent.createChooser(shareIntent, "Tell a friend..."));
            }
        });

        RelativeLayout airtel_layout = (RelativeLayout) findViewById(R.id.airtel);
        RelativeLayout safaricom_layout = (RelativeLayout) findViewById(R.id.safaricom);

        if (rYear.getText().toString().contains("airtel")) {
            safaricom_layout.setVisibility(View.INVISIBLE);
        } else {
            airtel_layout.setVisibility(View.INVISIBLE);
        }
        PD = new ProgressDialog(this);
        PD.setMessage("Loading.....");
        PD.setCancelable(false);

        pref = getSharedPreferences("MoviePref", MODE_PRIVATE);

        btn = (Button) findViewById(R.id.reserve);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert();

            }
        });

    }

    private void showAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to make this reservation?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //create first Calendar object
                                SimpleDateFormat sdf = new SimpleDateFormat("KK:mm aa dd' 'MMM,' 'yyyy ");
                                Calendar todayC = Calendar.getInstance() ;
                                Date today = todayC.getTime();
                                formattedDate = sdf.format(today);

                                insert();
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void insert() {
        PD.show();
        getmovie = pref.getString("Movie", "");
        getactor = pref.getString("Actor", "");
        data = title.getText().toString();
        time=formattedDate;
        item_name = rYear.getText().toString() + " " + "\n" + getmovie + "\n" + getactor;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PD.dismiss();
                        showAlertDialog();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(getApplicationContext(),
                        "Failed to send,Please try again later", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("item_name", item_name);
                params.put("data", data);
                params.put("time", time);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(postRequest);
    }

    private void showAlertDialog() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.thanks, null);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(layout);
        adb.setCancelable(false);

        adb.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        adb.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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
