import { Component, OnInit, Output } from '@angular/core';
import { Message } from 'src/app/model/message/message';
import { UserSeviceService } from 'src/app/services/userService/user-sevice.service';
import firebase from 'firebase/compat/app'

export const snapshotToArray = (snapshot: any) => { //ovo izdvojiti
  const returnArr = [];

  snapshot.forEach((childSnapshot: any) => {
      const item = childSnapshot.val();
      item.key = childSnapshot.key;
      returnArr.push(item);
  });

  return returnArr;
};

@Component({
  selector: 'app-chat-box',
  templateUrl: './chat-box.component.html',
  styleUrls: ['./chat-box.component.scss']
})
export class ChatBoxComponent implements OnInit {

  constructor( private userService: UserSeviceService) { }

  messageText: string;
  messages : Message[] = [];
  channel: string;

  setMessage(message: string){
  }

  ngOnInit(): void {
    this.channel = this.userService.getCurrentUserEmail();
    firebase.database().ref('messages/').orderByChild('channel').equalTo(this.channel).on('value', (response : any) => {
      let messages = snapshotToArray(response);
      messages.forEach((mess) => {
        this.messages.push(mess);
      })
    })
  }
}
