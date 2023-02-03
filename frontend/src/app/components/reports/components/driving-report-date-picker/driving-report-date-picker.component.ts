import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-driving-report-date-picker',
  templateUrl: './driving-report-date-picker.component.html',
  styleUrls: ['./driving-report-date-picker.component.scss']
})
export class DrivingReportDatePickerComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  maxDate: Date = new Date(); 
  @Output() formEmiter = new EventEmitter<FormGroup>();

  
  dateForm = new FormGroup({
    startDate: new FormControl<Date | null>(null),
    endDate: new FormControl<Date | null>(null),
  });

  onSubmit(){
    this.formEmiter.emit(this.dateForm)
  }
 

}
