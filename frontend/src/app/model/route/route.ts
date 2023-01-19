import {PlaceOnMap} from "../map/placeOnMap";

export class RouteDTO {
  start: PlaceOnMap;
  end: PlaceOnMap;
  intermediateStations: PlaceOnMap[];
}
