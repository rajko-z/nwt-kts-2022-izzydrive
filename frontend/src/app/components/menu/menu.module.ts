import { NgModule } from '@angular/core';
import { MenuRouterModule } from './menu.router';
import { CommonModule } from '@angular/common';
import { SharedComponentsModule } from '../shared/shared-components.module';
import { AngularMaterialModule } from '../shared/angular-material.module';
import { PassengerMenuComponent } from './passenger-menu/passenger-menu.component';
import { MenuComponent } from './menu.component';
import { AdminMenuComponent } from './admin-menu/admin-menu.component';
import { DriverMenuComponent } from './driver-menu/driver-menu.component';

@NgModule({
  declarations: [PassengerMenuComponent, MenuComponent, AdminMenuComponent, DriverMenuComponent],
  imports: [
    CommonModule,
    MenuRouterModule,
    SharedComponentsModule,
    AngularMaterialModule
  ],
  providers: [],
})
export class MenuModule {}