package com.example.ecoventur.ui.ecorewards;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoventur.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class e002 extends Fragment {

    private E002ViewModel viewModel;
    private TransactionAdapter spendingAdapter;
    private TransactionAdapter earningAdapter;
    private TextView ecocoinsbalance;
    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        viewModel = new ViewModelProvider(this).get(E002ViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_e002, container, false);

        // Find the TextView for ecocoinsbalance
        ecocoinsbalance = view.findViewById(R.id.ecocoinsbalance);

        RecyclerView spendingRecyclerView = view.findViewById(R.id.spendingView);
        spendingRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        spendingAdapter = new TransactionAdapter(requireContext(), new ArrayList<>(), false);
        spendingRecyclerView.setAdapter(spendingAdapter);

        RecyclerView earningRecyclerView = view.findViewById(R.id.earningView);
        earningRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        earningAdapter = new TransactionAdapter(requireContext(), new ArrayList<>(), true);
        earningRecyclerView.setAdapter(earningAdapter);

        db = FirebaseFirestore.getInstance();

        observeViewModel();

        return view;
    }

    private void observeViewModel() {
        viewModel.getSpending().observe(getViewLifecycleOwner(), spendingList -> {
            if (spendingList != null && !spendingList.isEmpty()) {
                spendingAdapter.setTransactionList(spendingList);
            } else {
                Log.d("e002 Fragment", "Received empty spendings list or null");
            }
        });

        viewModel.getEarning().observe(getViewLifecycleOwner(), earningList -> {
            if (earningList != null && !earningList.isEmpty()) {
                earningAdapter.setTransactionList(earningList);
            } else {
                Log.d("e002 Fragment", "Received empty earnings list or null");
            }
        });

        viewModel.getBalance().observe(getViewLifecycleOwner(), balance -> {
            if (balance != null) {
                ecocoinsbalance.setText(balance + " ec");
            } else {
                Log.d("e002 Fragment", "Received null balance");
            }
        });

        viewModel.fetchDataFromFirestore();
        viewModel.calculateBalance();
    }
}
