package com.manyanger.ui;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;

import com.manyanger.common.GlobalData;
import com.manyanger.data.ChapterDataLoader;
import com.manyanger.data.DetailDataLoader;
import com.manyanger.data.OnDataLoadedListener;
import com.manyanger.entries.ChapterItem;
import com.manyanger.entries.DetailInfo;
import com.manyanger.ui.widget.ToastUtil;
import com.manyounger.fiiish.R;

import sms.purchasesdk.cartoon.SMSPaycode;

import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class LogoActivity extends Activity implements OnClickListener, OnDataLoadedListener{
    
    private final static int BRIEF_STAT_INIT = 0;
    private final static int BRIEF_STAT_LOADED = 1;
    private final static int BRIEF_STAT_FAILED = -1;
    

	
	 private static long trigleCancel;
	 
	 private DetailDataLoader detailLoader;
	 private ChapterDataLoader chapterLoader;
	 
	 
     // 计费信息
    // 计费信息 (现网环境)   
    private static final String APPID = "300000000425";
    private static final String APPKEY = "24613208";
    
    protected String cmdLine;
    
    protected volatile int briefState = BRIEF_STAT_INIT;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_logo);

//		initPayData();
		
	      detailLoader = new DetailDataLoader(this);
	      chapterLoader = new ChapterDataLoader(this);
	      cmdLine = detailLoader.loadComicDetail(GlobalData.COMIC_ID);

	}/**
     * 
     */
    private void initPayData() {
        GlobalData.msmsPaycode = SMSPaycode.getInstance();
        /**
         * step3.向Purhase传入应用信息。APPID，APPKEY。 需要传入参数APPID，APPKEY。 APPID，见开发者文档
         * APPKEY，见开发者文档
         */
        try {
            GlobalData.msmsPaycode.setAppInfo(APPID, APPKEY);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        
    }
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_read:
		    if(briefState == BRIEF_STAT_LOADED){
		        startActivity(new Intent(this, DetailActivity.class));
		    } else {

		        GlobalData.setActivityState(GlobalData.ACTIVITY_STAT_DETAIL_WAIT);
		        startActivity(new Intent(this, WaitActivity.class));
	              if(briefState == BRIEF_STAT_FAILED){
	                    briefState = BRIEF_STAT_INIT;
//	                    cmdLine = detailLoader.loadComicDetail(GlobalData.COMIC_ID);
	                    loadDetailDelay();
	                }
		    } 
			break;
		case R.id.btn_about:
//			startAboutActivity(1);
	        startActivity(new Intent(this, AboutActivity2.class));
			break;
		case R.id.btn_brief:
//			startAboutActivity(0);
	          if(briefState == BRIEF_STAT_LOADED){
	                startActivity(new Intent(this, BriefActivity.class));
	          } else {

	              GlobalData.setActivityState(GlobalData.ACTIVITY_STAT_BRIEF_WAIT);
	              startActivity(new Intent(this, WaitActivity.class));
	               if(briefState == BRIEF_STAT_FAILED){
	                      briefState = BRIEF_STAT_INIT;
	                      loadDetailDelay();
	                      
	              }
	          }
			break;
		}
		
	}
	
	private void loadDetailDelay(){
	    new Handler().postDelayed(new Runnable(){   

	        @Override
            public void run() {   

	            cmdLine = detailLoader.loadComicDetail(GlobalData.COMIC_ID);

	        }   

	     }, 1500);   
	}
	
	protected void startAboutActivity(int type){
		Intent intent = new Intent(this, AboutActivity.class);
		intent.putExtra("type", type);
		startActivity(intent);
	}

	
    @Override
    public void onBackPressed()
    {
        ToastUtil.cancel();

            //“提示再按一次可退出..”
        toastExtrance();
    }
    
    protected void toastExtrance()
    {
        final long uptimeMillis = SystemClock.uptimeMillis();
        if (uptimeMillis - trigleCancel > 2000)
        {
            trigleCancel = uptimeMillis;
            ToastUtil.showToastShort(getString(R.string.note_exit));
        }
        else
        {
            finish();

            System.exit(0);
        }
    }
    @Override
    public void OnDataLoaded(Message msg) {
        // TODO Auto-generated method stub
        switch (msg.what) {
        case GlobalData.NOTIFY_COMICDETAIL_LOADED:
        {
            DetailInfo detailInfo = (DetailInfo) msg.obj;
            if(detailInfo != null){
                GlobalData.setBriefContent(detailInfo.getDepict());
                GlobalData.setBriefAuthor(detailInfo.getAuthor());
                GlobalData.setChapterList(detailInfo.getChapterList());
                
                chapterLoader.loadChapterList(GlobalData.COMIC_ID);
                
                
            } else {
                briefState = BRIEF_STAT_FAILED;
                Activity activity = GlobalData.getWaitActivity();
                if(null != activity){
                    activity.finish();
                    ToastUtil.showToastShort(getString(R.string.no_connect_hint));
                } else {
                    checkActivityDelay();
                }
                
            }
        }
            break;
            
        case GlobalData.NOTIFY_CHAPTERDETAIL_LOADED:{
            List<ChapterItem> chapterList = (List<ChapterItem>) msg.obj;
            if (chapterList != null) {
                GlobalData.appendChapterList(chapterList);
                
                briefState = BRIEF_STAT_LOADED;
                if(GlobalData.getActivityState() == GlobalData.ACTIVITY_STAT_BRIEF_WAIT){
                    startActivity(new Intent(this, BriefActivity.class));
                }else if(GlobalData.getActivityState() == GlobalData.ACTIVITY_STAT_DETAIL_WAIT){
                    startActivity(new Intent(this, DetailActivity.class));
                }
                GlobalData.setActivityState(GlobalData.ACTIVITY_STAT_INIT);
                Activity activity = GlobalData.getWaitActivity();
                if(null != activity){
                    activity.finish();
                }
            } else {
                briefState = BRIEF_STAT_FAILED;
                Activity activity = GlobalData.getWaitActivity();
                if(null != activity){
                    activity.finish();
                    ToastUtil.showToastShort(getString(R.string.no_connect_hint));
                } else {
                    checkActivityDelay();
                }
            }
        }
        }
    }
    
    private void checkActivityDelay(){
        new Handler().postDelayed(new Runnable(){   

            @Override
            public void run() {   
                if(briefState == BRIEF_STAT_FAILED){
                    Activity activity = GlobalData.getWaitActivity();
                    if(null != activity){
                        activity.finish();
                        ToastUtil.showToastShort(getString(R.string.no_connect_hint));
                    }
                }
            }   

         }, 1500);   
    }
}
