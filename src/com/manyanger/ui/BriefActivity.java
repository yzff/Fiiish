package com.manyanger.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.manyanger.common.GlobalData;
import com.manyounger.fiiish.R;

/**
 * @ClassName: BriefActivity
 * @Description: TODO
 * @author Zephan.Yu
 * @date 2014-8-19 下午10:30:14
 */
public class BriefActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_brief);
        
        TextView contentView = (TextView)findViewById(R.id.content);
        contentView.setText(getString(R.string.brief_c, GlobalData.getBriefContent()));

        TextView authorView = (TextView)findViewById(R.id.author);
        authorView.setText(getString(R.string.brief_a, GlobalData.getBriefAuthor()));
    }
    

    @Override
    public void onBackPressed() {
        finish();
    }
}
