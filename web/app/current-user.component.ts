'use strict';


import { Component } from '@angular/core';

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
    <div *ngIf="currentUser">
      <h1>Hello, {{currentUser.firstName}}!</h1>
      <label>Username: </label> {{currentUser.username}} <br>
      <label>First Name: </label> {{currentUser.firstName}} <br>
      <label>Last Name: </label> {{currentUser.lastName}} <br>
      <label>Courses: </label> 
      <ul>
        <li *ngFor="let course of currentUser.courses">{{course}}</li>
      </ul>
    </div>
    <div *ngIf="!currentUser">
      <h1>Sorry, you aren't logged in :(</h1>
    </div>
  `
})


export class CurrentUser {
  currentUser: any;

  constructor() {
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
  }
  //user = null;
}

