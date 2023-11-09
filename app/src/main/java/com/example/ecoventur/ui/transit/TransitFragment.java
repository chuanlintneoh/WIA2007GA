package com.example.ecoventur.ui.transit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecoventur.databinding.FragmentTransitBinding;

public class TransitFragment extends Fragment {

    private FragmentTransitBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TransitViewModel transitViewModel =
                new ViewModelProvider(this).get(TransitViewModel.class);

        binding = FragmentTransitBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textTransit;
        transitViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}