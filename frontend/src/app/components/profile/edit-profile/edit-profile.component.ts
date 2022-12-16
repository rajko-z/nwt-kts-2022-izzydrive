import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.scss']
})
export class EditProfileComponent implements OnInit {

  name_regexp = "^[a-zA-Z]+$";

  editForm: FormGroup = new FormGroup({ firstName: new FormControl('',[Validators.required, Validators.pattern(this.name_regexp)]),
  lastName: new FormControl('',[Validators.required, Validators.pattern(this.name_regexp)]),
  email: new FormControl('', [Validators.email, Validators.required]),
  phoneNumber: new FormControl('',[Validators.required, Validators.pattern("^[+][0-9]*$"),
                                                        Validators.minLength(13), 
                                                        Validators.maxLength(13)]),
    
  });

  constructor() { }

  ngOnInit(): void {
    //pozvati metodu za dobavljanje postojecih podataka i postaviti ih u place holdere
  }

}
