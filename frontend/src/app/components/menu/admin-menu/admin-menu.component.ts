import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-menu',
  templateUrl: './admin-menu.component.html',
  styleUrls: ['./admin-menu.component.scss', '../menu.component.scss']
})
export class AdminMenuComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  routAddDriver(){
    this.router.navigateByUrl("/profile/add-driver")
  }

}
