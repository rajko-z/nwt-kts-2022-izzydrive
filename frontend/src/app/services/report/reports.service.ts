import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {DrivingReport} from 'src/app/model/reports/DrivingReport';
import {ReportRequest} from 'src/app/model/reports/ReportRequest';
import {environment} from 'src/environments/environment';
import {HttpClientService} from '../custom-http/http-client.service';
import {UserService} from '../userService/user-sevice.service';

@Injectable({
  providedIn: 'root'
})
export class ReportsService {

  constructor(private http: HttpClientService) {
  }

  getPassengerReportData(startDate: Date, endDate: Date, userId: number): Observable<DrivingReport> {
    const reportRequest: ReportRequest = new ReportRequest(userId, startDate, endDate);
    return this.http.postT<DrivingReport>(environment.apiUrl + `drivings/reports-passenger`, reportRequest);
  }

  getDriverReportData(startDate: Date, endDate: Date, userId: number): Observable<DrivingReport> {
    const reportRequest: ReportRequest = new ReportRequest(userId, startDate, endDate);
    return this.http.postT<DrivingReport>(environment.apiUrl + `drivings/reports-driver`, reportRequest);
  }

  getAdminReportData(startDate: Date, endDate: Date): Observable<DrivingReport> {
    const reportRequest: ReportRequest = new ReportRequest(null, startDate, endDate);
    return this.http.postT<DrivingReport>(environment.apiUrl + `drivings/reports-admin`, reportRequest);
  }


}
