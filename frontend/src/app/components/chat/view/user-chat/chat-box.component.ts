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
    private chatService : ChatService,
    private router : Router) { }

  messageText: string;
  messages : Message[] = [];
  channel: string;
  isCollapsed: boolean = false;

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
      open_by_admin: false
    }
    firebase.database().ref("channels/").child(key.toString()).set(newChannel);

    firebase.database().ref('channels/' + key).update({open_by_user:'true'})

    firebase.database().ref('messages/').orderByChild('channel').equalTo(this.channel).on('value', (response : any) => {
      let messages = this.chatService.snapshotToArray(response);
      messages.forEach((mess) => {
        firebase.database().ref('messages/' + mess.key).update({read:'true'}) //svaka poruka u chatu postaje procitana
        this.messages.push(mess);
      })
    })
  }

  ngOnInit(): void {
    this.initChat();
  }

  onCLose(){
    this.isCollapsed = true;
    this.chatService.firebaseChannels.child(this.userService.getCurrentUserId().toString()).update({open_by_user:'false'})
  }
}
