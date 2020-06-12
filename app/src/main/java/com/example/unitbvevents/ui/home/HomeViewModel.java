package com.example.unitbvevents.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unitbvevents.model.Event;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<Event> eventMutableLiveData=new MutableLiveData<>();

    public LiveData<Event> getEvents(){
        return eventMutableLiveData;
    }

    public HomeViewModel() {

    }
    

}