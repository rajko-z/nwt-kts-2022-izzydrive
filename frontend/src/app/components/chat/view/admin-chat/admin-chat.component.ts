import { Component, OnInit } from '@angular/core';
import { Message } from 'src/app/model/message/message';
import { UserService } from 'src/app/services/userService/user-sevice.service';

@Component({
  selector: 'app-admin-chat',
  templateUrl: './admin-chat.component.html',
  styleUrls: ['./admin-chat.component.scss']
})
export class AdminChatComponent implements OnInit {

  constructor(
    private userService: UserService) { }

  messageText: string;
  messages : Message[] = [];
  channel: string;

  //(messageEmiter)="setMessage($event)"

  setMessage(message: string){
    this.messageText = message
    // let newMessage : Message = new Message("user1", message, new Date());
    // this.messageText = message;
    // this.messages.push(newMessage);
    // if (message) {
    //   this.stompService.publish({
    //     destination: '/app/messages', body:
    //       JSON.stringify({
    //         'channel': this.channel,
    //         'sender':  this.userService.getCurrentUserEmail(),
    //         'text': message
    //       })
    //   });
    //   this.messageText = '';
    // }
  }

  loadMessages(messages : any[]){
    this.messages = messages;
    this.channel = messages["channel"];
  }

  ngOnInit(): void {
    // this.messages.push(new Message("user1", "text text", new Date()));
    // this.messages.push(new Message("user2", "text text  bla bla ", new Date()));
    // this.messages.push(new Message("user2", "text text  bla aaaaa ", new Date()));
    // console.log(this.messages)
    // this.channelService.getChannel().subscribe(channel => {
    //   this.channel = channel;
    //   this.filterMessages();
    // });

    // this.messageService.getMessages().subscribe(messages => {
    //   this.filterMessages();
    // });
  }
//   filterMessages() {
//     this.messages = this.messageService.filterMessages(this.channel);
// ;
//   }

}
