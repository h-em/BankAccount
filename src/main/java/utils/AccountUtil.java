package utils;

import model.CurrencyType;

public class AccountUtil {

    public static boolean isValidId(String id){

        if(id.length() != ApplicationConst.ACCOUNT_NAME_LENGTH) return false;
        if(!id.substring(0,2).equals(ApplicationConst.ACCOUNT_NAME_PREFIX)) return false;
        return true;
    }

    public static boolean isCurrencyType(String currencyType){
        if(currencyType.equalsIgnoreCase("RON") ||
        currencyType.equalsIgnoreCase("EUR") )return true;
        return false;
    }

    public static CurrencyType getCurrencyType(String type){
        if(type.equals(CurrencyType.RON.toString())){
            return CurrencyType.RON;
        }
        if(type.equals(CurrencyType.EUR.toString())){
            return CurrencyType.EUR;
        }
        return CurrencyType.NO_CURRENCY;
    }

    public static boolean isValidCurrentType(String currentType){
        if(currentType.equalsIgnoreCase(CurrencyType.RON.toString())) return true;
        if(currentType.equalsIgnoreCase(CurrencyType.EUR.toString())) return true;
        return false;
    }

    public static boolean isAValidBalance(Integer balance){
        if(balance < 0) return false;
        return true;
    }
}
