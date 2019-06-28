package com.orane.icliniq;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.orane.icliniq.Model.Model;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditSomeOneActivity extends AppCompatActivity {


    Spinner spinner_gender;
    Map<String, String> gen_map = new HashMap<String, String>();
    MaterialEditText edt_name, edt_age;
    ImageView img_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.someone_edit);

        img_close = (ImageView) findViewById(R.id.img_close);
        spinner_gender = (Spinner) findViewById(R.id.spinner_gender);
        edt_name = (MaterialEditText) findViewById(R.id.edt_name);
        edt_age = (MaterialEditText) findViewById(R.id.edt_age);

        //------- Setting spinner_gender ----------------------
        final List<String> lang_categories = new ArrayList<String>();

        lang_categories.add("Select Gender");

        lang_categories.add("Male");
        gen_map.put("Male", "1");
        lang_categories.add("Female");
        gen_map.put("Female", "2");
        lang_categories.add("Female");
        gen_map.put("Thirdgender", "3");

        ArrayAdapter<String> lang_dataAdapter = new ArrayAdapter<String>(EditSomeOneActivity.this, android.R.layout.simple_spinner_item, lang_categories);
        lang_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(lang_dataAdapter);
        //---------------------------------------------


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        Typeface tf = Typeface.createFromAsset(getAssets(), Model.font_name);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Closing-----");
                finish();
            }
        });

        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String gen_name = spinner_gender.getSelectedItem().toString();
                String gen_val = gen_map.get(gen_name);

                System.out.println("gen_name----------" + gen_name);
                System.out.println("gen_val----------" + gen_val);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
