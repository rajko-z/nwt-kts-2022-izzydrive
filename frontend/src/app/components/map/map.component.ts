import {Component, OnInit} from '@angular/core';
import {geoJson, latLng, LayerGroup, Marker, marker, tileLayer} from "leaflet";
import {MapService} from "../../services/mapService/map.service";
import {PlaceOnMap} from "../../model/map/placeOnMap";
import {MarkerType} from "../../model/map/markerType";
import {DrawnRoute} from "../../model/map/drawnRoute";
import {DriverLocation} from "../../model/driver/driverLocation";
import {
  createMarkerOptionForIntermediateStation,
  createMarkerOptionsForCar,
  markerOptionsEnd,
  markerOptionsStart
} from "../../services/mapService/map-configuration";

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {

  options = {
    layers: [
      tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 18,
        attribution: '...',
      }),
    ],
    zoom: 14,
    center: latLng(45.253434, 19.831323),
  };
  mainGroup: LayerGroup[] = [];
  startMarkerLayer: LayerGroup;
  endMarkerLayer: LayerGroup;
  firstIntermediateLayer: LayerGroup;
  secondIntermediateLayer: LayerGroup;
  thirdIntermediateLayer: LayerGroup;
  driverLocationsLayer: LayerGroup;

  currentTrackingDriverEmail: string = null;
  routesLayer: LayerGroup = new LayerGroup();

  constructor(private mapService: MapService) { }

  ngOnInit(): void {
    this.fetchNewDriverLocationsPeriodically();

    this.mapService.placeToAdd.subscribe(p => p != null && this.addPlaceToMap(p));
    this.mapService.placeToRemove.subscribe(p => p != null && this.removePlaceFromMap(p));
    this.mapService.routeToDraw.subscribe(r => r != null && this.drawRoute(r));
    this.mapService.drawnRoutesToRemove.subscribe(_ => this.removeAllDrawnRoutes());
    this.mapService.currentDriverEmailToTrack.subscribe(e => e != null && this.setCurrentTrackingDriverEmail(e));
  }

  private setCurrentTrackingDriverEmail(driverEmail: string) {
    this.currentTrackingDriverEmail = driverEmail;
    this.fetchNewDriverLocationsAndAddToMap();
  }

  private fetchNewDriverLocationsPeriodically() {
    //setInterval(() => this.fetchNewDriverLocationsAndAddToMap(), 2000);
    this.fetchNewDriverLocationsAndAddToMap();
  }

  private fetchNewDriverLocationsAndAddToMap() {
    this.mapService.getAllDriverLocations().subscribe(p => this._addAllDriversToMap(p));
  }

  private drawRoute(drawnRoute: DrawnRoute) {
    let coordinates: number[][] = drawnRoute.coordinates.map(l => [l.lon, l.lat]);
    let geoJsonObject = {
      "type": "Feature",
      "geometry": {
        "type": "LineString",
        "coordinates": coordinates
      }
    };
    // @ts-ignore
    let routeLayer = geoJson(geoJsonObject);
    routeLayer.setStyle({ color: drawnRoute.color });
    routeLayer.addTo(this.routesLayer);
    this.mainGroup = [...this.mainGroup, this.routesLayer];
  }

  private removeAllDrawnRoutes() {
    this.mainGroup = this.mainGroup.filter((lg: LayerGroup) => lg !== this.routesLayer);
    this.routesLayer = new LayerGroup();
  }

  private _addAllDriversToMap(points: DriverLocation[]) {
    this.removeAllDriversFromMap();

    this.driverLocationsLayer = new LayerGroup();
    points.forEach(dl => {
      let markerPoint: Marker;
      if (dl.driverEmail === this.currentTrackingDriverEmail) {
        markerPoint = marker([dl.location.lat, dl.location.lon], createMarkerOptionsForCar(true));
      } else {
        markerPoint = marker([dl.location.lat, dl.location.lon], createMarkerOptionsForCar(false, dl.driverStatus));
      }
      markerPoint.addTo(this.driverLocationsLayer);
    });

    this.mainGroup = [...this.mainGroup, this.driverLocationsLayer];
  }

  private removeAllDriversFromMap() {
    this.mainGroup = this.mainGroup.filter((lg: LayerGroup) => lg !== this.driverLocationsLayer);
    delete this.driverLocationsLayer;
  }

  private addPlaceToMap(place: PlaceOnMap) {
    if (place.markerType === MarkerType.START) {
      this.startMarkerLayer = new LayerGroup()
      let markerPoint = marker([place.latitude, place.longitude], markerOptionsStart);
      markerPoint.addTo(this.startMarkerLayer);
      this.mainGroup = [...this.mainGroup, this.startMarkerLayer];
    }
    else if (place.markerType === MarkerType.END) {
      this.endMarkerLayer = new LayerGroup()
      let markerPoint = marker([place.latitude, place.longitude], markerOptionsEnd);
      markerPoint.addTo(this.endMarkerLayer);
      this.mainGroup = [...this.mainGroup, this.endMarkerLayer];
    }
    else if (place.markerType === MarkerType.FIRST_INTERMEDIATE) {
      this.firstIntermediateLayer = new LayerGroup();
      let markerPoint = marker([place.latitude, place.longitude], createMarkerOptionForIntermediateStation(place.markerType));
      markerPoint.addTo(this.firstIntermediateLayer);
      this.mainGroup = [...this.mainGroup, this.firstIntermediateLayer];
    }
    else if (place.markerType === MarkerType.SECOND_INTERMEDIATE) {
      this.secondIntermediateLayer = new LayerGroup();
      let markerPoint = marker([place.latitude, place.longitude], createMarkerOptionForIntermediateStation(place.markerType));
      markerPoint.addTo(this.secondIntermediateLayer);
      this.mainGroup = [...this.mainGroup, this.secondIntermediateLayer];
    }
    else if (place.markerType === MarkerType.THIRD_INTERMEDIATE) {
      this.thirdIntermediateLayer = new LayerGroup();
      let markerPoint = marker([place.latitude, place.longitude], createMarkerOptionForIntermediateStation(place.markerType));
      markerPoint.addTo(this.thirdIntermediateLayer);
      this.mainGroup = [...this.mainGroup, this.thirdIntermediateLayer];
    }
  }

  private removePlaceFromMap(place: PlaceOnMap) {
    if (place.markerType === MarkerType.START) {
      this.mainGroup = this.mainGroup.filter((lg: LayerGroup) => lg !== this.startMarkerLayer);
      delete this.startMarkerLayer;
    }
    else if (place.markerType === MarkerType.END) {
      this.mainGroup = this.mainGroup.filter((lg: LayerGroup) => lg !== this.endMarkerLayer);
      delete this.endMarkerLayer;
    }
    else if (place.markerType === MarkerType.FIRST_INTERMEDIATE) {
      this.mainGroup = this.mainGroup.filter((lg: LayerGroup) => lg !== this.firstIntermediateLayer);
      delete this.firstIntermediateLayer;
    }
    else if (place.markerType === MarkerType.SECOND_INTERMEDIATE) {
      this.mainGroup = this.mainGroup.filter((lg: LayerGroup) => lg !== this.secondIntermediateLayer);
      delete this.secondIntermediateLayer;
    }
    else if (place.markerType === MarkerType.THIRD_INTERMEDIATE) {
      this.mainGroup = this.mainGroup.filter((lg: LayerGroup) => lg !== this.thirdIntermediateLayer);
      delete this.thirdIntermediateLayer;
    }
  }
}
