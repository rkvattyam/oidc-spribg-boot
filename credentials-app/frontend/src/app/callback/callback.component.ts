import { Component, OnInit, AfterViewInit  } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { OrgService } from '../../services/organisation.service';
import { Router } from '@angular/router';
import { CommonModule, JsonPipe } from '@angular/common';

@Component({
  selector: 'app-callback',
  imports: [CommonModule, JsonPipe],
  templateUrl: './callback.component.html',
  styleUrl: './callback.component.scss',
})
export class CallbackComponent  implements AfterViewInit { //implements OnInit
  claims: any;
  constructor(private auth: AuthService, private router: Router, private orgService: OrgService) {
    // library already processed login in AuthService.configure()
if (this.auth.isLoggedIn) {
        console.log('claims constructor')
        this.claims = this.auth.identityClaims;
      }

  }
  // async ngOnInit() {
  //   try {
  //     // Handle OIDC token processing
  //   //  const val = await this.auth.loginCallback();
  //     const loggedIn = await this.auth.loginCallback();

  //     if (!loggedIn) {
  //       console.error('Login failed: no token received');
  //       this.router.navigate(['/']);
  //       return;
  //     }

  //     // Tokens are now guaranteed to be available
  //     const accessToken = this.auth.accessToken;
  //     const idToken = this.auth.idToken;
  //     console.log('Access Token:', accessToken);
  //     console.log('ID Token:', idToken);
  //     // Call backend to check user existence
  //     const userInfo = await this.orgService.checkUserExistence(this.claims?.sub);

  //     if (!userInfo.exists) {
  //       // New user → show all orgs to select from
  //       this.router.navigate(['/org-select'], { state: { availableOrgs: userInfo.availableOrgs } });
  //     } else {
  //       // Existing user
  //       const orgs = userInfo.organizations || [];
  //       if (orgs.length === 1) {
  //         this.router.navigate([`/org/${orgs[0].id}`]);
  //       } else {
  //         this.router.navigate(['/org-select'], { state: { availableOrgs: orgs } });
  //       }
  //     }
  //   } catch (err) {
  //     console.error('Login callback failed', err);
  //     this.router.navigate(['/']);
  //   }
  // }

   async ngAfterViewInit() {
  const success = await this.auth.finalizeLogin();
  this.claims = this.auth.identityClaims;
  const userInfo = await this.orgService.checkUserExistence(this.claims?.sub);

      if (!userInfo.userId) {
         // New user → show all orgs to select from
         this.router.navigate(['/organization'], { state: { availableOrgs: userInfo } });
       } else {
         // Existing user
        const orgs = userInfo.organizations || [];
         if (orgs.length === 1) {
           this.router.navigate(['/credentials'], { state: { userInfo: userInfo } });
         } else {
          this.router.navigate(['/organization'], { state: { availableOrgs: userInfo } });
         }
        }
  //this.router.navigate([success ? '/dashboard' : '/login']);
}

}
