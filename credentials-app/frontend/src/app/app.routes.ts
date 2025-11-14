import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { HomeComponent } from './home/home.component';
import { CallbackComponent } from './callback/callback.component';
import { CredentialsComponent } from './credentials/credentials.component';
import { OrganizationComponent } from './organization/organization.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'callback', component: CallbackComponent },
  { path: 'credentials', component: CredentialsComponent },
  {path: 'organization', component: OrganizationComponent },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}