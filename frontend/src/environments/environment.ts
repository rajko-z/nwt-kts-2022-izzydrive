// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

import { HttpHeaders } from "@angular/common/http";

export const environment = {
  production: false,
  apiUrl: "https://localhost:8443/izzydrive/v1/",
  clientId: '83743871907-93ba97v7t6j53p316sl6js0pgj9hmp51.apps.googleusercontent.com',
  header : {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
       'Authorization': "Bearer "+ (JSON.parse(sessionStorage.getItem('currentUser'))? JSON.parse(sessionStorage.getItem('currentUser')).username : '')
    })
  },
  maxImageSize: 20971520,
  maxImageHeight: 15200,
  maxImageWidth: 25600
}

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
