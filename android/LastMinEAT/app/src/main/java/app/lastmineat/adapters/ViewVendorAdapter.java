package app.lastmineat.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import app.lastmineat.R;
import app.lastmineat.models.Deal;
import app.lastmineat.models.Vendor;

/**
 * Created by andrew.lim.2013 on 11/3/2017.
 */

public class ViewVendorAdapter extends RecyclerView.Adapter<ViewVendorAdapter.ViewHolder> {
    private final Context context;
    private final List<Deal> dealList;
    private static ViewVendorAdapter.ClickListener clickListener;

    public ViewVendorAdapter(Context context, List<Deal> dealList) {
        this.context = context;
        this.dealList = dealList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dealVendor;
        TextView dealDescription;
        TextView dealInitialPrice;
        TextView dealFinalPrice;
        TextView dealRequired;
        TextView dealPercentage;
        TextView dealTime;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            dealVendor = (TextView) view.findViewById(R.id.deal_vendor);
            dealDescription = (TextView) view.findViewById(R.id.deal_description);
            dealInitialPrice = (TextView) view.findViewById(R.id.deal_initial_price);
            dealFinalPrice = (TextView) view.findViewById(R.id.deal_final_price);
            dealRequired = (TextView) view.findViewById(R.id.deal_required);
            dealPercentage = (TextView) view.findViewById(R.id.deal_percentage);
            dealTime = (TextView) view.findViewById(R.id.deal_time);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ViewVendorAdapter.ClickListener clickListener) {
        ViewVendorAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    @Override
    public ViewVendorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deal, parent, false);
        ViewVendorAdapter.ViewHolder holder = new ViewVendorAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewVendorAdapter.ViewHolder holder, int position) {
        Deal deal = dealList.get(position);
        Vendor vendor = deal.getVendor();

        holder.dealVendor.setText(vendor.getName());
        holder.dealDescription.setText(deal.getDescription());
        holder.dealInitialPrice.setText("$" + deal.getInitialPrice());
        holder.dealInitialPrice.setPaintFlags(holder.dealInitialPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.dealFinalPrice.setText("$" + deal.getInitialPrice() * (100 - deal.getPercentage()) / 100.0);
        holder.dealRequired.setText(deal.getRequiredUsers() + "");
        holder.dealPercentage.setText(deal.getPercentage() + "%");

        if (deal.getPercentage() <= 25) {
            holder.dealPercentage.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRange1));
        } else if (deal.getPercentage() <= 50) {
            holder.dealPercentage.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRange2));
        } else {
            holder.dealPercentage.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRange3));
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date today = new Date();
            Date end = format.parse(deal.getEndDateTime());
            long difference = (end.getTime() - today.getTime()) / (1000 * 60 * 60);
            holder.dealTime.setText(difference + "h left");
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return dealList.size();
    }

    public Deal getItem(int position) {
        //returns the number of elements the RecyclerView will display
        return dealList.get(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Deal deal) {
        dealList.add(position, deal);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Deal deal) {
        int position = dealList.indexOf(deal);
        dealList.remove(position);
        notifyItemRemoved(position);
    }

    // Clean all elements of the recycler
    public void clear() {
        dealList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Deal> list) {
        dealList.addAll(list);
        notifyDataSetChanged();
    }
}
