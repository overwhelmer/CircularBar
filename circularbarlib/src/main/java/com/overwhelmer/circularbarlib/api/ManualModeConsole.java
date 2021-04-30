package com.overwhelmer.circularbarlib.api;

import android.app.Activity;
import android.hardware.camera2.CameraCharacteristics;

import com.overwhelmer.circularbarlib.control.ManualParamModel;

import java.util.Observer;

public interface ManualModeConsole {

    void init(Activity activity, CameraCharacteristics cameraCharacteristics);

    void onResume();

    void onPause();

    void addParamObserver(Observer observer);

    ManualParamModel getManualParamModel();

    void removeParamObservers();

    void setPanelVisibility(boolean visible);

    void resetAllValues();

    boolean isManualMode();

    boolean isPanelVisible();

    void retractAllKnobs();
}
