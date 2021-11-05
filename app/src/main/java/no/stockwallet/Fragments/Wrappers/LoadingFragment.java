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

import com.fasterxml.jackson.databind.ObjectMapper;
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

import no.stockwallet.Model.Investment;
import no.stockwallet.Login.LoginActivity;
import no.stockwallet.R;
import no.stockwallet.ViewModels.StockViewModel;

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
           if(task.isSuccessful()) {
               DocumentSnapshot ss = task.getResult();
               Map<String, Object> map = ss.getData();
               for (Map.Entry<String, Object> entry : map.entrySet()) {
                   if (entry.getKey().equals("stocks")) {
                       ObjectMapper mapper = new ObjectMapper();
                       Map<String, Investment> objectMap = mapper.convertValue(entry.getValue(), Map.class);

                       HashMap<String, Investment> temp = new HashMap<>();
                       for(Object inv : objectMap.values()) {
                           Investment investment = mapper.convertValue(inv, Investment.class);
                           temp.put(investment.getTicker(), investment);
                       }
                       viewModel.setStockMap(temp);
                       Navigation.findNavController(requireActivity(), R.id.loadingLayout).navigate(R.id.homeFragmentsWrapper);
                   }
           }
        }});
    }
}