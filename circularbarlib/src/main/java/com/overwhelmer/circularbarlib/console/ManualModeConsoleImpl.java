package com.overwhelmer.circularbarlib.console;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.view.View;

import com.overwhelmer.circularbarlib.api.ManualModeConsole;
import com.overwhelmer.circularbarlib.camera.CameraProperties;
import com.overwhelmer.circularbarlib.control.ManualParamModel;
import com.overwhelmer.circularbarlib.control.models.EvModel;
import com.overwhelmer.circularbarlib.control.models.FocusModel;
import com.overwhelmer.circularbarlib.control.models.IsoModel;
import com.overwhelmer.circularbarlib.control.models.ManualModel;
import com.overwhelmer.circularbarlib.control.models.ShutterModel;
import com.overwhelmer.circularbarlib.model.KnobModel;
import com.overwhelmer.circularbarlib.model.ManualModeModel;
import com.overwhelmer.circularbarlib.ui.ViewObserver;
import com.overwhelmer.circularbarlib.ui.views.knobview.KnobView;

import java.util.Observer;

/**
 * Responsible for initialising and updating {@link KnobModel} and {@link ManualModeModel}
 * <p>
 * This class also manages the attaching/detaching of {@link ManualModel} subclasses to {@link KnobView}
 * and setting listeners to models
 * <p>
 * Authors - Vibhor, KillerInk
 */
public class ManualModeConsoleImpl implements ManualModeConsole {
    private static final String TAG = "ManualModeConsole";
    private final ManualModeModel manualModeModel;
    private final KnobModel knobModel;
    private final ManualParamModel manualParamModel = new ManualParamModel();
    private ManualModel<?> mfModel, isoModel, expoTimeModel, evModel, selectedModel;
    private ViewObserver viewObserver;

    private ManualModeConsoleImpl() {
        this.manualModeModel = new ManualModeModel();
        this.knobModel = new KnobModel();
    }

    public static ManualModeConsole getInstance() {
        return ManualModeConsoleImpl.Singleton.INSTANCE;
    }

    public ManualModeModel getManualModeModel() {
        return manualModeModel;
    }

    @Override
    public void addParamObserver(Observer observer) {
        manualParamModel.addObserver(observer);
    }

    @Override
    public void removeParamObservers() {
        manualParamModel.deleteObservers();
    }

    @Override
    public ManualParamModel getManualParamModel() {
        return manualParamModel;
    }

    public KnobModel getKnobModel() {
        return knobModel;
    }

    @Override
    public void init(Activity activity, CameraCharacteristics cameraCharacteristics) {
        viewObserver = new ViewObserver(activity);
        addObserver();
        addKnobs(activity, cameraCharacteristics);
        setupOnClickListeners();
        setAutoText();
    }

    @Override
    public void onResume() {
        if (viewObserver != null) {
            viewObserver.enableOrientationListener();
        }
        addObserver();
    }

    @Override
    public void onPause() {
        if (viewObserver != null) {
            viewObserver.disableOrientationListener();
        }
        removeObservers();
    }

    private void addObserver() {
        if (viewObserver != null) {
            removeObservers();
            knobModel.addObserver(viewObserver);
            manualModeModel.addObserver(viewObserver);
        }
    }

    private void removeObservers() {
        knobModel.deleteObservers();
        manualModeModel.deleteObservers();
    }

    private void addKnobs(Context context, CameraCharacteristics cameraCharacteristics) {
        CameraProperties cameraProperties = new CameraProperties(cameraCharacteristics);
        manualParamModel.reset();
        mfModel = new FocusModel(context, cameraCharacteristics, cameraProperties.focusRange, manualParamModel, manualModeModel::setFocusText);
        evModel = new EvModel(context, cameraCharacteristics, cameraProperties.evRange, manualParamModel, manualModeModel::setEvText);
        ((EvModel) evModel).setEvStep((cameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP).floatValue()));
        isoModel = new IsoModel(context, cameraCharacteristics, cameraProperties.isoRange, manualParamModel, manualModeModel::setIsoText);
        expoTimeModel = new ShutterModel(context, cameraCharacteristics, cameraProperties.expRange, manualParamModel, manualModeModel::setExposureText);
        knobModel.setKnobVisible(false);
        manualModeModel.setCheckedTextViewId(-1);
    }

    @Override
    public void setPanelVisibility(boolean visible) {
        manualModeModel.setManualPanelVisible(visible);
        if (!visible) {
            manualParamModel.reset();
        }
    }

    @Override
    public boolean isManualMode() {
        return manualParamModel.isManualMode();
    }

    @Override
    public void resetAllValues() {
        manualParamModel.reset();
    }

    @Override
    public boolean isPanelVisible() {
        return manualModeModel.isManualPanelVisible();
    }

    private void setupOnClickListeners() {
        manualModeModel.setFocusTextClicked(v -> setListeners(v, mfModel));
        manualModeModel.setEvTextClicked(v -> setListeners(v, evModel));
        manualModeModel.setExposureTextClicked(v -> setListeners(v, expoTimeModel));
        manualModeModel.setIsoTextClicked(v -> setListeners(v, isoModel));
    }

    private void setListeners(View view, ManualModel<?> model) {
        setModelToKnob(view.getId(), model);
        view.setOnLongClickListener(v -> {
            if (selectedModel == model) {
                knobModel.setKnobResetCalled(true);
            }
            model.resetModel();
            return true;
        });
    }

    private void setAutoText() {
        evModel.setAutoTxt();
        mfModel.setAutoTxt();
        expoTimeModel.setAutoTxt();
        isoModel.setAutoTxt();
    }

    @Override
    public void retractAllKnobs() {
        knobModel.setKnobVisible(false);
        knobModel.setKnobResetCalled(true);
        selectedModel = null;
        mfModel.resetModel();
        expoTimeModel.resetModel();
        isoModel.resetModel();
        evModel.resetModel();
        manualModeModel.setCheckedTextViewId(-1);
    }

    private void setModelToKnob(int viewId, ManualModel<?> modelToKnob) {
        if (modelToKnob == selectedModel) {
            knobModel.setManualModel(null);
            knobModel.setKnobVisible(false);
            manualModeModel.setCheckedTextViewId(-1);
            selectedModel = null;
        } else {
            if (modelToKnob.getKnobInfoList().size() > 1) {
                knobModel.setManualModel(modelToKnob);
                knobModel.setKnobVisible(true);
                manualModeModel.setCheckedTextViewId(viewId);
                selectedModel = modelToKnob;
            }
        }
    }

    private static class Singleton {
        private static final ManualModeConsole INSTANCE = new ManualModeConsoleImpl();
    }
}
