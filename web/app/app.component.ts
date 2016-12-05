import {Component, OnInit} from '@angular/core';

import { UserService } from './services/user.service';
import { User } from './user';
import { Subscription } from 'rxjs/Subscription';


@Component({
  selector: 'main-app',
  template: `
    <div class="topBar">
      <h1>Coplan</h1>
      <ul class="navBar" *ngIf="(user !== null)">
        <a routerLink="/dashboard"><li>Dashboard</li></a>
      </ul>
      <ul class="userBar" *ngIf="(user !== null)">
        <a routerLink="/login"><li>Logout</li></a><li>{{user.username}}</li>
      </ul>
      <ul class="userBar" *ngIf="(user === null)">
        <li>Sign Up</li><a routerLink="/login"><li>Login</li></a>
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
    
    ul.navBar li:hover {
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
export class AppComponent {
  user: User = null;
  subscription: Subscription;

  constructor(userService: UserService) {
    this.user = userService.currentUser() || null;
    this.subscription = userService.userChanged.subscribe((newStatus) => {
      console.log('newStatus', newStatus);
      this.user = userService.currentUser();
    });
  }

}
