import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { Shared1Component } from './components/shared1/shared1.component';
import {MatTooltipModule} from '@angular/material/tooltip';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatSnackBarModule} from '@angular/material/snack-bar';

// If you have any additional modules to be shared add them to the list
const materialModules = [
  MatInputModule,
  MatFormFieldModule,
  MatIconModule,
  MatButtonModule,
  MatTooltipModule,
  BrowserAnimationsModule,
  MatSnackBarModule
];

@NgModule({
  declarations: [Shared1Component],
  exports: [Shared1Component],
})
export class SharedComponentsModule{}
