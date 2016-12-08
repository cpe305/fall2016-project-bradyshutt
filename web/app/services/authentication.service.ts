'use strict';

import { Injectable } from '@angular/core';
import {Http, Headers, Response, RequestOptions, RequestMethod, Request} from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import {User} from "../user";


@Injectable()
export class AuthenticationService {

  constructor(private http: Http) {};

  login(username: String, pass: String): Promise<User> {
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    let requestOpts = new RequestOptions({
      method: RequestMethod.Post,
      url: '/api/authenticate',
      headers: headers,
      body: JSON.stringify({
        username: username,
        password: pass
      })
    });

    return new Promise((resolve, reject) => {
      this.http.request(new Request(requestOpts)).subscribe(
        res => {
          // If jwt is present in response, login was successful.
          let json = JSON.parse(res.json());
          let jwt = json.jwt;
          let user = json.user;
          if (user && jwt) {
            localStorage.setItem('jwt', JSON.stringify(jwt));
            localStorage.setItem('user', JSON.stringify(user));
            console.log('login success');
            resolve(new User(user));
          } else {
            reject("Invalid username or password.");
          }
        },
        err => {
          console.log('wtf');
        }
      );
    });
  }

  logout() {
    localStorage.removeItem('user');
    localStorage.removeItem('jwt');
  }

}



