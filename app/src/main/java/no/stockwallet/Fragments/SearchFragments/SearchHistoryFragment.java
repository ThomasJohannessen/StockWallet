package no.stockwallet.Fragments.SearchFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crazzyghost.alphavantage.AlphaVantage;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import no.stockwallet.Adapters.SearchResultRecyclerAdapter;
import no.stockwallet.R;
import no.stockwallet.Support.JsonSupport;

public class SearchHistoryFragment extends Fragment {
    private SearchResultRecyclerAdapter adapter;
    private RequestQueue queue;
    private String QUEUE_TAG = "SEARCHAPI";

    public SearchHistoryFragment() {
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
        return inflater.inflate(R.layout.fragment_search_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView(view);
        setUpSearchClickListener(view);
    }

    private void setUpRecyclerView(View view) {
        RecyclerView searchRecycler = view.findViewById(R.id.searchRecyclerView);
        adapter = new SearchResultRecyclerAdapter();
        searchRecycler.setAdapter(adapter);
        searchRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void setUpSearchClickListener(View view) {
        SearchView searchView = (SearchView) view.findViewById(R.id.searchBar);
        view.findViewById(R.id.searchRecyclerView).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                searchView.setIconified(true);
                view.requestFocus();
                hideKeyboard();
                return false;
            }
        });

        setUpQueryTextChangeListener(searchView);
    }

    private void setUpQueryTextChangeListener(SearchView searchView) {
        queue = Volley.newRequestQueue(getContext());
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String keyword) {
                queue.cancelAll(QUEUE_TAG);
                requestAPISearch(keyword, searchView);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String keyword) {
                queue.cancelAll(QUEUE_TAG);
                requestAPISearch(keyword, searchView);
                return false;
            }
        });
    }

    private void requestAPISearch(String keyword, View view) {
        if(keyword.equals("") == false) {
            new Thread(() -> {
                String baseURL = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&apikey=04C7U8DGXKH0OY8B&datatype=json&keywords=";
                String url = baseURL + keyword;
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("RESPONSE", response);
                            ArrayList<Pair<String, String>> stockPairs = JsonSupport.getInstance().jsonToPairArray(response);
                            //adapter.setData(stockPairs);
                            //adapter.notifyDataSetChanged();
                        }
                        catch(Exception e) {
                            Snackbar.make(view, "Error in response from server, try again later", Snackbar.LENGTH_SHORT).show();
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

            }).start();
        }
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