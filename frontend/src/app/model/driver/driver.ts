import {Car} from "../car/car";

export enum DriverStatus {
  FREE,
  TAKEN,
  ACTIVE,
  RESERVED
}

export class Driver {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  driverStatus: DriverStatus;
  carData: Car;
}
