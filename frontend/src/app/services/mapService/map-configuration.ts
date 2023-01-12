import {icon} from "leaflet";
import {MarkerType} from "../../model/map/markerType";

export function createMarkerOptionsForCar(tracking: boolean, driverStatus?: string) {
  let iconUrl =  '../../../assets/blue-car.png';
  let title = driverStatus;
  if (tracking) {
    iconUrl = '../../../assets/purple-car.png';
    title = "YOUR CAR";
  }
  return {
    icon: icon({
      iconUrl: iconUrl,
      iconSize: [45,40],
      iconAnchor: [18, 45]
    }),
    title: title
  };

}

export function createMarkerOptionForIntermediateStation(markerType: MarkerType) {
  if (markerType !== MarkerType.FIRST_INTERMEDIATE &&
      markerType !== MarkerType.SECOND_INTERMEDIATE &&
      markerType !== MarkerType.THIRD_INTERMEDIATE) {
    return;
  }
  let iconUrl = '../../../assets/intermediate1.png';
  let title = 'First intermediate station';

  if (markerType == MarkerType.SECOND_INTERMEDIATE) {
    iconUrl = '../../../assets/intermediate2.png';
    title = 'Second intermediate station';
  } else if (markerType == MarkerType.THIRD_INTERMEDIATE) {
    iconUrl = '../../../assets/intermediate3.png';
    title = 'Third intermediate station';
  }
  return {
    icon: icon({
      iconUrl: iconUrl,
      iconSize: [50,50],
      iconAnchor: [18, 45],
    }),
    title: title
  };

}

export let markerOptionsStart = {
  icon: icon({
    iconUrl: '../../../assets/rocket-launch.png',
    iconSize: [50,50],
    iconAnchor: [18, 45],
  }),
  title: 'Start location'
}

export let markerOptionsEnd = {
  icon: icon({
    iconUrl: '../../../assets/race-flag.png',
    iconSize: [45,45],
    iconAnchor: [18, 45],
  }),
  title: 'End location'
}
