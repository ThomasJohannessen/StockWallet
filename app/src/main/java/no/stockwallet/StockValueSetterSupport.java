package no.stockwallet;

import java.math.BigDecimal;
import java.util.Map;


public class StockValueSetterSupport {
    private static BigDecimal returnValue;
    private static Map<String, BigDecimal> returnValueMultiple;


    public static void setReturnValue(BigDecimal value) {
        returnValue = value;
        //Log.d("Stock-SupClass", String.valueOf(value));
    }

    public static void setReturnValueMultiple(Map<String, BigDecimal> value) {
        returnValueMultiple = value;
        //Log.d("Stock-SupClass", String.valueOf(value));
    }

    public static BigDecimal getReturnValue() {
        //Log.d("Stock-SupClassReturn", String.valueOf(returnValue));
        return returnValue;
    }

    public static Map<String, BigDecimal> getReturnValueMultiple() {
        //Log.d("Stock-SupClassReturn", String.valueOf(returnValue));
        return returnValueMultiple;
    }

}



