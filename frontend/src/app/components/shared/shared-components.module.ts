import { CommonModule, DatePipe, JsonPipe } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AngularMaterialModule } from './angular-material.module';
import { BaseUserDataFormComponent } from './components/base-user-data-form/base-user-data-form.component';
import { BaseCarDataFormComponent } from './components/base-car-data-form/base-car-data-form.component';
import { ReviewRideTableComponent } from './review-ride-table/review-ride-table.component';



@NgModule({
  declarations: [BaseUserDataFormComponent, BaseCarDataFormComponent, ReviewRideTableComponent],
  imports: [AngularMaterialModule,  
    CommonModule, 
    JsonPipe, 
    ReactiveFormsModule, 
    FormsModule,
    DatePipe],
  exports: [BaseUserDataFormComponent, BaseCarDataFormComponent, ReviewRideTableComponent],
})
export class SharedComponentsModule{}
