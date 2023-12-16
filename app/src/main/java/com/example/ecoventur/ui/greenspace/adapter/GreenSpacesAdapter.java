package com.example.ecoventur.ui.greenspace.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoventur.R;
import com.example.ecoventur.ui.greenspace.GreenSpace;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GreenSpacesAdapter extends RecyclerView.Adapter<GreenSpacesAdapter.GreenSpaceViewHolder> {
    private ArrayList<GreenSpace> greenSpaces;
    public GreenSpacesAdapter(ArrayList<GreenSpace> greenSpaces) {
        this.greenSpaces = greenSpaces;
    }
    @NonNull
    @Override
    public GreenSpaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_green_space, parent, false);
        return new GreenSpaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GreenSpaceViewHolder holder, int position) {
        GreenSpace currentSpace = greenSpaces.get(position);
        holder.bind(currentSpace);
    }

    @Override
    public int getItemCount() {
        return greenSpaces.size();
    }

    static class GreenSpaceViewHolder extends RecyclerView.ViewHolder {
        ImageView IVGreenSpace;
        TextView TVGreenSpaceName, TVApproxDist, TVRating;
        public GreenSpaceViewHolder(@NonNull View itemView) {
            super(itemView);
            IVGreenSpace = itemView.findViewById(R.id.IVGreenSpace);
            TVGreenSpaceName = itemView.findViewById(R.id.TVGreenSpaceName);
            TVApproxDist = itemView.findViewById(R.id.TVApproxDist);
            TVRating = itemView.findViewById(R.id.TVRating);
        }
        public void bind(GreenSpace space) {
//            Picasso.get().load(space.getImage()).into(IVGreenSpace);
            TVGreenSpaceName.setText(space.getName());
            TVApproxDist.setText(String.format("Approx. %.1f km", space.getApproxDistance()));
            TVRating.setText(String.valueOf(space.getRating()));
        }
    }
}
