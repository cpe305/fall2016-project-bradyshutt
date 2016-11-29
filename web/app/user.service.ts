'use strict';

import { Injectable } from '@angular/core';

import {User} from "./user";

const USERS: User[] = [
  {id: 1, username: 'bshutt', firstName: 'Brady', lastName: 'Shutt'},
  {id: 2, username: 'athena', firstName: 'Athena', lastName: 'Cat'},

]

@Injectable()
export class UserService {
  getUsers(): User[] {
    return USERS;
  }

  getUser(id: Number): User {
    return USERS.find((u) => u.id === id)
  }
}
