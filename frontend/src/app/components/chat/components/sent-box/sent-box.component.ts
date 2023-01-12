import { Component, EventEmitter, Output ,  ElementRef, ViewChild, OnInit, Input} from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormControl, FormGroupDirective, FormBuilder, FormGroup, NgForm, Validators } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import firebase from 'firebase/compat/app'
import { DatePipe } from '@angular/common';
import { UserSeviceService } from 'src/app/services/userService/user-sevice.service';


export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

export const snapshotToArray = (snapshot: any) => { //ovo izdvojiti
  const returnArr = [];

  snapshot.forEach((childSnapshot: any) => {
      const item = childSnapshot.val();
      item.key = childSnapshot.key;
      returnArr.push(item);
  });

  return returnArr;
};

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
  matcher = new MyErrorStateMatcher();

  constructor(private router: Router,
            private userService :UserSeviceService,
              private route: ActivatedRoute,    
              private formBuilder: FormBuilder,
              public datepipe: DatePipe) {
                this.currentUserEmail = this.userService.getCurrentUserEmail()
                 //nije u urlu treba slati kao input
                // firebase.database().ref('chats/').on('value', resp => {
                //   this.messages = [];
                //   this.messages = snapshotToArray(resp);
                //   setTimeout(() => this.scrolltop = this.chatcontent.nativeElement.scrollHeight, 500);
                // });
                // firebase.database().ref('roomusers/').orderByChild('roomname').equalTo(this.roomname).on('value', (resp2: any) => {
                //   const roomusers = snapshotToArray(resp2);
                //   this.users = roomusers.filter(x => x.status === 'online');
                // });
              }

  @Output() messageEmiter = new EventEmitter<string>();

  ngOnInit(): void {
    this.chatForm = this.formBuilder.group({
      'text': new FormControl('', [Validators.required])
    })
  }
  // sendMessage(message : string){
  //   this.messageEmiter.emit(message);
  // }

  onFormSubmit() {
    this.text = this.chatForm.controls['text'].value;
    this.messageEmiter.emit(this.text);
    let chat = {channel : this.channel,
                sender : this.userService.getCurrentUserEmail(),
                timeStamp : this.datepipe.transform(new Date(), 'dd/MM/yyyy HH:mm:ss'),
                text: this.text,
                read: false
    }
    const newMessage = firebase.database().ref('messages/').push();
    newMessage.set(chat);
    this.chatForm = this.formBuilder.group({
      'text' : ['', Validators.required]
    });
  }


}
