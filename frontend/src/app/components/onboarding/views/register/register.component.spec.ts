import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterComponent } from './register.component';
import {By} from "@angular/platform-browser";
import {BaseUserDataFormComponent} from "../../../shared/components/base-user-data-form/base-user-data-form.component";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {ReactiveFormsModule} from "@angular/forms";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {UserService} from "../../../../services/userService/user-sevice.service";

fdescribe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;


  const userServiceSpy = jasmine.createSpyObj<UserService>(['registration']);
  userServiceSpy.registration.and.returnValue(null);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [HttpClientTestingModule, ReactiveFormsModule, MatSnackBarModule],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // it('should be called with whatever the counter change emits', () => {
  //   spyOn(component, 'onSubmit');
  //   const counter = fixture.debugElement.query(By.directive(BaseUserDataFormComponent));
  //   const cmp = counter.componentInstance;
  //   cmp.onSubmit.emit(null);
  //   expect(component.onSubmit).toHaveBeenCalledTimes(1);
  // });
});
