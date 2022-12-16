import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedComponentsModule } from '../shared/shared-components.module';
import { AngularMaterialModule } from '../shared/angular-material.module';
import { ProfileRouterModule } from './profile.router';
import { AddDriverComponent } from './add-driver/add-driver.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { EditProfileComponent } from './edit-profile/edit-profile.component';


@NgModule({
  declarations: [AddDriverComponent, EditProfileComponent],
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