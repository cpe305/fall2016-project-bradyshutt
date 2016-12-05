'use strict';

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { UserService } from './services/user.service';
import { User } from './user';

@Component({
  selector: 'main-content',
  template: `
    <div class="main-content-wrapper">
      <h2>main content!</h2>
      <h3>Users:</h3>
      <ul>
        <li *ngFor="let user of users">
        {{user.username}}
        </li>
      </ul>
    </div>
  `,
  styles: [`
    .main-content-wrapper {
      float: left;
      padding: 10px;
      background-color: cyan;
    }
  `]
})

export class MainContent implements OnInit {
  user: User;
  constructor(
    private userService: UserService,
    private router: Router) {}

  ngOnInit(): void {
    this.user = this.userService.currentUser();
    if (!this.user)
      this.router.navigate(['/login']);
  }
}


