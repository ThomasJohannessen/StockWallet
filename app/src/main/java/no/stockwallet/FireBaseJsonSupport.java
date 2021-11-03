package no.stockwallet;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireBaseJsonSupport {

    public static <T, G> String convertToJson(HashMap<T, G> map) {
        return new Gson().toJson(map);
    }

    public static void test(HashMap<String, Investment> map) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference db = FirebaseFirestore.getInstance().collection("StockWallet").document(user.getUid());
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("stocks", map);
        db.update(temp);
    }

    public static void readDB(StockViewModel model) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference db = FirebaseFirestore.getInstance().collection("StockWallet").document(user.getUid());
        db.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot ss = task.getResult();

                HashMap<String, Investment> map = new HashMap<>();
                Map<String, String> temp = (Map<String, String>) ss.getData().get("stocks");

                for(Object obj : Arrays.asList(temp).get(0).values().toArray()) {
                    Investment inv = new Gson().fromJson(obj.toString(), Investment.class);
                    map.put(inv.getTicker(), inv);
                }

                model.setStockMap(map);
            }
        });
    }
}
