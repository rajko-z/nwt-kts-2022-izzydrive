import { TestBed } from '@angular/core/testing';

import { SearchPlaceComponentService } from './search-place-component.service';

describe('SearchPlaceComponentService', () => {
  let service: SearchPlaceComponentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SearchPlaceComponentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
