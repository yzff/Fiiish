package com.manyanger.ui;

import com.manyounger.fiiish.R;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class AboutActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);
        ImageView imageview = (ImageView)findViewById(R.id.content);
        int type = getIntent().getIntExtra("type", 0);
        if(type == 0){
        	imageview.setImageResource(R.drawable.brief);
        } else {
        	imageview.setImageResource(R.drawable.about);
        }
    }
    

    @Override
    public void onBackPressed() {
        finish();
    }
}
