package com.overwhelmer.circularbarlib.console;

import android.app.Activity;
import android.hardware.camera2.CameraCharacteristics;

import java.util.Observer;

public interface ManualModeConsole {

    void init(Activity activity, CameraCharacteristics cameraCharacteristics);

    void onResume();

    void onPause();

    void addParamObserver(Observer observer);

    void removeParamObservers();

    void setPanelVisibility(boolean visible);

    void resetAllValues();

    boolean isManualMode();

    boolean isPanelVisible();

    void retractAllKnobs();
}
