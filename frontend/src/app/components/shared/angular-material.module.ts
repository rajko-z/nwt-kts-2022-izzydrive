import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatListModule} from '@angular/material/list';
import {MatStepperModule} from '@angular/material/stepper';
import {MatRadioModule} from '@angular/material/radio';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatTableModule} from "@angular/material/table";
import {MatDialogModule} from "@angular/material/dialog";
import {MatTooltipModule} from '@angular/material/tooltip';
import { MatSortModule } from '@angular/material/sort';
import { MatPaginatorModule } from '@angular/material/paginator';



// If you have any additional modules to be shared add them to the list
const materialModules = [
  MatInputModule,
  MatFormFieldModule,
  MatIconModule,
  MatButtonModule,
  MatSnackBarModule,
  MatListModule,
  MatStepperModule,
  MatRadioModule,
  MatCheckboxModule,
  MatProgressBarModule,
  MatTableModule,
  MatDialogModule,
  MatTooltipModule, 
  MatSortModule,
  MatPaginatorModule
];

@NgModule({
  imports: [...materialModules],
  exports: materialModules,
})
export class AngularMaterialModule {}
