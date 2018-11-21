package com.janiokq.Nlist;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.Map;

import javax.annotation.Nullable;

/**
 * Created by i on 27/02/2018.
 */

public class SmartrefreshManager  extends ViewGroupManager<Smartrefresh> {

    public static final int COMMAND_SET_PAGE = 1;
    public static final int COMMAND_SET_PAGE_WITHOUT_ANIMATION = 2;
    public static final int COMMAND_ON_SCROLL = 3;
    @Override
    public String getName() {
        return "Smartrefresh";
    }

    @Override
    protected Smartrefresh createViewInstance(ThemedReactContext reactContext) {
        Smartrefresh   refl =   new  Smartrefresh(reactContext,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        refl.setLayoutParams(new LinearLayout.LayoutParams(
                SmartRefreshLayout.LayoutParams.MATCH_PARENT, SmartRefreshLayout.LayoutParams.MATCH_PARENT));
        return refl;
    }

    @Override
    public Map<String,Integer> getCommandsMap() {
        return MapBuilder.of(
                "setloadstate",
                COMMAND_SET_PAGE,
                "setrefreshstate",
                COMMAND_SET_PAGE_WITHOUT_ANIMATION,
                "onScroll",
                COMMAND_ON_SCROLL);
                
    }
//    receiveCommand
    @Override
    public void receiveCommand(Smartrefresh root, int commandId, @Nullable ReadableArray args) {
        //super.receiveCommand(root, commandId, args);
        Assertions.assertNotNull(root);
        Assertions.assertNotNull(args);
        switch (commandId) {
            case COMMAND_SET_PAGE: {   //设置 加载状态
                root.loadState(args.getBoolean(0));
                return;
            }
            case COMMAND_SET_PAGE_WITHOUT_ANIMATION: {//设置 刷新状态
                root.refreshing(args.getBoolean(0));
                return;
            }
            case COMMAND_ON_SCROLL:{
                
                return;
            }
            default:
                throw new IllegalArgumentException(String.format(
                        "Unsupported command %d received by %s.",
                        commandId,
                        getClass().getSimpleName()));
        }

    }

    @Override
    public void addView(Smartrefresh parent, View child, int index) {
        parent.addNewView(child,index);
    }

    @ReactProp(name = "canRefresh")
    public void setEnableRefresh(Smartrefresh parent,boolean support){
        parent.setEnableRefresh(support);
    }
    @ReactProp(name = "canLoadmore")
    public void setEnableLoadMorestate(Smartrefresh parent,boolean support){
        parent.setEnableLoadMorestate(support);
    }


//    @ReactProp(name = "refreshState")
//    public void setRefreshState(Smartrefresh parent,boolean support){
//        parent.Refreshing(support);
//    }
//    @ReactProp(name = "loadinState")
//    public void setLoadinState(Smartrefresh parent,boolean support){
//        parent.loadState(support);
//    }


    @ReactProp(name = "springback")
    public  void setSpringback(Smartrefresh parent,boolean support){
        parent.setEnableOverScrollDrag(support);
    }




}
