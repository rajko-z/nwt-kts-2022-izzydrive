import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SharedComponentsModule} from '../../modules/shared/shared-components.module';
import {AngularMaterialModule} from '../../modules/shared/angular-material.module';
import {PassengerMenuComponent} from './passenger-menu/passenger-menu.component';
import {MenuComponent} from './menu.component';
import {AdminMenuComponent} from './admin-menu/admin-menu.component';
import {DriverMenuComponent} from './driver-menu/driver-menu.component';

@NgModule({
  declarations: [
    PassengerMenuComponent,
    MenuComponent,
    AdminMenuComponent,
    DriverMenuComponent
  ],
  imports: [
    CommonModule,
    SharedComponentsModule,
    AngularMaterialModule,
  ],
  providers: [],
  exports: [MenuComponent]
})
export class MenuModule {}
