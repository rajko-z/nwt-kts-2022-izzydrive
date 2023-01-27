export class FavoriteRoute{
  routeId : number
  startLocation: string;
  endLocation: string;
  intermediateLocations? : Array<String>

  constructor(
    routeId? : number,
    startLocation?: string,
    endLocation?: string,
    intermediateLocations? : Array<String>,
    ){
      this.routeId = routeId;
      this.startLocation = startLocation;
      this.endLocation = endLocation;
      this.intermediateLocations = intermediateLocations;
    }
}
