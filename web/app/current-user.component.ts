'use strict';


import { Component } from '@angular/core';
import {UserService} from "./services/user.service";
import {User} from "./user";

const MOCK_USER = {
  username: 'bshutt',
  firstName: 'Brady',
  lastName: 'Shutt',
  courses: [
    'CPE-305',
    'PHYS-133',
    'CSC-445',
    'PHIL-331'
  ]
};

@Component({
  selector: 'whoami',
  template: `
    <div *ngIf="user">
      <h1>Hello, {{user.firstName}}!</h1>
      <label>Username: </label> {{user.username}} <br>
      <label>First Name: </label> {{user.firstName}} <br>
      <label>Last Name: </label> {{user.lastName}} <br>
      <label>Courses: </label> 
      <ul>
        <li *ngFor="let course of user.courses">{{course}}</li>
      </ul>
    </div>
    <div *ngIf="!user">
      <h1>Sorry, you aren't logged in :(</h1>
    </div>
  `
})


export class CurrentUser {
  user: User;

  constructor(private userService: UserService) {
    this.user = this.userService.currentUser();
  }
}

