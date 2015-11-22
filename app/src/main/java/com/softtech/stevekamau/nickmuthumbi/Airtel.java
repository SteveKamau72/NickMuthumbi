package com.softtech.stevekamau.nickmuthumbi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.softtech.stevekamau.nickmuthumbi.adapter.CustomGridAdapter;
import com.softtech.stevekamau.nickmuthumbi.app.AppController;
import com.softtech.stevekamau.nickmuthumbi.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Airtel extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    RelativeLayout relative1;
    // Movies json url
    private static final String url = "http://softtech.comuv.com/nick/airtel.js";
    private List<Model> modelList = new ArrayList<Model>();
    private GridView gridView;
    private CustomGridAdapter adapter;
    private static String Title = "title";
    private static String Rate = "rating";
    private static String Genre = "genre";
    private static String Year = "year";
    ImageView img1, img2;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airtel);
        gridView = (GridView) findViewById(R.id.grid);
        adapter = new CustomGridAdapter(this, modelList);
        gridView.setAdapter(adapter);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
        if (checkInterNet()) {
            dataRequest();
        } else {
            showDATADisabledAlertToUser();
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String name = ((TextView) view.findViewById(R.id.title))
                        .getText().toString();
                String rate = ((TextView) view.findViewById(R.id.rating))
                        .getText().toString();
                String genres = ((TextView) view.findViewById(R.id.genre))
                        .getText().toString();
                String year = ((TextView) view.findViewById(R.id.releaseYear))
                        .getText().toString();

                Intent intent = new Intent(getApplicationContext(), Details.class);
                intent.putExtra(Title, name);
                ;
                intent.putExtra(Rate, rate);
                intent.putExtra(Genre, genres);
                intent.putExtra(Year, year);

                startActivity(intent);
            }
        });
        img1 = (ImageView) findViewById(R.id.air_back);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void dataRequest() {

        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        progressBar.setVisibility(View.GONE);

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Model model = new Model();
                                model.setTitle(obj.getString("title"));
                                model.setThumbnailUrl(obj.getString("image"));
                                model.setRating(((Number) obj.get("rating"))
                                        .doubleValue());
                                model.setYear(obj.getString("releaseYear"));

                                // Genre is json array
                                JSONArray genreArry = obj.getJSONArray("genre");
                                ArrayList<String> genre = new ArrayList<String>();
                                for (int j = 0; j < genreArry.length(); j++) {
                                    genre.add((String) genreArry.get(j));
                                }
                                model.setGenre(genre);

                                // adding movie to movies array
                                modelList.add(model);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_airtel, menu);
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
            Intent intent = new Intent(getApplicationContext(), SettingsScreen.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDATADisabledAlertToUser() {
//Show AlertDialog to help to Enable DATA(3G)/WIFI connection
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("DATA is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
                                startActivity(callGPSSettingIntent);
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

    private boolean checkInterNet() {//CHECK the WIFI/DATA(3G) connection
        ConnectivityManager connec = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        // Check if wifi or mobile network is available or not. If any of them is
        // available or connected then it will return true, otherwise false;
        return mobile.isConnected() || wifi.isConnected();

    }
}
