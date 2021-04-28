package com.overwhelmer.circularbarapplication;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.overwhelmer.circularbarlib.console.ManualInstanceProvider;
import com.overwhelmer.circularbarlib.console.ManualModeConsole;
import com.overwhelmer.circularbarlib.control.ManualParamModel;
import com.overwhelmer.circularbarlib.ui.views.knobview.KnobInfo;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {
    private static final String TAG = "MainActivity";
    private final ManualModeConsole manualModeConsole = ManualInstanceProvider.getManualModeConsole();
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = findViewById(R.id.textView2);
        textView2 = findViewById(R.id.textView3);
        textView3 = findViewById(R.id.textView4);
        textView4 = findViewById(R.id.textView5);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initManualMode();
    }

    private void initManualMode() {
        CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics("0");
            manualModeConsole.init(this, cameraCharacteristics);
            manualModeConsole.onResume();
            manualModeConsole.addParamObserver(this);
            manualModeConsole.setPanelVisibility(true);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        manualModeConsole.onPause();
        super.onPause();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ManualParamModel) {
            ManualParamModel manualParamModel = (ManualParamModel) o;
            String args = (String) arg;
            switch (args) {
                case ManualParamModel.ID_FOCUS:
                    textView1.setText(String.valueOf(manualParamModel.getCurrentFocusValue()));
                    break;
                case ManualParamModel.ID_SHUTTER:
                    textView2.setText(String.valueOf(manualParamModel.getCurrentExposureValue()));
                    break;
                case ManualParamModel.ID_ISO:
                    textView3.setText(String.valueOf(manualParamModel.getCurrentISOValue()));
                    break;
                case ManualParamModel.ID_EV:
                    textView4.setText(String.valueOf(manualParamModel.getCurrentEvValue()));
                    break;
            }
        }
    }
}