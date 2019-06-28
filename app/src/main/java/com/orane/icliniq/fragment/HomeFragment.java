package com.orane.icliniq.fragment;

import android.animation.ValueAnimator;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.nineoldandroids.view.ViewHelper;
import com.orane.icliniq.Answeres_Activity;
import com.orane.icliniq.AskQuery1;
import com.orane.icliniq.ConsultationListActivity;
import com.orane.icliniq.DoctorsListActivity;
import com.orane.icliniq.HotlineHome;
import com.orane.icliniq.Instant_Chat;
import com.orane.icliniq.LoginActivity;
import com.orane.icliniq.Model.Model;
import com.orane.icliniq.MyDoctorsActivity;
import com.orane.icliniq.Parallex.slidingTab.SlidingTabLayout;
import com.orane.icliniq.PrePackActivity;
import com.orane.icliniq.QADetailNew;
import com.orane.icliniq.QueryActivity;
import com.orane.icliniq.R;
import com.orane.icliniq.Search_Screen;
import com.orane.icliniq.SignupActivity;
import com.orane.icliniq.SubscriptionPackActivity;
import com.orane.icliniq.network.JSONParser;
import com.orane.icliniq.network.NetCheck;
import com.orane.icliniq.network.ShareIntent;
import com.orane.icliniq.zoom.Video_Calling_Activity;
import com.skyfishjy.library.RippleBackground;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.materialdialog.MaterialDialog;


public class HomeFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, ObservableScrollViewCallbacks {

    Intent intent;
    private SliderLayout mDemoSlider;
    ImageView img_share_icon, offer_image2, img_search_logo;
    public File imageFile;
    TextView tv_offer_text, btn_free, offer_desc2, offer_title2, textview_title, textview_short, textview_docname, textview_ctype, offer_title, hotlinechat2, tv_pvcons2, offer_desc;
    LinearLayout innerLay, layout_offer1, layout_offer2;
    LinearLayout layout1, logo_layout, layout3, layout2, home_button, myhealth_layout, hotline_layout, phone_consult_layout, consult_layout, doc_layout, myquery_layout;
    TextView tv_chat_sub_text, tv_sub_chat, tv_chat, butt_text, tv_viewall, tv_url, how_title, how_desc, hotlinechat, tvdesc, tv_pvcons, tv_askquery;
    Bitmap bitmap_image;
    JSONObject logout_jsonobj;
    ObservableScrollView scrollview;
    SlidingTabLayout slide1;
    JSONObject logout_json_validate;
    JSONArray jsonarray, jsonarr, jsonarr_banner;
    PendingIntent pIntent;
    ImageView offer_image1;
    Bitmap bitmap_images;
    CircleImageView imageview_poster;
    public String logout_text,chat_tip_val, str_response_banner, qa_photo_url, qa_abstract, qa_title, qa_doctor_name, qa_url, str_response, ticker_text, ContentTitle, ContentText, SummaryText, photo_url, speciality, title_hash, title;
    Typeface font_reg, fonr_bold;
    Intent i;
    View vi;
    public HashMap<String, String> url_maps;
    public HashMap<String, String> url_maps_id;
    ImageView img_offer_banner;
    JSONObject qa_jsonobject, jsonobj1;
    private ImageView foundDevice;

    //SlidingTabLayout sliding_tabs;
    //public LinearLayout bottom_bar;
    Toolbar toolbar;

    private static final String ARG_TEXT = "arg_text";
    private static final String ARG_COLOR = "arg_color";

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String noti_status = "noti_status_key";
    public static final String noti_sound = "noti_sound_key";
    public static final String id = "id";
    public static final String chat_tip = "chat_tip_key";
    public String uname, docname, pass, stop_noti_val, noti_sound_val;
    ImageView q_logo;
    LinearLayout layout_AskQuery;

    TextView tv_title, tv_sub_title;
    Button btn_getitnow, btn_buynow;

    public static HomeFragment newInstance(int pageIndex) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt("pageIndex", pageIndex);
        homeFragment.setArguments(args);
        return homeFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        Model.have_free_credit = "";






/*        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), notification);
        ringtone.play();*/

     /*   //--------------- Alert Service -------------------------------------------------------
        if (checkRingerIsOn(getActivity())) {
            MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.phone_loud1);
            mediaPlayer.start();
        } else {
            Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(500);
        }
        //--------------- Alert Service -------------------------------------------------------

*/

        font_reg = Typeface.createFromAsset(getActivity().getAssets(), Model.font_name);
        fonr_bold = Typeface.createFromAsset(getActivity().getAssets(), Model.font_name_bold);

        ((TextView) view.findViewById(R.id.tv_mysection)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_recent)).setTypeface(font_reg);


        scrollview = (ObservableScrollView) view.findViewById(R.id.scrollview);
        tv_pvcons2 = (TextView) view.findViewById(R.id.tv_pvcons2);
        hotlinechat2 = (TextView) view.findViewById(R.id.hotlinechat2);
        //tvdesc = (TextView) view.findViewById(R.id.tvdesc);
        //tv_sub_ask_query = (TextView) view.findViewById(R.id.tv_sub_ask_query);
        tv_chat = (TextView) view.findViewById(R.id.tv_chat);
        tv_sub_chat = (TextView) view.findViewById(R.id.tv_sub_chat);
        tv_chat_sub_text = (TextView) view.findViewById(R.id.tv_chat_sub_text);
        tv_offer_text = (TextView) view.findViewById(R.id.tv_offer_text);
        tv_askquery = (TextView) view.findViewById(R.id.tv_askquery);
        hotlinechat = (TextView) view.findViewById(R.id.hotlinechat);
        tv_pvcons = (TextView) view.findViewById(R.id.tv_pvcons);
        offer_title = (TextView) view.findViewById(R.id.offer_title);
        offer_desc = (TextView) view.findViewById(R.id.offer_desc);
        myhealth_layout = (LinearLayout) view.findViewById(R.id.myhealth_layout);
        // top_search_layout = (LinearLayout) view.findViewById(R.id.top_search_layout);
        hotline_layout = (LinearLayout) view.findViewById(R.id.hotline_layout);
        layout2 = (LinearLayout) view.findViewById(R.id.layout2);
        layout1 = (LinearLayout) view.findViewById(R.id.layout1);
        layout3 = (LinearLayout) view.findViewById(R.id.layout3);
        doc_layout = (LinearLayout) view.findViewById(R.id.doc_layout);
        myquery_layout = (LinearLayout) view.findViewById(R.id.myquery_layout);
        // seenas_layout = (LinearLayout) view.findViewById(R.id.seenas_layout);
        home_button = (LinearLayout) view.findViewById(R.id.home_button);
        innerLay = (LinearLayout) view.findViewById(R.id.innerLay);
        how_desc = (TextView) view.findViewById(R.id.how_desc);
        how_title = (TextView) view.findViewById(R.id.how_title);
        butt_text = (TextView) view.findViewById(R.id.butt_text);
        img_offer_banner = (ImageView) view.findViewById(R.id.img_offer_banner);
        tv_viewall = (TextView) view.findViewById(R.id.tv_viewall);

        btn_free = (TextView) view.findViewById(R.id.btn_free);
        offer_desc2 = (TextView) view.findViewById(R.id.offer_desc2);
        offer_title2 = (TextView) view.findViewById(R.id.offer_title2);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_sub_title = (TextView) view.findViewById(R.id.tv_sub_title);
        img_search_logo = (ImageView) view.findViewById(R.id.img_search_logo);
        img_share_icon = (ImageView) view.findViewById(R.id.img_share_icon);
        layout3 = (LinearLayout) view.findViewById(R.id.layout3);
        offer_image1 = (ImageView) view.findViewById(R.id.offer_image1);
        logo_layout = (LinearLayout) view.findViewById(R.id.logo_layout);
        layout_offer1 = (LinearLayout) view.findViewById(R.id.layout_offer1);
        layout_offer2 = (LinearLayout) view.findViewById(R.id.layout_offer2);
        layout_AskQuery = (LinearLayout) view.findViewById(R.id.layout_AskQuery);
        offer_image2 = (ImageView) view.findViewById(R.id.offer_image2);

        btn_getitnow = (Button) view.findViewById(R.id.btn_getitnow);
        btn_buynow = (Button) view.findViewById(R.id.btn_buynow);


        btn_getitnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                Model.query_launch = "MainActivity";
                Intent intent = new Intent(getActivity(), HotlineHome.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
*/

                Intent intent = new Intent(getActivity(), Instant_Chat.class);
                intent.putExtra("doctor_id", "0");
                intent.putExtra("plan_id", "");
                startActivity(intent);
            }
        });


        layout_AskQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.query_launch = "MainActivity";
                Intent intent = new Intent(getActivity(), AskQuery1.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        btn_buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    System.out.println("Offer banner click---------------");

                    if ((Model.browser_country).equals("IN")) {

                        Model.query_launch = "prepack";
                        Intent intent = new Intent(getActivity(), PrePackActivity.class);
                        startActivity(intent);
                        FlurryAgent.logEvent("PrePackActivity");

                    } else {

                        Model.query_launch = "subscription";
                        Intent intent = new Intent(getActivity(), SubscriptionPackActivity.class);
                        startActivity(intent);
                        FlurryAgent.logEvent("SubscriptionPackActivity");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        q_logo = (ImageView) view.findViewById(R.id.q_logo);
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shakeanim);
        q_logo.startAnimation(shake);

        final RippleBackground rippleBackground = (RippleBackground) view.findViewById(R.id.content);
        final Handler handler = new Handler();

        tv_title.setTypeface(fonr_bold);
        tv_sub_title.setTypeface(font_reg);

        //bottom_bar = (LinearLayout) getActivity().findViewById(R.id.bottom_bar);
        //sliding_tabs = (SlidingTabLayout) getActivity().findViewById(R.id.sliding_tabs);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);


        tv_askquery.setTypeface(fonr_bold);
        tv_chat.setTypeface(fonr_bold);
        tv_sub_chat.setTypeface(font_reg);
        tv_chat_sub_text.setTypeface(font_reg);
        //tv_sub_ask_query.setTypeface(font_reg);
        tv_pvcons.setTypeface(fonr_bold);
        tv_pvcons2.setTypeface(font_reg);
        hotlinechat.setTypeface(fonr_bold);
        hotlinechat2.setTypeface(font_reg);

        offer_title.setTypeface(fonr_bold);
        offer_desc.setTypeface(font_reg);
        how_title.setTypeface(fonr_bold);
        how_desc.setTypeface(font_reg);
        butt_text.setTypeface(fonr_bold);

        Animation animSlideDown = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
        offer_image1.startAnimation(animSlideDown);

        img_search_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Search_Screen.class);
                intent.putExtra("search_type", "all");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        img_share_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ShareIntent sintent = new ShareIntent();
                    sintent.ShareApp(getActivity(), "MainActivity");
                } catch (Exception e) {
                    //System.out.println("Exception-- Share_Intent---" + e.toString());
                    e.printStackTrace();
                }
            }
        });

        logo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        q_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

/*
        Animation animSlideDown1 = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
        animSlideDown1.setStartOffset(100);
        layout1.startAnimation(animSlideDown1);

        Animation animSlideDown2 = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce2);
        animSlideDown2.setStartOffset(300);
        layout2.startAnimation(animSlideDown2);

        Animation animSlideDown3 = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce3);
        animSlideDown3.setStartOffset(500);
        layout3.startAnimation(animSlideDown3);

        Animation animSlideDown4 = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce4);
        animSlideDown4.setStartOffset(700);
        layout_offer1.startAnimation(animSlideDown4);
*/

      /*  //---------- Sneaker Button ----------------------------
        mainAction = new LGSnackbarAction("Action", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Action fired", Toast.LENGTH_LONG).show();
            }
        });
        ButterKnife.bind(getActivity());
        LGSnackbarManager.prepare(getActivity(), LGSnackBarThemeManager.LGSnackbarThemeName.SHINE);
        //---------- Sneaker Button ----------------------------
*/

        if ((Model.browser_country) != null && !(Model.browser_country).isEmpty() && !(Model.browser_country).equals("null") && !(Model.browser_country).equals("")) {
            if (!(Model.browser_country).equals("IN")) {

                System.out.println("==================== Subscription Packages");

                offer_image2.setImageResource(R.mipmap.nonindia_subs);

                offer_title2.setText("Subscription Packages");
                offer_desc2.setText("Get UNLIMITED chat with doctors by monthly SUBSCRIPTION packages");
                //how_desc2.setText("iCliniq is #1 Online Second Opinion Platform");

                tv_pvcons.setText("Book a Phone/Video Chat");
                tv_pvcons2.setText("Chat with doctors on Phone/Video");
                tv_offer_text.setText("Subscription Packages");
            } else {

                System.out.println("==================== PrePaid Packages");

                offer_title2.setText("Great offers on prepaid packages");
                offer_image2.setImageResource(R.mipmap.prep);
            }

        } else {
            Model.browser_country = "IN";
        }


        try {
            System.out.println("Model.have_free_credit------------------------" + Model.have_free_credit);

            if ((Model.have_free_credit).equals("1")) {
                //btn_free.setText("(You have 1 FREE query credit)");
                btn_free.setVisibility(View.VISIBLE);
            } else {
                //btn_free.setText("Get medical advice from doctors");
                btn_free.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



/*        img_offer_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("layout_offer1---------" + layout_offer1);
            }
        });*/

        layout_offer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


     /*   layout_offer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Model.query_launch = "Main";

                Intent intent = new Intent(getActivity(), IntroScreenActivity.class);
                intent.putExtra("screen_launch", "home");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);

                FlurryAgent.logEvent("HowitWorks");

                //noti();
            }
        });
*/

        hotline_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Instant_Chat.class);
                intent.putExtra("doctor_id", "0");
                intent.putExtra("plan_id", "");
                startActivity(intent);

                Model.query_launch = "MainActivity";
/*                Intent intent = new Intent(getActivity(), HotlineHome.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);*/

/*                Model.query_launch = "MainActivity";
                Intent intent = new Intent(getActivity(), PhoneAuthActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);*/

            }
        });


        tv_viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Model.query_launch = "MainActivity";
/*                Intent intent = new Intent(getActivity(), HotlineHome.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);*/

                Intent intent = new Intent(getActivity(), Answeres_Activity.class);
                startActivity(intent);
            }
        });

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getActivity(), QueryActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                    //------------ Google firebase Analitics--------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                    Bundle params = new Bundle();
                    params.putString("User", Model.id);
                    Model.mFirebaseAnalytics.logEvent("Home_MyQueries", params);
                    //------------ Google firebase Analitics--------------------

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getActivity(), ConsultationListActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                    //------------ Google firebase Analitics--------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                    Bundle params = new Bundle();
                    params.putString("User", Model.id);
                    Model.mFirebaseAnalytics.logEvent("Home_consultations", params);
                    //------------ Google firebase Analitics--------------------

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (new NetCheck().netcheck(getActivity())) {


                    if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {

                        Intent intent = new Intent(getActivity(), MyDoctorsActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                        //------------ Google firebase Analitics--------------------
                        Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                        Bundle params = new Bundle();
                        params.putString("User", Model.id);
                        Model.mFirebaseAnalytics.logEvent("Home_MyDoctors", params);
                        //------------ Google firebase Analitics--------------------

                    } else {
                        force_logout();
                    }

                } else {

                    Toast.makeText(getActivity(), "No Internet connection, please try again.", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getActivity(), AskQuery1.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                }
            }
        });


        doc_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getActivity(), DoctorsListActivity.class);
                    //Intent intent = new Intent(getActivity(), Home_CenterFabActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        mDemoSlider = (SliderLayout) view.findViewById(R.id.slider);

        //-------- Getting Banners --------------------------------
        String banner_url = Model.BASE_URL + "app/slides";
        System.out.println("banner_url-------" + banner_url);
        new JSON_bannerimages().execute(banner_url);
        //-------- Getting Banners --------------------------------

        //-------- Getting QA --------------------------------
        String qa_url = Model.BASE_URL + "app/qa?page=1";
        System.out.println("qa_url-------------" + qa_url);
        new JSON_qa_server().execute(qa_url);
        //-------- Getting QA --------------------------------

        //==================== Tool Tip =======================================================================
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        chat_tip_val = sharedpreferences.getString(chat_tip, "off");
        System.out.println("Get chat_tip_val------" + chat_tip_val);
        //-----------------------------------------------------

        //chat_tip_val="on";

        //============================================================
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(chat_tip, "off");
        editor.apply();
        System.out.println("Gone for Offline ------");
        //============================================================

        //==================== Tool Tip =======================================================================
/*
        //-------------------------Image Slider-----------------------------------------------------------
        mDemoSlider = (SliderLayout) view.findViewById(R.id.slider);

      */
/*      HashMap<String, String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");*//*


        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal", R.mipmap.slide001);
        file_maps.put("Big Bang Theory", R.mipmap.slide002);
        file_maps.put("House of Cards", R.mipmap.slide003);
        file_maps.put("Game of Thrones", R.mipmap.slide004);


        try {
            for (String name : file_maps.keySet()) {
                TextSliderView textSliderView = new TextSliderView(getActivity());
                textSliderView
                        .description(name)
                        .image(file_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);

                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putString("extra", name);

                mDemoSlider.addSlider(textSliderView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("End-----");

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.setPresetTransformer("Accordion");
        //-------------------------Image Slider-----------------------------------------------------------
*/


        scrollview.setOnTouchListener(new View.OnTouchListener() {
            float initialY, finalY;
            boolean isScrollingUp;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = MotionEventCompat.getActionMasked(event);

                switch (action) {
                    case (MotionEvent.ACTION_DOWN):
                        initialY = event.getY();
                    case (MotionEvent.ACTION_UP):

                        finalY = event.getY();

                       /* if (initialY < finalY) {
                            System.out.println("Scrolling up---Scrolling View--");
                            isScrollingUp = true;

                            if (bottom_bar.isShown()) {
                                //hideToolbar();
                                hideBottomBar();
                            }

                        } else if (initialY > finalY) {
                            System.out.println("Scrolling Down-----");
                            isScrollingUp = false;

                            if (bottom_bar.isShown()) {
                                //showToolbar();
                                showBottomBar();
                            }
                        }*/
                    default:
                }
                return false;
            }
        });

        scrollview.setScrollViewCallbacks(this);


        return view;
    }


    public void force_logout() {

        try {
            //----------------- Kissmetrics ----------------------------------
            Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getActivity());
            Model.kiss.identify(Model.kmid);
            Model.kiss.record("android.patient.Force_Logout");
            HashMap<String, String> properties = new HashMap<String, String>();
            properties.put("android.patient.Country:", (Model.browser_country));
            Model.kiss.set(properties);
            //----------------- Kissmetrics ----------------------------------
            //------------ Google firebase Analitics--------------------
            Bundle params = new Bundle();
            params.putString("Country", (Model.browser_country));
            Model.mFirebaseAnalytics.logEvent("Force_Logout", params);
            //------------ Google firebase Analitics--------------------

            final MaterialDialog alert = new MaterialDialog(getActivity());
            alert.setTitle("Please re-login the app..!");
            alert.setMessage("Something went wrong. Please Logout and Login again to continue");
            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //============================================================
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Login_Status, "0");
                    editor.apply();
                    //============================================================

                    getActivity().finishAffinity();
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                    alert.dismiss();
                    getActivity().finish();


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
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            System.out.println("Model.have_free_credit------------------------" + Model.have_free_credit);


           /* if ((Model.have_free_credit) != null && !(Model.have_free_credit).isEmpty() && !(Model.have_free_credit).equals("null") && !(Model.have_free_credit).equals("")) {

                if ((Model.have_free_credit).equals("1")) {
                    tv_sub_ask_query.setText("(You have 1 FREE query credit)");
                } else {
                    tv_sub_ask_query.setText("Get medical advice from doctors");
                }

            } else {
                tv_sub_ask_query.setText("Get medical advice from doctors");
            }
*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //Toast.makeText(getActivity(), slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();

        String hash_val = slider.getBundle().get("extra") + "";

        Intent intent = new Intent(getActivity(), QADetailNew.class);
        intent.putExtra("KEY_ctype", hash_val);
        intent.putExtra("KEY_url", hash_val);
        startActivity(intent);

        //------------ Google firebase Analitics--------------------
        Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        Bundle params = new Bundle();
        params.putString("user_id", (Model.id));
        params.putString("KEY_ctype", hash_val);
        params.putString("KEY_url", hash_val);
        Model.mFirebaseAnalytics.logEvent("Force_Logout", params);
        //------------ Google firebase Analitics--------------------
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(slide1) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(slide1) == -slide1.getHeight();
    }

    private void showToolbar() {
        moveToolbar(0);
    }

    private void hideToolbar() {
        moveToolbar(-slide1.getHeight());
    }

    private void moveToolbar(float toTranslationY) {
        if (ViewHelper.getTranslationY(slide1) == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(slide1), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(slide1, translationY);
                //ViewHelper.setTranslationY((View) scrollview, translationY);

/*                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ((View) vpPager).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;*/
                //((View) vpPager).requestLayout();

               /* LinearLayout scrolls = (LinearLayout) ((AppCompatActivity) getActivity()).findViewById(R.id.scrollview);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ((View) scrolls).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;
                scrolls.requestLayout();*/


            }
        });
        animator.start();
    }


    private class JSON_qa_server extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                System.out.println("urls[]--------------" + urls[0]);
                str_response = new JSONParser().getJSONString(urls[0]);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                System.out.println("Server response--------------" + str_response);

                apply_offers(str_response);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class JSON_bannerimages extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                str_response_banner = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response_banner--------------" + str_response_banner);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                //-------Short-films, vada poche, Health, channels, ---------------------------------------------------
                jsonarr_banner = new JSONArray(str_response_banner);

                if (str_response_banner.length() > 2) {

                    url_maps = new HashMap<String, String>();
                    url_maps_id = new HashMap<String, String>();

                    for (int i = 0; i < jsonarr_banner.length(); i++) {
                        JSONObject jsonobj1_ = jsonarr_banner.getJSONObject(i);

                        System.out.println("jsonobj1_------" + jsonobj1_.toString());

                        title = jsonobj1_.getString("title");
                        title_hash = jsonobj1_.getString("title_hash");
                        photo_url = jsonobj1_.getString("photo_url");
                        speciality = jsonobj1_.getString("speciality");

                        System.out.println("title_hash--------" + title_hash);
                        System.out.println("photo_url--------" + photo_url);

                        url_maps.put(title_hash, photo_url);
                        url_maps_id.put(title_hash, title);
                    }

                    setimages();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            //dialog.dismiss();
        }

    }


    public void setimages() {
        try {
            int i = 1;
            for (String name : (url_maps).keySet()) {
                DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
                //TextSliderView textSliderView = new TextSliderView(getActivity());
                textSliderView
                        //.description((url_maps_id).get(name))
                        .description(null)
                        .image((url_maps).get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);

                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putString("extra", name);

                System.out.println("name--------------" + name);

                mDemoSlider.addSlider(textSliderView);

                i = i + 1;
                System.out.println("i--------------" + i);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("End-----");

        //mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.setPresetTransformer("Stack");

    }


    public void apply_offers(String jsonarray_str) {

        try {

            innerLay.removeAllViews();

            JSONArray jsonarray = new JSONArray(jsonarray_str);

            System.out.println("jsonarray.length()-----" + jsonarray.length());
            System.out.println("jsonarray-----" + jsonarray.toString());

            if (jsonarray.length() > 0) {

                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobj1 = jsonarray.getJSONObject(i);
                    System.out.println("jsonobj_first-----" + jsonobj1.toString());

                    qa_url = jsonobj1.getString("url");
                    qa_doctor_name = jsonobj1.getString("doctor_name");
                    qa_title = jsonobj1.getString("title");
                    qa_abstract = jsonobj1.getString("abstract");
                    qa_photo_url = jsonobj1.getString("photo_url");

                    System.out.println("jsononjsec-----" + jsonobj1.toString());

                    vi = getActivity().getLayoutInflater().inflate(R.layout.cardview_qa, null);
                    textview_title = (TextView) vi.findViewById(R.id.textview_title);
                    textview_short = (TextView) vi.findViewById(R.id.textview_short);
                    textview_docname = (TextView) vi.findViewById(R.id.textview_docname);
                    textview_ctype = (TextView) vi.findViewById(R.id.textview_ctype);
                    tv_url = (TextView) vi.findViewById(R.id.tv_url);
                    imageview_poster = (CircleImageView) vi.findViewById(R.id.imageview_poster);

                    //---------------- Custom Font --------------------
                    Typeface robo_regular = Typeface.createFromAsset(getActivity().getAssets(), Model.font_name);
                    Typeface robo_bold = Typeface.createFromAsset(getActivity().getAssets(), Model.font_name_bold);

                    textview_title.setTypeface(robo_bold);
                    textview_short.setTypeface(robo_regular);
                    textview_docname.setTypeface(robo_regular);
                    textview_ctype.setTypeface(robo_regular);
                    tv_viewall.setTypeface(robo_regular);
                    //---------------- Custom Font --------------------

                    textview_title.setText(qa_title);
                    textview_short.setText(qa_abstract);
                    textview_docname.setText(qa_doctor_name);
                    textview_ctype.setText("QA");
                    tv_url.setText(qa_url);

                    Picasso.with(getActivity()).load(qa_photo_url).placeholder(R.drawable.progress_animation).error(R.mipmap.user_grey_icon).into(imageview_poster);

                    innerLay.addView(vi);
                }

                vi = getActivity().getLayoutInflater().inflate(R.layout.end_card_qa, null);

                LinearLayout end_qa_layout = (LinearLayout) vi.findViewById(R.id.end_qa_layout);

                end_qa_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), Answeres_Activity.class);
                        startActivity(intent);
                    }
                });

                innerLay.addView(vi);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*private void showTipWithPosition(ShowCasePosition position) {
        showTip(
                position,
                new Radius(186F)
        );
    }

    private void showTip(ShowCasePosition position, ShowCaseRadius radius) {
        new ShowCaseView.Builder(getActivity())
                .withTypedPosition(position)
                .withTypedRadius(radius)
                .withContent(
                        "Chat with Doctor for 100 hrs!"
                )
                .build()
                .show(getActivity());
    }*/

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {
        System.out.println("scroll Down--------");
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        //System.out.println("scrollState--------" + scrollState);

        if (scrollState == ScrollState.UP) {

            System.out.println("scrollDir-----UP---" + scrollState);
            //bottom_bar.animate().translationY(bottom_bar.getHeight());
            //sliding_tabs.animate().translationY(-sliding_tabs.getHeight());

        } else if (scrollState == ScrollState.DOWN) {

            System.out.println("scrollDir-----Down---" + scrollState);
            //bottom_bar.animate().translationY(0);
            //sliding_tabs.animate().translationY(0);
        }
    }

    //------------ Bottom Bar Hide ----------------------------------------------------------------
    private void showBottomBar() {
        moveBottomBar(0);
    }

    private void hideBottomBar() {
        System.out.println("toolbar.getHeight()------" + toolbar.getHeight());
        moveBottomBar(toolbar.getHeight());
    }

    private void moveBottomBar(float toTranslationY) {

        if (ViewHelper.getTranslationY(toolbar) == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(toolbar), toTranslationY).setDuration(400);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(toolbar, translationY);
                ViewHelper.setTranslationY((View) toolbar, translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ((View) toolbar).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;
                ((View) toolbar).requestLayout();
            }
        });
        animator.start();
    }

    //------------ Toolbar Hide ----------------------------------------------------------------
    protected int getScreenHeight() {
        return getActivity().findViewById(android.R.id.content).getHeight();
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

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static boolean checkRingerIsOn(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
    }

}
