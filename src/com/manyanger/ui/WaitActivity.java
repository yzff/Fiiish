package com.manyanger.ui;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.manyanger.common.GlobalData;
import com.manyounger.fiiish.R;

/**
 * @ClassName: WaitActivity
 * @Description: TODO
 * @author Zephan.Yu
 * @date 2014-8-19 下午11:43:21
 */
public class WaitActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GlobalData.setWaitActivity(this);
        setContentView(R.layout.activity_wait);

    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        
        ImageView imageView = (ImageView)findViewById(R.id.content);
        imageView.setImageResource(R.drawable.wait_anim);
        AnimationDrawable anim = (AnimationDrawable) imageView.getDrawable();
        
        anim.stop();
        anim.start();
    }
    
    @Override
    public void onDestroy(){
        super.onDestroy();
        GlobalData.setWaitActivity(null);
    }

    @Override
    public void onBackPressed() {
        finish();
        GlobalData.setActivityState(GlobalData.ACTIVITY_STAT_INIT);
    }
}
