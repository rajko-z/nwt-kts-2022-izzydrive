import { Component, EventEmitter, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-sent-box',
  templateUrl: './sent-box.component.html',
  styleUrls: ['./sent-box.component.scss']
})
export class SentBoxComponent implements OnInit {

  constructor() { }

  @Output() messageEmiter = new EventEmitter<string>();

  ngOnInit(): void {
  }
  sendMessage(message : string){
    this.messageEmiter.emit(message);
  }

}
