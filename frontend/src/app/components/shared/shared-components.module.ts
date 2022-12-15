import { CommonModule, JsonPipe } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AngularMaterialModule } from './angular-material.module';
import { BaseUserDataFormComponent } from './components/base-user-data-form/base-user-data-form.component';
import { BaseCarDataFormComponent } from './components/base-car-data-form/base-car-data-form.component';



@NgModule({
  declarations: [BaseUserDataFormComponent, BaseCarDataFormComponent],
  imports: [AngularMaterialModule,  
    CommonModule, 
    JsonPipe, 
    ReactiveFormsModule, 
    FormsModule],
  exports: [BaseUserDataFormComponent, BaseCarDataFormComponent],
})
export class SharedComponentsModule{}
