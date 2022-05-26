package com.example.recruitmenthelper.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.recruitmenthelper.model.Event;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<Event> eventMutableLiveData=new MutableLiveData<>();

    public LiveData<Event> getEvents(){
        return eventMutableLiveData;
    }

    public HomeViewModel() {

    }
    

}