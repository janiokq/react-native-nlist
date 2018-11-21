import React, { Component } from 'react';
import {
  requireNativeComponent,
  NativeModules,
  Dimensions,
  View
} from 'react-native';

const ReactNative = require('ReactNative');
import RealRecyclerItemView from './realRecyclerItemView.android';
const NativeRealRecyclerView = requireNativeComponent('RealRecyclerView', RealRecyclerView,{
  nativeOnly: {onChange: true}
});


 class  RealRecyclerView extends Component {
   constructor(props){
     super(props)
   }

  componentWillUnmount (){
	  this.props.renderRow=undefined;
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
  // const height=Dimensions.get('window').height;
  // var rCount = Math.round(height/this.props.rowHeight*1.6);
  // if(rCount<9)rCount=9;
  //   var items = [];
  //   for (var i=0; i<rCount; i++) {
  //     items.push(
	// 	<RealRecyclerItemView
  //     rowID={i}
	// 	  type={2}
	// 	  renderRow={this.props.renderRow}
	// 	  key={'r_' + i}
	// 	 />
  //     );
  //   }
    return (
        <NativeRealRecyclerView

          {...this.props}
          onChange={this._onChange.bind(this)}
          onScroll={(e)=>{
            e.nativeEvent.contentOffset = {
              x:e.nativeEvent.x,
              y:e.nativeEvent.y,
            }
            if(this.props.onScroll){
              this.props.onScroll(e);
            }
          }}
          
          onScrollto={(e)=>{
                
                if(this.props.onScrollto){
                   this.props.onScrollto(e.nativeEvent.state)
                }

          }}

        >
          {/* {items} */}
        </NativeRealRecyclerView>
    );
  }
 
}

module.exports = RealRecyclerView ;