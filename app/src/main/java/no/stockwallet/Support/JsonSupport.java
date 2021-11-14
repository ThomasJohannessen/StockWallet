package no.stockwallet.Support;

import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class JsonSupport {
    public static JsonSupport instance;

    public static JsonSupport getInstance() {
        if(instance == null)
            instance = new JsonSupport();
        return instance;
    }

    public ArrayList<Pair<String, String>> jsonToPairArray(String json) {
        Map map = new Gson().fromJson(json, Map.class);
        Log.d("JSON", map.keySet().toString());
        Log.d("JSON", map.get("bestMatches").getClass().getSimpleName());
        return null;
    }

}
