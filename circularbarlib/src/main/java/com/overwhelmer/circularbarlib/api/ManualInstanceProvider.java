package com.overwhelmer.circularbarlib.api;

import com.overwhelmer.circularbarlib.console.ManualModeConsoleImpl;

public class ManualInstanceProvider {
    public static ManualModeConsole getManualModeConsole() {
        return ManualModeConsoleImpl.getInstance();
    }
}
