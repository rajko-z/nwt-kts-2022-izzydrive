import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { By } from '@angular/platform-browser';
import { UserService } from 'src/app/services/userService/user-sevice.service';

import { EmailInputComponent } from './email-input.component';

describe('EmailInputComponent', () => {
  let component: EmailInputComponent;
  let fixture: ComponentFixture<EmailInputComponent>;

  const userServiceSpy = jasmine.createSpyObj<UserService>( ['sendResetPasswordEmail']);
  userServiceSpy.sendResetPasswordEmail.and.returnValue(null);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmailInputComponent ],
      imports: [HttpClientTestingModule, MatSnackBarModule, MatDialogModule, ReactiveFormsModule ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmailInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  fit('should give a warning below email field if email is touched AND empty', () => {
    const email = component.emailForm.controls.email;
    email.markAsTouched()

    fixture.detectChanges()

    expect(email.valid).toBeFalsy();
    expect(document.getElementById('email_required_error')).toBeTruthy()
    expect(email.hasError('required')).toBeTruthy();
  })

  fit('should give a warning below email field if email is not in correct pattern', () => {
    const email = component.emailForm.controls.email;
    email.setValue('123123')

    fixture.detectChanges()

    expect(email.valid).toBeFalsy();
    expect(document.getElementById('email_pattern_error')).toBeTruthy()
    expect(email.hasError('email')).toBeTruthy();
  })

  fit('should verify email restart call to action method is executed', () => {
    // Make form valid
    const email = component.emailForm.controls.email;
    email.setValue('natasha.lakovic@gmail.com')
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

