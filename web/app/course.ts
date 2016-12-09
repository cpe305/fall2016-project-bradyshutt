'use strict';
import {Pin} from "./pin";

export class Course {
  id: number;
  name: string;
  registeredUsers: string[];
  pins: any[];

  constructor(attributes) {
    this.id = attributes.id || null;
    this.name = attributes.courseName;
    this.registeredUsers = attributes.registeredUsers;
    this.pins = attributes.pins && attributes.pins.map((pin) => new Pin(pin));
  }
}




