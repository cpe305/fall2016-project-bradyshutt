'use strict';

import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";

import { UserService } from './services/user.service';
import {User} from "./user";


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
        <div class="incorrectPassword" *ngIf="(incorrectPassword)">
          <p>* Incorrect username or password.</p>
        </div>
        <div class="form-group">
            <button type="submit">Login</button>
        </div>
      </form>
      <div>
        <p>Don't have an account? <a [routerLink]="['/signup']" class="btn btn-link">Sign up here</a>.</p>
      </div>
            
    </div>

  `,
  styles: [`
    .incorrectPassword {
      color: red;
    }
  `]
})
export class LoginComponent implements OnInit {
  model: any = { };
  loading = false;
  incorrectPassword: boolean = false;

  constructor(
    private router: Router,
    private userService: UserService) { }

  login(): Promise<User> {
    this.loading = true;
    return this.userService.login(this.model.username, this.model.password)
      .then(
        user => {
          this.router.navigate(['/dashboard']);
          return user;
        },
        err => {
          this.incorrectPassword = true;
          this.loading = false;
          return err;
        }
      );
  }

  ngOnInit(): void {
    this.userService.logout();
  }
}



