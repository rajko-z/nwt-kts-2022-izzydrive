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
  @Output() chatMessagesEmiter = new EventEmitter<any[]>(); //dodati tip Message

  @Input() newMessage : any;
  
  constructor(  public datepipe: DatePipe, private userService: UserService, private chatService : ChatService) {
    }

  ngOnInit(): void {
    console.log("dsfdsfr")
    this.chatService.firebaseChannels.on('value', resp => {
      this.channels = this.chatService.snapshotToArray(resp);
      console.log( this.channels)
      this.isLoadingResults = false;
    }); 
    this.channels.forEach((channel) => {
      this.getUnreadMessages(channel);
    })
  }

  async enterChatRoom(channel_id: string) {

    this.userService.getUserData(channel_id).subscribe({
      next: (response)=>{
        let userId = response.id;
        this.chatService.firebaseChannels.child(userId.toString()).update({open_by_admin:'true'})//niej dobro, trenutni id je adminov i ne udje u dobar chet

        this.chatService.firebaseMessages.orderByChild('channel').equalTo(channel_id).on('value', (response : any) => {
          console.log(this.chatService.snapshotToArray(response))
          let messages = this.chatService.snapshotToArray(response);
          messages.forEach((mess) => { //kad se chat otvori sve poruku postaju procitane
            this.chatService.firebaseMessages.child(mess.key).update({read:'true'})
          });;
          this.chatMessagesEmiter.emit(messages);
        })
    
        if(this.previusChat && this.previusChat !== channel_id){
          this.chatService.setOpenChatByAdmin(false, this.previusChat)
    
        }
        this.previusChat = channel_id;
        console.log("SSSSSSS")

      },
      error: (response) =>{
        console.log(response.error.error);
      }
    })
    
  }

  counter = 0

  getUnreadMessages(channel : any){
    firebase.database().ref('messages/').orderByChild('channel').equalTo(channel.id).on('value', (response : any) => {
      let messages = this.chatService.snapshotToArray(response);

      messages.forEach((mess) => { //kad se chat otvori sve poruku postaju procitane
        if(!mess.read && mess.sender !== this.userService.getCurrentUserEmail()) {
          this.counter += 1;
        }
      })
    })
  }
}
