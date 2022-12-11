import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-base-car-data-form',
  templateUrl: './base-car-data-form.component.html',
  styleUrls: ['./base-car-data-form.component.scss']
})
export class BaseCarDataFormComponent implements OnInit {

  carRegistrationPattern: string = "^[a-zA-Z]{2,2}[-][0-9]{3,5}[-][a-zA-Z]{2,2}$";
  maxPassengersPattern: string = "^[0-9]{1,2}$"

  carAccommodation = this.formBuilder.group({
    baby: false,
    food: false,
    pet: false,
    baggage: false
  });

  carForm = new FormGroup({
    registration: new FormControl('',[Validators.required, Validators.pattern(this.carRegistrationPattern)]),
    model: new FormControl('',[Validators.required]),
    maxPassengers: new FormControl(1, [Validators.required, Validators.pattern(this.maxPassengersPattern)]),
    carType: new FormControl('regular', [Validators.required]),
    carAccommodation: this.carAccommodation
  });

  
  @Output() formData = new EventEmitter<FormGroup>();
  @Input() driverForm? : FormGroup;
  
  onSubmit(){
    this.formData.emit(this.carForm);
  }

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit(): void {
  }

}
