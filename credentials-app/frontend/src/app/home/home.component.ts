import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { OrgService, Org } from '../../services/organisation.service';
import { Router } from '@angular/router';
import { CommonModule, JsonPipe } from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [CommonModule, JsonPipe],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent implements OnInit {
  orgs: Org[] = [];
  selectedOrg: string | null = null;
  claims: any;
 userInfo: any;
  constructor(public auth: AuthService, private orgService: OrgService, private router: Router) { }

  ngOnInit() {
    this.orgService.listOrgs().subscribe(list => this.orgs = list);
    this.selectedOrg = this.orgService.getSelectedOrg();
   if (this.auth.isLoggedIn) {
      this.userInfo = this.auth.identityClaims;
    }
  }

  select(o: Org) {
    this.orgService.selectOrg(o.id);
    this.selectedOrg = o.id;
  }

  login() {
    this.auth.login();
  }

  logout() {
    this.auth.logout();
  }

 // goCredentials() { this.router.navigate(['/credentials']); }

}
