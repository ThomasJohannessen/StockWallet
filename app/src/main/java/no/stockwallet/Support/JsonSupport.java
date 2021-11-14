package no.stockwallet.Support;

import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class JsonSupport {
    public static JsonSupport instance;

    public static JsonSupport getInstance() {
        if(instance == null)
            instance = new JsonSupport();
        return instance;
    }

    public ArrayList<Pair<String, String>> jsonToPairArray(String json) throws Exception {
        ArrayList<Pair<String, String>> pairs = new ArrayList<>();
        try {
            Map map = new Gson().fromJson(json, Map.class);
            Log.d("JSON", map.keySet().toString());
            Log.d("JSON", map.get("bestMatches").getClass().getSimpleName());
            ArrayList<LinkedTreeMap<String, String>> stockList = (ArrayList) map.get("bestMatches");
            Log.d("JSON", stockList.get(0).toString());
            Log.d("JSON", stockList.get(0).getClass().getSimpleName());
            for(LinkedTreeMap<String, String> stock : stockList) {
                String first = stock.get("1. symbol");
                String second = stock.get("2. name");
                Pair<String, String> pair = new Pair<>(first, second);
                pairs.add(pair);
            }
            Log.d("JSON", Arrays.toString(pairs.toArray()));
        }
        catch(Exception e) {
            throw new Exception();
        }
        return pairs;
    }

}
