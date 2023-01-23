import {Car} from "../car/car";
import {Location} from "../map/location";

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
  location?: Location;
}
