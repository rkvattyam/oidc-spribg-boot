import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environments/environment';

export interface Credential {
  id: string;
  name: string;
  username: string;
  secret?: string;
  orgId?: string;
}

@Injectable({ providedIn: 'root' })
export class CredentialService {
  base = environment.apiBase;

  constructor(private http: HttpClient) { }



  create(organizationId: number, userId: number, clientSecret: string) {

    const payload = {
      userId: userId,
      organizationId: organizationId,
      clientSecret: clientSecret
    }
    return this.http.post(`${environment.apiBaseUrl}/credentials`, payload).toPromise();
  }


  delete(clientId: string) {
    return this.http.delete(`${environment.apiBaseUrl}/credentials/${clientId}`,{
    observe: 'response'
  });

  }

  getCredentialByOrg(orgId: number, email: string) {
    
    return this.http
      .get<any>(`${environment.apiBaseUrl}/credentials/${orgId}?email=${email}`)
      .toPromise()
      .catch(() => null);
  }

  getCredentialByUser(email: string) {
    // returns the credential if exists, else null
    return this.http
      .get<any>(`${environment.apiBaseUrl}/credentials/user?email=${email}`)
      .toPromise()
      .catch(() => null); 
  }

  saveCredential(orgId: number, credential: any) {
    return this.http.post(`${environment.apiBaseUrl}/credentials/`, credential).toPromise();
  }

  updateSecret(clientId: string, updatedSecret: string) {
    const body = { newSecret: updatedSecret };
    return this.http.put(`${environment.apiBaseUrl}/credentials/${clientId}/secret`,
    body, { responseType: 'text'}).toPromise();
  }
}
