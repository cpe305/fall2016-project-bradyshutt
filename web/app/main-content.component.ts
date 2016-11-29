'use strict';

import {Component, OnInit} from '@angular/core';
import {UserService} from "./user.service";
import {User} from "./user";

@Component({
  selector: 'main-content',
  template: `
    <h2>main content!</h2>
    <h3>Users:</h3>
    <ul>
      <li *ngFor="let user of users">
      {{user.username}}
      </li>
    
</ul>
  `
})

export class MainContent implements OnInit {
  users: User[];
  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.users = this.userService.getUsers();
  }
}


