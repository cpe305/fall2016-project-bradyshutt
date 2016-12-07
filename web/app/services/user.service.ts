'use strict';

import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';

import { User } from '../user';
import { AuthenticationService } from './authentication.service';
import { MessagingService } from './messaging.service';
import {Router} from "@angular/router";
import {tryCatch} from "rxjs/util/tryCatch";
import tryParse = http.tryParse;
import {Course} from "../course";

@Injectable()
export class UserService {
  private userChangedSource = new Subject<String>();
  userChanged = this.userChangedSource.asObservable();

  constructor(
    private authenticationService: AuthenticationService,
    private messagingService: MessagingService,
    private router: Router) { }

  getUsers(): Promise<User[]> {
    return Promise.resolve().then(
      () => this.messagingService.sendMessage({route: 'getAllUsers'})).then(
      (response) => response.users)
  }

  currentUser(): User {
    let userData = JSON.parse(localStorage.getItem('user')) || null;
    if (!userData) return null;
    let user = new User(userData);
    console.log('user: ', user);
    return user;
  }

  refreshCurrentUser(): Promise<User> {
    let jwt = JSON.parse(localStorage.getItem('jwt'));
    return this.messagingService.sendMessage({ route: 'getUserDetails', data: { jwt }})
      .then(
        message => {
          !!message.user &&  localStorage.setItem('user', JSON.stringify(message.user));
          !!message.jwt &&  localStorage.setItem('jwt', JSON.stringify(message.jwt));
          return Promise.resolve(message.user);
          },
        err => {
          console.log('error refreshing with the JWT');
          localStorage.removeItem('jwt');
          localStorage.removeItem('user');
          return Promise.reject("errooorr");
        });
  }


  logout(): any {
    this.authenticationService.logout();
    this.userChangedSource.next('logout');
  }

  login(username: String, password: String): any {
    return this.authenticationService.login(username, password)
      .then( success => {
        this.userChangedSource.next('login');
        return Promise.resolve(success);
      });
  }

  signUp(userData): Promise<User> {
    let message = {
      route: 'createUser',
      data: {
        username: userData.username,
        password: userData.password,
        firstName: userData.firstName,
        lastName: userData.lastName,
      }
    };

    return Promise.resolve(this.messagingService.sendMessage(message)).then(

      successResponse => {
        let userDocument = successResponse.user;
        let jwt = successResponse.jwt;
        let user: User = new User(userDocument);
        localStorage.setItem('jwt', JSON.stringify(jwt));
        localStorage.setItem('user', JSON.stringify(user));
        this.userChangedSource.next('login');
        return Promise.resolve(user);
      },

      errorResponse => {
        return Promise.reject({
          msg: "SignUp failed.",
          reason: errorResponse
        })
      }
    );

  }

  addCourse(newCourseName: String): Promise<Course> {
    let user = this.currentUser();
    let message = {
      route: 'registerForCourse',
      data: {
        username: user.username,
        jwt: JSON.parse(localStorage.getItem("jwt")),
        courseName: newCourseName
      }
    };
    console.log('pre-msg:', message);
    return this.messagingService.sendMessage(message).then(
      response => {
        let course: Course = new Course(response.course);
        this.userChangedSource.next('newCourse');
        return Promise.resolve(course);
      },
      () => Promise.reject(null)
    );
  }

}
