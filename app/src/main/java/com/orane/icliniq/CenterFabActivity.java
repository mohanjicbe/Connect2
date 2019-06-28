package com.orane.icliniq;

import android.Manifest;
import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.Labtest.Labtest_tabs_Activity;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Parallex.ParallexMainActivity;
import com.orane.icliniq.databinding.ActivityCenterFabBinding;
import com.orane.icliniq.fragment.ArticlesFragment;
import com.orane.icliniq.fragment.DoctorsFragment;
import com.orane.icliniq.fragment.HomeFragment;
import com.orane.icliniq.fragment.SettingsFragment;
import com.orane.icliniq.network.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CenterFabActivity extends AppCompatActivity {
    private static final String TAG = CenterFabActivity.class.getSimpleName();
    private ActivityCenterFabBinding bind;
    private VpAdapter adapter;
    BottomNavigationViewEx bottomNavigationView;
    ViewPager viewPager;
    FloatingActionButton fab;
    Typeface font_reg, font_opensans_light, font_name_bold_200, fonr_bold;
    // collections
    private List<Fragment> fragments;
    LinearLayout top_layout;
    TextView tv_tooltit;
    ImageView q_logo;
    String str_response;
    boolean doubleBackToExitPressedOnce = false;
    private FirebaseAnalytics mFirebaseAnalytics;

    private YoYo.YoYoString rope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_fab);

/*
        //--------------------------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Ask a Query");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //----------- initialize --------------------------------------
*/

        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //------------ Google firebase Analitics--------------------
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(CenterFabActivity.this);
        Bundle params = new Bundle();
        params.putString("User", Model.id);
        mFirebaseAnalytics.logEvent("Home_Screen", params);
        //------------ Google firebase Analitics--------------------

        if ((Model.device_token) != null && !(Model.device_token).isEmpty() && !(Model.device_token).equals("null") && !(Model.device_token).equals("")) {

            //-------------------------------------------------------------------
            String gcm_url = Model.BASE_URL + "sapp/updateDeviceRegId?reg_id=" + Model.device_token + "&user_id=" + (Model.id) + "&v=" + Model.App_ver_slno + "&token=" + Model.token;
            System.out.println("gcm_url---------" + gcm_url);
            new JSON_GCM_Regid().execute(gcm_url);
            //-------------------------------------------------------------------
        }

        font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        fonr_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);
        font_name_bold_200 = Typeface.createFromAsset(getAssets(), Model.font_name_bold_200);
        //font_opensans_light = Typeface.createFromAsset(getAssets(), Model.font_opensans_light);

        bind = DataBindingUtil.setContentView(this, R.layout.activity_center_fab);

        viewPager = (ViewPager) findViewById(R.id.vp);
        bottomNavigationView = (BottomNavigationViewEx) findViewById(R.id.bottom_navigation);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        rope = YoYo.with(Techniques.Pulse)
                .duration(1200)
                .repeat(YoYo.INFINITE)
                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                .interpolate(new AccelerateDecelerateInterpolator())
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        Toast.makeText(CenterFabActivity.this, "canceled previous animation", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(fab);

        bottomNavigationView.enableItemShiftingMode(false);
        bottomNavigationView.enableShiftingMode(false);
        bottomNavigationView.enableAnimation(false);

        // tv_tooltit.setTypeface(font_reg);

        initData();
        initView();
        initEvent();

    }

    private void initData() {
        fragments = new ArrayList<>(4);

        //------------------------------------------
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        homeFragment.setArguments(bundle);
        //------------------------------------------
        DoctorsFragment backupFragment = new DoctorsFragment();
        bundle = new Bundle();
        bundle.putString("title", "Doctors");
        backupFragment.setArguments(bundle);
        //------------------------------------------
        ArticlesFragment favorFragment = new ArticlesFragment();
        bundle = new Bundle();
        bundle.putString("title", "Articles");
        favorFragment.setArguments(bundle);
        //------------------------------------------
        SettingsFragment visibilityFragment = new SettingsFragment();
        bundle = new Bundle();
        bundle.putString("title", "Settings");
        visibilityFragment.setArguments(bundle);
        //------------------------------------------

        // add to fragments for adapter
        fragments.add(homeFragment);
        fragments.add(backupFragment);
        fragments.add(favorFragment);
        fragments.add(visibilityFragment);
    }

    private void initView() {

        bottomNavigationView.enableItemShiftingMode(false);
        bottomNavigationView.enableShiftingMode(false);
        bottomNavigationView.enableAnimation(false);

        adapter = new VpAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }


    private void initEvent() {

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            private int previousPosition = -1;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int position = 0;
                switch (item.getItemId()) {
                    case R.id.i_music:
                        position = 0;
                        break;
                    case R.id.i_backup:
                        position = 1;
                        break;
                    case R.id.i_favor:
                        position = 2;
                        break;
                    case R.id.i_visibility:
                        position = 3;
                        break;
                    case R.id.i_empty: {
                        return false;
                    }
                }
                if (previousPosition != position) {
                    viewPager.setCurrentItem(position, false);
                    previousPosition = position;
                }

                return true;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position >= 2)// 2 is center
                    position++;// if page is 2, need set bottom item to 3, and the same to 3 -> 4
                bottomNavigationView.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // center item click listener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog = new Dialog(CenterFabActivity.this);

                Window window = dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);

                WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
                layoutParams.x = 0; // right margin
                layoutParams.y = 150; // top margin
                dialog.getWindow().setAttributes(layoutParams);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.diag_cardviewrow);

                LinearLayout labtest_layout = (LinearLayout) dialog.findViewById(R.id.labtest_layout);
                LinearLayout htools_layout = (LinearLayout) dialog.findViewById(R.id.htools_layout);

                LinearLayout layout1 = (LinearLayout) dialog.findViewById(R.id.layout1);
                LinearLayout layout2 = (LinearLayout) dialog.findViewById(R.id.layout2);
                LinearLayout layout3 = (LinearLayout) dialog.findViewById(R.id.layout3);
                TextView tv_chat = (TextView) dialog.findViewById(R.id.tv_chat);
                TextView tv_pvcons = (TextView) dialog.findViewById(R.id.tv_pvcons);
                TextView tv_pvcons2 = (TextView) dialog.findViewById(R.id.tv_pvcons2);
                TextView tv_sub_ask_query = (TextView) dialog.findViewById(R.id.tv_sub_ask_query);


                ((TextView) dialog.findViewById(R.id.tv_chat)).setTypeface(font_name_bold_200);
                //((TextView) dialog.findViewById(R.id.tv_sub_ask_query)).setTypeface(font_opensans_light);
                ((TextView) dialog.findViewById(R.id.tv_pvcons)).setTypeface(font_name_bold_200);
                //((TextView) dialog.findViewById(R.id.tv_pvcons2)).setTypeface(font_opensans_light);


                //---------------- Check Country ------------------------
                System.out.println("Model.browser_country==================== " + Model.browser_country);

                if (!Model.browser_country.equals("IN")) {
                    labtest_layout.setVisibility(View.VISIBLE);

                    tv_pvcons.setText("Book a call");
                    tv_pvcons2.setText("Call doctors on Phone/Video");
                } else {
                    labtest_layout.setVisibility(View.VISIBLE);

                    tv_pvcons.setText("Book a consultation");
                    tv_pvcons2.setText("Consult doctors on Phone/Video");
                }
                //---------------- Check Country ------------------------

                layout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CenterFabActivity.this, AskQuery1.class);
                        startActivity(intent);
                        dialog.cancel();
                    }
                });

                htools_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CenterFabActivity.this, WebViewActivity.class);
                        intent.putExtra("url", Model.BASE_URL + "tool?web_view=true");
                        intent.putExtra("type", "Health Tools");
                        startActivity(intent);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                        //------------ Google firebase Analitics--------------------
                        Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                        Bundle params = new Bundle();
                        params.putString("User", Model.id + Model.first_time);
                        Model.mFirebaseAnalytics.logEvent("Health_Tools", params);
                        //------------ Google firebase Analitics--------------------
                        dialog.cancel();
                    }
                });

                labtest_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(CenterFabActivity.this, Labtest_tabs_Activity.class);
                            startActivity(intent);
                            dialog.cancel();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                layout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(CenterFabActivity.this, Instant_Chat.class);
                            startActivity(intent);
                            dialog.cancel();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                layout3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(CenterFabActivity.this, Consultation_Home.class);
                            startActivity(intent);
                            dialog.cancel();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


                dialog.show();
            }
        });
    }

    private static class VpAdapter extends FragmentPagerAdapter {
        private List<Fragment> data;

        public VpAdapter(FragmentManager fm, List<Fragment> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }
    }

    public void onClick_qa(View v) {

        try {

            switch (v.getId()) {

                case R.id.qa_layout:

                    //----------- get Doc Id ---------------------------------------
                    View parent_fav = (View) v.getParent();
                    TextView tv_url = (TextView) parent_fav.findViewById(R.id.tv_url);
                    String tv_url_val = tv_url.getText().toString();
                    System.out.println("tv_url_val-----------" + tv_url_val);
                    //----------- get Doc Id ---------------------------------------

                    Intent intent = new Intent(getApplicationContext(), QADetailNew.class);
                    intent.putExtra("KEY_ctype", "1");
                    intent.putExtra("KEY_url", tv_url_val);
                    startActivity(intent);

                    break;
            }

            System.out.println("onClick-------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick_offer(View v) {

        try {

            System.out.println("Offer banner click---------------");

            if ((Model.browser_country).equals("IN")) {

                Model.query_launch = "prepack";
                Intent intent = new Intent(CenterFabActivity.this, PrePackActivity.class);
                startActivity(intent);
                FlurryAgent.logEvent("PrePackActivity");

            } else {

                Model.query_launch = "subscription";
                Intent intent = new Intent(CenterFabActivity.this, SubscriptionPackActivity.class);
                startActivity(intent);
                FlurryAgent.logEvent("SubscriptionPackActivity");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick_hotline(View v) {

        try {

/*            Intent intent = new Intent(CenterFabActivity.this, HotlineHome.class);
            startActivity(intent);*/

            Intent intent = new Intent(CenterFabActivity.this, Instant_Chat.class);
            intent.putExtra("doctor_id", "0");
            intent.putExtra("plan_id", "");
            startActivity(intent);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick_docs(View v) {

        try {

            TextView tvid = (TextView) v.findViewById(R.id.tvid);

            String Doc_id = tvid.getText().toString();

            //----------------- Kissmetrics ----------------------------------
            Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext().getApplicationContext());
            Model.kiss.record("android.patient.doctor_select");
            HashMap<String, String> properties = new HashMap<String, String>();
            properties.put("doctor_id:", tvid.getText().toString());
            Model.kiss.set(properties);
            //----------------- Kissmetrics ----------------------------------

            //------------ Google firebase Analitics--------------------
            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            Bundle params = new Bundle();
            params.putString("doctor_id", tvid.getText().toString());
            Model.mFirebaseAnalytics.logEvent("doctor_select", params);
            //------------ Google firebase Analitics--------------------

            if (Doc_id != null && !Doc_id.isEmpty() && !Doc_id.equals("null") && !Doc_id.equals("")) {
                Intent intent = new Intent(CenterFabActivity.this, ParallexMainActivity.class);
                intent.putExtra("tv_doc_id", Doc_id);
                startActivity(intent);
            } else {
                Toast.makeText(CenterFabActivity.this, "Sorry, Something went wrong. GO Back and Try again..!", Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void onClick_book(View v) {

        try {

            View parent = (View) v.getParent();
            View grand_parent = (View) parent.getParent();

            TextView tvid = (TextView) grand_parent.findViewById(R.id.tvid);

            String Doc_id = tvid.getText().toString();

            //----------------- Kissmetrics ----------------------------------
            Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext().getApplicationContext());
            Model.kiss.record("android.patient.doctor_book_select");
            HashMap<String, String> properties = new HashMap<String, String>();
            properties.put("doctor_id:", tvid.getText().toString());
            Model.kiss.set(properties);
            //----------------- Kissmetrics ----------------------------------

            //------------ Google firebase Analitics--------------------
            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            Bundle params = new Bundle();
            params.putString("doctor_id", tvid.getText().toString());
            Model.mFirebaseAnalytics.logEvent("doctor_book_select", params);
            //------------ Google firebase Analitics--------------------


            if (Doc_id != null && !Doc_id.isEmpty() && !Doc_id.equals("null") && !Doc_id.equals("")) {
                Intent intent = new Intent(CenterFabActivity.this, ParallexMainActivity.class);
                intent.putExtra("tv_doc_id", Doc_id);
                startActivity(intent);
            } else {
                Toast.makeText(CenterFabActivity.this, "Sorry, Something went wrong. GO Back and Try again..!", Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Explode makeExplodeTransition() {
        Explode explode = new Explode();
        explode.setDuration(3000);
        explode.setInterpolator(new AnticipateOvershootInterpolator());
        return explode;
    }

    public void onArticlesClick(View v) {

        try {

            switch (v.getId()) {

                case R.id.share_layout:

                    //View parent = (View) v.getParent();
                    TextView tv_share_url = (TextView) v.findViewById(R.id.tv_share_url);
                    TextView tv_docname_share = (TextView) v.findViewById(R.id.tv_docname_share);

                    String article_title_val = tv_share_url.getText().toString();
                    String docname_share_val = tv_docname_share.getText().toString();

                    System.out.println("article_title_val-----------" + article_title_val);
                    System.out.println("docname_share_val-----------" + docname_share_val);

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    //i.putExtra(Intent.EXTRA_SUBJECT, );
                    String sAux = "Read the Articles on icliniq by " + docname_share_val + "\n\n" + article_title_val;
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));

                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class JSON_GCM_Regid extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception--GCM Response Json----" + e.toString());
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            System.out.println("str_response--------------" + str_response);
        }
    }

    private void requestSmsPermission() {
        String permission = Manifest.permission.RECEIVE_SMS;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }
    }


    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }


    public void onDoctorShare(View v) {

        try {

            switch (v.getId()) {

                case R.id.sharedoc_layout:

                    //View parent = (View) v.getParent();
                    TextView tv_doclink = (TextView) v.findViewById(R.id.tv_doclink);
                    TextView tvdocname_new = (TextView) v.findViewById(R.id.tvdocname_new);
                    TextView tv_spec_new = (TextView) v.findViewById(R.id.tv_spec_new);

                    String doclink = tv_doclink.getText().toString();
                    String docname_share_val = tvdocname_new.getText().toString();
                    String spec_new_val = tv_spec_new.getText().toString();

                    System.out.println("doclink-----------" + doclink);
                    System.out.println("docname_share_val-----------" + docname_share_val);
                    System.out.println("spec_new_val-----------" + spec_new_val);

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    //i.putExtra(Intent.EXTRA_SUBJECT, ); "\n\n
                    String sAux = "I found " + docname_share_val + " " + spec_new_val + " on iCliniq. #1 Online consultation app. \n\n View profile here : \n\n " + doclink;
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));

                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
