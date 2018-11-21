import React, { Component } from 'react';
import {
  requireNativeComponent,
	View,
} from 'react-native';


const NativeRealRecyclerItemView = requireNativeComponent('RealRecyclerItemView', RealRecyclerItemView,{
	nativeOnly: {onChange: true,onUpdateView:true}
});

class RealRecyclerItemView  extends  Component {
	static defaultProps = {
		rowID:-1,
		renderRow:undefined,
	}
	constructor(props){
			super(props);

			//console.log('初始化 rowid-----'+this.props.rowID);
			this.state={
				innerRowID:this.props.rowID
			}
	}
	
	componentWillUnmount (){
	  this.props.renderRow=undefined;
	}

  onUpdateView(event){
		
	const {rowID} = event.nativeEvent;

	// if(__DEV__)console.log("onUpdateView:new="+rowID+", old="+this.state.innerRowID);
	if(this.state.innerRowID!==rowID){
		this.props.rowID=rowID;
		this.setState({innerRowID:rowID});
		}

	}
  render () {
    return(<NativeRealRecyclerItemView
			{...this.props}
			// onChange={(e)=>{
			// 	this.onUpdateView(e)
			// }}
			onUpdateView={(e)=>{
				this.onUpdateView(e)
			}}

			>
				{this.props.renderRow(this.state.innerRowID)}
			</NativeRealRecyclerItemView>
		);
	}

}



module.exports = RealRecyclerItemView;