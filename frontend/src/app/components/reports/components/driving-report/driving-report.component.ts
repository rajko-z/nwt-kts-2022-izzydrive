import { Component, Input, OnChanges, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import { th } from 'date-fns/locale';
import {
ChartComponent,
ApexAxisChartSeries,
ApexChart,
ApexXAxis,
ApexDataLabels,
ApexTitleSubtitle,
ApexStroke,
ApexGrid
} from "ng-apexcharts";

export type ChartOptions = {
series: ApexAxisChartSeries;
chart: ApexChart;
xaxis: ApexXAxis;
dataLabels: ApexDataLabels;
grid: ApexGrid;
stroke: ApexStroke;
title: ApexTitleSubtitle;
colors: string[]
};
@Component({
  selector: 'app-driving-report',
  templateUrl: './driving-report.component.html',
  styleUrls: ['./driving-report.component.scss']
})
export class DrivingReportComponent implements OnInit {

  @Input() XAxisValues : string[];
  @Input() SourceValue: number[];
   @Input() title: string;

  ngOnInit(): void {
    this.chartOptions = {
      colors: ["#6B2167"],
      series: [
        {
          data: [...this.SourceValue]
        }
      ],
      chart: {
        height: 350,
        type: "line",
        zoom: {
          enabled: false
        },
      },
      dataLabels: {
        enabled: false
      },
      stroke: {
        curve: "smooth"
      },
      title: {
        text: this.title,
        align: "left",
        style: {
          color: "#191A23",
          fontWeight: 500,
          fontSize: "16px"
        }
      },
      grid: {
        row: {
          colors: ["#faf1fc", "transparent"], // takes an array which will be repeated on columns
          opacity: 0.5
        }
      },
      xaxis: {
        categories: [...this.XAxisValues]
      },

    };
  }

  @ViewChild("chart") chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;

  constructor() {}

}
