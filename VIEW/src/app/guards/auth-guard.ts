import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { SesionService } from '../services/sesion.service';

export const authGuard: CanActivateFn = (route, state) => {
  const sesionService = inject(SesionService);
  const router = inject(Router);

  if (sesionService.isAutenticado()) return true;
  return router.createUrlTree(['/login']);
};
