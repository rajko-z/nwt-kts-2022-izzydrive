import { DrivingDistanceReportItem } from "./DrivingDistanceReportItem";
import { DrivingPriceReportItem } from "./DrivingPriceReportItem";
import { DrivingsNumberReportItem } from "./DrivingsNumberReportItem";

export class DrivingReport{
    drivingsNumber: Array<DrivingsNumberReportItem>;
    averageDrivingsNumber: number;
    sumDrivingsNumber: number;
    drivingPrices: Array<DrivingPriceReportItem>;
    averageDrivingPrice: number;
    sumDrivingPrice : number;
    drivingsDistances: Array<DrivingDistanceReportItem>;
    averageDrivingDistance: number;
    sumDrivingDistance: number;
}