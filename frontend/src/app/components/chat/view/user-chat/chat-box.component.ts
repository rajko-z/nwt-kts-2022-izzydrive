import { Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { Message } from 'src/app/model/message/message';
import { UserService } from 'src/app/services/userService/user-sevice.service';
import { ChatService } from 'src/app/services/chat/chat.service';
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

    this.userService.getCurrentUserData().subscribe({
      next : (response) => {

        let key = this.userService.getCurrentUserId().toString();
        let newChannel : Channel = new Channel(this.channelId, response.firstName + " " + response.lastName, false, false, false, false, key);

        this.chatService.firebaseChannels.child(key).set(newChannel);

        this.chatService.updateChatOpenningForUserByChatKey(key,{open_by_user:true, unread_messages_by_user: false} )
         //firebase.database().ref('channels/' + key).update({open_by_user:true, unread_messages_by_user: false})

        this.chatService.firebaseMessages.orderByChild('channel').equalTo(this.channelId).on('value', (response : any) => {
          this.messages = this.chatService.snapshotToArray(response);
        })


      }, error: (error) => {
        console.log(error.error.message);
      }
      })
  }

  ngOnInit(): void {
    this.channelId = this.userService.getCurrentUserEmail();
    this.chatService.firebaseChannels.orderByChild('id').equalTo(this.channelId).on('value', (response : any) => {
      let channel = this.chatService.snapshotToArray(response);
      this.unreaMessages = channel[0]?.unread_messages_by_user;
      console.log(this.unreaMessages)
      this.tooltipText = this.unreaMessages ? "New message from support" : "Support chat";
    })
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
