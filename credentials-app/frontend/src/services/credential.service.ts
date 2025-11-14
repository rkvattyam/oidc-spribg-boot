import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environments/environment';
import { Observable, of } from 'rxjs';

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

  constructor(private http: HttpClient) {}

  list(orgId?: string): Observable<Credential[]> {
    // Real: return this.http.get<Credential[]>(`${this.base}/orgs/${orgId}/credentials`);
    // Mock data:
    return of([
      { id: 'c1', name: 'DB Admin', username: 'admin', secret: '••••••', orgId: 'org-a' },
      { id: 'c2', name: 'Service Account', username: 'svc', secret: '••••••', orgId: 'org-b' }
    ].filter(c => !orgId || c.orgId === orgId));
  }

  create(organizationId: number, userId: number, clientSecret: string) {
    // Real: return this.http.post(`${this.base}/orgs/${orgId}/credentials`, payload);
    const payload = {
        userId: userId,
  organizationId: organizationId,
  clientSecret: clientSecret
    }
    return this.http.post(`${environment.apiBaseUrl}/credentials`, payload).toPromise();
  }

  update(orgId: string, id: string, payload: Partial<Credential>) {
    // Real: return this.http.put(`${this.base}/orgs/${orgId}/credentials/${id}`, payload);
    return of({ ...payload, id, orgId });
  }

  delete(orgId: string, id: string) {
    // Real: return this.http.delete(`${this.base}/orgs/${orgId}/credentials/${id}`);
    return of({ success: true });
  }

  getCredentialByOrg(orgId: number, email: string) {
    // returns the credential if exists, else null
    return this.http
      .get<any>(`${environment.apiBaseUrl}/credentials/${orgId}?email=${email}`)
      .toPromise()
      .catch(() => null); // return null if 404
  }

  saveCredential(orgId: number, credential: any) {
    return this.http.post(`${environment.apiBaseUrl}/credentials/`, credential).toPromise();
  }

    updateSecret(clientId: string, updatedSecret: string) {
    return this.http.put(`${environment.apiBaseUrl}/credentials/${clientId}/secret`, null, {
      params: { newSecret: updatedSecret },
      responseType: 'text' 
    }).toPromise();
  }
}
