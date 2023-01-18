export class NotificationM {
  userEmail: string;
  message: string;
  startLocation?: string;
  endLocation?: string;
  intermediateLocations?: string[];
  duration?: number;
  price?: number;
  drivingId?: number;
  reservationTime?: Date;
}
