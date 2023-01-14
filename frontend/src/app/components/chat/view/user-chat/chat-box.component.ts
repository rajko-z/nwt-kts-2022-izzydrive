import { Component, ElementRef, OnInit, Output, ViewChild } from '@angular/core';
import { Message } from 'src/app/model/message/message';
import firebase from 'firebase/compat/app'
import { UserService } from 'src/app/services/userService/user-sevice.service';
import { ChatService } from 'src/app/services/chat/chat.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-chat-box',
  templateUrl: './chat-box.component.html',
  styleUrls: ['./chat-box.component.scss']
})
export class ChatBoxComponent implements OnInit {

  @ViewChild('chatcontent') chatcontent: ElementRef;
  scrolltop: number = null

  constructor( private userService: UserService, 
    private chatService : ChatService,) { }

  messageText: string;
  messages : Message[] = [];
  channel: string;
  isCollapsed: boolean = false;
  unreaMessages : boolean = false;
  tooltipText : string = "Support chat"

  setMessage(message: string){
  }

  initChat(){
    this.isCollapsed = false;
    this.channel = this.userService.getCurrentUserEmail();
    let key = this.userService.getCurrentUserId();
    let newChannel = {
      id : this.channel,
      name: "",
      open_by_user: true,
      open_by_admin: false,
      unread_messages_by_user: false,
      unread_messages_by_admin: false,
    }
    firebase.database().ref("channels/").child(key.toString()).set(newChannel);

    firebase.database().ref('channels/' + key).update({open_by_user:true, unread_messages_by_user: false})

    firebase.database().ref('messages/').orderByChild('channel').equalTo(this.channel).on('value', (response : any) => {
      this.messages = this.chatService.snapshotToArray(response);
    }) 
    firebase.database().ref('channels/').orderByChild('id').equalTo(this.channel).on('value', (response : any) => {
      let channel = this.chatService.snapshotToArray(response);
      console.log(channel);
      this.unreaMessages = channel[0].unread_messages_by_user;
      this.tooltipText = this.unreaMessages ? "New message from support" : "Support chat";
    })
  }

  ngOnInit(): void {
    this.initChat();
  
  }

  onCLose(){
    this.isCollapsed = true;
    this.chatService.firebaseChannels.child(this.userService.getCurrentUserId().toString()).update({open_by_user:false})
  }

  isUnread(){
    return this.unreaMessages;
  }
}
