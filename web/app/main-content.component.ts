'use strict';

import {Component, OnInit, Input} from '@angular/core';
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
        <h2>Pins for: {{selectedCourse.name}}</h2>
          <button (click)="createPinInit()" *ngIf="(!addingPin)">Add a Pin</button>
          <div class="addPinWrapper" *ngIf="addingPin">
            <h3>Add a new pin:</h3>
            <label>Title: </label><input [(ngModel)]="model.title" placeholder="Reminder!"><br>
            <label>Content: </label><br><textarea rows="4" cols="30" [(ngModel)]="model.content" placeholder="Don't forget to do the homework. It's due tomorrow!"></textarea><br>
            <div class="add-pin-buttons"><button (click)="addPinSubmit()">Add Pin</button></div>
            <div class="add-pin-buttons"><button (click)="addPinCancel()">Cancel</button></div>
          </div>
          <div class="pinsWrapper">
            <div class="pin" *ngFor="let pin of selectedCourse.pins">
              <p class="pin-title">{{pin.title}}</p>
              <p class="pin-content">{{pin.content}}</p>
            </div>
          </div>
      </div>
    </div>
  `,
  styles: [`
    .main-content-wrapper {
      max-width: 85%;
      display: block;
      /*padding: 10px;*/
      /*position: relative;*/
      /*left: 0;*/
      background-color: cyan;
    }
    .add-pin-buttons {
      padding: 3px;
      background-color: brown;
    }
    .add-pin-buttons button {
      padding: 3px;
    }
  `]
})

export class MainContent implements OnInit {
  model: any = {};
  user: User = null;
  @Input()
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


