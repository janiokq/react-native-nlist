package com.janiokq.Nlist;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.EventDispatcher;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.scroll.OnScrollDispatchHelper;
import com.janiokq.Nlist.ListItemView;


public class RealRecyclerView extends RecyclerView {
    private List<ListItemView> mRecycleViews = null;
    private int mRowHeight;
    private int mHoldItems;
    public  int Recyclingid;
    public  int onindex;
    private EventDispatcher mEventDispatcher;
    public   String  instertype;    //1 刷新  0追加
    public  Context con;
    public  ArrayList<ReadableMap> MypageViewtitle = new ArrayList<ReadableMap>();
    public  boolean   recycling = false;
    private final MyAdapter mMyAdapter;
    private final OnScrollDispatchHelper mOnScrollDispatchHelper = new OnScrollDispatchHelper();


    private class MyScrollEvent extends Event<MyScrollEvent> {
        private final int x;
        private final int y;
        private MyScrollEvent(int viewTag, float x,float y) {
            super(viewTag);
            this.x = (int)x;
            this.y = (int)y;
        }
        @Override
        public String getEventName() {
            return "onScroll";
        }
        @Override
        public void dispatch(RCTEventEmitter rctEventEmitter) {
            WritableMap eventData = Arguments.createMap();
            eventData.putInt("x", x);
            eventData.putInt("y", y);
            rctEventEmitter.receiveEvent(getViewTag(), getEventName(), eventData);
        }
    }//end class;


    private  class  ScrollToTopAndFooter extends  Event<ScrollToTopAndFooter>{
        private  final boolean state; // 状态 代表  true 为 滚动 到 头部  false 为滚动 到 底部
        private  ScrollToTopAndFooter(int ViewTag,boolean state){
            super(ViewTag);
            this.state = state;
        }
        @Override
        public String getEventName() {
            return "onScrollto";
        }

        @Override
        public void dispatch(RCTEventEmitter rctEventEmitter) {
            WritableMap eventData = Arguments.createMap();
            eventData.putBoolean("state", this.state);
            rctEventEmitter.receiveEvent(getViewTag(), getEventName(), eventData);
        }
    }



    public RealRecyclerView(Context context) {
        super(context);
        con=  context;
        setLayoutManager(new LinearLayoutManager(getContext()));
        //setSupportsChangeAnimations();
        LayoutManager laymage =  getLayoutManager();
        laymage.setAutoMeasureEnabled(true);
        //setItemAnimator(new DefaultItemAnimator());
        //setHasFixedSize(false);
        getItemAnimator().setChangeDuration(0);
        ((DefaultItemAnimator) getItemAnimator()).setSupportsChangeAnimations(false);
//       ((SimpleItemAnimator)getItemAnimator()).setSupportsChangeAnimations(false);
        mMyAdapter=new MyAdapter();
//        mMyAdapter.setHasStableIds(true);
        setAdapter(mMyAdapter);
        mEventDispatcher = ((ReactContext) getContext()).getNativeModule(UIManagerModule.class).getEventDispatcher();
//        this.addOnScrollListener();
        addOnScrollListener(new OnScrollListener(){

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                mEventDispatcher.dispatchEvent(new MyScrollEvent(getId(), (int)dx,(int)dy));
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                if(firstCompletelyVisibleItemPosition==0){
                    //滑动到 顶部
                    //ScrollToTopAndFooter
                    mEventDispatcher.dispatchEvent(new ScrollToTopAndFooter(getId(),true));
                }
                int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if(lastCompletelyVisibleItemPosition==layoutManager.getItemCount()-1){
                    //滑动到 底部
                    mEventDispatcher.dispatchEvent(new ScrollToTopAndFooter(getId(),false));
                }


            }
        });
//        setOnScrollChangeListener();

    }
    protected void onMeasure(int widthSpec, int heightSpec) {
        final int w = MeasureSpec.getSize( MeasureSpec.makeMeasureSpec(widthSpec, MeasureSpec.AT_MOST)), h = MeasureSpec.getSize(MeasureSpec.makeMeasureSpec(heightSpec, MeasureSpec.AT_MOST));
        setMeasuredDimension(w, h);
    }
    void addNewView(View view,int index) {
        //mMyAdapter.notifyDataSetChanged();
    }
    void removeAllView() {
        if (mRecycleViews != null) {
//            for (int a = 0; a < mRecycleViews.size(); a++) {
//                ListItemView  item  =   (ListItemView)mRecycleViews.get(a);
//                item.unmountReactView();
//            }
            mRecycleViews.clear();
            mMyAdapter.notifyDataSetChanged();
        }
        mEventDispatcher = null;
        mMyAdapter.setNumRows(0);
    }
    public ViewHolder getIndexforViewHolder (int position) {
        LinearLayoutManager layoutManager  = (LinearLayoutManager)getLayoutManager();
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
        int lastItemPosition = layoutManager.findLastVisibleItemPosition();
        if (position - firstItemPosition >= 0   &&    lastItemPosition -  position  >= 0 ) {
            //得到要更新的item的view
            View view = getChildAt(position - firstItemPosition);
            if (null != getChildViewHolder(view)){
                return (ViewHolder)getChildViewHolder(view);
            }
        }
        return  null;
    }
    public  void   trychangeitemVie(int index,int olall){
        ViewHolder nowviewHolder  =  getIndexforViewHolder(index);
        if(nowviewHolder!=null){
            ListItemView  nowview  =  (ListItemView) nowviewHolder.itemView;
            ReadableMap  nowobj =  MypageViewtitle.get(index);
            nowview.setHeight((int) PixelUtil.toPixelFromDIP(nowobj.getInt("height")));
            
            HashMap map =   nowobj.toHashMap();
            Bundle b =  Conversionobject(map);
            b.putInt("rowNumer",index);
            if(!nowview.hasrun){
                nowview.startApplist(b);
            }else{
                nowview.setAppProperties(b);
            }
        }else if(index <= olall) {
            mMyAdapter.notifyItemChanged(index);
        }
    }
    void setInserttheway(String type){
        instertype  = type;
   }
    void setNumRows(final ReadableArray dataSize) {
        int oldseiz  = MypageViewtitle.size();
        if(MypageViewtitle.size()>0  &&   MypageViewtitle.size() <   dataSize.size()  && recycling ){
            //第一次初始化 或者 增加 数据
            MypageViewtitle = new ArrayList<ReadableMap>();
            for( int i=0;i<dataSize.size();i++){
                ReadableMap x  = dataSize.getMap(i);
                MypageViewtitle.add(x);
            }
            int newSize = MypageViewtitle.size();
            mMyAdapter.addNumrow(oldseiz,(newSize-oldseiz),newSize);
            scrollBy(getScrollX(),getScrollY());
            //追加数据
        }else  {
            //刷新数据
            recycling = false;
            MypageViewtitle = new ArrayList<ReadableMap>();
            int newlistsize  = dataSize.size();
            for( int i=0;i<newlistsize;i++){
                ReadableMap x  = dataSize.getMap(i);
                MypageViewtitle.add(x);
            }
            int newSize = MypageViewtitle.size();
            if( oldseiz > 0 ){
                for(int i=0;i<newlistsize;i++){
                    trychangeitemVie(i,oldseiz);
                }
            }
           if(oldseiz < newlistsize) {
               mMyAdapter.addNumrow(oldseiz,(newSize-oldseiz),newSize);
           }else if(oldseiz > newlistsize){
               mMyAdapter.setNumRowsnnotchange(newlistsize);
               mMyAdapter.notifyItemRangeRemoved(newlistsize,oldseiz-newlistsize);
               mMyAdapter.notifyItemRangeChanged(newlistsize,oldseiz-newlistsize);
               //mMyAdapter.notifyDataSetChanged();
//             mMyAdapter.notifyItemRangeChanged(newlistsize+1, oldseiz - (newlistsize+1));
               //recycling=true;
               //mMyAdapter.setNumRows(newlistsize);
           }else if(oldseiz == 0 ) {
               mMyAdapter.setNumRows(newlistsize);
           }
            scrollBy(getScrollX(),getScrollY());
        }
    }
    void setRowHeight(int rowHeight) {
//        mRowHeight = (int) PixelUtil.toPixelFromDIP(rowHeight);
//        final int height = Math.max(DisplayMetricsHolder.getScreenDisplayMetrics().heightPixels, DisplayMetricsHolder.getScreenDisplayMetrics().widthPixels);
//        mHoldItems = Math.round(1.6f * height / this.mRowHeight);
//        if (mHoldItems < 9) mHoldItems = 9;
//        Log.i("模板总数",""+mHoldItems);
    }
    public  Bundle Conversionobject(HashMap   map ){
        Bundle b =   new Bundle();
        for(Object key : map.keySet() ){
            Object  nowvalue = map.get(key);
            if(nowvalue  instanceof  String){
                b.putString((String) key, (String) nowvalue );
            }
            if(nowvalue  instanceof Integer){
                b.putInt((String) key, (int)nowvalue );
            }
            if(nowvalue  instanceof HashMap){
                b.putBundle((String) key,Conversionobject((HashMap) nowvalue) );
            }
            if(nowvalue  instanceof Boolean){
                 boolean  stae =  (boolean) nowvalue;
                 b.putInt((String) key, stae?1:0);
            }
            if(nowvalue  instanceof Float){
                b.putFloat((String) key, (float) nowvalue);
            }
            if(nowvalue  instanceof  Double){
                b.putDouble((String) key, (double) nowvalue);
            }
        }
        return  b;
    }
    private class MyAdapter extends Adapter<ViewHolder> {
        private int mDataSize = 0;
        private int mUPos = 0;
        public void Refreshthescope(int newsize){
            this.mDataSize = newsize;
        }
        public  void addNumrow( int oldsize,int difference,int mDataSize){
            this.mDataSize = mDataSize;
            notifyItemInserted(oldsize+1);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        public  void setNumRowsnnotchange(int mDataSize){
            this.mDataSize = mDataSize;
        }
        public void setNumRows(int mDataSize) {
            this.mDataSize = mDataSize;
            notifyDataSetChanged();
        }
        @Override
        public void onViewRecycled(ViewHolder holder) {
            super.onViewRecycled(holder);
            recycling = true;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ListItemView nowive =  new ListItemView(con,instertype);
//            mRecycleViews.add( nowive);
            return new ViewHolder(nowive) {};
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
                ListItemView  nowview  =  (ListItemView) holder.itemView;
                ReadableMap  nowobj =  MypageViewtitle.get(position);
                nowview.setHeight((int) PixelUtil.toPixelFromDIP(nowobj.getInt("height")));
                HashMap map =   nowobj.toHashMap();
                Bundle b =  Conversionobject(map);
                b.putInt("rowNumer",position);
                if(!nowview.hasrun){
                    nowview.startApplist(b);
                }else{
                    nowview.setAppProperties(b);
                }
        }
        @Override
        public int getItemCount() {
            return mDataSize;
        }
    }//end  class MyAdapter
}
