package com.orane.icliniq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookingViewActivity extends AppCompatActivity {


    TextView tv_notify, tvdate, tvtime, tvquery, tvtz, tvlang, tvstatus;
    JSONObject jsonobj;
    public String str_response, booking_id, bid, bquery, bcdate, btime, btz, bctype, blang, bstatus;
    CircleImageView imageview_poster;
    ScrollView main_layout;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_view);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Booking View");
        }
        //------------ Object Creations -------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        imageview_poster = (CircleImageView) findViewById(R.id.imageview_poster);
        tv_notify = (TextView) findViewById(R.id.tv_notify);
        tvdate = (TextView) findViewById(R.id.tvdate);
        tvtime = (TextView) findViewById(R.id.tvtime);
        tvquery = (TextView) findViewById(R.id.tvquery);
        tvtz = (TextView) findViewById(R.id.tvtz);
        tvlang = (TextView) findViewById(R.id.tvlang);
        tvstatus = (TextView) findViewById(R.id.tvstatus);
        main_layout = (ScrollView) findViewById(R.id.main_layout);

        Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        tvdate.setTypeface(font_bold);
        tvtime.setTypeface(font_bold);
        tvtz.setTypeface(font_bold);
        tvlang.setTypeface(font_bold);
        tvquery.setTypeface(font_reg);

        ((TextView) findViewById(R.id.tv_date_lab)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_time_lab)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_schedule)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tvstatus)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_timezone_lab)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_lang_lab)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_query_lab)).setTypeface(font_bold);

        try {
            Model.kiss.record("android.Patient.BookingView");
            FlurryAgent.onPageView();
        } catch (Exception ee) {
            System.out.println("Exception-----------" + ee.toString());
            ee.printStackTrace();
        }

        try {
            Intent intent = getIntent();
            booking_id = intent.getStringExtra("tv_booking_id");
        } catch (Exception e) {
            System.out.println("Exception--Getting--Intent--" + e.toString());
            e.printStackTrace();
        }

        full_process();

        //-------------------------------------------
        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        Model.kiss.record("android.patient.Booking_View");
        //-------------------------------------------
    }

    public void full_process() {

        try {
            if (new NetCheck().netcheck(BookingViewActivity.this)) {
                //----------------------------------------------------
                String url = Model.BASE_URL + "/sapp/viewBooking?user_id=" + (Model.id) + "&id=" + booking_id + "&format=json&token=" + Model.token + "&enc=1";
                System.out.println("url-------------" + url);
                new JSON_View_Booking().execute(url);
                //----------------------------------------------------
            } else {
                System.out.println("Internet is not Available");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class JSON_View_Booking extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(BookingViewActivity.this);
            dialog.setMessage("please wait");
            //dialog.setTitle("");
            dialog.show();
            dialog.setCancelable(false);

            main_layout.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);
                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            try {

                jsonobj = new JSONObject(str_response);


                if (jsonobj.has("token_status")) {
                    String token_status = jsonobj.getString("token_status");
                    if (token_status.equals("0")) {
                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================
                        finishAffinity();
                        Intent intent = new Intent(BookingViewActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    bid = jsonobj.getString("id");
                    bquery = jsonobj.getString("query");
                    bcdate = jsonobj.getString("consult_date");
                    btime = jsonobj.getString("str_time_range");
                    btz = jsonobj.getString("timezone");
                    bctype = jsonobj.getString("strConsultType");
                    blang = jsonobj.getString("language");

                    bstatus = jsonobj.getString("strStatus");

                    if (bstatus != null && !bstatus.isEmpty() && !bstatus.equals("null") && !bstatus.equals("")) {
                        tvstatus.setText(bstatus);
                    } else {
                        tvstatus.setVisibility(View.GONE);
                    }

                    tvdate.setText(bcdate);
                    tvtime.setText(btime);
                    tvquery.setText(Html.fromHtml(bquery));
                    tvtz.setText(btz);
                    tvlang.setText(blang);


                    //-------------------------------------------
                    if (bctype.equals("Phone")) {
                        imageview_poster.setBackgroundResource(R.mipmap.phone_cons_ico_color);
                    } else if (bctype.equals("Direct Visit")) {
                        imageview_poster.setBackgroundResource(R.mipmap.direct_walk);
                    } else {
                        imageview_poster.setBackgroundResource(R.mipmap.video_cons_ico_color);
                    }
                    //-------------------------------------------

                    //---------------------------------------------------------
                    if (bstatus.equals("Doctor not assigned yet.")) {
                        tv_notify.setVisibility(View.VISIBLE);
                    } else {
                        tv_notify.setVisibility(View.GONE);
                    }
                    //---------------------------------------------------------

                    main_layout.setVisibility(View.VISIBLE);
                    dialog.cancel();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
