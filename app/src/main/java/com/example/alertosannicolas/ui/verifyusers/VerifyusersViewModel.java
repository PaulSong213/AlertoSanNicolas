package com.example.alertosannicolas.ui.verifyusers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VerifyusersViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public VerifyusersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is verify users fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}