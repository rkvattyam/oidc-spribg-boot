import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environments/environment';
import { Observable, of } from 'rxjs';

export interface Org { id: string; name: string; }

@Injectable({ providedIn: 'root' })
export class OrgService {
  base = environment.apiBase;

  constructor(private http: HttpClient) {}

   checkUserExistence(email: string) {
    return this.http.get<any>(`${environment.apiBaseUrl}/organizations/user/${email}`).toPromise();
  }

  selectOrganization(orgId: string) {
    return this.http.post(`${environment.apiBaseUrl}/users/select-org`, { orgId }).toPromise();
  }
  
  // For demo, get organizations from API
  listOrgs(): Observable<Org[]> {
    // replace with real API: return this.http.get<Org[]>(`${this.base}/orgs`);
    return of([
      { id: 'org-a', name: 'Org A' },
      { id: 'org-b', name: 'Org B' }
    ]);
  }

  selectOrg(orgId: string) {
    // In real app you might persist selection to backend or to localStorage
    localStorage.setItem('selectedOrg', orgId);
  }

  getSelectedOrg(): string | null {
    return localStorage.getItem('selectedOrg');
  }

  saveSelectedOrganizations(payload: any) {
    return this.http.post(`${environment.apiBaseUrl}/users`, payload).toPromise();
  }


}
