import React,{  PureComponent } from 'react';
import {Dimensions,View} from 'react-native';
// import TableView from "../../../PlugIn/tableview/src/index";

import TableView from "../iosTabview/index";

const { Section,Item } = TableView;
const {width} = Dimensions.get("window");

export  default class  List   extends PureComponent{
    constructor(props){
        super(props)
        this.refreshState  = this.props.refreshState;
        this.loadinState  = this.props.loadinState;
            //this.refs.listView.stopRefreshing();
            // this.refs.listView.startRefreshing
            // this.refs.listView.startLoadmore
            // this.refs.listView.stopLoadmore();
    }
    
    componentWillReceiveProps( newprops ){
            this.refreshState = newprops.refreshState;
            if(this.refs.listView){
                    if(this.refreshState){
                        this.refs.listView.startRefreshing();
                    }else{
                        this.refs.listView.stopRefreshing();
                    }
            }
            
            this.loadinState = newprops.loadinState;
            if(this.refs.listView){
                    if(this.loadinState){
                        this.refs.listView.startLoadmore();
                    }else{
                        this.refs.listView.stopLoadmore();
                    }
            }
        

    }

    componentDidMount(){
        if(this.refs.listView){
            //自动刷新
            if(this.refreshState){
                this.refs.listView.startRefreshing();
            }
            //自动加载
            if(this.loadinState){
                this.refs.listView.startLoadmore()
            }
        }
    }

    render(){
        //console.log(this.props.dataSource);
        return (<TableView
                onScroll={this.props.onScroll}
                reactModuleForCell={this.props.reactModuleForCell}
                ref={'listView'}
                allowsToggle={true}
                allowsMultipleSelection={true}
                tableViewStyle={TableView.Consts.Style.Grouped}
                tableViewCellStyle={TableView.Consts.CellStyle.Subtitle}
                separatorStyle={TableView.Consts.SeparatorStyle.None}
                dataSource={this.props.dataSource}

                canRefresh={this.props.canRefresh}
                canLoadmore={this.props.canLoadmore}
                // canRefresh={false}
                // canLoadmore={false}
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


              >
    
             <Section
                 arrow={false}>
                 
                 {/* {this.props.dataSource.map((d,index)=>{
                      return <Item
                      {...d}
                      selectionStyle={TableView.Consts.CellSelectionStyle.None}
                      accessoryType={TableView.Consts.AccessoryType.None}
                      backgroundColor="#fff"
                      height={d.height}
                      key={index}
                      width={width}
                      index={index}
                    />
                 })} */}


          </Section>
          </TableView>
          )
    }
}