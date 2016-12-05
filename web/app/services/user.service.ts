'use strict';

import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';

import { User } from '../user';
import { AuthenticationService } from './authentication.service';
import { MessagingService } from './messaging.service';

@Injectable()
export class UserService {
  private userChangedSource = new Subject<String>();
  userChanged = this.userChangedSource.asObservable();

  constructor(
    private authenticationService: AuthenticationService,
    private messagingService: MessagingService
  ) { }

  getUsers(): User[] {
    return null;
  }

  getUser(id: Number): User {
    return null;
  }

  currentUser(): User {
    let user = localStorage.getItem('currentUser');
    if (user) {
      return JSON.parse(user);
    } else {
      return null;
    }
  }

  logout(): any {
    this.authenticationService.logout();
    this.userChangedSource.next("logout");
  }

  login(username: String, password: String): any {
    return this.authenticationService.login(username, password)
      .then( success => {
        this.userChangedSource.next("login");
        return Promise.resolve(success);
      });
  }

  addCourse(newCourseName: String): any {
    let user = this.currentUser();
    let message = {
      route: 'registerForCourse',
      body: {
        username: user.username,
        jwt: localStorage.getItem("jwt"),
        courseName: newCourseName
      }
    };
    console.log('pre-msg:', message)
    return this.messagingService.sendMessage(message).then(
      (response) => {
        console.log('response:', response);
      },
      (err) => {
        console.log('err :0');
      }
    )
  }
}
