import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CredentialService, Credential } from '../../services/credential.service';
import { OrgService } from '../../services/organisation.service';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { firstValueFrom } from 'rxjs';
import { KeycloakService } from '../../services/keycloak.service';
@Component({
  selector: 'app-credentials',
  imports: [FormsModule, CommonModule],
  templateUrl: './credentials.component.html',
  styleUrl: './credentials.component.scss',
})
export class CredentialsComponent implements OnInit {
  orgId: string | null = null;
  creds: Credential[] = [];
  selectedOrgs: any[] = [];
  userInfo: any; // includes email, firstName, lastName, etc.
  credential: any[] = [];
  userExists: boolean = false;
  userOrgs: any[] = [];
  clientSecret: string = '';
  organisation: any;
  clientId: any;
  isCredentialsAvailable: boolean = false;
  editId: string | null = null;
  updatedSecret = '';
  isLoading = true;

  constructor(private cs: CredentialService, private org: OrgService,
     private router: Router, private cd: ChangeDetectorRef,
  private keyCloakService: KeycloakService) { //private authService: AuthService,
    const nav = this.router.getCurrentNavigation();
    this.organisation = nav?.extras?.state?.['organisation']
    this.userInfo = nav?.extras?.state?.['userInfo']
    this.credential = nav?.extras?.state?.['credential']
  }

  async ngOnInit() {
    this.orgId = this.org.getSelectedOrg();
    const userDetails = this.keyCloakService.getIdentityClaims();
    try {
      if (this.isCredentialEmpty) {
        const credential = await this.cs.getCredentialByUser(
           userDetails['email']
        );
        if(credential.length === 0) {
           this.isCredentialsAvailable = false;
        } else {
           this.isCredentialsAvailable = true;
        }
        this.isLoading = false;
        this.credential = credential;
        this.cd.detectChanges();
      

      }
    }
    catch (err) {
      console.error('Error fetching credential', err);
      alert('Failed to fetch credential');
    }
  }


  async create() {
    const orgId = this.userInfo.organizations[0].id;
    const credential = await this.cs.create(orgId, this.userInfo?.userId, this.clientSecret) as any;
   alert(`Client secret created/updated successfully: ${credential?.clientId}`);
   this.router.navigate(['/organization']);
  }

 edit(c: any) {
  
    this.editId = c.clientId;
    this.cd.detectChanges();
    this.updatedSecret = ''; 
  }

  cancel() {
    this.editId = null;
    this.updatedSecret = '';
    this.cd.detectChanges();
  }

  async save(c: any) {
    const clientId = c.clientId;
    const updateSecretClientId = await this.cs.updateSecret(clientId, this.updatedSecret);
    alert(`Client secret created/updated successfully: ` + JSON.stringify(updateSecretClientId));
    this.keyCloakService.logout();
    //this.authService.logout();
  }

  async delete(c: any) {
   const response =  await firstValueFrom (this.cs.delete(c.clientId));
   //const userDetails = this.authService.identityClaims;
    const userDetails = this.keyCloakService.getIdentityClaims();
   if(response.status === 200){
      const credential = await this.cs.getCredentialByUser(
           userDetails['email']
        );
        if(credential.length === 0) {
           this.isCredentialsAvailable = false;
        } else {
           this.isCredentialsAvailable = true;
        }
        this.isLoading = false;
        this.credential = credential;
        this.cd.detectChanges();
   }
  }

  get isCredentialEmpty() {
    return undefined !== this.credential || null == this.credential;

  }
}
