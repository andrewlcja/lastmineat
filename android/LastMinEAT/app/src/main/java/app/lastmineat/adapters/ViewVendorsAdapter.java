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
 * Created by andrew.lim.2013 on 10/3/2017.
 */

public class ViewVendorsAdapter extends RecyclerView.Adapter<ViewVendorsAdapter.ViewHolder> {
    private final Context context;
    private final List<Vendor> vendorList;
    private static ClickListener clickListener;

    public ViewVendorsAdapter(Context context, List<Vendor> vendorList) {
        this.context = context;
        this.vendorList = vendorList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView vendorName;
        TextView vendorAddress;
        TextView vendorHours;
        TextView vendorLetter;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            vendorName = (TextView) view.findViewById(R.id.vendor_name);
            vendorAddress = (TextView) view.findViewById(R.id.vendor_address);
            vendorHours = (TextView) view.findViewById(R.id.vendor_hours);
            vendorLetter = (TextView) view.findViewById(R.id.vendor_letter);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        ViewVendorsAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    @Override
    public ViewVendorsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vendor, parent, false);
        ViewVendorsAdapter.ViewHolder holder = new ViewVendorsAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewVendorsAdapter.ViewHolder holder, int position) {
        Vendor vendor = vendorList.get(position);

        holder.vendorName.setText(vendor.getName());
        holder.vendorAddress.setText(vendor.getAddress() + ", " + vendor.getUnitNo() + " S" + vendor.getPostalCode());
        holder.vendorHours.setText(vendor.getStartTime() + " - " + vendor.getEndTime());
        holder.vendorLetter.setText(vendor.getName().charAt(0) + "");
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return vendorList.size();
    }

    public Vendor getItem(int position) {
        //returns the number of elements the RecyclerView will display
        return vendorList.get(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Vendor vendor) {
        vendorList.add(position, vendor);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Vendor vendor) {
        int position = vendorList.indexOf(vendor);
        vendorList.remove(position);
        notifyItemRemoved(position);
    }

    // Clean all elements of the recycler
    public void clear() {
        vendorList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Vendor> list) {
        vendorList.addAll(list);
        notifyDataSetChanged();
    }
}
