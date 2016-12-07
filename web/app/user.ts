'use strict';
import { Course } from './course';

export class User {
  id: Number;
  username: string;
  firstName: string;
  lastName: string;
  courses: Course[];
  jwt: string;

  constructor(attributes) {
    this.id = attributes.id || null;
    this.username = attributes.username;
    this.firstName = attributes.firstName;
    this.lastName = attributes.lastName;
    this.courses = attributes.courses;
    this.jwt = attributes.jwt;
  }

  toString(): string {
    let attributes = {
      id: this.id,
      username: this.username,
      firstName: this.firstName,
      lastName: this.lastName,
      courses: this.courses,
      jwt: this.jwt
    };
    return JSON.stringify(attributes);
  }
}

