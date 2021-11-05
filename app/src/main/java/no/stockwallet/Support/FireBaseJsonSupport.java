package no.stockwallet.Support;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import no.stockwallet.Model.Investment;
import no.stockwallet.ViewModels.StockViewModel;

public class FireBaseJsonSupport {

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

    public static <T, G> void writeDB(HashMap<T, G> map) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference db = FirebaseFirestore.getInstance().collection("StockWallet").document(user.getUid());
        db.update("stocks", map);
    }
}
