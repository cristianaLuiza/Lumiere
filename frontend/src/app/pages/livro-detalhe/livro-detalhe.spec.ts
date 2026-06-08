import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LivroDetalhe } from './livro-detalhe';

describe('LivroDetalhe', () => {
  let component: LivroDetalhe;
  let fixture: ComponentFixture<LivroDetalhe>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LivroDetalhe],
    }).compileComponents();

    fixture = TestBed.createComponent(LivroDetalhe);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
