import {CarType} from "./carType";
import {CarAccommodation} from "./carAccommodation";

export class Car {
  registration: string;
  model: string;
  maxPassengers: number;
  carType : CarType;
  carAccommodation: CarAccommodation;
}

