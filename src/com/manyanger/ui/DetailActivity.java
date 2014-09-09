package com.manyanger.ui;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.manyanger.common.GlobalData;
import com.manyanger.entries.ChapterAdapter;
import com.manyanger.entries.ChapterItem;
import com.manyanger.pay.Purchase;
import com.manyanger.ui.widget.CustomDialog;
import com.manyanger.ui.widget.InScrollGridView;
import com.manyounger.fiiish.R;

import sms.purchasesdk.cartoon.OnSMSPaycodeListener;
import sms.purchasesdk.cartoon.PurchaseCode;
import sms.purchasesdk.cartoon.SMSPaycode;

import java.util.HashMap;
import java.util.List;

public class DetailActivity extends Activity implements OnItemClickListener, OnSMSPaycodeListener, android.view.View.OnClickListener{
    
    protected static final int CHAPTER_NUM = 10;

//    protected final int[] chapterIds = {
//            6376, 6378, 6379, 6380, 6386,
//            6387, 6388, 6389, 6390, 6391
//    };
    
//    protected final String[] chapterTitleS = {
//            "第一章", "第二章", "第三章", "第四章", "第五章", 
//            "第六章", "第七章", "第八章", "第九章", "第十章",
//            "第十一章", "第十二章", "第十三章", "第十四章", "第十五章", 
//            "第十六章", "第十七章", "第十八章", "第十九章", "第二十章",            
//    };
    
    private final int[] chapterPrice = {
            1, 2, 4, 4, 4,
            6, 6, 6, 6, 
            8, 8, 8, 8, 8, 
            10, 10, 10, 10, 10, 10
    };
    
    private final int[][] purchChapters = {
            {0}, {1}, {2, 3, 4}, {2, 3, 4}, {2, 3, 4},
            {5, 6, 7, 8}, {5, 6, 7, 8}, {5, 6, 7, 8}, {5, 6, 7, 8}, {9, 10, 11, 12, 13},
            {9, 10, 11, 12, 13}, {9, 10, 11, 12, 13}, {9, 10, 11, 12, 13}, {9, 10, 11, 12, 13}, {14, 15, 16, 17, 18, 19},
            {14, 15, 16, 17, 18, 19}, {14, 15, 16, 17, 18, 19}, {14, 15, 16, 17, 18, 19}, 
            {14, 15, 16, 17, 18, 19}, {14, 15, 16, 17, 18, 19}
    };
    
    private final String[] purchTitles = {
            "第1章", "第2章", "第3章~第5章", "第3章~第5章", "第3章~第5章",
            "第6章~第9章", "第6章~第9章", "第6章~第9章", "第6章~第9章",
            "第10章~第14章", "第10章~第14章", "第10章~第14章", 
            "第10章~第14章", "第10章~第14章",
            "第15章~第20章", "第15章~第20章", "第15章~第20章",
            "第15章~第20章", "第15章~第20章", "第15章~第20章",
    };
    
    private List<ChapterItem> chapterList = null;
    private InScrollGridView gv_chapter;
    private ChapterAdapter mChapterAdapter;
    
    private String mPaycode;
    private String mTradeCode;
    private int mTradeType;
//    private int mTradeId;
    private int mTradePos;
    
//    private int mChapterPrice;
//    private int mBookPrice;
    private int mCurrPrice;
    
    public static final int INIT_FINISH = 10000;
    public static final int BILL_FINISH = 10001;
    public static final int QUERY_FINISH = 10002;
    public static final int UNSUB_FINISH = 10003;
    
    private final Handler mHandler = new Handler(){


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            Log.i("IAPHandler", "handleMessage:"+what);
            switch (what) {
            case INIT_FINISH:

                break;
            case BILL_FINISH:
                if(msg.arg1 == Purchase.PAYCODE_CHAPTER) {
                    mChapterAdapter.notifyDataSetChanged();
                    try{
                    ChapterItem item = chapterList.get(msg.arg2);
                    if(item != null){
                        readComic(item.getId(), item.getTitle(), item.getPosition());
                    }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
//                    List<ChapterItem> chapterlist = chapterList;
//                    for(ChapterItem item : chapterlist){
//                        if(item.getId() == msg.arg2){
////                            Purchase.saveChapterPayed(DetailActivity.this, mDetailInfo.getId());
////                            mDetailInfo.incPayedCount();
////                            if(mDetailInfo.isAllChapterPayed()){
////                                buyBtn.setText(R.string.already_buy);
////                                buyBtn.setBackgroundResource(R.drawable.button5_i);
////                                buyBtn.setClickable(false);
////                            }
////                            if(!mDetailInfo.isProcess()) {
////                                mBookPrice = getBookPrice(mDetailInfo, mChapterPrice);
////                                tv_price.setText(getPriceString(mDetailInfo));
////                            }
//                            
//                            item.setPayed(true);
//                            mChapterAdapter.notifyDataSetChanged();
//                            readComic(item.getId(), item.getTitle(), item.getPosition());
//                            break;
//                        }
//                    }
                }
                break;
            default:
                break;
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        initChapterList();
        
        gv_chapter = (InScrollGridView)findViewById(R.id.gv_chapter);
        gv_chapter.setFocusable(false);
        gv_chapter.setOnItemClickListener(this);
        mChapterAdapter = new ChapterAdapter(DetailActivity.this,
                chapterList);
        gv_chapter.setAdapter(mChapterAdapter);
        
        try {
//          GlobalData.msmsPaycode.smsInit(this, mListener);
          GlobalData.msmsPaycode.smsInit(this, this);
      } catch (Exception e) {
          e.printStackTrace();
      }
        
    }
    
    @Override
    protected void onResume(){
        super.onResume();

            if(mChapterAdapter != null){
                mChapterAdapter.notifyDataSetChanged();
            }

    }
    
//    private static boolean checkChapterPayed(int id){
//        String payCode = Purchase.getPayCode(Purchase.PAYCODE_CHAPTER, id);
//        return Purchase.isPayed(AppInfo.getContext(), payCode);
//    }

    /**
     * 
     */
    private void initChapterList() {
//        chapterList = new ArrayList<ChapterItem>();
//        for(int i=0; i<CHAPTER_NUM; i++){
//            ChapterItem item = new ChapterItem(chapterIds[i], chapterTitleS[i]);
//            item.setPayed(isChapterPayed(chapterIds[i]));
//            item.setPosition(i);
//            chapterList.add(item);
//        }
        chapterList = GlobalData.getChapterList();
        
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
	    try{
    	    ChapterItem item = chapterList.get(position);
    	    if(item != null){
    	        readComic(item, position);
    	    }
	    } catch(Exception e){
	        e.printStackTrace();
	    }
        
    }
    
    private boolean isChapterPayed(int chapterId){
        String paycode = Purchase.getPayCode(Purchase.PAYCODE_CHAPTER,
                 chapterId);
         return Purchase.isPayed(this, paycode);
 }

protected void readComic(final ChapterItem item, int pos) {
    if(isChapterPayed(item.getId())){
        readComic(item.getId(), item.getTitle(), pos);
    } else {
        buyChapter(item.getId(), pos);
    }
//	readComic(item.getId(), item.getTitle(), item.getPosition());
}

private void readComic(int id, String title, int pos){
    Intent intent = new Intent(DetailActivity.this, ComicReaderActivity.class);
    intent.putExtra(GlobalData.STR_CHAPTERID, id);
    String titleName = getString(R.string.chapter_name, pos+1);
    intent.putExtra(GlobalData.STR_TITLE, titleName);
//    intent.putExtra(GlobalData.STR_TITLE, title);
//    intent.putExtra(GlobalData.STR_POSITION, pos);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
}

private void buyChapter(int chapterId, int pos) {
    mCurrPrice = chapterPrice[pos];
    mPaycode = Purchase.getPayStringByPirce(mCurrPrice); //(Purchase.PAYCODE_CHAPTER, mDetailInfo);
    mTradeCode = Purchase.getPayCode(Purchase.PAYCODE_CHAPTER,
            chapterId);
    mTradeType = Purchase.PAYCODE_CHAPTER;
//    mTradeId = chapterId;

    mTradePos = pos;

    showDialog(GlobalData.DIALOG_PAY_TIP);
    
}

private void saveBuyedChapters(int pos){
    try{
        int[] chapters = purchChapters[pos];
        for(int itemPos : chapters){
            ChapterItem item = chapterList.get(itemPos);
            String tradeCode = Purchase.getPayCode(Purchase.PAYCODE_CHAPTER,
                    item.getId());
            Purchase.savePayed(DetailActivity.this, tradeCode);
            item.setPayed(true);
        }
    } catch(Exception e){
        e.printStackTrace();
    }
}

@Override  
protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {  
    switch (id) {  
    case GlobalData.DIALOG_PAY_TIP:
//      int price = Purchase.getPrice(mTradeType, mDetailInfo);
        String content = getString(R.string.dialog_content, mCurrPrice); //price);
        
        ((CustomDialog)dialog).setMessage(content); 
        ((CustomDialog)dialog).setChapter(purchTitles[mTradePos]);
        break;  
    }  
    super.onPrepareDialog(id, dialog, args);  
}  


@Override
protected Dialog onCreateDialog(final int what)
{
    Dialog dialog = null;
    if(what == GlobalData.DIALOG_PAY_TIP){
        CustomDialog.Builder customBuilder = new  
                CustomDialog.Builder(this);  
            int price = 2; //Purchase.getPrice(mTradeType, mDetailInfo);
            String content = getString(R.string.dialog_content, price);
            
            customBuilder.setMessage(content)  
                .setNegativeButton(new DialogInterface.OnClickListener() {  
                    @Override
                    public void onClick(DialogInterface dialog, int which) {  
//                        DetailActivity.this  
//                        .dismissDialog(GlobalData.DIALOG_PAY_TIP);  
                        dialog.dismiss();
                    }  
                })  
                .setPositiveButton(new DialogInterface.OnClickListener() {  
                    @Override
                    public void onClick(DialogInterface dialog, int which) {  
                        
//                  try {
//                      GlobalData.msmsPaycode.smsOrder(DetailActivity.this, mPaycode, DetailActivity.this, mTradeCode);
//                  } catch (Exception e) {
//                      e.printStackTrace();
//                  }
//                        Purchase.savePayed(DetailActivity.this, mTradeCode);
                        saveBuyedChapters(mTradePos);
                        Message msg = new Message();
                        msg.what = BILL_FINISH;
                        msg.arg1 = mTradeType;
                        msg.arg2 = mTradePos; //mTradeId;
                        mHandler.sendMessage(msg);
                        
                  dialog.dismiss();  
                    }  
                });  
            dialog = customBuilder.create(); 
    }
    return dialog;
}

@Override
public void onBillingFinish(int code, HashMap arg1) {
    Log.d("DetailActivity", "billing finish, status code = " + code);
    String result = "订购结果：订购成功";
    // 商品信息
    String paycode = null;
    // 商品的交易 ID，用户可以根据这个交易ID，查询商品是否已经交易
    String tradeID = null;

    if (code == PurchaseCode.ORDER_OK){
        
//        Purchase.savePayed(this, mTradeCode);
        saveBuyedChapters(mTradePos);
        Message msg = new Message();
        msg.what = BILL_FINISH;
        msg.arg1 = mTradeType;
        msg.arg2 = mTradePos; //mTradeId;
        mHandler.sendMessage(msg);
        
        /**
         * 商品购买成功或者已经购买。 此时会返回商品的paycode，orderID,以及剩余时间(租赁类型商品)
         */
        if (arg1 != null) {
            
            paycode = (String) arg1.get(OnSMSPaycodeListener.PAYCODE);
            if (paycode != null && paycode.trim().length() != 0) {
                result = result + ",Paycode:" + paycode;
            }
            tradeID = (String) arg1.get(OnSMSPaycodeListener.TRADEID);
            if (tradeID != null && tradeID.trim().length() != 0) {
                result = result + ",tradeid:" + tradeID;
            }
        }
    } else if(code == PurchaseCode.INIT_OK){
        result = "订购结果：初始化完成";
    } else {
        /**
         * 表示订购失败。
         */
        result = "订购结果：" + SMSPaycode.getReason(code);
    }
//  dismissProgressDialog();
    System.out.println(result);

    
}

@Override
public void onClick(View v) {
    switch (v.getId()) {
        
        case R.id.purch_btn:
            startActivity(new Intent(this, PurchActivity.class));
            break;
    }
    
}

}

//{"BackCode":"1001","comic":{"process":"已完结","processType":2,"id":1065,"author":"筱筠978535","depict":"长盛不衰的全龄向搞笑：看咸鱼耍宝搞笑，脑洞大开，证明咸鱼也有明天！","chapterCount":10,"cover":"http://upload.otooman.com:82/common/2014/08/08/133016447837.jpg","title":"咸鱼也可以翻身","flag":1,"theme":"搞笑"},
//"chapterList":[{"id":6376,"title":"第一章","index":1},{"id":6378,"title":"第二章","index":2},{"id":6379,"title":"第三章","index":3},{"id":6380,"title":"第四章","index":4},
//{"id":6386,"title":"第五章","index":5},{"id":6387,"title":"第六章","index":6},{"id":6388,"title":"第七章","index":7},{"id":6389,"title":"第八章","index":8},{"id":6390,"title":"第九章","index":9}]}
//{"BackCode":"1001","chapterList":[{"id":6391,"title":"第十章","index":10}]}