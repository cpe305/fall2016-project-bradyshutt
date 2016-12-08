import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AllUsersComponent } from './all-users.component';
import { AppComponent }  from './app.component';
import { AuthenticationService } from './services/authentication.service';
import { CurrentUser } from './current-user.component';
import { Dashboard } from './dashboard.component';
import { LoginComponent } from './login.component';
import { MainContent } from './main-content.component';
import { MessagingService } from  './services/messaging.service';
import { SideBar } from './side-bar.component';
import { SignUpComponent } from './signup.component';
import { UserService } from './services/user.service';


@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule.forRoot([
      {
        path: 'dashboard',
        component: Dashboard
      },
      {
        path: 'whoami',
        component: CurrentUser
      },
      {
        path: 'login',
        component: LoginComponent
      },
      {
        path: 'signup',
        component: SignUpComponent
      },
      {
        path: 'all-users',
        component: AllUsersComponent
      }
    ])
  ],
  declarations: [
    AppComponent,
    SideBar,
    MainContent,
    Dashboard,
    CurrentUser,
    LoginComponent,
    SignUpComponent,
    AllUsersComponent
  ],
  providers: [
    UserService,
    AuthenticationService,
    MessagingService
  ],
  bootstrap: [ AppComponent ]
})

export class AppModule { }

