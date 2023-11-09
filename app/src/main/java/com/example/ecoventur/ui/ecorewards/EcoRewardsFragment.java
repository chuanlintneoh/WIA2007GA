package com.example.ecoventur.ui.ecorewards;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecoventur.databinding.FragmentEcorewardsBinding;

public class EcoRewardsFragment extends Fragment {

    private FragmentEcorewardsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        com.example.ecoventur.ui.ecorewards.EcoRewardsViewModel EcoRewardsViewModel =
                new ViewModelProvider(this).get(com.example.ecoventur.ui.ecorewards.EcoRewardsViewModel.class);

        binding = FragmentEcorewardsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textEcorewards;
        EcoRewardsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}