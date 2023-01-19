import { Injectable } from '@angular/core';
import { Evaluation } from 'src/app/model/evaluation/evaluation';
import { TextResponse } from 'src/app/model/response/textresponse';
import { environment } from 'src/environments/environment';
import { HttpClientService } from '../custom-http/http-client.service';

@Injectable({
  providedIn: 'root'
})
export class EvaluationService {

  constructor(private httpClientService: HttpClientService,) { }

  addNewEvaluation(evaluation: Evaluation) {
    return this.httpClientService.postT<TextResponse>(environment.apiUrl + 'evaluations/add', evaluation);
  }
}
