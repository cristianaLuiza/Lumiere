import { TestBed } from '@angular/core/testing';

import { Interesse } from './interesse';

describe('Interesse', () => {
  let service: Interesse;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Interesse);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
