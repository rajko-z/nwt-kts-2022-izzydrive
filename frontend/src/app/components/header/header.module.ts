import { NgModule } from '@angular/core';
import { JsonPipe } from '@angular/common';
import { HeaderComponent } from './header.component'; 
import { AngularMaterialModule } from '../shared/angular-material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedComponentsModule } from '../shared/shared-components.module';
import { MenuModule } from '../menu/menu.module';

@NgModule({
    declarations: [HeaderComponent],
    imports: [JsonPipe, ReactiveFormsModule, AngularMaterialModule, FormsModule, SharedComponentsModule, MenuModule],
    exports: [],
  })
  export class HeadingModule {

    sign_up_clicked : boolean;
    sign_in_clicked : boolean;

  }