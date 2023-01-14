import { Injectable } from '@angular/core';
import firebase from 'firebase/compat/app'

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  firebaseChannels = firebase.database().ref('channels/');
  firebaseMessages = firebase.database().ref('messages/');
  constructor() { }

  snapshotToArray(snapshot: firebase.database.DataSnapshot){ //ovo izdvojiti
    const returnArr = [];
  
    snapshot.forEach((childSnapshot: any) => {
        const item = childSnapshot.val();
        item.key = childSnapshot.key;
        returnArr.push(item);
    });
  
    return returnArr;
  };

  setOpenChatByuser(open:boolean, channelId : any):void{
    this.firebaseChannels.orderByChild('id').equalTo(channelId).once('value', (response : any) => {
      let channels = this.snapshotToArray(response);
      channels.forEach((channel: any) => {
        this.firebaseChannels.child(channel.key).update({open_by_user: open})
       });  
    })
  }

  closeAllAdminChat(){
    this.firebaseChannels.once('value', (response : any) => {
      let channels = this.snapshotToArray(response);
      channels.forEach((channel: any) => {
        this.firebaseChannels.child(channel.key).update({open_by_admin: false})
       });  
    })
  }
}
