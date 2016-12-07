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

  login() {
    this.loading = true;
    this.userService.login(this.model.username, this.model.password)
      .then(
        user => {
          console.log('success!');
          this.router.navigate(['/dashboard'])
        },
        err => {
          this.incorrectPassword = true;
          this.loading = false;
        }
      );
    return false;
  }

  ngOnInit(): void {
    this.userService.logout();
  }
}



