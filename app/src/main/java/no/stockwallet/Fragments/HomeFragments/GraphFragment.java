package no.stockwallet.Fragments.HomeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import no.stockwallet.Fragments.Wrappers.HomeFragmentsWrapper;
import no.stockwallet.R;
import no.stockwallet.ViewModels.StockViewModel;

public class GraphFragment extends Fragment {

    private View view;
    private StockViewModel viewModel;
    LineGraphSeries<DataPoint> series;

    public GraphFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        HomeFragmentsWrapper parent = (HomeFragmentsWrapper) getParentFragment();
        viewModel = parent.getViewModel();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        this.view = view;
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();

        GraphView graph = (GraphView) view.findViewById(R.id.graph);

        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setGridStyle( GridLabelRenderer.GridStyle.HORIZONTAL );
        graph.setVisibility(View.VISIBLE);

        ArrayList<Long> historyData = viewModel.getHistoryArrayInvestmentTotalValueForGraph();

        DataPoint[] dataPoints = new DataPoint[historyData.size()];

        for (int i=0; i<historyData.size();i++){
            long temp = historyData.get(i);
            dataPoints[i] = new DataPoint(i, temp);
        }

        series = new LineGraphSeries<DataPoint>(dataPoints);

        graph.addSeries(series);


    }
}