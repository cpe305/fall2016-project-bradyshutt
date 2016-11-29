import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent }  from './app.component';
import { SideBar } from './side-bar.component';
import { MainContent } from './main-content.component';
import {UserService} from "./user.service";


@NgModule({
  imports:      [ BrowserModule ],
  declarations: [
    AppComponent,
    SideBar,
    MainContent
  ],
  providers: [
    UserService
  ],
  bootstrap:    [ AppComponent ]
})
export class AppModule { }
