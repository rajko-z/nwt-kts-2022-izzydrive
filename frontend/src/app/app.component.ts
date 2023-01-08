import { Component } from '@angular/core';
import { UserSeviceService } from './services/userService/user-sevice.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
      title = 'NWT-KTS 2022 IZZYDRIVE';

      // isUserLoggedIn: boolean = false;

      constructor(private userService: UserSeviceService) {}
      
      ngOnInit(): void {
      }

      isUserLoggedIn() : boolean {
          return this.userService.isUserLoggedIn();
      }
}
