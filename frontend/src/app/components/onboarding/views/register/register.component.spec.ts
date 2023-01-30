import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterComponent } from './register.component';
import {By} from "@angular/platform-browser";
import {BaseUserDataFormComponent} from "../../../shared/components/base-user-data-form/base-user-data-form.component";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {ReactiveFormsModule} from "@angular/forms";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {UserService} from "../../../../services/userService/user-sevice.service";


function fillFormValid(component: BaseUserDataFormComponent) {
  component.registerForm.controls['firstName'].setValue('Mila');
  component.registerForm.controls['lastName'].setValue('Milovanovic');
  component.registerForm.controls['email'].setValue('mila@gmail.com');
  component.registerForm.controls['password'].setValue('12345678');
  component.registerForm.controls['repeatedPassword'].setValue('12345678');
  component.registerForm.controls['phoneNumber'].setValue('+381652228789');
}

fdescribe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;


  const userServiceSpy = jasmine.createSpyObj<UserService>(['registration']);
  userServiceSpy.registration.and.returnValue(null);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent, BaseUserDataFormComponent],
      imports: [HttpClientTestingModule, ReactiveFormsModule, MatSnackBarModule],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('onSubmit should be called', () => {
    spyOn(component, 'onSubmit');
    const counter = fixture.debugElement.query(By.directive(BaseUserDataFormComponent));
    const cmp = counter.componentInstance;
    fillFormValid(cmp);
    cmp.register.emit(cmp.registerForm); //register emit fromGroup
    expect(component.onSubmit).toHaveBeenCalledTimes(1);
  });
});
