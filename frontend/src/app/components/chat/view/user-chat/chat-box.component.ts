import { Component, ElementRef, OnInit, Output, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { Message } from 'src/app/model/message/message';
import firebase from 'firebase/compat/app'
import { UserService } from 'src/app/services/userService/user-sevice.service';
import { ChatService } from 'src/app/services/chat/chat.service';
import { Router } from '@angular/router';
import { Channel } from 'src/app/model/channel/channel';

@Component({
  selector: 'app-chat-box',
  templateUrl: './chat-box.component.html',
  styleUrls: ['./chat-box.component.scss']
})
export class ChatBoxComponent implements OnInit {

  
  @ViewChildren('chatcontent') chatcontent: QueryList<any>;
  @ViewChild('chat_container') chat_container: ElementRef;

  constructor( private userService: UserService, 
    private chatService : ChatService,) { 
      
    }

  messageText: string;
  messages : Message[] = [];
  channelId: string;
  isCollapsed: boolean =   true;                                                                               ;
  unreaMessages : boolean = false;
  tooltipText : string = "Support chat"

  ngAfterViewInit() {
    this.scrollToBottom();
    this.chatcontent.changes.subscribe(this.scrollToBottom);
  }

  scrollToBottom = () => {
    try {
      this.chat_container.nativeElement.scrollTop = this.chat_container.nativeElement.scrollHeight;
    } catch (err) {}
  }

  setMessage(message: Message){
    //this.messages.push(message);
  }

  initChat(){
    this.channelId = this.userService.getCurrentUserEmail(); 
    this.userService.getCurrentUserData().subscribe({
      next : (response) => {
                               
        let key = this.userService.getCurrentUserId();
        let newChannel : Channel = new Channel(this.channelId, response.firstName + " " + response.lastName, false, false, false, false);

        firebase.database().ref("channels/").child(key.toString()).set(newChannel);
    
        firebase.database().ref('channels/' + key).update({open_by_user:true, unread_messages_by_user: false})
    
        firebase.database().ref('messages/').orderByChild('channel').equalTo(this.channelId).on('value', (response : any) => {
          this.messages = this.chatService.snapshotToArray(response);
        }) 
        firebase.database().ref('channels/').orderByChild('id').equalTo(this.channelId).on('value', (response : any) => {
          let channel = this.chatService.snapshotToArray(response);
          this.unreaMessages = channel[0].unread_messages_by_user;
          console.log(this.unreaMessages)
          this.tooltipText = this.unreaMessages ? "New message from support" : "Support chat";
        })
    
      }, error: (error) => {
        console.log(error.error.message);
      }
      })
  }

  ngOnInit(): void {
    //this.initChat();
  }

  onCLose(){
    this.isCollapsed = true;
    this.chatService.firebaseChannels.child(this.userService.getCurrentUserId().toString()).update({open_by_user:false})
  }

  isUnread(){
    return this.unreaMessages;
  }

  openChat(){
    this.isCollapsed = false;
    this.initChat();
  }
}
