package no.stockwallet.Support;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import no.stockwallet.Handlers.API_InvestmentDataHandler;
import no.stockwallet.Model.Investment;
import no.stockwallet.ViewModels.StockViewModel;

public class FireBaseJsonSupport {

    public static void readDB(StockViewModel model, DocumentSnapshot ss) {
        Map<String, Object> map = ss.getData();

        HashMap<String, Investment> tempHash = new HashMap<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().equals("stocks")) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Investment> objectMap = mapper.convertValue(entry.getValue(), Map.class);

                for(Object inv : objectMap.values()) {
                    Investment investment = mapper.convertValue(inv, Investment.class);
                    tempHash.put(investment.getTicker(), investment);
                }
            }
        }
        model.setStockMap(tempHash);
    }

    public static <T, G> void writeDB(HashMap<T, G> map) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference db = FirebaseFirestore.getInstance().collection("StockWallet").document(user.getUid());
        db.update("stocks", map);
    }


    public static void readHistoryArrayFromDB(StockViewModel model,DocumentSnapshot ss) {
        ArrayList<Long> temp = new ArrayList<>();

        temp = (ArrayList<Long>) ss.getData().get("totalInvestedHistoryArray");

        //placeholder data for history view on graph. This is only to show some data in the graph for the exam
        if (temp == null) {
            API_InvestmentDataHandler calc = new API_InvestmentDataHandler(model);
            int totValue = calc.getTotalMarkedValue();

            if (totValue < 1000){
                totValue = 1000;
            }

            model.getHistoryArrayInvestmentTotalValueForGraph().add((long) 0);
            model.getHistoryArrayInvestmentTotalValueForGraph().add((long) (totValue * 0.1));
            model.getHistoryArrayInvestmentTotalValueForGraph().add((long) (totValue * 0.4));
            model.getHistoryArrayInvestmentTotalValueForGraph().add((long) (totValue * 0.2));
            model.getHistoryArrayInvestmentTotalValueForGraph().add((long) (totValue * 0.7));
            model.getHistoryArrayInvestmentTotalValueForGraph().add((long) (totValue * (-0.15)));
            model.getHistoryArrayInvestmentTotalValueForGraph().add((long) (totValue * 0.8));
            model.getHistoryArrayInvestmentTotalValueForGraph().add((long) (totValue * 0.6));
            model.getHistoryArrayInvestmentTotalValueForGraph().add((long) (totValue * 0.9));
            model.getHistoryArrayInvestmentTotalValueForGraph().add((long) (totValue * 0.8));
            model.getHistoryArrayInvestmentTotalValueForGraph().add((long) totValue);
            writeHistoryArrayToDB(model.getHistoryArrayInvestmentTotalValueForGraph());
            temp = model.getHistoryArrayInvestmentTotalValueForGraph();
        }

        if (model.getHistoryArrayInvestmentTotalValueForGraph().size() > temp.size()){
            writeHistoryArrayToDB(model.getHistoryArrayInvestmentTotalValueForGraph());
        }
        else{
            model.setHistoryArrayInvestmentTotalValueForGraph(temp);
        }
    }

    public static void writeHistoryArrayToDB(ArrayList<Long> array) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference db = FirebaseFirestore.getInstance().collection("StockWallet").document(user.getUid());
        db.update("totalInvestedHistoryArray", array);
    }
}
