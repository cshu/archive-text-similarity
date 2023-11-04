import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';
import { RandomToken } from './random-token';
import { SimResp } from './sim-resp';
import { Similarity } from './similarity';

@Injectable({
  providedIn: 'root'
})
export class DefService {
  //private path: string = "/newtext";

  constructor(private http: HttpClient,) { }

  get url(): string {
    //return (location.port === '4200' ? 'http://localhost:8888' : '') + this.path;
    return location.protocol+'//'+location.hostname+':8081';
  }
  token(): Observable<RandomToken> {
    return this.http.post<RandomToken>(this.url + '/token', '');
  }
  sim(sseid: string, hashinhex: string): Observable<SimResp> {
    return this.http.post<SimResp>(this.url+'/sim', {id: sseid, hash: hashinhex});
  }
  //send(sid: string, filename: string, text: Blob): Observable<string> {
    //let hdrs = new HttpHeaders();
    //hdrs = hdrs.append('X-arg-sid', sid);
    //hdrs = hdrs.append('X-arg-filename', filename);
    //hdrs = hdrs.append('Content-Type', 'application/octet-stream');
    //return this.http.post<string>(this.url, text, {headers: hdrs});
  //}
}
