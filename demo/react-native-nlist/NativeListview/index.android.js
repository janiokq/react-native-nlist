import React,{  PureComponent,Component } from 'react';
import {Dimensions,View} from 'react-native';
import RealRecyclerView from './SmartrefreshView';

export  default class  List   extends Component{
    constructor(props){
        super(props)
        this.state = {
            dataSource:this.props.dataSource, 
        }
        this.refreshState  = this.props.refreshState;
        this.loadinState  = this.props.loadinState;
    }

static defaultProps = {  
    inserttheway:1,  
  }  
  
    componentWillReceiveProps( newprops ){
            let   li   = newprops.dataSource;
            //alert(JSON.stringify(li));
            this.refreshState = newprops.refreshState;
            if(this.refs.listView){
                    if(this.refreshState){
                        this.refs.listView.refreshState(true);
                    }else{
                        this.refs.listView.refreshState(false);
                    }
            }
            
            this.loadinState = newprops.loadinState;
            if(this.refs.listView){
                    if(this.loadinState){
                        this.refs.listView.loadinState(true);
                    }else{
                        this.refs.listView.loadinState(false);
                    }
            }

            this.setState({
                    dataSource:li 
            })
    }
    
    componentDidMount(){
        if(this.refs.listView){
            //自动刷新
            if(this.refreshState){
                this.refs.listView.refreshState(true);
            }
            //自动加载
            if(this.loadinState){
                this.refs.listView.loadinState(true);
            }
        }
    }

    Converthighly( obj ){
        let list =[];
        obj.map(d=>{
                list.push(d.height)
            })
        return list
    }

    render(){

        return (<RealRecyclerView
            //仿苹果 支持越界回弹
            ref={'listView'}
            onScroll={this.props.onScroll}

            // springback={this.props.springback?this.props.springback:false}
            springback={false}

            canRefresh={this.props.canRefresh}  //是否能刷新
            canLoadmore={this.props.canLoadmore} //是否能加载更多
            //刷新状态控制
            reactModuleForCell={this.props.reactModuleForCell}
                        
            // refreshState={this.props.refreshState}
            // loadinState={this.props.loadinState}
            onScrollto={this.props.onScrollto}
            onRefresh={()=>{
                if(this.props.onRefresh){
                    this.props.onRefresh();
                }
            }}

            onLoadmore={()=>{
                if(this.props.onLoadmore){
                    this.props.onLoadmore();
                }
            }}

            style={this.props.style}
           
            numRows={this.state.dataSource}
            //最小高度
            rowHeight={this.props.rowHeight?this.props.rowHeight:80}
            
          />)
    }
}