package no.stockwallet.Adapters;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import no.stockwallet.Investment;
import no.stockwallet.R;

public class SearchResultRecyclerAdapter extends RecyclerView.Adapter<SearchResultRecyclerAdapter.ViewHolder>{
    private ArrayList<Investment> data;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dummy1;
        private TextView dummy2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dummy1 = itemView.findViewById(R.id.dummyName);
            dummy2 = itemView.findViewById(R.id.dummyValue);
        }

        public TextView getDummy1() {return dummy1;}
        public TextView getDummy2() {return dummy2;}
    }

    public SearchResultRecyclerAdapter() {
        initDummyData();
    }

    private void initDummyData() {
        data = new ArrayList<>();

        data.add(new Investment("NHY.OL",200,60,"NOK",1));
        data.add(new Investment("AKH.OL",100,30,"NOK",10));
        data.add(new Investment("NOD.OL",10,250,"NOK",100));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Investment object = data.get(position);
        holder.getDummy1().setText(object.getTicker());
        holder.getDummy2().setText(String.valueOf(object.getPrice()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
