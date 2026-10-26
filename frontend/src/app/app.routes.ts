import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { CadastrarUsuario } from './pages/cadastrar-usuario/cadastrar-usuario';
import { Home } from './pages/home/home';
import { MeusLivros } from './pages/meus-livros/meus-livros';
import { Interesses } from './pages/interesses/interesses';
import { PerfilUsuario } from './pages/perfil-usuario/perfil-usuario';
import { MeuPerfil } from './pages/meu-perfil/meu-perfil';
import { ChatComponent } from './pages/chat-component/chat-component';
import { AdminComponent } from './pages/admin/admin';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'cadastrar-usuario', component: CadastrarUsuario },
  { path: 'home', component: Home },
  { path: 'meus-livros', component: MeusLivros },
  { path: 'interesses', component: Interesses },
  { path: 'perfil/:id', component: PerfilUsuario },
  { path: 'meu-perfil', component: MeuPerfil},
  { path: 'chat', component: ChatComponent},
  { path: 'admin', component: AdminComponent },
];