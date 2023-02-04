import { MatDialogModule } from '@angular/material/dialog';
import { FacebookLoginProvider, SocialAuthServiceConfig } from '@abacritt/angularx-social-login';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import firebase from 'firebase/compat/app';
import { environment } from 'src/environments/environment';
import { ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { UserService } from 'src/app/services/userService/user-sevice.service';
import { of } from 'rxjs';


import { LoginComponent } from './login.component';

const mockLoggedInUser = {
  token: '12345321312412312',
  user: {
    id: 123,
    email: 'mock',
    firstName: 'mock',
    lastName: 'mock',
    phoneNumber: '123',
    activated: true,
    imageName: 'mock',
    blocked: true,
  }
}

class MockUserService extends UserService {

  /**
   * This method is implemented in the AuthService
   * we extend, but we overload it to make sure we
   * return a value we wish to test against
   */
  regularLogin() {
      return of(mockLoggedInUser);
  }
}

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LoginComponent ],
      imports: [ HttpClientTestingModule, MatSnackBarModule, MatDialogModule, ReactiveFormsModule ],
      providers: [
        {
          provide: 'SocialAuthServiceConfig',
          useValue: {
            autoLogin: false,
            providers: [
              {
                id: FacebookLoginProvider.PROVIDER_ID,
                provider: new FacebookLoginProvider('430844285930561'),
              },
            ],
          } as SocialAuthServiceConfig,
        },
        { provide: UserService, useClass: MockUserService}
      ]
    })
    .compileComponents();
    firebase.initializeApp(environment.firebaseConfig);
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
  afterEach(() => {
    fixture.destroy();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  fit('should give a warning below email field if email is touched AND empty', () => {
    const email = component.loginForm.controls.email;
    email.markAsTouched()
    expect(email.valid).toBeFalsy();
    expect(document.getElementById('email_error')).toBeTruthy()
    expect(email.hasError('required')).toBeTruthy();
  })

  fit('should give a warning below email field if email is not in correct pattern', () => {
    const email = component.loginForm.controls.email;
    email.setValue('123123')
    expect(email.valid).toBeFalsy();
    expect(document.getElementById('email_error')).toBeTruthy()
    expect(email.hasError('email')).toBeTruthy();
  })

  fit('should give a warning below email field if password is touched AND empty', () => {
    const password = component.loginForm.controls.email;
    password.markAsTouched()
    expect(password.valid).toBeFalsy();
    expect(document.getElementById('password_error')).toBeTruthy()
    expect(password.hasError('required')).toBeTruthy();
  })

  fit('should test show and hide password button', () => {
    // Set up a password to enable button to be clicked
    const hide = component.hide
    let button = fixture.debugElement.query(By.css(".icon"));
    component.loginForm.controls.password.setValue('12345');

    // Detect changes to enable the button while selecting it
    fixture.detectChanges()

    // Spying on switching state to capture function call
    spyOn(component, 'switchHide');

    // Expect before call to action button is clicked
    expect(hide).toBeTrue()
    expect(document.getElementById('password_input').getAttribute('type')).toBe('password')

    // Actual click on DOM
    button.nativeElement.click()

    // Detect DOM changes due to click
    fixture.detectChanges()

    // After component lifecycle change is detected check if spy has detected call and new values are set
    fixture.whenStable().then(() => {
      expect(component.switchHide).toHaveBeenCalledTimes(1)
      expect(component.hide).toBeFalse()
      expect(document.getElementById('password_input').getAttribute('type')).toBe('text')
    });
  })

  fit('should check if button is disabled while form is invalid', () => {
    // Initially login form is invalid
    expect(component.loginForm.valid).toBeFalse()
    const button = document.getElementById('submit_form')

    // If value is empty button is disabled
    expect(button.getAttribute('disabled')).toBe('')

    component.loginForm.setValue({
      email: 'natasha.lakovic@gmail.com',
      password: '123123123',
    })

    fixture.detectChanges()
    // If disabled value is null button is NOT disabled
    expect(button.getAttribute('disabled')).toBe(null)
    expect(component.loginForm.valid).toBeTrue()
  })

  fit('should test if reset password callback is being called', () => {
    // Set up a password to enable button to be clicked
    let button = fixture.debugElement.query(By.css(".reset-password-link"));

    // Spying on password reset capture callback call
    spyOn(component, 'onResetPassword');

    // Actual click on DOM
    button.nativeElement.click()

    // Detect DOM changes due to click
    fixture.detectChanges()

    // After component lifecycle change is detected check if spy has detected call and new values are set
    fixture.whenStable().then(() => {
      expect(component.onResetPassword).toHaveBeenCalledTimes(1)
    });
  })

  fit('should verify user logged in and set correct session storage params', () => {
    // Make form valid
    component.loginForm.setValue({
      email: 'natasa.lakovic@gmail.com',
      password: '12345678',
    })

    const service = fixture.debugElement.injector.get(UserService);
    spyOn(service, 'regularLogin').and.returnValue(of(mockLoggedInUser))

    component.onSubmit()
    fixture.detectChanges()

    expect(sessionStorage.getItem('currentUser')).toBe(JSON.stringify({email:"mock", token:"12345321312412312",id:123}))
    sessionStorage.clear()
  })
});
