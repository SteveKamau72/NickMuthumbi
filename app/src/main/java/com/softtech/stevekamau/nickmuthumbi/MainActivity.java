package com.softtech.stevekamau.nickmuthumbi;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.softtech.stevekamau.nickmuthumbi.adapter.CustomListAdapter;
import com.softtech.stevekamau.nickmuthumbi.app.AppController;
import com.softtech.stevekamau.nickmuthumbi.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {
    MyHorizontalScrollView scrollView;
    View menu;
    View app;
    LinearLayout lay1, lay2, lay3, lay4, lay5, lay6;
    ImageView btnSlide, check, settings;
    ImageView img1, img2, img3;
    boolean menuOut = false;
    Handler handler = new Handler();
    int btnWidth;
    private ProgressBar bar;
    private static final String TAG = MainActivity.class.getSimpleName();
    RelativeLayout relative1;
    // Movies json url
    private static final String url = "http://softtech.comuv.com/nick/nick.js";
    private List<Model> modelList = new ArrayList<Model>();
    private ListView listView;
    private CustomListAdapter adapter;
    private static String Title = "title";
    private static String Rate = "rating";
    private static String Genre = "genre";
    private static String Year = "year";
    TextView t1, t2, txtResponse, txtResponse2, txt1, txt2;
    SharedPreferences pref;
    private String jsonResponse, jsonResponse2;
    private String urlJsonObj = "http://softtech.comuv.com/nick/update.js";
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.activity_main, null);
        setContentView(scrollView);

        //slide
        menu = inflater.inflate(R.layout.horz_scroll_menu, null);
        ViewGroup slideBar = (ViewGroup) menu.findViewById(R.id.slideBar);
        lay1 = (LinearLayout) slideBar.findViewById(R.id.layout_one);
        lay1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Airtel.class);
                startActivity(intent);
            }
        });
        lay2 = (LinearLayout) slideBar.findViewById(R.id.layout_two);
        lay2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Safaricom.class);
                startActivity(intent);
            }
        });
        lay3 = (LinearLayout) slideBar.findViewById(R.id.layout_three);
        lay3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BlogView.class);
                startActivity(intent);
            }
        });
        lay4 = (LinearLayout) slideBar.findViewById(R.id.layout_four);
        lay4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AndroidTips.class);
                startActivity(intent);
            }
        });
        lay5 = (LinearLayout) slideBar.findViewById(R.id.layout_five);
        lay5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfile.class);
                startActivity(intent);
            }
        });
        lay6 = (LinearLayout) slideBar.findViewById(R.id.layout_six);
        lay6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
                startActivity(intent);
            }
        });
        t1 = (TextView) slideBar.findViewById(R.id.name);
        t2 = (TextView) slideBar.findViewById(R.id.number);
        pref = getSharedPreferences("MoviePref", MODE_PRIVATE);
        String getmovie, getactor;
        getmovie = pref.getString("Movie", "");
        getactor = pref.getString("Actor", "");
        t1.setText(getmovie);
        t2.setText(getactor);
        String fontPath = "fonts/MadrasExtraLight.otf";

        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        t1.setTypeface(tf);
        t2.setTypeface(tf);
        if (t1.getText().toString().length() > 0 || t2.getText().toString().length() > 0) {

        } else {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        }


        //main
        app = inflater.inflate(R.layout.horz_scroll_app, null);
        final ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.tabBar);

        txt1 = (TextView) tabBar.findViewById(R.id.txt1);
        txt2 = (TextView) tabBar.findViewById(R.id.txt2);
        check = (ImageView) tabBar.findViewById(R.id.img_availability);
        settings = (ImageView) tabBar.findViewById(R.id.settings);
        btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide);
        btnSlide.setOnClickListener(new ClickListenerForScrolling(scrollView, menu));
        check.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInterNet()) {
                    bar = (ProgressBar) tabBar.findViewById(R.id.progressBar);
                    bar.setVisibility(View.VISIBLE);
                    check.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    dataRequest();
                    makeJsonObjectRequest();
                } else {
                    showDATADisabledAlertToUser();
                }

            }
        });
        settings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterPopup(view);
            }
        });
        listView = (ListView) tabBar.findViewById(R.id.list);
        adapter = new CustomListAdapter(this, modelList);
        listView.setVisibility(View.GONE);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

        fontPath = "fonts/MadrasExtraLight.otf";
        TextView txtGhost = (TextView) tabBar.findViewById(R.id.check_txt);
        tf = Typeface.createFromAsset(getAssets(), fontPath);
        txtGhost.setTypeface(tf);

        relative1 = (RelativeLayout) tabBar.findViewById(R.id.check_availability_layout);
        //views go here
        final View[] children = new View[]{menu, app};

        // Scroll to app (view[1]) when layout finished.
        int scrollToViewIdx = 1;
        scrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        CustomListAdapter listAdapter = (CustomListAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private void dataRequest() {

        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        relative1.setVisibility(View.GONE);

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
                relative1.setVisibility(View.GONE);

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }


    private void showFilterPopup(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        // Inflate the menu from xml
        popup.getMenuInflater().inflate(R.menu.popup_filter, popup.getMenu());
        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_share:
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "For affordable Data Bundles,Android tricks,latest Tech news and tips,download the NickMuthumbi App for android at PlayStore and enjoy.");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "NickMuthumbi App for android");
                        startActivity(Intent.createChooser(shareIntent, "Tell a friend..."));
                        return true;
                    case R.id.menu_find:
                        socialDialog();
                        return true;
                    case R.id.menu_settings:
                        Intent intent = new Intent(getApplicationContext(), SettingsScreen.class);
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });
        // Handle dismissal with: popup.setOnDismissListener(...);
        // Show the menu
        popup.show();
    }

    private void socialDialog() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.social, null);
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        img1 = (ImageView) layout.findViewById(R.id.imageView);
        img2 = (ImageView) layout.findViewById(R.id.imageView2);
        img3 = (ImageView) layout.findViewById(R.id.imageView3);
        img1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", "0711219490");
                smsIntent.putExtra("sms_body", "");
                startActivity(smsIntent);

            }
        });
        img2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                igOpen();
            }
        });
        img3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                newFacebookIntent();
            }
        });
        adb.setView(layout);
        adb.setCancelable(true);

        adb.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        adb.show();
    }

    private void igOpen() {
        Uri uri = Uri.parse("http://instagram.com/_u/nick_muthumbi");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/nick_muthumbi")));
        }
    }

    private void newFacebookIntent() {
        try {
            Intent followIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/100000213014827"));
            startActivity(followIntent);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent followIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/100000213014827"));
                    startActivity(followIntent);
                }
            }, 1000 * 2);

        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/mwanginick")));
            String errorMessage = (e.getMessage() == null) ? "Message is empty" : e.getMessage();
            Log.e("Unlock_ScreenActivity:FacebookAppNotFound", errorMessage);
        }
    }

    /**
     * Helper for examples with a HSV that should be scrolled by a menu View's width.
     */
    static class ClickListenerForScrolling implements OnClickListener {
        HorizontalScrollView scrollView;
        View menu;
        /**
         * Menu must NOT be out/shown to start with.
         */
        boolean menuOut = false;

        public ClickListenerForScrolling(HorizontalScrollView scrollView, View menu) {
            super();
            this.scrollView = scrollView;
            this.menu = menu;
        }

        @Override
        public void onClick(View v) {

            int menuWidth = menu.getMeasuredWidth();

            // Ensure menu is visible
            menu.setVisibility(View.VISIBLE);

            if (!menuOut) {
                // Scroll to 0 to reveal menu
                int left = 0;
                scrollView.smoothScrollTo(left, 0);
            } else {
                // Scroll to menuWidth so menu isn't on screen.
                int left = menuWidth;
                scrollView.smoothScrollTo(left, 0);
            }
            menuOut = !menuOut;
        }
    }

    /**
     * Helper that remembers the width of the 'slide' button, so that the 'slide' button remains in view, even when the menu is
     * showing.
     */
    static class SizeCallbackForMenu implements MyHorizontalScrollView.SizeCallback {
        int btnWidth;
        View btnSlide;

        public SizeCallbackForMenu(View btnSlide) {
            super();
            this.btnSlide = btnSlide;
        }

        @Override
        public void onGlobalLayout() {
            btnWidth = btnSlide.getMeasuredWidth();
            System.out.println("btnWidth=" + btnWidth);
        }

        @Override
        public void getViewSize(int idx, int w, int h, int[] dims) {
            dims[0] = w;
            dims[1] = h;
            final int menuIdx = 0;
            if (idx == menuIdx) {
                dims[0] = w - btnWidth;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

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

    private void makeJsonObjectRequest() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String name = response.getString("name");
                    JSONObject phone = response.getJSONObject("phone");
                    String mobile = phone.getString("mobile");

                    jsonResponse = "";
                    jsonResponse2 = "";
                    jsonResponse += name + "\n\n";
                    jsonResponse2 += mobile + "\n\n";

                    txt1.setText(jsonResponse);
                    txt2.setText(jsonResponse2);


                    if (txt1.getText().toString().contains("none")) {

                    } else if (txt2.getText().toString().contains("none")) {

                    } else {
                        showAlert();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void showAlert() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.display, null);
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        txtResponse = (TextView) layout.findViewById(R.id.txtResponse);
        txtResponse2 = (TextView) layout.findViewById(R.id.txtResponse2);
        btn = (Button) layout.findViewById(R.id.btn);
        txtResponse2.setVisibility(View.GONE);
        txtResponse.setText(jsonResponse);
        txtResponse2.setText(jsonResponse2);
        adb.setView(layout);
        adb.setCancelable(false);

        adb.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        adb.show();

    }

    public void goToSo(View view) {
        goToUrl(jsonResponse2);
    }

    private void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

}

