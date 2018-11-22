package com.janiokq.Nlist;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
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


    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;
    private RecyclerView self;


    /**
     * 滑动到指定位置
     */
    public void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前，使用smoothScrollToPosition
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后，最后一个可见项之前
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                // smoothScrollToPosition 不会有效果，此时调用smoothScrollBy来滑动到指定位置
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }


    private class MyScrollEvent extends Event<MyScrollEvent> {
        private  int x;
        private  int y;
        private  int nx;
        private  int ny;
        private MyScrollEvent(int viewTag, int x,int y,int nx,int ny) {
            super(viewTag);

            this.x = x;
            this.y = y;
            this.nx = nx;
            this.ny = ny;

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
            eventData.putInt("Nx", nx);
            eventData.putInt("Ny", ny);
            rctEventEmitter.receiveEvent(getViewTag(), getEventName(), eventData);
        }
    }//end class;


    private  class  ScrollToTopAndFooter extends  Event<ScrollToTopAndFooter>{
        private   int fstate;
        private   int estate;
        private  ScrollToTopAndFooter(int ViewTag,int state,int etate){
            super(ViewTag);
            this.fstate = state;
            this.estate = etate;
        }
        @Override
        public String getEventName() {
            return "onScrollto";
        }

        @Override
        public void dispatch(RCTEventEmitter rctEventEmitter) {
            WritableMap eventData = Arguments.createMap();
            eventData.putInt("fstate", this.fstate);
            eventData.putInt("estate", this.estate);
            rctEventEmitter.receiveEvent(getViewTag(), getEventName(), eventData);
        }
    }












    public RealRecyclerView(Context context) {
        super(context);
        con=  context;
        self = this;
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
            int scrollDy = 0;
            int scrollDx = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mShouldScroll && RecyclerView.SCROLL_STATE_IDLE == newState) {
                    mShouldScroll = false;
                    smoothMoveToPosition(self, mToPosition);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                scrollDy += dy;
                scrollDx += dx;
                super.onScrolled(recyclerView, dx, dy);
                mEventDispatcher.dispatchEvent(new MyScrollEvent(getId(), scrollDx,scrollDy,(int)dx,(int)dy));
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

//                mEventDispatcher.dispatchEvent(new ScrollToTopAndFooter(getId(),arr[0],arr[1]));
//                                                    //layoutManager.findLastCompletelyVisibleItemPosition()
//                                                    int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
//                                                    if(firstCompletelyVisibleItemPosition==0){
//                                                        //滑动到 顶部
//                                                        //ScrollToTopAndFooter
//                                    //                    mEventDispatcher.dispatchEvent(new ScrollToTopAndFooter(getId(),true));
//                                                    }
//                                                    int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
//                                                    if(lastCompletelyVisibleItemPosition==layoutManager.getItemCount()-1){
//                                                        //滑动到 底部
//                                    //                    mEventDispatcher.dispatchEvent(new ScrollToTopAndFooter(getId(),false));
//                                                    }


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
