package com.example.ecoventur.ui.greenspace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecoventur.databinding.FragmentGreenspaceBinding;

public class GreenSpaceFragment extends Fragment {

    private FragmentGreenspaceBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GreenSpaceViewModel GreenSpaceViewModel =
                new ViewModelProvider(this).get(GreenSpaceViewModel.class);

        binding = FragmentGreenspaceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGreenspace;
        GreenSpaceViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}