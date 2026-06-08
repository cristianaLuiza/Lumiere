import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Interesses } from './interesses';

describe('Interesses', () => {
  let component: Interesses;
  let fixture: ComponentFixture<Interesses>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Interesses],
    }).compileComponents();

    fixture = TestBed.createComponent(Interesses);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
