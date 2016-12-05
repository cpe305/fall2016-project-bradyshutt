'use strict';

import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";

import { UserService } from './services/user.service';


@Component({
  moduleId: module.id,
  template: `
    <div>
      <h2>Login</h2>
      <form name="form" (ngSubmit)="login()" novalidate>
        <div class="form-group">
          <label for="username">Username</label>
          <input type="text" name="username" [(ngModel)]="model.username" />
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" name="password" [(ngModel)]="model.password" />
        </div>
        <div class="form-group">
            <button type="submit">Login</button>
            <a [routerLink]="['/register']" class="btn btn-link">Register</a>
        </div>
      </form>
    
    </div>

  `,
})
export class LoginComponent implements OnInit {
  model: any = { };
  loading = false;

  constructor(
    private router: Router,
    private userService: UserService) { }

  login() {
    this.loading = true;
    this.userService.login(this.model.username, this.model.password)
      .then(
        user => {
          console.log('success!');
          this.router.navigate(['/dashboard'])
        },
        err => {
          console.log('Communication Error.');
          console.log(err);
          this.loading = false;
        }
      );
    return false;
  }

  ngOnInit(): void {
    this.userService.logout();
    console.log('ngOnInit for login component');

    //this.router.navigate(['/login'])
  }
}



