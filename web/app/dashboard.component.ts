'use strict';

import { Component, Injectable, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { UserService } from './services/user.service';
import { User } from './user';

@Component({
  selector: 'dashboard',
  template: `
    <side-bar></side-bar>
    <main-content></main-content>
  `,
})

export class Dashboard implements OnInit {
  private user: User;

  constructor(
    private userService: UserService,
    private router: Router) {}

  ngOnInit(): void {
    this.user = this.userService.currentUser();
    if (!this.user)
      this.router.navigate(['/login']);
  }




}