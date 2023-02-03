import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {AdminNoteService} from "../../services/adminNoteService/admin-note.service";
import {Note} from "../../model/adminNote/note";
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-review-and-write-admin-notes',
  templateUrl: './review-and-write-admin-notes.component.html',
  styleUrls: ['./review-and-write-admin-notes.component.scss']
})
export class ReviewAndWriteAdminNotesComponent implements OnInit {

  noteForm = new FormGroup({
    note: new FormControl('')
  });
  notes: Note[];

  constructor(@Inject(MAT_DIALOG_DATA) public data, private adminNoteService: AdminNoteService) {
    this.loadData();
  }

  ngOnInit(): void {
  }

  private loadData() {
    this.adminNoteService.getAdminNotesByUser(this.data.id).subscribe((res) => {
      this.notes = res as Note[];
    })
  }

  onSubmit() {
    if (!this.noteForm.value.note && this.noteForm.value.note.trim() === '') {
      return;
    }
    const note: Note = {text: this.noteForm.value.note, userId: this.data.id};
    this.noteForm.setValue({note: ''});
    this.adminNoteService.writeNewAdminNote(note).subscribe((res) => {
      this.loadData();
    })
  }
}
