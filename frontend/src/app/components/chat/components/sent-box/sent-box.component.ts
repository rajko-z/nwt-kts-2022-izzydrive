import { Component, EventEmitter, Output ,  ElementRef, ViewChild, OnInit, Input} from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormControl, FormGroupDirective, FormBuilder, FormGroup, NgForm, Validators } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import firebase from 'firebase/compat/app'
import { DatePipe } from '@angular/common';
import { UserService } from 'src/app/services/userService/user-sevice.service';
import { Role } from 'src/app/model/user/role';
import { ChatService } from 'src/app/services/chat/chat.service';


@Component({
  selector: 'app-sent-box',
  templateUrl: './sent-box.component.html',
  styleUrls: ['./sent-box.component.scss']
})
export class SentBoxComponent implements OnInit {

  chatForm: FormGroup;
  sender = '';
  currentUserEmail = '';

  @Input() channel : string;
  text = '';
  users = [];
  messages = [];

  constructor(private router: Router,
            private userService :UserService,
              private route: ActivatedRoute,    
              private formBuilder: FormBuilder,
              public datepipe: DatePipe,
              private chatService: ChatService) {
                this.currentUserEmail = this.userService.getCurrentUserEmail();
              }

  @Output() messageEmiter = new EventEmitter<any>();

  ngOnInit(): void {
    this.chatForm = this.formBuilder.group({
      'text': new FormControl('', [])
    })
  }

  onFormSubmit() {
    this.text = this.chatForm.controls['text'].value;
    let mess = {channel : this.channel,
                sender : this.userService.getCurrentUserEmail(),
                timeStamp : this.datepipe.transform(new Date(), 'dd/MM/yyyy HH:mm:ss'),
                text: this.text,
                cahnnel_key : this.userService.getCurrentUserId().toString()
                
    }
    this.markMessageAsReadIfChatOpen(mess);
    this.messageEmiter.emit(mess);
    const newMessage = firebase.database().ref('messages/').push();
    newMessage.set(mess);
    this.chatForm = this.formBuilder.group({
      'text' : ['']
    });
  }

  markMessageAsReadIfChatOpen(message : any){
    firebase.database().ref('channels/').orderByChild('id').equalTo(message.channel).once('value', (response : any) => {
      let channels = this.chatService.snapshotToArray(response);
      channels.forEach((channel) => {
      if(this.userService.getRoleCurrentUserRole() == "ROLE_ADMIN"){ 
            console.log("admin");//ovde obeleziti channel da ima neprocitane poruke
            if (channel.open_by_user) {
              //message.read = true;
              this.chatService.firebaseChannels.child(channel.key).update({unread_messages_by_user: false})
            }
            else{
              this.chatService.firebaseChannels.child(channel.key).update({unread_messages_by_user: true})
            }
          }
          else {
            if (channel.open_by_admin){
              //message.read = true; 
              this.chatService.firebaseChannels.child(channel.key).update({unread_messages_by_admin: false})
            }
            else{
              this.chatService.firebaseChannels.child(channel.key).update({unread_messages_by_admin: true})
            }
          
      }
      })
    }
    )
    

  }

}
