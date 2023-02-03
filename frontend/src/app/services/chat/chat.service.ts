import {Injectable} from '@angular/core';
import {MatSnackBar} from '@angular/material/snack-bar';
import firebase from 'firebase/compat/app'
import {Channel} from 'src/app/model/channel/channel';
import {Message} from 'src/app/model/message/message';
import {Role} from 'src/app/model/user/role';
import {UserService} from '../userService/user-sevice.service';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  firebaseChannels = firebase.database().ref('channels/');
  firebaseMessages = firebase.database().ref('messages/');
  constructor(private userService : UserService, public snackBar: MatSnackBar) { }

  snapshotToArray(snapshot: firebase.database.DataSnapshot){ //ovo izdvojiti
    const returnArr = [];

    snapshot.forEach((childSnapshot: any) => {
        const item = childSnapshot.val();
        item.key = childSnapshot.key;
        returnArr.push(item);
    });

    return returnArr;
  };

  closeAllAdminChat(): void{
    this.firebaseChannels.once('value', (response : any) => {
      let channels = this.snapshotToArray(response);
      channels.forEach((channel : Channel) => {
        this.firebaseChannels.child(channel.key).update({open_by_admin: false})
       });
    })
  }

  markChannelAsRead(message : Message) : void{
    this.firebaseChannels.orderByChild('id').equalTo(message.channel).once('value', (response : any) => {
      let channels = this.snapshotToArray(response);
      channels.forEach((channel : Channel) => {
      if(this.userService.getRoleCurrentUserRole() == "ROLE_ADMIN"){
            if (channel.open_by_user) {
              this.firebaseChannels.child(channel.key).update({unread_messages_by_user: false})
            }
            else{
              this.firebaseChannels.child(channel.key).update({unread_messages_by_user: true})
            }
          }
          else {
            if (channel.open_by_admin){
              this.firebaseChannels.child(channel.key).update({unread_messages_by_admin: false})
            }
            else{
              this.firebaseChannels.child(channel.key).update({unread_messages_by_admin: true})
            }
          }
        })
      }
    )
  }

  updateChatOpenningForAdminByChatKey(chatKey : string, newData : {open_by_admin: boolean, unread_messages_by_admin: boolean}) : void{
    this.firebaseChannels.child(chatKey).update(newData);
  }

  updateChatOpenningForUserByChatKey(chatKey : string, newData : {open_by_user: boolean, unread_messages_by_user: boolean}) : void{
    this.firebaseChannels.child(chatKey).update(newData);
  }

  listenForNewMessages(){
    this.firebaseMessages.on('child_added', (snapshot, prevChildKey) => {
      let message : Message = snapshot.val();
      this.firebaseChannels.orderByChild('id').equalTo(message.channel).once('value', (response) => {
        let channel : Channel = this.snapshotToArray(response)[0];
        if(channel.unread_messages_by_admin && this.userService.getRoleCurrentUserRole() === Role.ROLE_ADMIN.toString()){
          this.snackBar.open("You have new message", "OK");
        }
      })
    })
  }

  checkNewMessagesForAdmin(){
    this.firebaseChannels.once('value', (response) => {
      let channels : Channel [] = this.snapshotToArray(response);
      channels.forEach((channel) => {
        if(channel.unread_messages_by_admin && this.userService.getRoleCurrentUserRole() === Role.ROLE_ADMIN.toString()){
          this.snackBar.open("You have new message", "OK");
        }
      })
    })
  }
  closeUserChats(userId : number, role: string) : void{
    if(role === Role.ROLE_ADMIN){
      console.log("tuu")
      this.closeAllAdminChat()
    }
    else{
      this.firebaseChannels.child(userId.toString()).update({open_by_user:false})
    }
  }
}
