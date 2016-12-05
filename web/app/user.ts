'use strict';
import { Course } from './course';

export class User {
  id: Number;
  username: String;
  firstName: String;
  lastName: String;
  courses: Course[];
  jwt: String;
}

