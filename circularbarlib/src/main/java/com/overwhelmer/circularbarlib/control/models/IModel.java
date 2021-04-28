package com.overwhelmer.circularbarlib.control.models;


import com.overwhelmer.circularbarlib.ui.views.knobview.KnobInfo;
import com.overwhelmer.circularbarlib.ui.views.knobview.KnobItemInfo;

import java.util.List;

public interface IModel {
    List<KnobItemInfo> getKnobInfoList();

    KnobItemInfo getCurrentInfo();

    KnobInfo getKnobInfo();
}
