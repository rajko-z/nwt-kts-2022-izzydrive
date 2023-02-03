import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ChatService } from 'src/app/services/chat/chat.service';

@Component({
  selector: 'app-admin-menu',
  templateUrl: './admin-menu.component.html',
  styleUrls: ['./admin-menu.component.scss', '../menu.component.scss']
})
export class AdminMenuComponent  {

  constructor(private router: Router, private chatService: ChatService) { }

  routAddDriver(){
    this.chatService.closeAllAdminChat()
    this.router.navigateByUrl("/admin/add-driver")
  }
  routAllDrivers(){
    this.chatService.closeAllAdminChat()
    this.router.navigateByUrl("/admin/drivers")
  }
  routAllPassengers(){
    this.chatService.closeAllAdminChat()
    this.router.navigateByUrl("/admin/passengers")
  }

  routSupportChat(){
    this.router.navigateByUrl("/admin/admin-chat")
  }


}
