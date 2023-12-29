package com.example.ecoventur.ui.ecorewards;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoventur.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class e003 extends Fragment {
    private E003ViewModel viewModelE003;
    private CollectionReference catalogRef;
    private RecyclerView recyclerView;
    private TextView ecocoinsbalance;
    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter<Catalog, CatalogViewHolder> adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_e003, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModelE003 = new ViewModelProvider(requireActivity()).get(E003ViewModel.class);

        FirebaseApp.initializeApp(requireContext());

        db = FirebaseFirestore.getInstance();
        catalogRef = db.collection("catalogs");

        // Initialize TextView
        ecocoinsbalance = view.findViewById(R.id.ecocoinsbalance);

        // Retrieve ecocoin field from Firestore and set the value in TextView
        DocumentReference userDocRef = db.collection("users").document("uaPJZguefgcNGyl0Ig2sy1Yq6tu1");
        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Long ecocoin = documentSnapshot.getLong("ecocoin");
                if (ecocoin != null) {
                    ecocoinsbalance.setText(String.valueOf(ecocoin) + " ec");
                } else {
                    // Handle the case where ecocoin field is null
                    ecocoinsbalance.setText("N/A");
                }
            } else {
                // Handle the case where the document doesn't exist
                ecocoinsbalance.setText("Document not found");
            }
        }).addOnFailureListener(e -> {
            // Handle any errors while fetching the ecocoin field
            Log.e("e003 Fragment", "Error fetching ecocoin field: " + e.getMessage());
            ecocoinsbalance.setText("Error fetching ecocoin");
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        FirestoreRecyclerOptions<Catalog> options =
                new FirestoreRecyclerOptions.Builder<Catalog>()
                        .setQuery(catalogRef, Catalog.class)
                        .build();

        adapter = new FirestoreRecyclerAdapter<Catalog, CatalogViewHolder>(options) {
            @NonNull
            @Override
            public CatalogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog, parent, false);
                return new CatalogViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull CatalogViewHolder holder, int position, @NonNull Catalog model) {
                holder.bindData(model);

                holder.itemView.setOnClickListener(v -> {
                    int clickedPosition = holder.getAdapterPosition();
                    if (clickedPosition != RecyclerView.NO_POSITION) {
                        String selectedDocId = getSnapshots().getSnapshot(clickedPosition).getId();

                        Log.d("SelectedDocID", "Selected Document ID: " + selectedDocId);

                        // Set selectedDocId in ViewModel
                        viewModelE003.setSelectedDocId(selectedDocId);

                        Bundle bundle = new Bundle();
                        bundle.putString("selectedDocId", selectedDocId);

                        Navigation.findNavController(holder.itemView)
                                .navigate(R.id.action_e003_to_e001, bundle);
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public static class CatalogViewHolder extends RecyclerView.ViewHolder {
        android.widget.ImageView villagegrocer;
        android.widget.TextView vouchertitle;
        android.widget.ImageView coinbag;
        android.widget.TextView vouchervalue;

        public CatalogViewHolder(@NonNull View itemView) {
            super(itemView);
            villagegrocer = itemView.findViewById(R.id.villagegrocer);
            vouchertitle = itemView.findViewById(R.id.vouchertitle);
            coinbag = itemView.findViewById(R.id.coinbag);
            vouchervalue = itemView.findViewById(R.id.vouchervalue);
        }

        public void bindData(Catalog model) {
            vouchertitle.setText(model.getVoucherTitle());
            Picasso.get().load(model.getImgURL1()).into(villagegrocer);
            Picasso.get().load(model.getImgURL2()).into(coinbag);
            vouchervalue.setText(String.valueOf(model.getEcoCoins() + " ec"));
        }
    }
}
