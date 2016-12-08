'use strict';
import {Injectable, ErrorHandler} from "@angular/core";
import {Router} from "@angular/router";



@Injectable()
export class MyErrorHandler implements ErrorHandler {
  constructor(private router: Router) { }
  handleError(error: any): void {
    this.router.navigate(['CustomErrorPage']);
  }
}

// class MyErrorHandler implements ErrorHandler {
//   call(error, stackTrace = null, reason = null) {
//     // do something with the exception
//   }
// }
// @NgModule({
//   providers: [{provide: ErrorHandler, useClass: MyErrorHandler}]
// })
// class MyModule {}
