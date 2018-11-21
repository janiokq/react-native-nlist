package com.janiokq.Nlist;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactRootView;

// import com.reactnativenavigation.NavigationApplication;   //    react-native-navigation   With this


import com.janiokq.Nlist.ViewMeasurer;
import com.facebook.react.ReactNativeHost;

/**
 * Created by i on 27/03/2018.
 */

public class ListItemView  extends ReactRootView {
    protected ViewMeasurer viewMeasurer;
    private final String screenId;
    private int mHeight;
    public boolean hasrun =false;
    public Context  context;

    public ListItemView(Context context, String screenId) {
//        Bundle initialProps
        super(context);
        this.screenId = screenId;
        this.context = context;
        //this.initialProps = initialProps;
        viewMeasurer = new ViewMeasurer();
        //attachToJS();
    }

    public  void  startApplist(Bundle bundle){
        attachToJS(bundle);
    }
    private void attachToJS(Bundle b) {
        hasrun = true;
        ReactApplication app = (ReactApplication) this.context.getApplicationContext();

        startReactApplication(
                app.getReactNativeHost().getReactInstanceManager()
                // NavigationApplication.instance.getReactGateway().getReactNativeHost().getReactInstanceManager()    //    react-native-navigation   With this
            ,
                screenId,
                b
        );
    }

    public void setViewMeasurer(ViewMeasurer viewMeasurer) {
        this.viewMeasurer = viewMeasurer;
    }

    public void unmountReactView() {
        unmountReactApplication();
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



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST);
        int heightSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, heightSpec);

        int measuredHeight = viewMeasurer.getMeasuredHeight(heightSpec);
        setMeasuredDimension(viewMeasurer.getMeasuredWidth(widthSpec), mHeight);
    }


    @Override
    public void onViewAdded(final View child) {
        super.onViewAdded(child);
    }
}
