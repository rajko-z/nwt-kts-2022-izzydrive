import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DrivingReport } from 'src/app/model/reports/DrivingReport';
import { ReportRequest } from 'src/app/model/reports/ReportRequest';
import { environment } from 'src/environments/environment';
import { HttpClientService } from '../custom-http/http-client.service';
import { UserService } from '../userService/user-sevice.service';

@Injectable({
  providedIn: 'root'
})
export class ReportsService {

  constructor(private http: HttpClientService, 
    private userService : UserService) { }

    getPassengerReportData(startDate: Date, endDate: Date): Observable<DrivingReport>{
      let userId : number = this.userService.getCurrentUserId();
    let reportRequest : ReportRequest = new ReportRequest(userId, startDate, endDate);
      return this.http.postT<DrivingReport>(environment.apiUrl + `drivings/reports-passenger`, reportRequest);
    }

}
