'use strict';

import {Component, OnInit} from '@angular/core';
import {User} from "./user";
import {UserService} from "./services/user.service";

@Component({
  selector: 'all-users',
  template: `
    <h1>All Users</h1>
    <ul>
      <li *ngFor="let username of users">{{username}}</li>
    
    </ul>
  `

})

export class AllUsersComponent implements OnInit {
  users: User[];
  constructor(
    private userService: UserService) {}


  ngOnInit(): void {
    this.userService.getUsers().then(
      (users) => {
        console.log('success');
        console.log(users);
       this.users = users
      },
      (error) => {
        console.log('err: ', error);
      }
    )
  }

}


