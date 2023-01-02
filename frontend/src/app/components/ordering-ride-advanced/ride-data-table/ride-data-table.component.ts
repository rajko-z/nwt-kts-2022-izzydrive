import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'app-ride-data-table',
  templateUrl: './ride-data-table.component.html',
  styleUrls: ['./ride-data-table.component.scss']
})
export class RideDataTableComponent implements OnInit {

  @Output() formData = new EventEmitter<void>();
  @Input() rideForm?: FormGroup;

  constructor() {
  }

  ngOnInit(): void {
  }

  onSubmit() {
    this.formData.emit();
  }

}
