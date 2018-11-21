package com.janiokq.Nlist;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

import javax.annotation.Nullable;

public class RealRecyclerItemViewManager extends ViewGroupManager<RealRecyclerItemView> {
    @Override
    public RealRecyclerItemView createViewInstance(ThemedReactContext context) {
        return new RealRecyclerItemView(context);
    }

    @Override
    public String getName() {
        return RealRecyclerItemView.class.getSimpleName();
    }

    @ReactProp(name = "innerRowID")
    public void setInnerRowID(RealRecyclerItemView view, int val) {
        view.setInnerRowID(val);
    }

    @Override
    public
    @Nullable
    Map getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.builder()
                .put("onUpdateView", MapBuilder.of("registrationName", "onUpdateView"))
                .build();
    }
}
