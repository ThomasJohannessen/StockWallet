package no.stockwallet.Fragments.Wrappers;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import no.stockwallet.Investment;
import no.stockwallet.LoginActivity;
import no.stockwallet.R;
import no.stockwallet.StockViewModel;
import yahoofinance.Stock;

public class LoadingFragment extends Fragment {
    StockViewModel viewModel;
    FirebaseUser user;

    public LoadingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(StockViewModel.class);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            Intent loginIntent = new Intent(getContext(), LoginActivity.class);
            startActivity(loginIntent);
        }
        else {
            fetchViewModel();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void fetchViewModel() {
        DocumentReference db = FirebaseFirestore.getInstance().collection("StockWallet").document(user.getUid());
        db.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot ss = task.getResult();

                HashMap<String, Investment> map = new HashMap<>();
                Map<String, String> temp = (Map<String, String>) ss.getData().get("stocks");
                if(temp != null) {
                    for(Object obj : Arrays.asList(temp).get(0).values().toArray()) {
                        Investment inv = new Gson().fromJson(obj.toString(), Investment.class);
                        map.put(inv.getTicker(), inv);
                    }
                    viewModel.setStockMap(map);
                }
                else {
                    viewModel.setStockMap(new HashMap<>());
                }

                Log.d("ViewModel", "SWITCHING TO HOMEFRAGMENT");
                Navigation.findNavController(requireActivity(), R.id.loadingLayout).navigate(R.id.homeFragmentsWrapper);
            }
        });
    }
}