import { Routes } from '@angular/router';
import { authGuard } from './guards/auth-guard';
import { Login} from './pages/login/login';
import { Registro } from './pages/registro/registro';
import { Clientes } from './pages/clientes/clientes';
import { AprobacionCupoInicial } from './pages/aprobacion-cupo-inicial/aprobacion-cupo-inicial';
import { Compras } from './pages/compras/compras';
import { Restricciones } from './pages/restricciones/restricciones';
import { SolicitudesSobrecupo } from './pages/solicitudes-sobrecupo/solicitudes-sobrecupo';
import { VistaCliente} from './pages/vista-cliente/vista-cliente';
import { VistaPareja} from './pages/vista-pareja/vista-pareja';

export const routes: Routes = [
   { path: '', redirectTo: 'login', pathMatch: 'full' },
   { path: 'login', component: Login },
   { path: 'registro', component: Registro },
   { path: 'clientes', component: Clientes, canActivate: [authGuard] },
   { path: 'aprobacion-cupo-inicial', component: AprobacionCupoInicial, canActivate: [authGuard] },
   { path: 'compras', component: Compras, canActivate: [authGuard] },
   { path: 'restricciones', component: Restricciones, canActivate: [authGuard] },
   { path: 'solicitudes-sobrecupo', component: SolicitudesSobrecupo, canActivate: [authGuard] },
   { path: 'vista-cliente', component: VistaCliente, canActivate: [authGuard] },
   { path: 'vista-pareja', component: VistaPareja, canActivate: [authGuard] },
   { path: '**', redirectTo: 'login' }
];
