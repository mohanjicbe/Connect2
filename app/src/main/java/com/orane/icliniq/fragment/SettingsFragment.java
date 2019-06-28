package com.orane.icliniq.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.icliniq.BookingListActivity;
import com.orane.icliniq.CommonActivity;
import com.orane.icliniq.FamilyProfileListActivity;
import com.orane.icliniq.FeedbackActivity;
import com.orane.icliniq.InboxActivity;
import com.orane.icliniq.Invite_doctors;
import com.orane.icliniq.LoginActivity;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.Patient_Profile_Activity;
import com.orane.icliniq.QueryActivity;
import com.orane.icliniq.R;
import com.orane.icliniq.Referal_Activity;
import com.orane.icliniq.Video_WebViewActivity;
import com.orane.icliniq.WalletTransactions;
import com.orane.icliniq.WebViewActivity;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.SetLanguage;
import com.orane.icliniq.network.ShareIntent;
import com.zipow.videobox.MyProfileActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;


public class SettingsFragment extends Fragment {


    LinearLayout wallet_layout, inbox_layout, support_layout, my_family_profile, mywallet_layout, about_app_layout, mybooking_layout, myvideos_layout, terms_layout, profile_layout, policy_layout, reportissue_layout, rate_layout, share_layout, aredoctor_layout, pv_consult_layout, signout_layout;
    Switch switch_notisound, switch_stopnoti;
    RelativeLayout user_layout;
    public String noti_sound_val, stop_noti_val, name_val, email_val;
    TextView tv_pname, tv_emailid;
    String str_response;
    JSONObject logout_jsonobj,logout_json_validate;

    SharedPreferences sharedpreferences;
    public static final String noti_status = "noti_status_key";
    public static final String noti_sound = "noti_sound_key";
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String app_language = "app_language_key";

    Map<String, String> lang_map = new HashMap<String, String>();
    Spinner spinner_lang;

    String lang_name, lang_val;
    View view;
    Typeface font_reg, font_bold;
    LinearLayout suggest_layout,refer_layout;

    public static SettingsFragment newInstance(int pageIndex) {
        SettingsFragment homeFragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putInt("pageIndex", pageIndex);
        homeFragment.setArguments(args);
        return homeFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings, container, false);

        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getActivity());

        suggest_layout = (LinearLayout) view.findViewById(R.id.suggest_layout);
        refer_layout = (LinearLayout) view.findViewById(R.id.refer_layout);
        wallet_layout = (LinearLayout) view.findViewById(R.id.wallet_layout);
        inbox_layout = (LinearLayout) view.findViewById(R.id.inbox_layout);
        user_layout = (RelativeLayout) view.findViewById(R.id.user_layout);
        support_layout = (LinearLayout) view.findViewById(R.id.support_layout);
        my_family_profile = (LinearLayout) view.findViewById(R.id.my_family_profile);
        about_app_layout = (LinearLayout) view.findViewById(R.id.about_app_layout);
        mybooking_layout = (LinearLayout) view.findViewById(R.id.mybooking_layout);
        myvideos_layout = (LinearLayout) view.findViewById(R.id.myvideos_layout);
        signout_layout = (LinearLayout) view.findViewById(R.id.signout_layout);
        mywallet_layout = (LinearLayout) view.findViewById(R.id.mywallet_layout);
        terms_layout = (LinearLayout) view.findViewById(R.id.terms_layout);
        policy_layout = (LinearLayout) view.findViewById(R.id.policy_layout);
        reportissue_layout = (LinearLayout) view.findViewById(R.id.reportissue_layout);
        rate_layout = (LinearLayout) view.findViewById(R.id.rate_layout);
        share_layout = (LinearLayout) view.findViewById(R.id.share_layout);
        aredoctor_layout = (LinearLayout) view.findViewById(R.id.aredoctor_layout);
        pv_consult_layout = (LinearLayout) view.findViewById(R.id.pv_consult_layout);
        profile_layout = (LinearLayout) view.findViewById(R.id.profile_layout);
        switch_notisound = (Switch) view.findViewById(R.id.switch_notisound);
        switch_stopnoti = (Switch) view.findViewById(R.id.switch_stopnoti);
        spinner_lang = (Spinner) view.findViewById(R.id.spinner_lang);
        tv_pname = (TextView) view.findViewById(R.id.tv_pname);
        tv_emailid = (TextView) view.findViewById(R.id.tv_emailid);

        font_reg = Typeface.createFromAsset(getActivity().getAssets(), Model.font_name);
        font_bold = Typeface.createFromAsset(getActivity().getAssets(), Model.font_name_bold);

        ((TextView) view.findViewById(R.id.tv_share_tit)).setTypeface(font_bold);
        ((TextView) view.findViewById(R.id.tv_rate_app)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_share_friends)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_sugg_doc)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_app_sett)).setTypeface(font_bold);
        ((TextView) view.findViewById(R.id.tv_noti_sound)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_noti_stat)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_about)).setTypeface(font_bold);
        ((TextView) view.findViewById(R.id.tv_terms)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_privatepolicy)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_reportissue)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_rudoc)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_signout)).setTypeface(font_bold);
        ((TextView) view.findViewById(R.id.tv_mywallet)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_profile)).setTypeface(font_reg);


        //================ Initialize ======================---------------
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        stop_noti_val = sharedpreferences.getString(noti_status, "off");
        noti_sound_val = sharedpreferences.getString(noti_sound, "off");
        lang_val = sharedpreferences.getString(app_language, "en");


        if (stop_noti_val.equals("on")) switch_stopnoti.setChecked(true);
        else switch_stopnoti.setChecked(false);

        if (noti_sound_val.equals("on")) switch_notisound.setChecked(true);
        else switch_notisound.setChecked(false);
        //================ Initialize ======================---------------

        try {
            //-----------------------------------------------------
            String url = Model.BASE_URL + "sapp/patientProfile?user_id=" + Model.id;
            System.out.println("url-------------" + url);
            new JSON_get_Patient_Details().execute(url);
            //----------------------------------------
        } catch (Exception e) {
            e.printStackTrace();
        }

        suggest_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(i);

            }
        });

        inbox_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), InboxActivity.class);
                startActivity(i);
            }
        });

        wallet_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), WalletTransactions.class);
                startActivity(i);
            }
        });

        my_family_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), FamilyProfileListActivity.class);
                startActivity(i);
            }
        });

        myvideos_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Video_WebViewActivity.class);
                i.putExtra("url", Model.BASE_URL + "videos/myfavorite?t=mob&layout=empty&user_id=" + Model.id);
                i.putExtra("type", "My Videos");
                startActivity(i);
            }
        });

        mybooking_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), BookingListActivity.class);
                startActivity(i);
            }
        });

        about_app_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CommonActivity.class);
                intent.putExtra("type", "aboutus");
                startActivity(intent);
            }
        });

        support_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CommonActivity.class);
                intent.putExtra("type", "support");
                startActivity(intent);
            }
        });

        user_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("User Account Details.............");
                Intent intent = new Intent(getActivity(), Patient_Profile_Activity.class);
                startActivity(intent);

            }
        });

        refer_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("User Account Details.............");

                Intent intent = new Intent(getActivity(), Referal_Activity.class);
                startActivity(intent);

            }
        });


        tv_pname.setText(Model.name);
        tv_emailid.setText(Model.email);

        System.out.println("Country-------------" + Model.browser_country);
        System.out.println("Model.name-------------" + Model.name);
        System.out.println("Model.email-------------" + Model.email);



      /*  //------- Setting Language ----------------------
        final List<String> lang_categories = new ArrayList<String>();

        lang_categories.add("Choose Language");
        lang_map.put("Choose Language", "en");

        if ((Model.browser_country).equals("IN")) {
            lang_categories.add("English");
            lang_map.put("English", "en");
            lang_categories.add("Hindi");
            lang_map.put("Hindi", "hi");
            lang_categories.add("Telugu");
            lang_map.put("Telugu", "te");
            lang_categories.add("Tamil");
            lang_map.put("Tamil", "ta");
            lang_categories.add("Kannada");
            lang_map.put("Kannada", "ka");
        }

        ArrayAdapter<String> lang_dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, lang_categories);
        lang_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_lang.setAdapter(lang_dataAdapter);
        //---------------------------------------------

        spinner_lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                lang_name = spinner_lang.getSelectedItem().toString();
                lang_val = lang_map.get(lang_name);
                Model.app_lang = lang_val;

                *//*Model.cons_lang = lang_name;
                Model.cons_lang_code = lang_val;*//*

                //----------------- Kissmetrics ----------------------------------
                Model.kiss.record("android.patient.Sett_Language");
                HashMap<String, String> properties = new HashMap<String, String>();
                properties.put("lang_val", lang_val);
                properties.put("lang_name", lang_name);
                Model.kiss.set(properties);
                //----------------- Kissmetrics ----------------------------------

                //----------- Flurry -------------------------------------------------
                HashMap<String, String> properties2 = new HashMap<String, String>();
                properties2.put("lang_val", lang_val);
                properties2.put("lang_name", lang_name);
                FlurryAgent.logEvent("android.patient.Sett_Language", properties2);
                //----------- Flurry -------------------------------------------------

                System.out.println("lang_name----------" + lang_name);
                System.out.println("lang_val----------" + lang_val);

                set_lang();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/

        switch_notisound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) noti_sound_val = "on";
                else noti_sound_val = "off";

                //===============Apply Noti Settings Values=============================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(noti_sound, noti_sound_val);
                editor.apply();
                //===============Apply Noti Settings Values=============================================

                System.out.println("noti_sound_val-------" + noti_sound_val);

                try {
                    Model.kiss.record("android.Patient.Switch_Notify_Sound");
                    HashMap<String, String> properties = new HashMap<String, String>();
                    properties.put("android.Patient.Sound_value", noti_sound_val);
                    Model.kiss.set(properties);
                } catch (Exception ee) {
                    System.out.println("Exception-----------" + ee.toString());
                    ee.printStackTrace();
                }

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("switch_notisound", noti_sound_val);
                Model.mFirebaseAnalytics.logEvent("Settings", params);
                //------------ Google firebase Analitics--------------------

            }
        });

        switch_stopnoti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) stop_noti_val = "on";
                else stop_noti_val = "off";

                //===============Apply Noti Settings Values=============================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(noti_status, stop_noti_val);
                editor.apply();
                //===============Apply Noti Settings Values=============================================

                System.out.println("stop_noti_val-------" + stop_noti_val);

                try {
                    Model.kiss.record("android.Patient.Switch_Notify_Status");
                    HashMap<String, String> properties = new HashMap<String, String>();
                    properties.put("android.Patient.Notify_value", stop_noti_val);
                    Model.kiss.set(properties);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        });


        terms_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Model.kiss.record("android.Patient.Terms");
                    HashMap<String, String> properties = new HashMap<String, String>();
                    Model.kiss.set(properties);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(getActivity(), WebViewActivity.class);
                i.putExtra("url", "https://www.icliniq.com/p/terms");
                i.putExtra("type", "Terms");
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);


/*                //------------ Tracker ------------------------
                MyApp.tracker().send(new HitBuilders.EventBuilder()
                        .setCategory("Settings")
                        .setAction("terms_layout")
                        .build());
                //------------ Tracker ------------------------*/

                //----------------- Kissmetrics ----------------------------------
                Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getActivity());
                Model.kiss.record("android.patient.Sett_Terms");
                //----------------- Kissmetrics ----------------------------------


            }
        });

        policy_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Model.kiss.record("android.Patient.PrivatePolicy");
                    HashMap<String, String> properties = new HashMap<String, String>();
                    Model.kiss.set(properties);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(getActivity(), WebViewActivity.class);
                i.putExtra("url", "https://www.icliniq.com/p/privacy");
                i.putExtra("type", "Privacy Policy");
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                try {
                    Model.kiss.record("android.Patient.Sett_PrivatePolicy");
                    HashMap<String, String> properties = new HashMap<String, String>();
                    properties.put("android.Patient.Sound_value", noti_sound_val);
                    Model.kiss.set(properties);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        });

        reportissue_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), CommonActivity.class);
                intent.putExtra("type", "feedback");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

            }
        });

        rate_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "http://po.st/icliniqapp";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

                try {
                    Model.kiss.record("android.Patient.RateApp_Clicked");
                    HashMap<String, String> properties = new HashMap<String, String>();
                    properties.put("android.Patient.url_link", url);
                    Model.kiss.set(properties);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }

        });

        share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    ShareIntent sintent = new ShareIntent();
                    sintent.ShareApp(getActivity(), "MainActivity");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    Model.kiss.record("android.Patient.Sett_ShareApp");
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        });

        aredoctor_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //----------------- Kissmetrics ----------------------------------
                Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getActivity());
                Model.kiss.record("android.patient.AreYouDoctor");
                //----------------- Kissmetrics ----------------------------------

                String url = "https://play.google.com/store/apps/details?id=com.orane.docassist";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
/*
                //------------ Tracker ------------------------
                MyApp.tracker().send(new HitBuilders.EventBuilder()
                        .setCategory("Settings")
                        .setAction("AreYouDoctor")
                        .build());
                //------------ Tracker ------------------------*/

            }
        });

        profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //----------------- Kissmetrics ----------------------------------
                Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getActivity());
                Model.kiss.record("android.patient.View_Profile");
                //----------------- Kissmetrics ----------------------------------
                Intent i = new Intent(getActivity(), MyProfileActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

/*
                //------------ Tracker ------------------------
                MyApp.tracker().send(new HitBuilders.EventBuilder()
                        .setCategory("Settings")
                        .setAction("View_Profile")
                        .build());
                //------------ Tracker ------------------------
*/
            }
        });


        pv_consult_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), Invite_doctors.class);
                startActivity(intent);

/*                //------------ Tracker ------------------------
                MyApp.tracker().send(new HitBuilders.EventBuilder()
                        .setCategory("Settings")
                        .setAction("Invite_doctors")
                        .build());
                //------------ Tracker ------------------------*/
            }
        });

        signout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Model.kiss.record("android.Patient.Sett_Signout");
                    HashMap<String, String> properties = new HashMap<String, String>();
                    properties.put("android.Patient.Sound_value", noti_sound_val);
                    Model.kiss.set(properties);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }

/*                //------------ Tracker ------------------------
                MyApp.tracker().send(new HitBuilders.EventBuilder()
                        .setCategory("Settings")
                        .setAction("Logout")
                        .build());
                //------------ Tracker ------------------------*/

                ask_logout();
            }
        });


        return view;
    }


    public void ask_logout() {

        try {
            Model.kiss.record("android.Patient.Signout");
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        final MaterialDialog alert = new MaterialDialog(getActivity());
        alert.setTitle("Logout.!");
        alert.setMessage("Are you sure you want to logout?");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //============================================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Login_Status, "0");
                editor.apply();
                //============================================================


                //-------------- Logout-------------------------------------------------
                try {

                    logout_json_validate = new JSONObject();
                    logout_json_validate.put("user_id", Model.id);
                    logout_json_validate.put("reg_id", Model.device_token);
                    logout_json_validate.put("os_type", "1");
                    System.out.println("logout_json_validate----" + logout_json_validate.toString());
                    new JSON_logout().execute(logout_json_validate);

                    //--------------------------------------------------
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //--------------- Logout------------------------------------------------



            }
        });


        alert.setNegativeButton("No", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        alert.show();
    }


    public void set_lang() {
        //================ Set Language ======================---------------
        SetLanguage sl = new SetLanguage();
        sl.set_lang((TextView) view.findViewById(R.id.tv_pref_tit), lang_val, "tv_pref_tit");
        sl.set_lang((TextView) view.findViewById(R.id.tv_share_tit), lang_val, "tv_share_tit");
        sl.set_lang((TextView) view.findViewById(R.id.tv_rate_app), lang_val, "tv_rate_app");
        sl.set_lang((TextView) view.findViewById(R.id.tv_share_friends), lang_val, "tv_share_friends");
        sl.set_lang((TextView) view.findViewById(R.id.tv_sugg_doc), lang_val, "tv_sugg_doc");
        sl.set_lang((TextView) view.findViewById(R.id.tv_noti_sound), lang_val, "tv_noti_sound");
        sl.set_lang((TextView) view.findViewById(R.id.tv_noti_stat), lang_val, "tv_noti_stat");
        sl.set_lang((TextView) view.findViewById(R.id.tv_terms), lang_val, "tv_terms");
        sl.set_lang((TextView) view.findViewById(R.id.tv_privatepolicy), lang_val, "tv_privatepolicy");
        sl.set_lang((TextView) view.findViewById(R.id.tv_reportissue), lang_val, "tv_reportissue");
        sl.set_lang((TextView) view.findViewById(R.id.tv_rudoc), lang_val, "tv_rudoc");
        sl.set_lang((TextView) view.findViewById(R.id.tv_signout), lang_val, "tv_signout");
        //================ Set Language ======================---------------
    }

    @Override
    public void onResume() {
        super.onResume();

        if ((Model.query_launch).equals("profile_update")) {

            try {
                //-----------------------------------------------------
                String url = Model.BASE_URL + "sapp/patientProfile?user_id=" + Model.id;
                System.out.println("url-------------" + url);
                new JSON_get_Patient_Details().execute(url);
                //----------------------------------------
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private class JSON_get_Patient_Details extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);
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

                JSONObject jsonobj = new JSONObject(str_response);

                if (jsonobj.has("token_status")) {
                    String token_status = jsonobj.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        getActivity().finishAffinity();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }

                } else {

                    System.out.println("jsonobj-----------" + jsonobj.toString());

                    name_val = jsonobj.getString("name");
                    email_val = jsonobj.getString("email");

                    tv_pname.setText(name_val);
                    tv_emailid.setText(email_val);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();

        }
    }


    class JSON_logout extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Validating. Please Wait...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {

                System.out.println("Parameters---------------" + urls[0]);

                JSONParser jParser = new JSONParser();
                logout_jsonobj = jParser.JSON_POST(urls[0], "logout");


                System.out.println("logout_jsonobj---------------" + logout_jsonobj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                System.out.println("logout_jsonobj---------------" + logout_jsonobj.toString());

                dialog.cancel();

                getActivity().finishAffinity();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}