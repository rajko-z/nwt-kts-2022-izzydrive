import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedComponentsModule } from '../shared/shared-components.module';
import { AngularMaterialModule } from '../shared/angular-material.module';
import { PassengerMenuComponent } from './passenger-menu/passenger-menu.component';
import { MenuComponent } from './menu.component';
import { AdminMenuComponent } from './admin-menu/admin-menu.component';
import { DriverMenuComponent } from './driver-menu/driver-menu.component';
import { ProfileModule } from '../profile/profile.module';

@NgModule({
  declarations: [PassengerMenuComponent, MenuComponent, AdminMenuComponent, DriverMenuComponent],
  imports: [
    CommonModule,
    SharedComponentsModule,
    AngularMaterialModule,
    ProfileModule,
  ],
  providers: [],
  exports: [MenuComponent]
})
export class MenuModule {}