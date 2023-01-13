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

  setMessage(message: any){
    this.message = message;
  }

  loadMessages(messages : any[]){
    this.messages = messages;
    this.channel = messages[0]["channel"];
    
  }

  ngOnInit(): void {
    console.log("tuuuuuu")
  }

  onCLose(){
    firebase.database().ref('channels/').on('value', (response : any) => { //svi chetovi su za admina sad zatvoreni
      let channels = this.chatService.snapshotToArray(response);
      channels.forEach((c: any) => {
        firebase.database().ref('channels/' + c.key).update({open_by_admin:'false'})
       });  
    })
    this.router.navigateByUrl('/logged');
    console.log("dsadsafaf")
  }
}
