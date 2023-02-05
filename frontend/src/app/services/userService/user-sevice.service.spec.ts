import {getTestBed, TestBed} from '@angular/core/testing';
import {UserService} from './user-sevice.service';
import {NewUser, User} from "../../model/user/user";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {environment} from "../../../environments/environment";
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {LoginResponse} from "../../model/response/loginResponse";

const dummyUser: NewUser = {
  firstName: "Mile",
  lastName: "Mitrovic",
  email: "mile@gmail.com",
  password: "12345678",
  repeatedPassword: "12345678",
  phoneNumber: "+381654434545"
}

const mockLoginData = {
  username: "name",
  password: "123"
}

const mockUser: User = {
  email: "email",
  firstName: "firstName",
  lastName: "lastName",
  phoneNumber: "+381654434545",
}

const mockLoginResponse: LoginResponse = {
  user: mockUser,
  token: "token"
}

fdescribe('UserSeviceService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;
  let injector: TestBed;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MatSnackBarModule],
      providers: [UserService],
    });

    injector = getTestBed();
    service = injector.inject(UserService);
    httpMock = injector.get(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('registration() should be success', () => {
    service.registration(dummyUser).subscribe((res) => {
      expect(res).toEqual("Success");
    });
    const req = httpMock.expectOne(environment.apiUrl + 'passengers/registration/');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(dummyUser);
    req.flush("Success");
  });

  it('registration() should not be success', () => {
    service.registration(dummyUser).subscribe({
      next: _ => {
      },
      error: (error) => {
        expect(error).toEqual("User with the same email already exits.");
      }
    });
    const req = httpMock.expectOne(environment.apiUrl +'passengers/registration/');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(dummyUser);
    req.flush("User with the same email already exits.");
  });

  it('regular login() should be success', () => {
    service.regularLogin(mockLoginData).subscribe((res) => {
      expect(res).toEqual(mockLoginResponse);
    });
    const req = httpMock.expectOne(environment.apiUrl + 'auth/login');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockLoginData);
    req.flush(mockLoginResponse);
  });

  it('regular login() should not be success', () => {
    service.regularLogin(mockLoginData).subscribe({
      next: _ => {
      },
      error: (error) => {
        expect(error).toEqual("Invalid username or password.");
      }
    });
    const req = httpMock.expectOne(environment.apiUrl + 'auth/login');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockLoginData);
    req.flush("Invalid username or password");
  });
});
