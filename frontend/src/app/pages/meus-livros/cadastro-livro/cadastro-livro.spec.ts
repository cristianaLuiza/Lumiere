import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CadastroLivro } from './cadastro-livro';

describe('CadastroLivro', () => {
  let component: CadastroLivro;
  let fixture: ComponentFixture<CadastroLivro>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CadastroLivro],
    }).compileComponents();

    fixture = TestBed.createComponent(CadastroLivro);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
