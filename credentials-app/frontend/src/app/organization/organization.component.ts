import { Component , OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { OrgService } from '../../services/organisation.service';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';
import { CredentialService } from '../../services/credential.service';

@Component({
  selector: 'app-organization',
  imports: [ CommonModule, FormsModule ],
  templateUrl: './organization.component.html',
  styleUrl: './organization.component.scss',
})
export class OrganizationComponent implements OnInit {
  availableOrgs: any[] = [];
  selectedOrgs: any[] = [];
  userInfo: any; // includes email, firstName, lastName, etc.
  firstName: string = '';
  lastName: string = '';
  userExists: boolean = false;
  userOrgs: any[] = [];
selectedOrg: any = null;
  constructor(private router: Router, private orgService: OrgService, private authService: AuthService, private credentialService: CredentialService) {
    const nav = this.router.getCurrentNavigation();
    const state = nav?.extras?.state;
    this.availableOrgs = state?.['availableOrgs'].organizations || [];
    this.userInfo = state?.['availableOrgs'];
  }

  ngOnInit(): void {
    
    if(this.userInfo?.userId){
      this.userExists = true;
      this.userOrgs = this.userInfo.organizations;
    }
  }

toggleSelection(org: any) {
  const exists = this.selectedOrgs.find(o => o.orgId === org.orgId);
  if (exists) {
    // Remove the org if already selected
    this.selectedOrgs = this.selectedOrgs.filter(o => o.orgId !== org.orgId);
  } else {
    // Add the org object to the selected list
    this.selectedOrgs.push(org);
  }
}

  async saveSelections() {
    const userDetails = this.authService.identityClaims;
    
    const payload = {
      email: userDetails['sub'],
      firstName: this.firstName,
      lastName: this.lastName,
      organizations: this.selectedOrgs.map(org => ({
        id: org.id,
        name: org.name
      }))
    };

    try {
      const response = await this.orgService.saveSelectedOrganizations(payload);
    
      this.router.navigate(['/credentials'],  { state: {
          organisation: response,
          userInfo: this.userInfo,
          credential: null,
        }});
    } catch (err) {
      console.error(err);
      console.error('Error saving organizations.');
    }
  }

  async navigateToCredentials(organisation: any) {
    this.router.navigate(['/credentials'], { state: { organisation: organisation } });
  }

  selectOrg(org: any) {
    this.selectedOrg = org;
  }

  async goToCredential() {
    if (!this.selectedOrg) return;
    const userDetails = this.authService.identityClaims;
    try {
      const credential = await this.credentialService.getCredentialByOrg(
        this.selectedOrg.id, userDetails['sub']
      );

      // navigate with state: include credential if exists
      this.router.navigate(['/credentials'], {
        state: {
          organisation: this.selectedOrg,
          userInfo: this.userInfo,
          credential: credential || null,
        },
      });
    } catch (err) {
      console.error('Error fetching credential', err);
      alert('Failed to fetch credential');
    }
  }

  get userOrgsArray() {
  return Object.values(this.availableOrgs || {});
}
}
