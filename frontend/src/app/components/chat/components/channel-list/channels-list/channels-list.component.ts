import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import firebase from 'firebase/compat/app'
import 'firebase/compat/database'
import { UserService } from 'src/app/services/userService/user-sevice.service';

export const snapshotToArray = (snapshot: any) => {
  const returnArr = [];

  snapshot.forEach((childSnapshot: any) => {
      const item = childSnapshot.val();
      item.key = childSnapshot.key;
      returnArr.push(item);
  });

  return returnArr;
};

@Component({
  selector: 'app-channels-list',
  templateUrl: './channels-list.component.html',
  styleUrls: ['./channels-list.component.scss']
})
export class ChannelsListComponent implements OnInit {

  currentUserEmail = '';
  displayedColumns: string[] = ['channel-id', 'channel-name'];
  channels = [];
  isLoadingResults = true;
  @Output() chatMessagesEmiter = new EventEmitter<any[]>(); //dodati tip Message

  constructor(private route: ActivatedRoute, private router: Router, public datepipe: DatePipe, private userSeervice: UserService) {
    this.currentUserEmail = this.userSeervice.getCurrentUserEmail();
    firebase.database().ref('channels/').on('value', resp => {
      this.channels = [];
      this.channels = snapshotToArray(resp);
      this.isLoadingResults = false;
    }); }

  ngOnInit(): void {
  }

  enterChatRoom(channel_id: string) {
    // const chat = { channel: '', sender: '', text: '', timeStamp: '', read: '' };
    // chat.channel = channel_id;
    // chat.sender = this.currentUserEmail;
    // chat.timeStamp = this.datepipe.transform(new Date(), 'dd/MM/yyyy HH:mm:ss');
    // chat.text = `${this.currentUserEmail} enter the room`;
    // chat.type = 'join';
    // const newMessage = firebase.database().ref('chats/').push();
    // newMessage.set(chat);

    firebase.database().ref('messages/').orderByChild('channel').equalTo(channel_id).on('value', (response : any) => {
      let messages = snapshotToArray(response);
      this.chatMessagesEmiter.emit(messages)
    })

    // firebase.database().ref('channelusers/').orderByChild('channel-id').equalTo(channel_id).on('value', (resp: any) => {
    //   let roomuser = [];
    //   roomuser = snapshotToArray(resp);
    //   const user = roomuser.find(x => x.nickname === this.currentUserEmail);
    //   if (user !== undefined) {
    //     const userRef = firebase.database().ref('roomusers/' + user.key);
    //     userRef.update({status: 'online'});
    //   } else {
    //     const newroomuser = { roomname: '', nickname: '', status: '' };
    //     newroomuser.roomname = channel-id;
    //     newroomuser.nickname = this.currentUserEmail;
    //     newroomuser.status = 'online';
    //     const newRoomUser = firebase.database().ref('roomusers/').push();
    //     newRoomUser.set(newroomuser);
    //   }
    // });

    //this.router.navigate(['/admin-chat', channel_id]);
  }

}
