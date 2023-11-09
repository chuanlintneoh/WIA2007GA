package com.example.ecoventur.ui.greenspace;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GreenSpaceViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public GreenSpaceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Green Space Discovery module contents...\n[TNEOH CHUAN LIN]");
    }

    public LiveData<String> getText() {
        return mText;
    }
}