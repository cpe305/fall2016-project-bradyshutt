'use strict'

const path = require('path')
const spawn = require('child_process').spawn
const conf = require('./config.js')

let JavaApp = (onRecMsgFn) => {

   if (JavaApp.singleton) return JavaApp.singleton 
   else {

      const javaApp = JavaApp.singleton || (JavaApp.singleton = 
         spawn('java', ['-jar', path.join(conf.base, 'target/', conf.jar)]))

      let isAlive = true
      let outBuffer = []

      javaApp.stdout.on('data', (data) => {
         buff.push(data)
      })
      
      javaApp.stdout.on('end', () => {
         onRecMsgFn
            ? onRecMsgFn(outBuffer)
            : console.log(outBuffer)
         outBuffer = []
      })

      javaApp.stderr.on('data', (data) => {
         console.log('JavaApp <ERROR> output: ')
         console.log(data.toString())
      })

      javaApp.on('exit', (code, signal) => {
         javaApp.isAlive = false
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

//http.createServer((req, res) => {
//
//
//
//   
//}).listen(8080)
