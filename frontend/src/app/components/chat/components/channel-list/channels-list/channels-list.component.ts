import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import firebase from 'firebase/compat/app'
import 'firebase/compat/database'
import { UserService } from 'src/app/services/userService/user-sevice.service';
import { ChatService } from 'src/app/services/chat/chat.service';
import { Channel } from 'src/app/model/channel/channel';
import { ResponseMessageService } from 'src/app/services/response-message/response-message.service';

@Component({
  selector: 'app-channels-list',
  templateUrl: './channels-list.component.html',
  styleUrls: ['./channels-list.component.scss']
})
export class ChannelsListComponent implements OnInit {

  displayedColumns: string[] = ['channel-id', 'channel-name'];
  channels : Channel[] = [];
  channel_ids : string[] = [];
  isLoadingResults = true;
  previusChat : string = undefined;
  @Output() chatMessagesEmiter = new EventEmitter<Channel>();

  constructor(public datepipe: DatePipe, 
              private userService: UserService, 
              private chatService : ChatService,
              private responseMessage: ResponseMessageService) {
    }

  initChannels(): void {
    this.chatService.firebaseChannels.on('value', resp => {
      this.channels = this.chatService.snapshotToArray(resp);
    });
    
  }
  ngOnInit(): void {
    this,this.initChannels();
  }

  enterChatRoom(channel: Channel) {
    this.chatService.closeAllAdminChat();
    this.userService.getUserData(channel.id).subscribe({
      next: (response)=>{
        let userId = response.id;
        this.chatService.updateChatOpenningForAdminByChatKey(userId.toString(), {open_by_admin: true, unread_messages_by_admin: false})        
        this.chatMessagesEmiter.emit(channel);
        this.previusChat = userId.toString();

      },
      error: (response) =>{
        this.responseMessage.openErrorMessage(response.error.message)
      }
    })
    
  }
}
