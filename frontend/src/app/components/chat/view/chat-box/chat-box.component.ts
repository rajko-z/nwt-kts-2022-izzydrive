import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Message } from 'src/app/model/message/message';

@Component({
  selector: 'app-chat-box',
  templateUrl: './chat-box.component.html',
  styleUrls: ['./chat-box.component.scss']
})
export class ChatBoxComponent implements OnInit {

  constructor() { }

  messageText: string;
  messages : Message[] = [];

  //(messageEmiter)="setMessage($event)"

  setMessage(message: string){
    let newMessage : Message = new Message("user1", message, new Date());
    this.messageText = message;
    this.messages.push(newMessage);
  }

  ngOnInit(): void {
    this.messages.push(new Message("user1", "text text", new Date()));
    this.messages.push(new Message("user2", "text text  bla bla ", new Date()));
    this.messages.push(new Message("user2", "text text  bla aaaaa ", new Date()));
    console.log(this.messages)
  }

}
