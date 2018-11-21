/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View, Dimensions,Image,AppRegistry} from 'react-native';

const {width,height } = Dimensions.get('window')
const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' + 'Cmd+D or shake for dev menu',
  android:
    'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});
import  RNNlist   from "./react-native-nlist";
console.disableYellowBox = true;
type Props = {};
export default class App extends Component<Props> {

  constructor(props){
    super(props)
    this.state={
      refreshState:true,
      loadinState:false,
      dataSource:[]
    }

  }



  getdata(){

    let list = [];
    for(let i =0;i<15;i++){
      list.push({
        height:150,
        index:i,
      });
    }

    this.setState({
      dataSource:list,
      refreshState:false,
    })
  }
  

  adddata(){
    let newlist  = JSON.parse(JSON.stringify(this.state.dataSource) );    // Deep copy
    let nowindex = newlist.length;
    for(let i =0;i<10000;i++){
      newlist.push({
        height:150,
        index:nowindex+i,
      });
    }


    this.setState({
      dataSource:newlist,
      loadinState:false,
    })


  }





  render() {
    return (
      <View style={styles.container}>
        <RNNlist
        onScroll={(e)=>{
          //ios is  Height of content
          //android is   The last rolling start difference       The negative number is upward.      The positive number is downward. 
          //direction
          // console.log(e.nativeEvent.contentOffset)


        }}
        // only  android
        onScrollto={(e)=>{
            // o and 1    
            // 1  Scroll to the head
            // 0  Scroll to the bottom
            //e
        }}
        inserttheway={0}
        //ios   Rendering template
        reactModuleForCell="Itemlist"
        //android  Rendering template
        renderItem={Itemlist}
        //Rendering initialization of Android
        Initializeprops={{}}

        //Android rolling rebound effect
        springback={true}
        //Android template quantity
        // rowHeight={40}
        
        style={{
          width:width,
          height:height
        }}

        canRefresh={true}
        canLoadmore={true}

        refreshState={this.state.refreshState}
        loadinState={this.state.loadinState}
        dataSource={this.state.dataSource}

        onRefresh={()=>{
          setTimeout(()=>{
            this.getdata();
          },1000)
        }}
        onLoadmore={()=>{
          setTimeout(()=>{
            this.adddata();
          },1000)
        }}
    />

      </View>
    );
  }
}


class Itemlist extends  Component{
  constructor(props){
    super(props)
  }



  render() {
    let data  =  this.props;
    if(Platform.OS=='ios'){
        data =this.props.data;
    }

    return (
      <View style={{flex:1,}} >
            <View style={{
                width:width,
                height:data.height,
              }} >

              <Text>{data.index}</Text>

              <Image 
              // source={{
              //   uri:'http://img3.imgtn.bdimg.com/it/u=3360690558,3623061169&fm=11&gp=0.jpg'
              // }}
              source={{uri: 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3360690558,3623061169&fm=111&gp=0.jpg'}}
              style={{
                width:width,
                height:data.height-30,
              }}
              resizeMode='stretch'
              />

              </View>
      </View>
    )
  }
}

AppRegistry.registerComponent('Itemlist', () => Itemlist);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
