import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-perfil-usuario',
  imports: [],
  templateUrl: './perfil-usuario.html',
  styleUrl: './perfil-usuario.css',
})
export class PerfilUsuario {

  constructor(
  private route: ActivatedRoute,

) {}

ngOnInit() {

  const id = Number(this.route.snapshot.paramMap.get('id'));
}
}
