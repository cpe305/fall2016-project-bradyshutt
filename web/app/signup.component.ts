
import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {UserService} from "./services/user.service";


@Component({
  moduleId: module.id,
  template: `
    <div>
      <h2>Register</h2>
      <form name="registerForm" (ngSubmit)="register()" novalidate>
        <div class="form-group">
          <label for="username">Username</label>
          <input type="text" name="username" [(ngModel)]="model.username" />
        </div>
        <div class="form-group">
          <label for="firstName">firstName</label>
          <input type="text" name="firstName" [(ngModel)]="model.firstName" />
        </div>
        <div class="form-group">
          <label for="lastName">lastName</label>
          <input type="text" name="lastName" [(ngModel)]="model.lastName" />
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" name="password" [(ngModel)]="model.password" />
        </div>
        <div class="missingFields" *ngIf="(missingFields)">
          <p>* Please fill in required fields.</p>
        </div>
        <div class="form-group">
            <button type="submit">Sign Up</button>
            <a [routerLink]="['/register']" class="btn btn-link">Register</a>
        </div>
      </form>
      <div>
        <p>Already have an account? <a [routerLink]="['/login']" class="btn btn-link">Login here</a>.</p>
      </div>
    </div>
  `,
  styles: [`
    .missingFields {
      color: red;
    }
  `]

})

export class SignUpComponent implements OnInit {
  model: any = { };
  missingFields: boolean = false;

  constructor(
    private router: Router,
    private userService: UserService) {};

  register() {
    let data = this.validateInput();
    this.userService.signUp(data).then(
      user => {
        console.log('Registration success.');
        console.log('localstorage.user: ', localStorage.getItem("user"));
        console.log('localstorage.jwt: ', localStorage.getItem("jwt"));
        this.router.navigate(['/dashboard']);
      },

      error => {
        console.log('Registration failed.');
        console.log('error: ', error);
      })
  }

  validateInput(): Object {
    let data = {
      username: this.model.username,
      firstName: this.model.firstName,
      lastName: this.model.lastName,
      password: this.model.password,
    };
    if (
      data.username == null || data.username == '' ||
      data.firstName == null || data.firstName == '' ||
      data.lastName == null || data.lastName == '' ||
      data.password == null || data.password == ''
      ) {
      return null;
    } else return data;
  };

  ngOnInit(): void {

  }
}

