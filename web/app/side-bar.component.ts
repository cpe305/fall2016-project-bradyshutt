// 'use strict';
//
// import { Component, Injectable, OnInit } from '@angular/core';
//
// import { UserService } from './services/user.service' ;
// import { User } from './user';
// import { Course } from './course';
// import {Subscription} from "rxjs";
//
//
// @Component({
//   selector: 'side-bar',
//   moduleId: module.id,
//   template: `
//     <div class="sideBar">
//       <div class="heading">
//         <h2>My Courses</h2>
//         <button (click)="addCourseInit()">Add Course</button>
//       </div>
//       <div class="content">
//         <div class="addCourseWrapper" *ngIf="addingCourses">
//           <h3>Add a new course:</h3>
//           <label>Col|lege: &nbsp;</label><input [(ngModel)]="model.college" maxlength="4" placeholder="CSC"><br>
//           <label>Number: </label><input [(ngModel)]="model.number" type="number" maxlength="4" placeholder="123"><br>
//           <div>
//             <button class="addCourseButtons" (click)="addCourseSubmit()">Add Course</button>
//           </div>
//           <div>
//             <button class="addCourseButtons" (click)="addCourseCancel()">Cancel</button>
//           </div>
//         </div>
//         <ul>
//           <li *ngFor="let course of user?.courses" (click)="onSelectCourse(course)">
//             {{course.name}}
//           </li>
//         </ul>
//       </div>
//     </div>
//   `,
//   styles: [`
//     .sideBar {
//       background-color: red;
//       display: block;
//       width: 15%;
//       min-width: 250px;
//       height: 100%;
//       float: left;
//       padding-bottom: 9999px;
//       margin-bottom: -9999px;
//       overflow: hidden;
//     }
//     .heading {
//       background-color: orange;
//       padding: 15px;
//       display: flex;
//       justify-content: space-between;
//     }
//     .heading button {
//       padding: 4px;
//     }
//     ul {
//       list-style-type: none;
//     }
//     li:first-child {
//       border-top: 2px solid black;
//     }
//     li {
//       display: block;
//       background-color: pink;
//       padding: 10px;
//       border-bottom: 2px solid black;
//     }
//     .addCourseWrapper {
//       padding: 15px;
//       background-color: white;
//     }
//
//     .addCourseButtons {
//       display: block;
//       color: red;
//
//     }
//
//   `]
// })
//
// export class SideBar implements OnInit {
//   model: {college: string, number: number} = {
//     college: null,
//     number: null
//   };
//
//   private user: User;
//   private addingCourses: boolean = false;
//   private subscription: Subscription;
//
//   constructor(private userService: UserService) {}
//
//   ngOnInit(): void {
//     this.user = this.userService.currentUser();
//
//     this.subscription = this.userService.userChanged.subscribe((userChange) => {
//       console.log('newStatus', userChange.change);
//       this.user = userChange.user;
//       console.log(this.user.courses);
//     });
//   }
//
//   addCourseInit() {
//     this.addingCourses = true;
//   }
//
//   addCourseCancel() {
//     this.addingCourses = false;
//   }
//
//   onSelectCourse(course: Course) {
//     console.log(course);
//   }
//
//   addCourseSubmit() {
//     let collegeName: String = this.model.college;
//     let courseNumber: Number = this.model.number;
//     let courseName:String = collegeName.toUpperCase() + '-' + courseNumber;
//     console.log('course about to add: ', courseName);
//     if (this.user.courses.find((course) => course.name === courseName)) {
//       alert("You've already registered for this course.");
//       return;
//     }
//
//     this.userService.addCourse(courseName).then(
//       course => {
//         console.log('course added successfully!');
//       }
//     );
//     this.addingCourses = false;
//   }
//
// }
