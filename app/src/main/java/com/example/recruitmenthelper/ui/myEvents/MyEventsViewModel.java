package com.example.recruitmenthelper.ui.myEvents;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyEventsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyEventsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}