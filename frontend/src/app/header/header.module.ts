import { NgModule } from '@angular/core';
import { JsonPipe } from '@angular/common';
import { HeaderComponent } from './header.component'; 
import { AngularMaterialModule } from '../shared/angular-material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedComponentsModule } from '../shared/shared-components.module';

@NgModule({
    declarations: [HeaderComponent],
    imports: [JsonPipe, ReactiveFormsModule, AngularMaterialModule, FormsModule, SharedComponentsModule],
    exports: [],
  })
  export class HeadingModule {}