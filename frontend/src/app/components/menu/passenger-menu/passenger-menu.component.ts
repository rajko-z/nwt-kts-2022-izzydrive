import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-passenger-menu',
  templateUrl: './passenger-menu.component.html',
  styleUrls: ['./passenger-menu.component.scss', '../menu.component.scss']
})
export class PassengerMenuComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  onSupportChat(){
    this.router.navigateByUrl("/support-chat")
  }

  onDrivingHistory(){
    this.router.navigateByUrl("/history/driving");
  }

}
