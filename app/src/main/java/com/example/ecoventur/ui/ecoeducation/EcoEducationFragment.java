package com.example.ecoventur.ui.ecoeducation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecoventur.databinding.FragmentEcoeducationBinding;

public class EcoEducationFragment extends Fragment {

    private FragmentEcoeducationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        com.example.ecoventur.ui.ecoeducation.EcoEducationViewModel EcoEducationViewModel =
                new ViewModelProvider(this).get(com.example.ecoventur.ui.ecoeducation.EcoEducationViewModel.class);

        binding = FragmentEcoeducationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textEcoeducation;
        EcoEducationViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}