package no.stockwallet.Fragments.HomeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import no.stockwallet.Fragments.Wrappers.HomeFragmentsWrapper;
import no.stockwallet.Handlers.API_InvestmentDataHandler;
import no.stockwallet.R;

public class TotalValuesFragment extends Fragment {

    public TotalValuesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_total, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fillWithData(view);
    }

    private void fillWithData(View view) {
        HomeFragmentsWrapper parent = (HomeFragmentsWrapper) getParentFragment();
        API_InvestmentDataHandler API = new API_InvestmentDataHandler(parent.getViewModel());

        TextView totalInvestedNOKTextView = view.findViewById(R.id.Total_Invested);
        int markedValue = API.getTotalMarkedValue();
        totalInvestedNOKTextView.setText(String.valueOf(markedValue) + " kr");

        TextView totalInvestedPercentEarned = view.findViewById(R.id.Prcent_change_earnings_period);
        double totalEarnedPercent = API.getTotalPercentEarnings();
        totalInvestedPercentEarned.setText(String.valueOf(totalEarnedPercent)+ " %");
    }
}
