import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { AngularMaterialModule } from "../shared/angular-material.module";
import { SharedComponentsModule } from "../shared/shared-components.module";
import { DrivingHistoryComponent } from "./driving-history.component";
import { DrivingHistoryRouterModule } from './driving-history.router'
import { EvaluationComponent } from "./evaluation/evaluation.component";

@NgModule({
    declarations: [DrivingHistoryComponent, EvaluationComponent],
    imports: [
      CommonModule,
      SharedComponentsModule,
      AngularMaterialModule,
      DrivingHistoryRouterModule,
      FormsModule, 
      ReactiveFormsModule
    ],
    providers: [],
    exports: [EvaluationComponent]
  })
  export class DrivingHistoryModule {}