package no.stockwallet;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

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

    public static void readDB() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference db = FirebaseFirestore.getInstance().collection("StockWallet").document(user.getUid());
        db.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot ss = task.getResult();

                Log.d("JSONTHREE", ss.get("stocks").toString());
                HashMap<String, Investment> map = (HashMap<String, Investment>) ss.get("stocks");
                Log.d("JSONFOUR", map.toString());
            }
        });
    }

}
