'use strict';

export class Course {
  id: Number;
  name: String;
  registeredUsers: String[];
  pins: String[];

  constructor(attributes) {
    this.id = attributes.id || null;
    this.name = attributes.name;
    this.registeredUsers = attributes.registeredUsers;
    this.pins = attributes.pins;


  }
}




