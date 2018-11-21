package com.janiokq.Nlist;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import  com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

public class Smartrefresh extends SmartRefreshLayout {
    public  RealRecyclerView list;
    public ThemedReactContext reactContext;
    public RefreshLayout mrefreshlayout;
    
    public void setEnableRefreshstate(boolean  Supportrefresh){
        setEnableRefresh(Supportrefresh);//是否启用下拉刷新功能
    }

    public void setEnableLoadMorestate(boolean  Supportrefresh){
        setEnableLoadmore(Supportrefresh);//是否启用下拉刷新功能
    }

    //控制刷新状态
    public void   refreshing (boolean  state){


        if(state){
            autoRefresh();
        }else{
            if(mrefreshlayout == null){
                return;
            }
            mrefreshlayout.finishRefresh(10);
        }
    }

    //控制加载状态
    public  void  loadState(boolean state){
        if(state){
            autoLoadmore();
        }else{
            if(mrefreshlayout == null){
                return;
            }
            mrefreshlayout.finishLoadmore(10);
        }
    }





    public Smartrefresh(Context context , ViewGroup.LayoutParams  params) {
        super(context);
        reactContext = (ThemedReactContext)context;
        //addView(list);
        //setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果果）1.0.4

        setDisableContentWhenRefresh(true);
        //setDisableContentWhenLoading(true);

        ClassicsHeader  header  = new ClassicsHeader(context);
        ClassicsFooter  footer = new ClassicsFooter(context);
        
        ClassicsHeader.REFRESH_HEADER_FINISH = "正在刷新...";
        ClassicsFooter.REFRESH_FOOTER_FINISH = "正在加载...";

        setRefreshHeader(header);
        setRefreshFooter(footer);
        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                WritableMap writableMap = Arguments.createMap();
                writableMap.putString("type", "refresh");
                mrefreshlayout = refreshlayout;
                sendEvent("event", writableMap);
            }
        });
        setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mrefreshlayout = refreshlayout;

                WritableMap writableMap = Arguments.createMap();
                writableMap.putString("type", "loadmore");
                sendEvent("event", writableMap);

            }
        });
    }


    public void sendEvent(String eventName, @android.support.annotation.Nullable WritableMap params) {
        WritableMap event = Arguments.createMap();
        event.putMap("params", params);
        event.putString("type", eventName);
        reactContext
                .getJSModule(RCTEventEmitter.class)
                .receiveEvent(getId(),
                        "topChange",
                        event);
    }

    public  void addNewView(View child,int index){
        addView(child);
    }


}
