package com.manyanger.pay;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

/**
 * @ClassName: Purchase
 * @Description: TODO
 * @author Zephan.Yu
 * @date 2014-7-20 下午4:08:48
 */
public class Purchase {
    public static final int PAYCODE_MONTH = 3;
    public static final int PAYCODE_BOOK = 2;
    public static final int PAYCODE_CHAPTER = 1;
    public static final int PAYCODE_CHAPTER_COUNT = 9;
    
    public static final int PAYTYPE_MONTH = 8;
    public static final int PAYTYPE_CHAPTER_S_C = 0;
    public static final int PAYTYPE_CHAPTER_S_B = 1;    
    public static final int PAYTYPE_CHAPTER_L_C = 2;
    public static final int PAYTYPE_CHAPTER_L_B = 3;
    public static final int PAYTYPE_BOOK_S_C = 4;
    public static final int PAYTYPE_BOOK_S_B = 5;    
    public static final int PAYTYPE_BOOK_L_C = 6;
    public static final int PAYTYPE_BOOK_L_B = 7;
    
    public static final String PAYSTR_MONTH = "000001";
    public static final String PAYSTR_BOOK = "000002";
    public static final String PAYSTR_BOOK_BEST = "000003";
    public static final String PAYSTR_CHAPTER = "000004";
    public static final String PAYSTR_CHAPTER_BEST = "000005";
    
    private static String payStr = "00";
    private static int typeMul = 1000000;
    private static int[] maskInt= {
        112212, 223121, 313212, 122113, 143212,
        211132, 154322, 132421, 211313, 122223
    };
    
    private static String[] payStrings = {
        "300000425001", "300000425002", "300000425003", "300000425004", 
        "300000425005", "300000425006", "300000425007", "300000425008",
        "300000425009", "300000425010"
    };
    
    private static String[] payStrings2 = {
        "300000425001", "300000425001", "300000425002", 
        "300000425004", "300000425004", 
        "300000425006", "300000425006", 
        "300000425008", "300000425008", 
        "300000425010", "300000425010", 
    };
    
    private static String max_payString = "300000425006";
    
    private static int[] prices = {
        1, 2, 3, 4, 
        5, 6, 7, 8,
        9, 10
    };
    
    public static String getPayCode(int type, int id){
        int code = type*typeMul+id;
        return payStr+code;
    }
    
    public static String getPayStringByPirce(int price){
    	if(price <= 0){
    		return "";
    	}
    	if(price > 10){
    		return max_payString;
    	}
    	
    	return payStrings2[price];
    }
    public static String getPayString(){
        return "300000425001";
    }
    
//    public static String getPayString(int type, com.manyanger.entries.DetailInfo info){
//        if(type == PAYCODE_CHAPTER){
//        	if(info == null){
//        		return payStrings[PAYTYPE_CHAPTER_S_C];
//        	}
//            if(info.isBest()){
//                if(info.isLong()){
//                    return payStrings[PAYTYPE_CHAPTER_L_B];
//                } else {
//                    return payStrings[PAYTYPE_CHAPTER_S_B];
//                }
//            } else {
//                if(info.isLong()){
//                    return payStrings[PAYTYPE_CHAPTER_L_C];
//                } else {
//                    return payStrings[PAYTYPE_CHAPTER_S_C];
//                }
//            }
//        } else if(type == PAYCODE_BOOK){
//        	if(info == null){
//        		return payStrings[PAYTYPE_BOOK_S_C];
//        	}
//            if(info.isBest()){
//                if(info.isLong()){
//                    return payStrings[PAYTYPE_BOOK_L_B];
//                } else {
//                    return payStrings[PAYTYPE_BOOK_S_B];
//                }
//            } else {
//                if(info.isLong()){
//                    return payStrings[PAYTYPE_BOOK_L_C];
//                } else {
//                    return payStrings[PAYTYPE_BOOK_S_C];
//                }
//            }
//        } else if(type == PAYCODE_MONTH) {
//            return payStrings[PAYTYPE_MONTH];
//        }
//        return payStrings[PAYTYPE_CHAPTER_S_C];
//        
//    }
    
//    public static int getPrice(int type, com.manyanger.entries.DetailInfo info){
//        if(type == PAYCODE_CHAPTER){
//        	if(info == null){
//        		return prices[PAYTYPE_CHAPTER_S_C];
//        	}
//            if(info.isBest()){
//                if(info.isLong()){
//                    return prices[PAYTYPE_CHAPTER_L_B];
//                } else {
//                    return prices[PAYTYPE_CHAPTER_S_B];
//                }
//            } else {
//                if(info.isLong()){
//                    return prices[PAYTYPE_CHAPTER_L_C];
//                } else {
//                    return prices[PAYTYPE_CHAPTER_S_C];
//                }
//            }
//        } else if(type == PAYCODE_BOOK){
//        	if(info == null){
//        		return prices[PAYTYPE_BOOK_S_C];
//        	}
//            if(info.isBest()){
//                if(info.isLong()){
//                    return prices[PAYTYPE_BOOK_L_B];
//                } else {
//                    return prices[PAYTYPE_BOOK_S_B];
//                }
//            } else {
//                if(info.isLong()){
//                    return prices[PAYTYPE_BOOK_L_C];
//                } else {
//                    return prices[PAYTYPE_BOOK_S_C];
//                }
//            }
//        } else if(type == PAYCODE_MONTH) {
//            return prices[PAYTYPE_MONTH];
//        }
//        return prices[PAYTYPE_CHAPTER_S_C];
//        
//    }
//    
    private static String getMaskPayCode(String payCode){
        int temp;
        if(payCode == null){
            return null;
        }
        try{
            temp = Integer.parseInt(payCode);
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
        int index = temp % 10;
        temp = temp + maskInt[index];
        String code = String.valueOf(temp);
        
        return code;
    }
    
    public static boolean isPayed(Context context, String payCode){
        String code = getMaskPayCode(payCode);
        if(code == null || context == null){
            return false;
        }
        String lastValue = payCode.substring(payCode.length()-1);
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", 0);
        String paycode = sharedPreferences.getString(code, "-1");
        return paycode.equals(lastValue); 
    }
    
    public static boolean isAllChaptersPayed(Context context, int id, int chapters){
    	String payCode = getPayCode(PAYCODE_CHAPTER_COUNT, id);
        String code = getMaskPayCode(payCode);
        if(code == null || context == null || chapters <= 0){
            return false;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", 0);
        int count = sharedPreferences.getInt(code, 0);
        return count >= chapters; 
    }
    
    public static int getPayedChapterCount(Context context, int id){
    	String payCode = getPayCode(PAYCODE_CHAPTER_COUNT, id);
        String code = getMaskPayCode(payCode);
        if(code == null || context == null){
            return 0;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", 0);
        int count = sharedPreferences.getInt(code, 0);
        
        return count;
    }
    
    public static boolean savePayed(Context context, String payCode){
        String code = getMaskPayCode(payCode);
        if(code == null || context == null){
            return false;
        }
        String lastValue = payCode.substring(payCode.length()-1);
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", 0);
        android.content.SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(code, lastValue);
        edit.commit();
        return true;
    }
    
    public static void saveChapterPayed(Context context, int id){
    	String payCode = getPayCode(PAYCODE_CHAPTER_COUNT, id);
        String code = getMaskPayCode(payCode);
        if(code == null || context == null){
            return;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", 0);
        int count = sharedPreferences.getInt(code, 0);
        
        android.content.SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(code, count+1);
        edit.commit();
    }
    
    @SuppressWarnings("deprecation")
    public static boolean isMonthPay(Context context){
        Date date = new Date(System.currentTimeMillis());
        int id = date.getYear()*100+date.getMonth();
        String paycode = getPayCode(PAYCODE_MONTH, id);
        return isPayed(context, paycode);
    }

}
