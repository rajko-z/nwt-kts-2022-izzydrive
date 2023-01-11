import { Component, Input, OnInit } from '@angular/core';
import { Message } from 'src/app/model/message/message';

@Component({
  selector: 'app-message-box',
  templateUrl: './message-box.component.html',
  styleUrls: ['./message-box.component.scss']
})
export class MessageBoxComponent implements OnInit {

  constructor() { }

  @Input()  message? : Message;
  isSenderMessage: boolean;

  ngOnInit(): void {
    console.log(this.message)
    let currentUser : string = "user1";
    this.isSenderMessage = this.message.sender === currentUser;
  }

}
