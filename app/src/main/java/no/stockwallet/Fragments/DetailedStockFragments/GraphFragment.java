package no.stockwallet.Fragments.DetailedStockFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import no.stockwallet.Adapters.SearchResultRecyclerAdapter;
import no.stockwallet.Fragments.Wrappers.DetailStockFragmentsWrapper;
import no.stockwallet.Fragments.Wrappers.HomeFragmentsWrapper;
import no.stockwallet.R;
import no.stockwallet.Support.JsonSupport;
import no.stockwallet.ViewModels.StockViewModel;
import yahoofinance.Stock;

public class GraphFragment extends Fragment {

    private RequestQueue queue;
    private String QUEUE_TAG = "DETAILEDGRAPHAPI";
    private View view;
    private StockViewModel viewModel;
    ArrayList<Double> historyValues = new ArrayList<>();
    LineGraphSeries<DataPoint> series;

    public GraphFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DetailStockFragmentsWrapper parent = (DetailStockFragmentsWrapper) getParentFragment();
        viewModel = parent.getViewModel();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph2, container, false);
        this.view = view;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();

        GraphView graph = (GraphView) view.findViewById(R.id.graphDetailed);

        Bundle parentArguments = getParentFragment().getArguments();
        HashMap<String, Stock> map = (HashMap<String, Stock>) parentArguments.getSerializable("hashmap");
        Stock stock = map.get("Stock");
        queue = Volley.newRequestQueue(getContext());
        requestHistorydata("stock.getSymbol()", view);


        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setGridStyle( GridLabelRenderer.GridStyle.HORIZONTAL );
        graph.setVisibility(View.VISIBLE);

        DataPoint[] dataPoints = new DataPoint[historyValues.size()];

        for (int i=0; i<historyValues.size();i++){
            Double temp = historyValues.get(i);
            dataPoints[i] = new DataPoint(i, temp);
        }

        series = new LineGraphSeries<DataPoint>(dataPoints);

        graph.addSeries(series);


    }


    private void requestHistorydata(String keyword, View view) {
        if (keyword.equals("") == false) {
            new Thread(() -> {
                String baseURL = "https://www.alphavantage.co/query?function=TIME_SERIES_WEEKLY_ADJUSTED&outputsize=compact&apikey=04C7U8DGXKH0OY8B&symbol=";
                String url = baseURL + keyword;
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("RESPONSE", response);
                            historyValues = JsonSupport.getInstance().jsonToPairArrayGraph(response);
                            Log.d("ARESPONSE123return", String.valueOf(historyValues));

                        } catch (Exception e) {
                            Snackbar.make(view, "Error in response from server, try again later", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d("ARESPONSE", error.toString());
                        Snackbar.make(view, "Something went wrong, check your internet connection", Snackbar.LENGTH_SHORT).show();
                    }
                });

                request.setTag(QUEUE_TAG);
                queue.add(request);

            }).start();
        }
    }
}

