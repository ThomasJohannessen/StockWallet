package no.stockwallet.Support;

import android.util.Log;

public class ValueSetterSupport {
    double returnValue, checkValue;

    public void setReturnValue(Double value) {
        this.returnValue = value;
        this.checkValue = value;
    }

    public double getReturnValue() {
        double temp = returnValue;
        returnValue = 0;
        checkValue = 0;
        return temp;
    }
    public double getCheckValue() {
        return checkValue;
    }
}
