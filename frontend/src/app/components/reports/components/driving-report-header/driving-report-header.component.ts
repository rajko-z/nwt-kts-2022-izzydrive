import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-driving-report-header',
  templateUrl: './driving-report-header.component.html',
  styleUrls: ['./driving-report-header.component.scss']
})
export class DrivingReportHeaderComponent  {

  constructor() { }

  @Input() averageText: string;
  @Input() averageValue: number;
  @Input() sumText: string;
  @Input() sumValue: number

}
