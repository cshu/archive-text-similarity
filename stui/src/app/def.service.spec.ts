import { TestBed } from '@angular/core/testing';

import { DefService } from './def.service';

describe('DefService', () => {
  let service: DefService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DefService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
