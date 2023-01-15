import { Component, EventEmitter, Output ,  ElementRef, ViewChild, OnInit, Input} from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormControl, FormGroupDirective, FormBuilder, FormGroup, NgForm, Validators } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import firebase from 'firebase/compat/app'
import { DatePipe } from '@angular/common';
import { UserService } from 'src/app/services/userService/user-sevice.service';
import { Role } from 'src/app/model/user/role';
import { ChatService } from 'src/app/services/chat/chat.service';
import { Message } from 'src/app/model/message/message';


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
  // messages : Message[] = [];

  constructor(private userService :UserService,
              private formBuilder: FormBuilder,
              public datepipe: DatePipe,
              private chatService: ChatService) {
                this.currentUserEmail = this.userService.getCurrentUserEmail();
              }

  @Output() messageEmiter = new EventEmitter<Message>();

  ngOnInit(): void {
    this.chatForm = this.formBuilder.group({
      'text': new FormControl('', [])
    })
  }

  onFormSubmit() {
    this.text = this.chatForm.controls['text'].value;
    this.userService.getCurrentUserData().subscribe({
      next: (user) => {
        let mess : Message = new Message(this.userService.getCurrentUserEmail(),
                                          this.text, 
                                          this.datepipe.transform(new Date(), 'dd/MM/yyyy HH:mm:ss'),
                                          user.id.toString(),
                                          this.channel)
        this.chatService.markChannelAsRead(mess);
        
        const newMessage = firebase.database().ref('messages/').push();
        newMessage.set(mess);
        this.messageEmiter.emit(mess);
        this.chatForm = this.formBuilder.group({
          'text' : ['']
        });
      }
    })
   
  }

}
