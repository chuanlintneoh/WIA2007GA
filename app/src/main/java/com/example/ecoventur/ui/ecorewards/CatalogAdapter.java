//package com.example.ecoventur.ui.ecorewards;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.ecoventur.R;
//import com.google.firebase.database.DatabaseReference;
//
//import java.util.ArrayList;
//
//public class catalogAdapter extends RecyclerView.Adapter<catalogAdapter.ViewHolder> {
//    private Context context;
//    private ArrayList<Catalog> catalogArrayList;
//    private DatabaseReference vouchersRef; // Declare DatabaseReference
//    private OnItemClickListener mListener; // Declare listener
//
//    public interface OnItemClickListener {
//        void onItemClick(int position);
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        mListener = listener;
//    }
//
//    public catalogAdapter(Context context, ArrayList<Catalog> catalogArrayList, DatabaseReference vouchersRef) {
//        this.context = context;
//        this.catalogArrayList = catalogArrayList;
//        this.vouchersRef = vouchersRef; // Assign DatabaseReference
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog, parent, false);
//        return new ViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Catalog catalog = catalogArrayList.get(position);
//        holder.vouchertitle.setText(catalog.vouchertitle);
//        holder.villagegrocer.setImageResource(catalog.voucherimage);
//        holder.coinbag.setImageResource(catalog.coinbag);
//        holder.vouchervalue.setText(catalog.vouchervalue);
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int clickedPosition = holder.getAdapterPosition();
//                if (clickedPosition != RecyclerView.NO_POSITION && mListener != null) {
//                    mListener.onItemClick(clickedPosition);
//                }
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return catalogArrayList.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView villagegrocer;
//        TextView vouchertitle;
//        ImageView coinbag;
//        TextView vouchervalue;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            villagegrocer = itemView.findViewById(R.id.villagegrocer);
//            vouchertitle = itemView.findViewById(R.id.vouchertitle);
//            coinbag = itemView.findViewById(R.id.ecocoin);
//            vouchervalue = itemView.findViewById(R.id.vouchervalue);
//        }
//    }
//}

package com.example.ecoventur.ui.ecorewards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoventur.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

// Your imports...

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Catalog> catalogList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public CatalogAdapter(Context context, ArrayList<Catalog> catalogList) {
        this.context = context;
        this.catalogList = catalogList;
    }

    public void updateList(ArrayList<Catalog> newList) {
        catalogList.clear();
        catalogList.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Catalog catalog = catalogList.get(position);
        holder.voucherTitle.setText(catalog.getVoucherTitle());
        holder.ecoCoins.setText(String.valueOf(catalog.getEcoCoins()));
        Picasso.get().load(catalog.getImgURL1()).into(holder.img1);
        Picasso.get().load(catalog.getImgURL2()).into(holder.img2);

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return catalogList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView voucherTitle;
        TextView ecoCoins;
        ImageView img1;
        ImageView img2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            voucherTitle = itemView.findViewById(R.id.vouchertitle);
            ecoCoins = itemView.findViewById(R.id.vouchervalue);
            img1 = itemView.findViewById(R.id.villagegrocer);
            img2 = itemView.findViewById(R.id.coinbag);
        }
    }
}
