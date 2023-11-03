import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DefService {
  private path: string = "/newtext";

  constructor(private http: HttpClient,) { }

  get url(): string {
    return (location.port === '4200' ? 'http://localhost:8888' : '') + this.path;
  }
  send(sid: string, filename: string, text: Blob): Observable<string> {
    let hdrs = new HttpHeaders();
    hdrs = hdrs.append('X-arg-sid', sid);
    hdrs = hdrs.append('X-arg-filename', filename);
    hdrs = hdrs.append('Content-Type', 'application/octet-stream');
    return this.http.post<string>(this.url, text, {headers: hdrs});
  }
}
