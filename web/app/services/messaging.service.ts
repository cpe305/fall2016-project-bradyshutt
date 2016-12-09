'use strict';

import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import {Headers, RequestOptions, RequestMethod, Request, Http} from "@angular/http";
import 'rxjs/add/operator/toPromise';

@Injectable()
export class MessagingService {

  constructor(private http: Http) {};

  sendMessage(msg: Object): Promise<any> {

    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    let opts = new RequestOptions({
      method: RequestMethod.Post,
      url: '/api/endpoint',
      headers: headers,
      body: JSON.stringify({message: msg})
    });

    console.log('sending msg');
    return this.http
      .request(new Request(opts))
      .map((response) => response.json()).toPromise()
      .then((response) => {

        // do stuff
        console.log('got a response.');
        let json = JSON.parse(response);
        console.log('json[res]: ', json['res']);

        if (json['res'] === 1)
          return json;
        else
          return json;

        //return response.json();
      })

  }
}
