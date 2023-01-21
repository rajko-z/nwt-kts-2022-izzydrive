import {PlaceOnMap} from "../map/placeOnMap";

export class RouteDTO {
  id: number;
  start: PlaceOnMap;
  end: PlaceOnMap;
  intermediateStations: PlaceOnMap[];
}
