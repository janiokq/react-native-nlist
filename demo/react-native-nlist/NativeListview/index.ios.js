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

          
            //this.listView.stopRefreshing();
            // this.listView.startRefreshing
            // this.listView.startLoadmore
            // this.listView.stopLoadmore();
    }
    
    componentWillReceiveProps( newprops ){
            this.refreshState = newprops.refreshState;
            if(this.listView){
                    if(this.refreshState){
                        this.listView.startRefreshing();
                    }else{
                        this.listView.stopRefreshing();
                    }
            }
            
            this.loadinState = newprops.loadinState;
            if(this.listView){
                    if(this.loadinState){
                        this.listView.startLoadmore();
                    }else{
                        this.listView.stopLoadmore();
                    }
            }
        

    }

    componentDidMount(){
        if(this.listView){
            //自动刷新
            if(this.refreshState){
                this.listView.startRefreshing();
            }
            //自动加载
            if(this.loadinState){
                this.listView.startLoadmore()
            }
        }
    }

    render(){
        //console.log(this.props.dataSource);
        return (<TableView
                onScroll={this.props.onScroll}
                reactModuleForCell={this.props.reactModuleForCell}
                ref={(e)=>{

                    this.listView = e;
                    if(this.props.reference){
                        this.props.reference({
                            scrollToPosition:(index)=>{

                                debugger
                                this.listView.scrollToIndex({
                                    'index':index,
                                    animated:true
                                });
                            }
                        });
                    }
                    
                }}
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