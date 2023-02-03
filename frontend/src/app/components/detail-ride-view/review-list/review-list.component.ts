import {Component, Input} from '@angular/core';
import {Evaluation} from "../../../model/evaluation/evaluation";

@Component({
  selector: 'app-review-list',
  templateUrl: './review-list.component.html',
  styleUrls: ['./review-list.component.scss']
})
export class ReviewListComponent {

  @Input()
  evaluations: Evaluation[]

  constructor() { }

}
