import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { WelcomeComponent } from './pages/welcome/welcome.component';

export const routes: Routes = [
    { path: 'login', component: LoginComponent},
    { path: '', component: WelcomeComponent},
];
