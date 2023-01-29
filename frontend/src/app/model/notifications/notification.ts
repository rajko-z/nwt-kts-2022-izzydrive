import { Car } from "../car/car";
import { Driver } from "../driver/driver";
import {NotificationStatus} from "./notificationStatus";

export class NotificationM {
  id?:number;
  userEmail: string;
  message: string;
  startLocation?: string;
  endLocation?: string;
  intermediateLocations?: string[];
  duration?: number;
  price?: number;
  reservationTime?: string;
  notificationStatus: NotificationStatus;
  creationDate: string;
  driverEmail?:string;
  drivingId?:number;
  driverData: Driver;
  carData: Car;
  driverStr: string;
  carStr: string;
}
