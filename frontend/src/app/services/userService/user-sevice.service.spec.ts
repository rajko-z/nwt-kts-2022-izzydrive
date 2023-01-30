import {getTestBed, TestBed} from '@angular/core/testing';
import {UserService} from './user-sevice.service';
import {NewUser} from "../../model/user/user";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {environment} from "../../../environments/environment";

const dummyUser: NewUser = {
  firstName: "Mile",
  lastName: "Mitrovic",
  email: "mile@gmail.com",
  password: "12345678",
  repeatedPassword: "12345678",
  phoneNumber: "+381654434545"
}

fdescribe('UserSeviceService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;
  let injector: TestBed;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
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
});
