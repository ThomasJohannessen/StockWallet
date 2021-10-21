package no.stockwallet;

import no.stockwallet.Fragments.OverviewFragments.StockRecyclerAdapter;

public class BinderSingleton {
    private static BinderSingleton instance;
    private StockViewModel viewModel;
    private StockRecyclerAdapter adapter;

    public static BinderSingleton getInstance() {
        if(instance == null){
            synchronized (BinderSingleton.class) {
                if(instance == null)
                    instance = new BinderSingleton();
            }
        }
        return instance;
    }

    public void setAdapter(StockRecyclerAdapter adapterReference) {
        adapter = adapterReference;
    }
    public void setViewModel(StockViewModel viewModelReference) {
        viewModel = viewModelReference;
    }

    public StockRecyclerAdapter getAdapter() {
        return adapter;
    }
    public StockViewModel getViewModel() {
        return viewModel;
    }
}
