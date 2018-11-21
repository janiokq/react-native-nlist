package com.janiokq.Nlist;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.header.WaveSwipeHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;

import java.util.Map;

import javax.annotation.Nullable;


//Smartrefresh
public class RealRecyclerViewManager extends ViewGroupManager<RealRecyclerView> {
    @Override
    public String getName() {
        return RealRecyclerView.class.getSimpleName();
    }

    @Override
    protected RealRecyclerView createViewInstance(ThemedReactContext reactContext) {
//        Smartrefresh   refl =   new  Smartrefresh(reactContext,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
//        refl.setLayoutParams(new LinearLayout.LayoutParams(
//                SmartRefreshLayout.LayoutParams.MATCH_PARENT, SmartRefreshLayout.LayoutParams.MATCH_PARENT));
        RealRecyclerView   refl =   new  RealRecyclerView(reactContext);
        refl.setLayoutParams(new LinearLayout.LayoutParams(
                SmartRefreshLayout.LayoutParams.MATCH_PARENT, SmartRefreshLayout.LayoutParams.MATCH_PARENT));
        return refl;
    }

    @Override
    public void addView(RealRecyclerView parent, View child, int index) {
        parent.addNewView(child,index);
    }

    @ReactProp(name = "inserttheway")
    public void setInserttheway(RealRecyclerView parent, String type) {
        //1  刷新  0追加
        parent.setInserttheway(type);
    }

    @ReactProp(name="rowHeight")
    public void setRowHeight(RealRecyclerView parent, int val){
        parent.setRowHeight(val);
    }


    @ReactProp(name = "numRows")
    public void setNumRows(RealRecyclerView parent, ReadableArray size) {
        parent.setNumRows(size);
    }

    @Override
    public int getChildCount(RealRecyclerView parent) {
         return parent.getChildCount();
    }

    @Override
    public void removeAllViews(RealRecyclerView parent) {
        parent.removeAllView();
    }

    @Override
    public
    @Nullable
    Map getExportedCustomDirectEventTypeConstants() {




        return MapBuilder.builder()
                .put("onScroll", MapBuilder.of("registrationName", "onScroll"))
                .put("onScrollto", MapBuilder.of("registrationName", "onScrollto"))
                .build();
    }



}
