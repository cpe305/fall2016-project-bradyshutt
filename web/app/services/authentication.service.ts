'use strict';

import { Injectable } from '@angular/core';
import {Http, Headers, Response, RequestOptions, RequestMethod, Request} from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';


@Injectable()
export class AuthenticationService {

  constructor(private http: Http) {};

  login(uname: String, pass: String) {
    console.log(uname);
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    let requestOpts = new RequestOptions({
      method: RequestMethod.Post,
      url: '/api/authenticate',
      headers: headers,
      body: JSON.stringify({
        username: uname,
        password: pass
      })
    });

    return new Promise((resolve, reject) => {
      this.http.request(new Request(requestOpts))
        .subscribe((res) => {
          // If jwt is present in response, login was successful.
          console.log('res:', res);
          let json = res.json();
          console.log('json:', json);
          let jwt = json['jwt'];
          let user = json['user'];
          if (user && jwt) {
            localStorage.setItem('jwt', JSON.stringify(jwt));
            localStorage.setItem('user', JSON.stringify(user));
            resolve(user);
          } else {
            reject("Invalid username or password.");
          }
        });
    });
  }

  logout() {
    localStorage.removeItem('user');
    localStorage.removeItem('jwt');
  }

}



