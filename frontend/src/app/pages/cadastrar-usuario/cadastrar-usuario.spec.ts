import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CadastrarUsuario } from './cadastrar-usuario';

describe('CadastrarUsuario', () => {
  let component: CadastrarUsuario;
  let fixture: ComponentFixture<CadastrarUsuario>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CadastrarUsuario],
    }).compileComponents();

    fixture = TestBed.createComponent(CadastrarUsuario);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
