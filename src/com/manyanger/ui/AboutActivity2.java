package com.manyanger.ui;

import android.app.Activity;
import android.os.Bundle;

import com.manyounger.fiiish.R;

/**
 * @ClassName: AboutActivity2
 * @Description: TODO
 * @author Zephan.Yu
 * @date 2014-8-19 下午10:23:20
 */
public class AboutActivity2 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about2);
//        ImageView imageview = (ImageView)findViewById(R.id.content);
//        int type = getIntent().getIntExtra("type", 0);
//        if(type == 0){
//            imageview.setImageResource(R.drawable.brief);
//        } else {
//            imageview.setImageResource(R.drawable.about);
//        }
    }
    

    @Override
    public void onBackPressed() {
        finish();
    }
}
