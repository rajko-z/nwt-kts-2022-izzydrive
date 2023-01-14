import { Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
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

  @ViewChildren('chatcontent') chatcontent: QueryList<any>;
  @ViewChild('chat_container') chat_container: ElementRef;

  message :string;
  messages : Message[] = [];
  channel: string;
  newMessage : undefined
  channels = []

  
  constructor( private userService: UserService, 
    private chatService : ChatService,
    private router : Router) { 

    }

  ngAfterViewInit() {
    this.scrollToBottom();
    this.chatcontent.changes.subscribe(this.scrollToBottom);
  }

  scrollToBottom = () => {
    try {
      this.chat_container.nativeElement.scrollTop = this.chat_container.nativeElement.scrollHeight;
    } catch (err) {}
  }
  
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
