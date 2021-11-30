package no.stockwallet.Support;

import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import no.stockwallet.Fragments.DetailedStockFragments.GraphFragment;

public class JsonSupport {
    public static JsonSupport instance;

    public static JsonSupport getInstance() {
        if(instance == null)
            instance = new JsonSupport();
        return instance;
    }

    public ArrayList<Pair<String, String>> jsonToPairArraySearch(String json) throws Exception {
        ArrayList<Pair<String, String>> pairs = new ArrayList<>();
        try {
            Map map = new Gson().fromJson(json, Map.class);
            ArrayList<LinkedTreeMap<String, String>> stockList = (ArrayList) map.get("bestMatches");

            for(LinkedTreeMap<String, String> stock : stockList) {
                String first = stock.get("1. symbol");
                String second = stock.get("2. name");
                Pair<String, String> pair = new Pair<>(first, second);
                pairs.add(pair);
            }
        }
        catch(Exception e) {
            throw new Exception();
        }
        return pairs;
    }

    public ArrayList<Double> jsonToPairArrayGraph(String json) throws Exception {
        ArrayList<Double> historyValues = new ArrayList<>();

        try {
            JSONObject jsonObjectOuter = new JSONObject(json);
            JSONObject jsonObject = jsonObjectOuter.getJSONObject("Monthly Adjusted Time Series");
            Iterator<String> dates = jsonObject.keys();

            while(dates.hasNext()) {
                String date = dates.next();
                JSONObject jsonObjectData = jsonObject.getJSONObject(date);
                String value = jsonObjectData.getString("4. close");
                historyValues.add(Double.parseDouble(value));
            }
        }
        catch(Exception e) {
            throw new Exception();
        }
        return historyValues;
    }

}
