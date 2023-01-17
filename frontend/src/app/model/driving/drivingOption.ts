import {Driver} from "../driver/driver";
import {CalculatedRoute} from "../map/calculatedRoute";
import {Location} from "../map/location";

export class DrivingOption {
  driver: Driver;
  driverCurrentLocation: Location;
  timeForWaiting: number;
  price: number;
  driverToStartPath: CalculatedRoute;
  startToEndPath: CalculatedRoute;
  reservation: boolean;
}
