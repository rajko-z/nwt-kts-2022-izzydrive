import { Component } from '@angular/core';
import { UserService } from './services/userService/user-sevice.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
      title = 'NWT-KTS 2022 IZZYDRIVE';

      isUserLoggedIn: boolean = false;

      constructor(private userService: UserService) {}

      ngOnInit(): void {
        this.isUserLoggedIn = this.userService.isUserLoggedIn()
      }
}
