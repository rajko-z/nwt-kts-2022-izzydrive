import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { Car } from 'src/app/model/car/car';

@Component({
  selector: 'app-base-car-data-form',
  templateUrl: './base-car-data-form.component.html',
  styleUrls: ['./base-car-data-form.component.scss']
})
export class BaseCarDataFormComponent implements OnInit {

  @Input() existingCarData : Car;
  isDisabled: boolean = true;

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
    if (this.existingCarData){
      this.carForm.controls.carType.setValue(this.existingCarData.carType.toLowerCase())
      this.carForm.controls.model.setValue(this.existingCarData.model)
      this.carForm.controls.registration.setValue(this.existingCarData.registration)
      this.carForm.controls.maxPassengers.setValue(this.existingCarData.maxPassengers)
      //this.carForm.controls.carAccommodation.setValue(this.existingCarData.carAccommodation)
      if(this.existingCarData.carAccommodation){
        this.carAccommodation.controls.baby.setValue(this.existingCarData.carAccommodation.baby)
        this.carAccommodation.controls.food.setValue(this.existingCarData.carAccommodation.food)
        this.carAccommodation.controls.baggage.setValue(this.existingCarData.carAccommodation.baggage)
        this.carAccommodation.controls.pet.setValue(this.existingCarData.carAccommodation.pet)
      }
    }

    console.log(this.carForm.invalid)
    this.isDisabled = this.driverForm? this.carForm.invalid || this.driverForm.invalid : this.carForm.invalid;
  }
}
