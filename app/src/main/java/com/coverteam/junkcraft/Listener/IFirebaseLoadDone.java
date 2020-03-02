package com.coverteam.junkcraft.Listener;

import com.coverteam.junkcraft.model.DataTutorialSlide;

import java.util.List;

public interface IFirebaseLoadDone {
    void onFirebaseLoadSuccess(List<DataTutorialSlide> dataTutorialSlideList);
    void onFirebaseLoadFailed(String message);
}
