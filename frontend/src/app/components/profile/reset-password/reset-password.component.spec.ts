import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { By } from '@angular/platform-browser';
import { UserService } from 'src/app/services/userService/user-sevice.service';

import { ResetPasswordComponent } from './reset-password.component';

describe('ResetPasswordComponent', () => {
  let component: ResetPasswordComponent;
  let fixture: ComponentFixture<ResetPasswordComponent>;

  const userServiceSpy = jasmine.createSpyObj<UserService>(['verifyResetPasswordToken']);
  userServiceSpy.verifyResetPasswordToken.and.returnValue(null);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ResetPasswordComponent ],
      imports: [HttpClientTestingModule, MatSnackBarModule, MatDialogModule, ReactiveFormsModule ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResetPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  fit('should give a warning below email field if new password is touched AND empty', () => {
    const password = component.passwordForm.controls.newPassword;
    password.markAsTouched()

    fixture.detectChanges()

    expect(password.valid).toBeFalsy();
    expect(document.getElementById('new_required')).toBeTruthy()
    expect(password.hasError('required')).toBeTruthy();
  })

  fit('should test show and hide new password button', () => {
    // Set up a password to enable button to be clicked
    const hide = component.hideNewPassword
    let button = fixture.debugElement.query(By.css(".icon"));
    component.passwordForm.controls.newPassword.setValue('12345');

    // Detect changes to enable the button while selecting it
    fixture.detectChanges()

    // Expect before call to action button is clicked
    expect(hide).toBeTrue()
    expect(document.getElementById('new_password_input').getAttribute('type')).toBe('password')

    // Actual click on DOM
    button.nativeElement.click()

    // Detect DOM changes due to click
    fixture.detectChanges()

    // After component lifecycle change is detected check if spy has detected call and new values are set
    fixture.whenStable().then(() => {
      expect(component.hideNewPassword).toBeFalse()
      expect(document.getElementById('new_password_input').getAttribute('type')).toBe('text')
    });
  })

  fit('should give a warning below email field if repeated password is less then 8 chars', () => {
    const password = component.passwordForm.controls.newPassword;
    password.setValue('123123')

    fixture.detectChanges()

    expect(password.valid).toBeFalsy();
    expect(document.getElementById('new_min_length')).toBeTruthy()
    expect(password.hasError('minlength')).toBeTruthy();
  })

  fit('should give a warning below email field if new password is touched AND empty', () => {
    const password = component.passwordForm.controls.repeatedPassword;
    password.markAsTouched()

    fixture.detectChanges()

    expect(password.valid).toBeFalsy();
    expect(document.getElementById('repeat_required')).toBeTruthy()
    expect(password.hasError('required')).toBeTruthy();
  })

  fit('should give a warning below email field if repeated password is less then 8 chars', () => {
    const password = component.passwordForm.controls.repeatedPassword;
    password.setValue('123123')

    fixture.detectChanges()

    expect(password.valid).toBeFalsy();
    expect(document.getElementById('repeat_min_length')).toBeTruthy()
    expect(password.hasError('minlength')).toBeTruthy();
  })

  fit('should check if both new password and repeated pasword match', () => {
    component.passwordForm.setValue({
      newPassword: 'natasa12345',
      repeatedPassword: 'natasa1234',
    })
    fixture.detectChanges()

    let button = fixture.debugElement.query(By.css(".submit-button"));
    button.nativeElement.click()

    fixture.detectChanges()

    expect(document.getElementById('no-match')).toBeTruthy()
    expect(component.passwordForm.controls.repeatedPassword.hasError('noMatch')).toBeTruthy();
  })

  fit('should test show and hide old password button', () => {
    // Set up a password to enable button to be clicked
    const hide = component.hideRepeatPassword
    let button = fixture.debugElement.query(By.css(".icon"));
    component.passwordForm.controls.newPassword.setValue('12345');

    // Detect changes to enable the button while selecting it
    fixture.detectChanges()

    // Expect before call to action button is clicked
    expect(hide).toBeTrue()
    expect(document.getElementById('repeat_password_input').getAttribute('type')).toBe('password')

    // Actual click on DOM
    button.nativeElement.click()

    // Detect DOM changes due to click
    fixture.detectChanges()

    // After component lifecycle change is detected check if spy has detected call and new values are set
    fixture.whenStable().then(() => {
      expect(component.hideNewPassword).toBeFalse()
      expect(document.getElementById('repeat_password_input').getAttribute('type')).toBe('text')
    });
  })

  fit('should make button disabled if there are errors in form', () => {
    component.passwordForm.setValue({
      newPassword: '123',
      repeatedPassword: '12345',
    })
    fixture.detectChanges()
    const button = document.getElementById('submit-button')

    // If value is empty button is disabled
    expect(button.getAttribute('disabled')).toBe('')

    component.passwordForm.setValue({
      newPassword: '123',
      repeatedPassword: '123',
    })
    fixture.detectChanges()
    expect(button.getAttribute('disabled')).toBe('')

    component.passwordForm.setValue({
      newPassword: '12345678',
      repeatedPassword: '12345678',
    })
    fixture.detectChanges()
    expect(button.getAttribute('disabled')).toBe(null)
  })

  fit('should verify email restart call to action method is executed', () => {
    // Make form valid
    component.passwordForm.setValue({
      newPassword: '12345678',
      repeatedPassword: '12345678',
    })
    fixture.detectChanges()

    // Spy on CTA
    spyOn(component, 'onSubmit').and.callThrough();

    // Get a button and click it
    let button = fixture.debugElement.query(By.css(".submit-button"));
    button.nativeElement.click()

    fixture.detectChanges()

    expect(component.onSubmit).toHaveBeenCalledTimes(1)
  })
});
