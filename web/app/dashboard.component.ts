'use strict';

import { Component, Injectable, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { UserService } from './services/user.service';
import { User } from './user';
import {Course} from "./course";
import {Subscription} from "rxjs";

@Component({
  selector: 'dashboard',
  template: `
    <div class="dashboard-wrapper">
      <div class="sideBar">
        <div class="heading">
          <h2>My Courses</h2>
          <button (click)="addCourseInit()">Add Course</button>
        </div>
        <div class="content">
          <div class="addCourseWrapper" *ngIf="addingCourses">
            <h3>Add a new course:</h3>
            <label>College: &nbsp;</label><input [(ngModel)]="model.college" maxlength="4" placeholder="CSC"><br>
            <label>Number: </label><input [(ngModel)]="model.number" type="number" maxlength="4" placeholder="123"><br>
            <button (click)="addCourseSubmit()">Add Course</button>
            <button (click)="addCourseCancel()">Cancel</button>
          </div>
          <ul>
            <li *ngFor="let course of user?.courses" (click)="onSelectCourse(course)">
              {{course.name}}
            </li>
          </ul>
        </div>
      </div>
      <main-content [selectedCourse]="selectedCourse"></main-content>
    </div>
  `,
  styles: [`
    .sideBar {
      background-color: red;
      display: block;
      width: 15%;
      min-width: 250px;
      height: 100%;
      padding-bottom: 9999px;
      margin-bottom: -9999px;
      border-right: black solid 3px;
      overflow: hidden;
    }
    .heading {
      background-color: orange;
      padding: 15px;
      display: flex;
      justify-content: space-between;
    }
    .heading button {
      padding: 4px;
    }
    ul {
      list-style-type: none;
    }
    li:first-child {
      border-top: 2px solid black;
    }
    li {
      display: block;
      background-color: pink;
      padding: 10px;
      border-bottom: 2px solid black;
    }
    .addCourseWrapper {
      padding: 15px;
      background-color: white;
    }
    
    .content button {
      display: block;
      width: 80%;
      text-align: center;
      margin: 10px auto 3px auto;
      padding: 3px;
    }
    
    .dashboard-wrapper {
      display: flex;
      width: 100%;
      background-color: darkmagenta;
      
    
    }
    
  `]
})

export class Dashboard implements OnInit {
  user: User;
  addingCourses: boolean = false;
  subscription: Subscription;
  selectedCourse: Course = null;
  model: {college: string, number: number} = {
    college: null,
    number: null
  };


  constructor(
    private userService: UserService,
    private router: Router) {}

  ngOnInit(): void {
    this.user = this.userService.currentUser();
    this.subscription = this.userService.userChanged.subscribe((userChange) => {
      console.log('newStatus', userChange.change);
      this.user = userChange.user;
      console.log(this.user.courses);
    });
    if (!this.user)
      this.router.navigate(['/login']);
  }

  addCourseInit() {
    this.addingCourses = true;
  }

  addCourseCancel() {
    this.addingCourses = false;
  }

  onSelectCourse(course: Course) {
    this.selectedCourse = course;
  }

  addCourseSubmit() {
    let collegeName: String = this.model.college;
    let courseNumber: Number = this.model.number;
    let courseName:String = collegeName.toUpperCase() + '-' + courseNumber;
    console.log('course about to add: ', courseName);
    if (this.user.courses.find((course) => course.name === courseName)) {
      alert("You've already registered for this course.");
      return;
    }

    this.userService.addCourse(courseName).then(
      course => {
        console.log('course added successfully!');
      }
    );
    this.addingCourses = false;
  }



}
