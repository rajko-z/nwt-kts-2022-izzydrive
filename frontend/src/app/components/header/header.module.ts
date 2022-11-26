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
  export class HeadingModule {

    sign_up_clicked : boolean;
    sign_in_clicked : boolean;

    // on_sign_up_select() : void  {
    //   console.log("tii")
    //   this.sign_up_clicked = true;
    //   this.sign_in_clicked = false;
    // }

    // on_sign_in_select() : void  {
    //   this.sign_up_clicked = false;
    //   this.sign_in_clicked = true;
    // }

  }