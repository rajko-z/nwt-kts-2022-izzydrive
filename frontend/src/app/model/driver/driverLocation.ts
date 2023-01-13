import {Location} from "../map/location";

export class DriverLocation {
  driverEmail: string;
  driverStatus: string;
  location: Location;

  constructor(driverEmail: string, driverStatus: string, location: Location) {
    this.driverEmail = driverEmail;
    this.driverStatus = driverStatus;
    this.location = location;
  }
}
