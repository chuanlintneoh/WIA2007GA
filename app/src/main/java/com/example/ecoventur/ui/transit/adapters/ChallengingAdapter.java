package com.example.ecoventur.ui.transit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecoventur.ui.transit.model.Challenging;
import com.makeramen.roundedimageview.RoundedImageView;
import com.example.ecoventur.R; // Adjust the package name as per your project structure

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChallengingAdapter extends RecyclerView.Adapter<ChallengingAdapter.ViewHolder> {

    private final Context context;
    private List<Challenging> challengingList;

    public ChallengingAdapter(Context context, List<Challenging> challengingList) {
        this.context = context;
        this.challengingList = challengingList;
    }

    public void setChallengingList(List<Challenging> challengingList) {
        this.challengingList = challengingList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.challenging_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Challenging challenging = challengingList.get(position);

        // Set data to the ViewHolder
        holder.challengingTitleTextView.setText(challenging.getTitle());

        //Format and set end dates
        String formattedDate = "Ends in " + formatDate(challenging.getEndDate());
        holder.challengingEndDateTextView.setText(formattedDate);

        // You might want to load the image using Glide or another library
        // Load the image from the URL into the ImageView using Glide
        Glide.with(context).load(challenging.getImageUrl()).into(holder.challengingImageView);
    }

    // Add this method to set up horizontal scrolling
    public void setHorizontalLayoutManager(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public int getItemCount() {
        return challengingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView challengingImageView;
        TextView challengingTitleTextView;
        TextView challengingEndDateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            challengingImageView = itemView.findViewById(R.id.challenging_img);
            challengingTitleTextView = itemView.findViewById(R.id.challenging_name);
            challengingEndDateTextView = itemView.findViewById(R.id.challenging_end_date);
        }
    }

    // Helper method to format date
    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }
}
