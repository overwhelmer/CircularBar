package com.overwhelmer.circularbarlib.console;

public class ManualInstanceProvider {
    public static ManualModeConsole getManualModeConsole() {
        return ManualModeConsoleImpl.getInstance();
    }
}
