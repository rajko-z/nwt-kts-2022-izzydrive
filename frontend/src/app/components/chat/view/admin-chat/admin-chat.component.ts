import { Component, OnInit } from '@angular/core';
import { Message } from 'src/app/model/message/message';
import { UserService } from 'src/app/services/userService/user-sevice.service';
import firebase from 'firebase/compat/app'
import { ChatService } from 'src/app/services/chat/chat.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-chat',
  templateUrl: './admin-chat.component.html',
  styleUrls: ['./admin-chat.component.scss']
})
export class AdminChatComponent implements OnInit {

  constructor( private userService: UserService, 
    private chatService : ChatService,
    private router : Router) { }

  message :string;
  messages : Message[] = [];
  channel: string;
  newMessage : undefined
  channels = []

  setMessage(message: any){
    this.message = message;
    this.messages.push(message);
    //this.initChannels();
  }

  loadMessages(channel : any){ 
    this.channel = channel.id;
    this.chatService.firebaseMessages.orderByChild('channel').equalTo(this.channel).on('value', (response : any) => {
        this.messages = this.chatService.snapshotToArray(response);
    })
    }

  ngOnInit(): void {
   
  }

  onCLose(){
    firebase.database().ref('channels/').once('value', (response : any) => { //svi chetovi su za admina sad zatvoreni
      let channels = this.chatService.snapshotToArray(response);
      channels.forEach((c: any) => {
        firebase.database().ref('channels/' + c.key).update({open_by_admin:false})
       });  
    })
    this.router.navigateByUrl('/logged');
    console.log("dsadsafaf")
  }
}
