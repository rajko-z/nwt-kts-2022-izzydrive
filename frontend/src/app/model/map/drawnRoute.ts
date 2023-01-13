import {Location} from "./location";

export class DrawnRoute {
  coordinates: Location[];
  color: string;
  constructor(coordinates: Location[], color: string) {
    this.coordinates = coordinates;
    this.color = color;
  }

}
