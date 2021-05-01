package com.overwhelmer.circularbarlib.api;

import com.overwhelmer.circularbarlib.console.ManualModeConsoleImpl;

public class ManualInstanceProvider {
    /**
     * @return Singleton Instance
     */
    public static ManualModeConsole getManualModeConsole() {
        return ManualModeConsoleImpl.getInstance();
    }

    /**
     * @return new Instance
     */
    public static ManualModeConsole getNewManualModeConsole() {
        return ManualModeConsoleImpl.newInstance();
    }
}
