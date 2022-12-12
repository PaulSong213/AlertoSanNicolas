package com.example.alertosannicolas.ui.addadmin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddAdminViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AddAdminViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is admin fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}