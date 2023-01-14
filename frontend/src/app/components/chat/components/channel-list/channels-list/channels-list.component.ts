import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import firebase from 'firebase/compat/app'
import 'firebase/compat/database'
import { UserService } from 'src/app/services/userService/user-sevice.service';
import { ChatService } from 'src/app/services/chat/chat.service';

@Component({
  selector: 'app-channels-list',
  templateUrl: './channels-list.component.html',
  styleUrls: ['./channels-list.component.scss']
})
export class ChannelsListComponent implements OnInit {

  displayedColumns: string[] = ['channel-id', 'channel-name'];
  channels = [];
  isLoadingResults = true;
  previusChat : string = undefined;
  @Output() chatMessagesEmiter = new EventEmitter<any>(); 

  @Input() newMessage : any;

  //@Input() channels : any[];
  
  constructor(  public datepipe: DatePipe, private userService: UserService, private chatService : ChatService) {
    }

  initChannels(): void {
    this.chatService.firebaseChannels.on('value', resp => {
      this.channels = this.chatService.snapshotToArray(resp);
      console.log(this.channels)
    });
    
  }
  ngOnInit(): void {
    this,this.initChannels();
    // this.chatService.firebaseChannels.on('child_changed', resp => {
    //   this.channels = this.chatService.snapshotToArray(resp);
    //   console.log(this.channels)
    // });
  }

  enterChatRoom(channel: any) {
    //this.initChannels();
    this.chatService.closeAllAdminChat();
    this.userService.getUserData(channel.id).subscribe({
      next: (response)=>{
        let userId = response.id;
        this.chatService.firebaseChannels.child(userId.toString()).update({open_by_admin: true, unread_messages_by_admin: false})//niej dobro, trenutni id je adminov i ne udje u dobar chet
 
        this.chatMessagesEmiter.emit(channel);
        // })
        this.previusChat = userId.toString();

      },
      error: (response) =>{
        console.log(response.error.error);
      }
    })
    
  }
}
