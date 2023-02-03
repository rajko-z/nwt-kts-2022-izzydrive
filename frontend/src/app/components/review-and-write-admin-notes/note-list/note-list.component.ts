import {Component, Input, OnInit} from '@angular/core';
import {Note} from "../../../model/adminNote/note";

@Component({
  selector: 'app-note-list',
  templateUrl: './note-list.component.html',
  styleUrls: ['./note-list.component.scss']
})
export class NoteListComponent {
  @Input() notes:  Note[];

  constructor() { }

}
