var UIManager = require('UIManager');
import React, { Component } from 'react';
import {
  requireNativeComponent,
  NativeModules,
  Dimensions,
  View
} from 'react-native';
const ReactNative = require('ReactNative');

import RecyclerView from './realRecyclerView.android.js';
const NativeRealRecyclerView = requireNativeComponent('Smartrefresh', RealRecyclerView,{
  nativeOnly: {onChange: true}
});


 class  RealRecyclerView extends Component {
   constructor(props){
     super(props)
   }

  componentWillUnmount (){
	  this.props.renderRow=undefined;
  }


  getInnerViewNode(){
    return this.refs["Refreshtheview"].getInnerViewNode();
  }
  
  refreshState(state){
    UIManager.dispatchViewManagerCommand(
      ReactNative.findNodeHandle(this),
      UIManager.Smartrefresh.Commands.setrefreshstate,
      [state],
    );
  }
  
  loadinState(state){
    UIManager.dispatchViewManagerCommand(
      ReactNative.findNodeHandle(this),
      UIManager.Smartrefresh.Commands.setloadstate,
      [state],
    );
  }

  _onChange(event) {
    if(event.nativeEvent.params.type  == 'refresh'){
      if(this.props.onRefresh){
        this.props.onRefresh(event.nativeEvent.params)
      }
    }

    if(event.nativeEvent.params.type  == 'loadmore'){
      if(this.props.onLoadmore){
        this.props.onLoadmore(event.nativeEvent.params)
      }
    }
  }


  render() {
    return (
        <NativeRealRecyclerView
          ref={'Refreshtheview'}

          
          springback={this.props.springback}
          canRefresh={this.props.canRefresh}  //是否能刷新
          canLoadmore={this.props.canLoadmore} //是否能加载更多

          //refreshState={this.props.refreshState}
          //loadinState={this.props.loadinState}

          onChange={this._onChange.bind(this)}
          style={this.props.style}
        >
        
          {/* {this.props.children} */}
          <RecyclerView

            onScrollto={this.props.onScrollto}
            onScroll={this.props.onScroll}
            style={this.props.style}
            renderRow={this.props.renderRow}
            numRows={this.props.numRows}
            rowHeight={this.props.rowHeight}
            inserttheway={this.props.reactModuleForCell}
            
          />

        </NativeRealRecyclerView>
    );
  }
 
}

module.exports = RealRecyclerView ;