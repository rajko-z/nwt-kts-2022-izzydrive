import {Component, ElementRef, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {Message} from 'src/app/model/message/message';
import {ChatService} from 'src/app/services/chat/chat.service';
import {Router} from '@angular/router';
import {Channel} from 'src/app/model/channel/channel';

@Component({
  selector: 'app-admin-chat',
  templateUrl: './admin-chat.component.html',
  styleUrls: ['./admin-chat.component.scss']
})
export class AdminChatComponent {

  @ViewChildren('chatcontent') chatcontent: QueryList<any>;
  @ViewChild('chat_container') chat_container: ElementRef;

  message: Message;
  messages: Message[] = [];
  channelId: string;
  channels: Channel[] = []
  isChatOpen: boolean = false;

  constructor(private chatService: ChatService,
              private router: Router) {

  }

  scrollToBottom = () => {
    try {
      this.chat_container.nativeElement.scrollTop = this.chat_container.nativeElement.scrollHeight;
    } catch (err) {
    }
  }

  setMessage(message: Message) {
    this.message = message;
    //this.messages.push(message);
  }

  loadMessages(channel: any) {
    this.channelId = channel.id;
    this.isChatOpen = true;
    this.chatService.firebaseMessages.orderByChild('channel').equalTo(this.channelId).on('value', (response: any) => {
      this.messages = this.chatService.snapshotToArray(response);
    })
  }

  onCLose() {
    this.isChatOpen = false;
    this.chatService.closeAllAdminChat();
    this.router.navigateByUrl('/logged');
  }
}
