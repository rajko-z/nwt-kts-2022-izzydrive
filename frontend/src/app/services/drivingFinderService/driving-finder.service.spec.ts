import { TestBed } from '@angular/core/testing';

import { DrivingFinderService } from './driving-finder.service';

describe('DrivingFinderService', () => {
  let service: DrivingFinderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DrivingFinderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
