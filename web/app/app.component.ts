import {Component, OnInit} from '@angular/core';

import { UserService } from './services/user.service';
import { User } from './user';
import { Subscription } from 'rxjs/Subscription';
import {Router} from "@angular/router";


@Component({
  selector: 'main-app',
  template: `
    <div class="topBar">
      <h1>Coplan</h1>
      <ul class="navBar" *ngIf="(user !== null)">
        <a routerLink="/dashboard"><li>Dashboard</li></a>
      </ul>
      <ul class="userBar" *ngIf="(user !== null)">
        <a (click)="clickLogout()"><li>Logout</li></a><a routerLink="/whoami"><li>{{user.username}}</li></a>
      </ul>
      <ul class="userBar" *ngIf="(user === null)">
        <a routerLink="/signup"><li>Sign Up</li></a><a routerLink="/login"><li>Login</li></a>
      </ul>
    </div>
    <router-outlet></router-outlet>
  `,
  styles: [`
    .topBar {
      background-color: #333;
      height: 65px;
    }
    h1 {
      float: left;
      height: 65px;
      padding-left: 10px;
      line-height: 65px;
      display: inline-block;
    }
    ul.navBar {
      display: inline-block;
      float: left;
      margin-left: 25px;
      border-left: 1px solid #595959;
      height: 65px;
    }
    ul.navBar li {
      display: inline-block;
      height: 65px;
      line-height: 80px;
      color: #CCC;
      padding: 0 14px;
      border-right: 1px solid #595959;
    }
    
    ul li:hover {
      background-color: #444;
    }
    
    ul.userBar {
      display: inline-block;
      float: right;
      margin-right: 15px;
      height: 65px;
    }
    ul.userBar li {
      display: inline-block;
      height: 65px;
      line-height: 80px;
      color: #CCC;
      padding: 0 14px;
      border-left: 1px solid #595959;
    }
  `]
})
export class AppComponent implements OnInit {
  user: User = null;
  subscription: Subscription;

  constructor(
    private userService: UserService,
    private router: Router) { };

  clickLogout() {
    this.userService.logout();
    this.router.navigate(['/login']);
  }

  userLoggedOut() {
    this.user = null;
    this.router.navigate(['/login']);
  }

  setUser() {
    this.user = this.userService.currentUser();
    if (!this.user) {
      console.log('user not logged din');
      this.userLoggedOut();
      return;
    }

    if (this.user) {
      this.userService.refreshCurrentUser().then(
        user => {
          this.user = user;
        },
        err => {
          console.log('could not refresh current user.');
          console.log('err: ', err);
          this.router.navigate(["/login"]);
        })

        .catch((reject) => {
          console.log('oops');
          console.log('rejected: ', reject);

        });
    }
  }

  ngOnInit(): void {
    this.setUser();
    this.subscription = this.userService.userChanged.subscribe((newStatus) => {
      console.log('newStatus', newStatus);
      this.user = this.userService.currentUser();
    });

    this.userService.userLoggedOut.subscribe(() => this.userLoggedOut());
  }

}
