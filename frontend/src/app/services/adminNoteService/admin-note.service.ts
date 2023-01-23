import {Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClientService} from "../custom-http/http-client.service";
import {Note} from "../../model/adminNote/note";

@Injectable({
  providedIn: 'root'
})
export class AdminNoteService {

  constructor(private httpClientService: HttpClientService) {
  }

  getAdminNotesByUser(id: number) {
    return this.httpClientService.get(environment.apiUrl + `admin-notes/${id}`);
  }

  writeNewAdminNote(note: Note) {
    return this.httpClientService.post(environment.apiUrl + `admin-notes` , note);
  }
}
