package com.manyanger.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.manyanger.common.GlobalData;
import com.manyanger.entries.ChapterItem;
import com.manyanger.pay.Purchase;
import com.manyanger.ui.widget.CustomDialog;
import com.manyounger.fiiish.R;

import sms.purchasesdk.cartoon.OnSMSPaycodeListener;
import sms.purchasesdk.cartoon.PurchaseCode;
import sms.purchasesdk.cartoon.SMSPaycode;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName: PurchActivity
 * @Description: TODO
 * @author Zephan.Yu
 * @date 2014-8-31 下午5:40:10
 */
public class PurchActivity extends Activity implements android.view.View.OnClickListener, OnSMSPaycodeListener{
    
    private final int MAX_PRICE_TYPE = 6;
    
    public static final int INIT_FINISH = 10000;
    public static final int BILL_FINISH = 10001;
    public static final int QUERY_FINISH = 10002;
    public static final int UNSUB_FINISH = 10003;
    
    
    private final int[] chapterPrice = {
            1, 2, 4, 6, 
            8, 10
    };
    
    private final int[][] purchChapters = {
            {0}, {1}, {2, 3, 4}, {5, 6, 7, 8},
            {9, 10, 11, 12, 13},
            {14, 15, 16, 17, 18, 19}
    };
    
    private final int[] purchChapterPos = {
            0, 1, 2, 5, 9, 14
    };
    
    private final String[] purchTitles = {
            "第1章", "第2章", "第3章~第5章", "第6章~第9章",
            "第10章~第14章", "第15章~第20章",
    };
    
    private List<ChapterItem> chapterList = null;
    private final Button[] buttons = new Button[MAX_PRICE_TYPE];
    
    private String mPaycode;
    private String mTradeCode;
    private int mTradeType;
    private int mTradePos;
    private int mCurrPrice;
    
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

                    try{
                        int pos = msg.arg2;
                        buttons[pos].setText(R.string.buyed);
                        buttons[pos].setClickable(false);
                    }catch(Exception e){
                        e.printStackTrace();
                    }

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

        setContentView(R.layout.activity_purch);

        chapterList = GlobalData.getChapterList();
        initView();

    }
    

    /**
     * 
     */
    private void initView() {
        buttons[0] = (Button) findViewById(R.id.buy1);
        buttons[1] = (Button) findViewById(R.id.buy2);
        buttons[2] = (Button) findViewById(R.id.buy3);
        buttons[3] = (Button) findViewById(R.id.buy4);
        buttons[4] = (Button) findViewById(R.id.buy5);
        buttons[5] = (Button) findViewById(R.id.buy6);
        
        if(chapterList == null){
            return;
        }
        try{
        for(int i=0; i<MAX_PRICE_TYPE; i++){
            ChapterItem item = chapterList.get(purchChapterPos[i]);
            if(item.isPayed()){
                buttons[i].setText(R.string.buyed);
                buttons[i].setClickable(false);
            }
        }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int index = -1;
        switch (v.getId()) {
            
            case R.id.buy1:
                index = 0;
                break;
            case R.id.buy2:
                index = 1;
                break;
            case R.id.buy3:
                index = 2;
                break;
            case R.id.buy4:
                index = 3;
                break;
            case R.id.buy5:
                index = 4;
                break;
            case R.id.buy6:
                index = 5;
                break;
            default:
                break;
        }
        if(index >= 0 && index < MAX_PRICE_TYPE){
            buyChapter(index);
        }
    }
    
    private void buyChapter(int pos) {
        mCurrPrice = chapterPrice[pos];
        mPaycode = Purchase.getPayStringByPirce(mCurrPrice); //(Purchase.PAYCODE_CHAPTER, mDetailInfo);
        mTradeType = Purchase.PAYCODE_CHAPTER;
        mTradePos = pos;

        showDialog(GlobalData.DIALOG_PAY_TIP);
        
    }

    @Override  
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {  
        switch (id) {  
        case GlobalData.DIALOG_PAY_TIP:
//          int price = Purchase.getPrice(mTradeType, mDetailInfo);
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
//                            DetailActivity.this  
//                            .dismissDialog(GlobalData.DIALOG_PAY_TIP);  
                            dialog.dismiss();
                        }  
                    })  
                    .setPositiveButton(new DialogInterface.OnClickListener() {  
                        @Override
                        public void onClick(DialogInterface dialog, int which) {  
                            
//                      try {
//                          GlobalData.msmsPaycode.smsOrder(DetailActivity.this, mPaycode, DetailActivity.this, mTradeCode);
//                      } catch (Exception e) {
//                          e.printStackTrace();
//                      }
//                            Purchase.savePayed(DetailActivity.this, mTradeCode);
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

    private void saveBuyedChapters(int pos){
        try{
            int[] chapters = purchChapters[pos];
            for(int itemPos : chapters){
                ChapterItem item = chapterList.get(itemPos);
                String tradeCode = Purchase.getPayCode(Purchase.PAYCODE_CHAPTER,
                        item.getId());
                Purchase.savePayed(PurchActivity.this, tradeCode);
                item.setPayed(true);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onBillingFinish(int code, HashMap arg1) {
        String result = "订购结果：订购成功";
        // 商品信息
        String paycode = null;
        // 商品的交易 ID，用户可以根据这个交易ID，查询商品是否已经交易
        String tradeID = null;

        if (code == PurchaseCode.ORDER_OK){
            
//            Purchase.savePayed(this, mTradeCode);
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

}
