package no.stockwallet.Fragments.Wrappers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import no.stockwallet.Login.LoginActivity;
import no.stockwallet.R;
import no.stockwallet.Support.FireBaseJsonSupport;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        hideKeyboard();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    private void fetchViewModel() {
        DocumentReference db = FirebaseFirestore.getInstance().collection("StockWallet").document(user.getUid());
        db.get().addOnCompleteListener(task -> {
           if(task.isSuccessful()) {
               DocumentSnapshot ss = task.getResult();

               FireBaseJsonSupport.readDB(viewModel,ss);
               FireBaseJsonSupport.readHistoryArrayFromDB(viewModel, ss);

               Navigation.findNavController(requireActivity(), R.id.loadingLayout).navigate(R.id.homeFragmentsWrapper);
        }});
    }
    private void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}