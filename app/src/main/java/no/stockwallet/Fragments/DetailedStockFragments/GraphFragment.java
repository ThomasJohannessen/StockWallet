package no.stockwallet.Fragments.DetailedStockFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import no.stockwallet.Fragments.Wrappers.DetailStockFragmentsWrapper;
import no.stockwallet.R;
import no.stockwallet.Support.JsonSupport;
import no.stockwallet.ViewModels.StockViewModel;
import yahoofinance.Stock;

public class GraphFragment extends Fragment {

    private RequestQueue queue;
    private String QUEUE_TAG = "DETAILEDGRAPHAPI";
    private View view;
    private StockViewModel viewModel;
    TextView errMsg1;
    ArrayList<Double> historyValues = new ArrayList<>();

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

        Bundle parentArguments = getParentFragment().getArguments();
        HashMap<String, Stock> map = (HashMap<String, Stock>) parentArguments.getSerializable("hashmap");
        Stock stock = map.get("Stock");
        queue = Volley.newRequestQueue(getContext());

        if (!stock.getSymbol().equals("")) {
            String baseURL = "https://www.alphavantage.co/query?function=TIME_SERIES_WEEKLY_ADJUSTED&outputsize=compact&apikey=04C7U8DGXKH0OY8B&symbol=";
            String url = baseURL + stock.getSymbol();
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    if (response.equals("{}")) {
                        errMsg1 = view.findViewById(R.id.errorGraph);
                        errMsg1.setVisibility(View.VISIBLE);
                        Snackbar.make(view, "Denne aksjen har dessverre ikke støtte for historisk visning", Snackbar.LENGTH_LONG).show();
                    }else{
                        try {
                            historyValues = JsonSupport.getInstance().jsonToPairArrayGraph(response);
                        } catch (Exception e) {
                            Snackbar.make(view, "Error in response from server, try again later", Snackbar.LENGTH_SHORT).show();
                        }

                        GraphView graph = (GraphView) view.findViewById(R.id.graphDetailed);
                        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
                        graph.setVisibility(View.VISIBLE);

                        graph.addSeries(makeDataPoints());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("RESPONSE", error.toString());
                    Snackbar.make(view, "Something went wrong, check your internet connection", Snackbar.LENGTH_SHORT).show();
                }
            });
            request.setTag(QUEUE_TAG);
            queue.add(request);
        }
    }


    private LineGraphSeries<DataPoint> makeDataPoints(){

        DataPoint[] dataPoints = new DataPoint[historyValues.size()];

        for(int i = historyValues.size()-1; i >= 0; i--){
            Double temp = historyValues.get(i);
            dataPoints[i] = new DataPoint(i, temp);
        }
        return new LineGraphSeries<DataPoint>(dataPoints);
    }

    private void requestHistorydata(String keyword) {
        if (keyword.equals("") == false) {
            new Thread(() -> {
                String baseURL = "https://www.alphavantage.co/query?function=TIME_SERIES_WEEKLY_ADJUSTED&outputsize=compact&apikey=04C7U8DGXKH0OY8B&symbol=";
                String url = baseURL + keyword;
                Log.d("ARESPONSE123", "starter henting");
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("RESPONSE", response);
                            historyValues = JsonSupport.getInstance().jsonToPairArrayGraph(response);
                            Log.d("ARESPONSE123", "ferig hentet");
                            Log.d("ARESPONSE123return", String.valueOf(historyValues));


                        } catch (Exception e) {
                            Snackbar.make(view, "Error in response from server, try again later", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ARESPONSE", error.toString());
                        Snackbar.make(view, "Something went wrong, check your internet connection", Snackbar.LENGTH_SHORT).show();
                    }
                });

                request.setTag(QUEUE_TAG);
                queue.add(request);

            }).start();
        }
    }
}

