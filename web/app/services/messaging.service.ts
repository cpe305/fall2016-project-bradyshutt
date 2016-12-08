'use strict';

import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import {Headers, RequestOptions, RequestMethod, Request, Http} from "@angular/http";

@Injectable()
export class MessagingService {

  constructor(private http: Http) {};

  sendMessage(msg: Object): Promise<any> {

    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    let requestOpts = new RequestOptions({
      method: RequestMethod.Post,
      url: '/api/endpoint',
      headers: headers,
      body: JSON.stringify({message: msg})
    });

    return new Promise((resolve, reject) => {
      this.http.request(new Request(requestOpts))
        .subscribe((res) => {
          let json = JSON.parse(res.json());
          console.log('json[res]: ', json['res']);
          if (json['res'] === 1)
            resolve(json);
          else
            reject(json)
        });
    });
  }


}
