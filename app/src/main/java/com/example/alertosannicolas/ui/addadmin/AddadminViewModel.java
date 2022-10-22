package com.example.alertosannicolas.ui.addadmin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddadminViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AddadminViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is add admin fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}