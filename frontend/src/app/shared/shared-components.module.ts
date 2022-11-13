import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { Shared1Component } from './components/shared1/shared1.component';

// If you have any additional modules to be shared add them to the list
const materialModules = [
  MatInputModule,
  MatFormFieldModule,
  MatIconModule,
  MatButtonModule,
];

@NgModule({
  declarations: [Shared1Component],
  exports: [Shared1Component],
})
export class SharedComponentsModule{}
