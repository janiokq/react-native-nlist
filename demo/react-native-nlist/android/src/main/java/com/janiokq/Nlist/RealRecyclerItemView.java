package com.janiokq.Nlist;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactViewGroup;

public class RealRecyclerItemView extends ReactViewGroup {
    final EventDispatcher mEventDispatcher;
    public ThemedReactContext reactContext;
    private int mHeight;
    public  boolean myTag = false;
    public  int  tempid =0;
    public  int rowid = 0;
    public RealRecyclerItemView(Context context) {
        super(context);
        reactContext = (ThemedReactContext)context;
        mEventDispatcher = ((ReactContext) getContext()).getNativeModule(UIManagerModule.class).getEventDispatcher();
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec), h = mHeight;
        if (mHeight < 1 && getChildCount() > 0) {
            final View child = getChildAt(0);
            LayoutParams lp = child.getLayoutParams();
            if (lp == null) {
                lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            }
            int w1 = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.AT_MOST), h1 = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.AT_MOST);
            child.measure(w1, h1);
            h = child.getHeight();
        }
        setMeasuredDimension(w, h);
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
        forceLayout();
    }
    public void setHeight(int val) {
        if (mHeight != val) {
            mHeight = val;
            requestLayout();
        }
    }

    public void setInnerRowID(int rowID) {
        rowid = rowID;
//        WritableMap writableMap = Arguments.createMap();
//        writableMap.putInt("rowID", rowID);
//        sendEvent("event", writableMap);
        mEventDispatcher.dispatchEvent(new MyEvent(getId(), rowID));
    }

    private class MyEvent extends Event<MyEvent> {
        private final int mRowId;
        private MyEvent(int viewTag, int mRowId) {
            super(viewTag);
            this.mRowId = mRowId;
        }

        @Override
        public String getEventName() {
            return "onUpdateView";
        }
        @Override
        public void dispatch(RCTEventEmitter rctEventEmitter) {
            WritableMap eventData = Arguments.createMap();
            eventData.putInt("rowID", mRowId);
            rctEventEmitter.receiveEvent(getViewTag(), getEventName(), eventData);
        }
    }//end class;







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

}