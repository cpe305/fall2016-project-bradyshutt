'use strict';

const path = require('path');
const conf = {
  base: '/home/bshutt/school/cpe305/project/fall2016-project-bradyshutt/',
  jar: 'coplan-1.0-SNAPSHOT.jar'
}
const spawn = require('child_process').spawn;

let JavaApp = (onRecMsgFn) => {

  if (JavaApp.singleton) return JavaApp.singleton;
  else {

    const javaApp = JavaApp.singleton || (JavaApp.singleton =
        spawn('java', ['-jar', path.join(conf.base, 'target/', conf.jar)]));

    let isAlive = true;
    let callback;

    javaApp.stdout.on('readable', () => {
      let data = javaApp.stdout.read().toString();
      let json;
      console.log('java responded');
      if (!(json = isJson(data))) {
        return
      }
      else {
        if (callback) {
          console.log('sending!');
          callback(json)
        }
        onRecMsgFn && onRecMsgFn(json);
        let pretty = JSON.stringify(json, null, 4);
        console.log("\nResponse:\n" + pretty + "\n")
      }
    });

    javaApp.stderr.on('data', (data) => {
      console.log('JavaApp threw an uncaught exception:');
      console.log(data.toString())
      javaApp.kill()
    });

    javaApp.on('exit', (code, signal) => {
      console.log(`JavaApp <EXITED>: Code[${code}] Signal:[${signal}]`);
      javaApp.kill();
    });

    javaApp.on('error', (err) => {
      console.log('JavaApp <error>:');
      Error.captureStackTrace(err);
      console.log(err.message);
      console.log(err)
      javaApp.kill();
    });

    function isJson(str) {
      let json;
      try {
        json = JSON.parse(str)
      } catch (e) {
        return false
      }
      return json
    }


    return {
      send(msg, cb) {
        if (msg.toString().split('\n').length === 1 || msg.split('\n')[1] === '')
          msg += '\n';
        javaApp.stdin.write(msg, 'utf8', (err) => {
          if (err) throw err
        });
        callback = cb || null
        return javaApp
      },

      onMessage(fn) {
        javaApp.stdout.on('end', fn)
        return javaApp
      },

      status() {
        console.log(isAlive
          ? 'Java is still alive'
          : 'Java is not still alive')
      },

      killJavaApp() {
        if (isAlive) {
          javaApp.kill()
          console.log('Killing the java app');
          isAlive = false;
        }
      }

    }
  }
};

module.exports = JavaApp;


// let jPath = path.join(conf.base, 'target/', conf.jar))
// let java = JavaApp(jPath)
//

//console.log('java process is... ' + (javaApp.isAlive ? 'still ': 'not ')+ 'connected!')
//
//let buf = []
//


//console.log('java process is... ' + (javaApp.isAlive ? 'still ': 'not ')+ 'connected!')
//console.log('listening...')

//process.stdin.on('readable', () => {
//   console.log('java process is... ' + (javaApp.isAlive ? 'still ': 'not ')+ 'connected!')
//   let chunk = process.stdin.read()
//   if (chunk !== null) {
//      javaApp.stdin.write(chunk, 'utf8', (err) => {
//         if (err) {
//            console.error(err)
//         }
//         console.log('wrote to javaApp\'s stdin!')
//      })
//   }
//})
//
