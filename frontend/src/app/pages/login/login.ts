import { Component } from '@angular/core';
import { RouterModule } from '@angular/router'; // 1. Importação que estava faltando

@Component({
  selector: 'app-login',
  standalone: true, // Garante que o Angular reconheça o formato Standalone
  imports: [RouterModule], // 2. O RouterModule aqui libera o uso do routerLink no HTML
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {}
