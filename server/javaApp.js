'use strict'

const path = require('path')
const conf = require('./config.js')
const spawn = require('child_process').spawn

let JavaApp = (onRecMsgFn) => {

   if (JavaApp.singleton) return JavaApp.singleton 
   else {

      const javaApp = JavaApp.singleton || (JavaApp.singleton = 
         spawn('java', ['-jar', path.join(conf.base, 'target/', conf.jar)]))

      let isAlive = true

      javaApp.stdout.on('readable', () => {
         let data = javaApp.stdout.read()
         onRecMsgFn
            ? onRecMsgFn(data)
            : console.log(data)
      })

      javaApp.stderr.on('data', (data) => {
         console.log('JavaApp <ERROR> output: ')
         console.log(data.toString())
      })

      javaApp.on('exit', (code, signal) => {
         isAlive = false
         console.log(`JavaApp <EXITED>: Code[${code}] Signal:[${signal}]`)
      })

      javaApp.on('error', (err) => {
         console.log('JavaApp <error>:')
         console.log(err)
      })

      return {
         send(msg) {
            javaApp.stdin.write(msg, 'utf8', (err) => {
               if (err) throw err
            })
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

         kill() {
            console.log('Killing Java process')
            javaApp.kill()
            isAlive = false
         }

      }
   }
}

module.exports = JavaApp


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

