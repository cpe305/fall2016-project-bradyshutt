'use strict';

export class Course {
  id: number;
  name: string;
  registeredUsers: string[];
  pins: string[];

  constructor(attributes) {
    this.id = attributes.id || null;
    this.name = attributes.courseName;
    this.registeredUsers = attributes.registeredUsers;
    this.pins = attributes.pins;
  }
}




