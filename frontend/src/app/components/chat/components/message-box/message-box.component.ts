import { Component, Input, OnInit } from '@angular/core';
import { Message } from 'src/app/model/message/message';
import { UserService } from 'src/app/services/userService/user-sevice.service';

@Component({
  selector: 'app-message-box',
  templateUrl: './message-box.component.html',
  styleUrls: ['./message-box.component.scss']
})
export class MessageBoxComponent implements OnInit {

  constructor(private userService: UserService) { }

  @Input()  message? : Message;
  isSenderMessage: boolean;

  ngOnInit(): void {
    console.log(this.message)
    let currentUser : string = this.userService.getCurrentUserEmail();
    this.isSenderMessage = this.message.sender === currentUser;
  }

}
