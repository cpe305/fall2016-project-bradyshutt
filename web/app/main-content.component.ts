'use strict';

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { UserService } from './services/user.service';
import { User } from './user';
import {Course} from "./course";

@Component({
  selector: 'main-content',
  template: `
    <div class="main-content-wrapper">
      <div *ngIf="(selectedCourse == null)">
        <h3>Select a course on the left, or create a new one!</h3>
      </div>
      <div *ngIf="(selectedCourse)">
        <h3>Pins for: {{selectedCourse.name}}</h3>
          <button (click)="createPinInit()" *ngIf="(!addingPin)">Add a Pin</button>
            <div class="addPinWrapper" *ngIf="addingPin">
              <h3>Add a new pin:</h3>
              <label>Title: </label><input [(ngModel)]="model.title" placeholder="Reminder!"><br>
              <label>Content: </label><br><textarea rows="4" cols="30" [(ngModel)]="model.content" placeholder="Don't forget to do the homework. It's due tomorrow!"></textarea><br>
              <button (click)="addPinSubmit()">Add Pin</button>
              <button (click)="addPinCancel()">Cancel</button>
            </div>
          <ul>
            <li *ngFor="let pin of selectedCourse?.pins">
              {{pin}}
            </li>
          </ul>
      </div>
    </div>
  `,
  styles: [`
    .main-content-wrapper {
      display: block;
      padding: 10px;
      background-color: cyan;
    }
  `]
})

export class MainContent implements OnInit {
  model: any = {};
  user: User = null;
  selectedCourse: Course = null;

  private addingPin: boolean = false;

  constructor(
    private userService: UserService,
    private router: Router) {}

  ngOnInit(): void {
    this.user = this.userService.currentUser();
  }

  createPinInit() {
    console.log('add pin init');
    this.addingPin = true;
  }

  addPinSubmit() {
    let pin = {
      title: this.model.title,
      content: this.model.content
    };
    this.userService.addPinToCourse(pin, this.selectedCourse.name);
    console.log('adding pin submit');
    this.addingPin = false;
  }

  addPinCancel() {

    console.log('add pin cancel');
    this.addingPin = false;
  }
}


