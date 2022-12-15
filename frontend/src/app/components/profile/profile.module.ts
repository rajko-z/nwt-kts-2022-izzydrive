import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedComponentsModule } from '../shared/shared-components.module';
import { AngularMaterialModule } from '../shared/angular-material.module';
import { ProfileRouterModule } from './profile.router';
import { AddDriverComponent } from './add-driver/add-driver.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [AddDriverComponent],
  imports: [
    CommonModule,
    SharedComponentsModule,
    AngularMaterialModule,
    ProfileRouterModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
})
export class ProfileModule {}