import {CarType} from "./carType";
import {CarAccommodation} from "./carAccommodation";

export class Car {
  id: number;
  registration: string;
  model: string;
  maxPassengers: number;
  carType : CarType;
  carAccommodation: CarAccommodation;
  accommodations: string;
}

export const carImageMapper = {
  [CarType.AVERAGE]: 'avarage-car.png',
  [CarType.REGULAR]: 'regular-car.png',
  [CarType.PREMIUM]: 'premium-car.png',

}
