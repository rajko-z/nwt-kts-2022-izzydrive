import { TestBed } from '@angular/core/testing';

import { UserService } from './user-sevice.service';

describe('UserSeviceService', () => {
  let service: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
