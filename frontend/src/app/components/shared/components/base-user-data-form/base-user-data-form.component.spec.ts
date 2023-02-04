import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BaseUserDataFormComponent} from './base-user-data-form.component';
import {UserService} from "../../../../services/userService/user-sevice.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {By} from "@angular/platform-browser";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

function fillFormValid(component: BaseUserDataFormComponent) {
  component.registerForm.controls['firstName'].setValue('Mila');
  component.registerForm.controls['lastName'].setValue('Milovanovic');
  component.registerForm.controls['email'].setValue('mila@gmail.com');
  component.registerForm.controls['password'].setValue('12345678');
  component.registerForm.controls['repeatedPassword'].setValue('12345678');
  component.registerForm.controls['phoneNumber'].setValue('+381652228789');
}

function fillFormInvalid(component: BaseUserDataFormComponent) {
  component.registerForm.controls['firstName'].setValue('');
  component.registerForm.controls['lastName'].setValue('');
  component.registerForm.controls['email'].setValue('');
  component.registerForm.controls['password'].setValue('');
  component.registerForm.controls['repeatedPassword'].setValue('');
  component.registerForm.controls['phoneNumber'].setValue('');
}

fdescribe('BaseUserDataFormComponent', () => {
  let component: BaseUserDataFormComponent;
  let fixture: ComponentFixture<BaseUserDataFormComponent>;

  //if return value is null then property isRegistration is true and this component use for registration new user
  const userServiceSpy = jasmine.createSpyObj<UserService>(['getRoleCurrentUserRole']);
  userServiceSpy.getRoleCurrentUserRole.and.returnValue(null);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BaseUserDataFormComponent],
      imports: [HttpClientTestingModule, ReactiveFormsModule, FormsModule, MatSnackBarModule],
    })
      .compileComponents();

    fixture = TestBed.createComponent(BaseUserDataFormComponent);
    component = fixture.componentInstance;
    component.ngOnInit();
    fixture.detectChanges();
  });

  afterEach(() => {
    fixture.destroy();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('check form init value', () => {
    const registrationFormValues = {
      firstName: '',
      lastName: '',
      email: '',
      password: '',
      repeatedPassword: '',
      phoneNumber: ''
    }
    expect(component.registerForm.value).toEqual(registrationFormValues);
    expect(component.registerForm.errors).toBeNull();
  })

  it('form should be invalid', () => {
    fillFormInvalid(component);
    expect(component.registerForm.valid).toBeFalsy();
  })

  it('form should be valid', () => {
    fillFormValid(component);

    expect(component.registerForm.valid).toBeTruthy();
    expect(component.registerForm.invalid).toBeFalsy();

  })

  it('should submit button is disable', () => {
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css(".submit-button"));
    expect(button.nativeElement.disabled).toBeTruthy();
  })

  it('should submit button is not disable', () => {
    fillFormValid(component);

    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css(".submit-button"));
    expect(button.nativeElement.disabled).toBeFalsy();
  })

  it('should field name with space is invalid', () => {
    const name = component.registerForm.controls.firstName;
    expect(name.valid).toBeFalsy();

    name.setValue(' ');
    expect(name.hasError('pattern')).toBeTruthy();
  })

  it('should field name is touched', () => {
    const name = component.registerForm.controls.firstName;
    name.markAsTouched();

    expect(name.hasError('required')).toBeTruthy();
  })

  it('should field name is invalid', () => {
    const name = component.registerForm.controls.firstName;
    expect(name.valid).toBeFalsy();

    name.setValue('Mića');

    expect(name.hasError('pattern')).toBeTruthy();
  })

  it('should field name is invalid - invalid character', () => {
    const name = component.registerForm.controls.firstName;
    expect(name.valid).toBeFalsy();

    name.setValue('Mica.');

    expect(name.hasError('pattern')).toBeTruthy();
  })

  it('should field lastname is empty', () => {
    const lastname = component.registerForm.controls.lastName;
    expect(lastname.valid).toBeFalsy();

    lastname.setValue('');
    expect(lastname.hasError('required')).toBeTruthy();
  })

  it('should field lastname is touched', () => {
    const lastname = component.registerForm.controls.lastName;
    lastname.markAsTouched();

    expect(lastname.hasError('required')).toBeTruthy();
  })

  it('should field lastname is invalid', () => {
    const lastname = component.registerForm.controls.lastName;
    expect(lastname.valid).toBeFalsy();

    lastname.setValue('Micić');

    expect(lastname.hasError('pattern')).toBeTruthy();
  })

  it('should field email is empty', () => {
    const email = component.registerForm.controls.email;
    expect(email.valid).toBeFalsy();

    email.setValue('');
    expect(email.hasError('required')).toBeTruthy();
  })

  it('should field email is touched', () => {
    const email = component.registerForm.controls.email;
    email.markAsTouched();

    expect(email.hasError('required')).toBeTruthy();
  })

  it('should field email is invalid', () => {
    const email = component.registerForm.controls.email;
    expect(email.valid).toBeFalsy();

    email.setValue('someEmail123');

    expect(email.hasError('email')).toBeTruthy();
  })

  it('should field email is valid', () => {
    const email = component.registerForm.controls.email;
    expect(email.valid).toBeFalsy();

    email.setValue('some@gmail'); //validator for email on front is not a same as validator on back

    expect(email.hasError('email')).toBeFalse();
  })

  it('should field phoneNumber is empty', () => {
    const phoneNumber = component.registerForm.controls.phoneNumber;
    expect(phoneNumber.valid).toBeFalsy();

    phoneNumber.setValue('');
    expect(phoneNumber.hasError('required')).toBeTruthy();
  })

  it('should field phoneNumber is touched', () => {
    const phoneNumber = component.registerForm.controls.phoneNumber;
    phoneNumber.markAsTouched();

    expect(phoneNumber.hasError('required')).toBeTruthy();
  })

  it('should field phoneNumber is invalid - text', () => {
    const phoneNumber = component.registerForm.controls.phoneNumber;
    expect(phoneNumber.valid).toBeFalsy();

    phoneNumber.setValue('someNumber./');

    expect(phoneNumber.hasError('pattern')).toBeTruthy();
  })

  it('should field phoneNumber is invalid - number', () => {
    const phoneNumber = component.registerForm.controls.phoneNumber;
    expect(phoneNumber.valid).toBeFalsy();

    phoneNumber.setValue('+12');

    expect(phoneNumber.hasError('pattern')).toBeTruthy();
  })

  it('should field phoneNumber is valid', () => {
    const phoneNumber = component.registerForm.controls.phoneNumber;
    expect(phoneNumber.valid).toBeFalsy();

    phoneNumber.setValue('+38112228979');

    expect(phoneNumber.hasError('pattern')).toBeFalsy();
  })

  it('should field password and repeatedPassword is empty', () => {
    const password = component.registerForm.controls.password;
    expect(password.valid).toBeFalsy();

    const repeatedPassword = component.registerForm.controls.password;
    expect(repeatedPassword.valid).toBeFalsy();

    password.setValue('');
    expect(password.errors['required']).toBeTruthy();

    repeatedPassword.setValue('');
    expect(repeatedPassword.errors['required']).toBeTruthy();
  })

  it('should field password and repeatedPassword is touched', () => {
    const password = component.registerForm.controls.password;
    password.markAsTouched();

    expect(password.errors['required']).toBeTruthy();

    const repeatedPassword = component.registerForm.controls.repeatedPassword;
    repeatedPassword.markAsTouched();

    expect(repeatedPassword.errors['required']).toBeTruthy();
  })

  it('should field password and repeatedPassword do not have 8 characters', () => {
    const password = component.registerForm.controls.password;
    expect(password.valid).toBeFalsy();
    password.setValue('1234567');

    expect(password.errors['minlength']).toBeTruthy();

    const repeatedPassword = component.registerForm.controls.repeatedPassword;
    expect(repeatedPassword.valid).toBeFalsy();

    repeatedPassword.setValue('1');

    expect(repeatedPassword.errors['minlength']).toBeTruthy();
  })

  it('should field email has correct value', () => {
    const email = 'mila@gmail.com';
    const emailControl = component.registerForm.controls.email;

    emailControl.setValue(email);
    expect(emailControl.value).toEqual(email);
  })

  it('should click on submit button', () => {
    fillFormValid(component);
    let button = fixture.debugElement.query(By.css(".submit-button"));
    expect(button.nativeElement.disabled).toBeTruthy();

    spyOn(component, 'onSubmit');
    button.nativeElement.dispatchEvent(new Event('click'));
    fixture.whenStable().then(() => {
      expect(component.onSubmit).toHaveBeenCalledTimes(1);
    });
  })

  it('should click on eye button repeatedPassword', () => {
    const button = fixture.debugElement.query(By.css('button[name="repeatedPassword"]'));
    button.nativeElement.dispatchEvent(new Event('click'));

    fixture.whenStable().then(() => {
      expect(component.hidePassword).toEqual(true);
    });
  })

  it('should click on eye button password', () => {
    const button = fixture.debugElement.query(By.css('button[name="password"]'));
    button.nativeElement.dispatchEvent(new Event('click'));

    fixture.whenStable().then(() => {
      expect(component.hideRepeatPassword).toEqual(true);
    });
  })
});
