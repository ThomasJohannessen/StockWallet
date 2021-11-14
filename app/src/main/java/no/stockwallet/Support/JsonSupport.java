package no.stockwallet.Support;

import android.util.Pair;

public class JsonSupport {
    public static JsonSupport instance;

    public static JsonSupport getInstance() {
        if(instance == null)
            instance = new JsonSupport();
        return instance;
    }

    public static Pair<String, String> jsonToPair(String json) {

    }

}
