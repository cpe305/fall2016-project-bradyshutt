import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import { RouterModule } from '@angular/router';

import { AppComponent }  from './app.component';
import { Dashboard } from './dashboard.component';
import { MainContent } from './main-content.component';
import { SideBar } from './side-bar.component';
import { UserService } from './services/user.service';
import { CurrentUser } from './current-user.component';
import { LoginComponent } from './login.component';
import { AuthenticationService } from './services/authentication.service';
import { HttpModule } from '@angular/http';
import { MessagingService } from  './services/messaging.service';


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
      }
    ])
  ],
  declarations: [
    AppComponent,
    SideBar,
    MainContent,
    Dashboard,
    CurrentUser,
    LoginComponent
  ],
  providers: [
    UserService,
    AuthenticationService,
    MessagingService
  ],
  bootstrap: [ AppComponent ]
})

export class AppModule { }
