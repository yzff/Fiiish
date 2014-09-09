package com.manyanger.common;

import android.app.Activity;

import com.manyanger.cache.ImageCacheNew;
import com.manyanger.data.IServerInterface;
import com.manyanger.data.net.HttpService;
import com.manyanger.entries.ChapterItem;

import sms.purchasesdk.cartoon.SMSPaycode;

import java.util.List;
//import com.manyanger.cache.ImageCacheNew;
//import sms.purchasesdk.cartoon.SMSPaycode;

/**
 * @ClassName: GlobalData
 * @Description: TODO
 * @author Zephan.Yu
 * @date 2014-6-14 下午10:29:43
 */
public class GlobalData implements IServerInterface{
    
    public final static int COMIC_ID = 1065;
    
	public final static int STATE_INIT = 0;
	public final static int STATE_LOADING = 1;
	public final static int STATE_FINISHED = 2;
	public final static int STATE_FAILED = -1;
	
	public final static String STR_TITLE = "title";
	public final static String STR_LISTTYPE = "listType";
    public final static String STR_LISTID = "listId";
    public final static String STR_COMICID = "comicId";
	public final static String STR_URL = "url";
	public final static String STR_CHAPTERID = "chapterId";
	public final static String STR_KEYWORD = "keyword";
	public final static String STR_POSITION = "position";
    
    public final static int NOTIFY_COMICLIST_LOADED = 1;
    public final static int NOTIFY_COMICTHEME_LOADED = 2;
    public final static int NOTIFY_COMICDETAIL_LOADED = 3;
    public final static int NOTIFY_COMICCOVER_LOADED = 4;
    public final static int NOTIFY_BANNERIMAGE_LOADED = 5;
    public final static int NOTIFY_CONTENTIMAGE_LOADED = 6;
    public final static int NOTIFY_PICTURELIST_LOADED = 7;
    public final static int NOTIFY_CHAPTERDETAIL_LOADED = 8;
    
    public final static int DIALOG_PAY_TIP = 1;
    public final static int DIALOG_PAYED = 2;
    
    public final static int ACTIVITY_STAT_INIT = 0;
    public final static int ACTIVITY_STAT_DETAIL_WAIT = 1;
    public final static int ACTIVITY_STAT_BRIEF_WAIT = 2;
    
    private volatile static int activityState = ACTIVITY_STAT_INIT;
    private volatile static Activity waitActivity = null;
   
    private static ImageCacheNew imageCache;
    
    private static HttpService httpService;
    
	public static SMSPaycode msmsPaycode;
	
	private static String briefContent;
	private static String briefAuthor;
	private static List<ChapterItem> chapterList = null;
	
    
    public static void init(){
        setImageCache(ImageCacheNew.getInstance());
        
        setHttpService(new HttpService());
    }

    /**
     * @return the httpService
     */
    public static HttpService getHttpService() {
        return httpService;
    }

    /**
     * @param httpService the httpService to set
     */
    public static void setHttpService(HttpService httpService) {
        GlobalData.httpService = httpService;
    }

    /**
     * @return the imageCache
     */
    public static ImageCacheNew getImageCache() {
        return imageCache;
    }

    /**
     * @param imageCache the imageCache to set
     */
    public static void setImageCache(ImageCacheNew imageCache) {
        GlobalData.imageCache = imageCache;
    }

    public static String getBriefContent(){
        return briefContent;
    }
    
    public static void setBriefContent(String brief){
        briefContent = brief;
    }
    
    public static String getBriefAuthor(){
        return briefAuthor;
    }
    
    public static void setBriefAuthor(String author){
        briefAuthor = author;
    }

    /**
     * @return
     */
    public static List<ChapterItem> getChapterList() {
        // TODO Auto-generated method stub
        return chapterList;
    }

    /**
     * @param chapterList2
     */
    public static void setChapterList(List<ChapterItem> chapterList2) {
        // TODO Auto-generated method stub
        chapterList = chapterList2;
    }

    /**
     * @return the activityState
     */
    public static int getActivityState() {
        return activityState;
    }

    /**
     * @param activityState the activityState to set
     */
    public static void setActivityState(int activityState) {
        GlobalData.activityState = activityState;
    }

    /**
     * @return the waitActivity
     */
    public static Activity getWaitActivity() {
        return waitActivity;
    }

    /**
     * @param waitActivity the waitActivity to set
     */
    public static void setWaitActivity(Activity waitActivity) {
        GlobalData.waitActivity = waitActivity;
    }

    /**
     * @param chapterList2
     */
    public static void appendChapterList(List<ChapterItem> chapterList2) {
        if(GlobalData.chapterList != null && chapterList2 != null){
            int offset = GlobalData.chapterList.size();
            for(ChapterItem item : chapterList2){
                item.setPosition(offset + 1);
                GlobalData.chapterList.add(item);
                offset += 1;
            }
        }
        
    }
}
