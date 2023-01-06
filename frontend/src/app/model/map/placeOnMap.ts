import {MarkerType} from "./markerType";

export class PlaceOnMap {
  longitude: number;
  latitude: number;
  name: string;
  markerType?: MarkerType
  constructor(longitude: number, latitude: number, markerType?: MarkerType, name?: string) {
    this.longitude = longitude;
    this.latitude = latitude;
    this.markerType = markerType;
    this.name = name;
  }
}
