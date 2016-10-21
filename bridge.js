'use strict'

const path = require('path')
const conf = require('./config.js')
const spawn = require('child_process').spawn


let buf = []

function JavaApp() {
   return  JavaApp.singleton || (JavaApp.singleton = (() => {
      let sgltn = spawn('java', ['-jar', path.join(conf.base, 'target/', conf.jar)])
      sgltn.isAlive = true;
      return sgltn;
   })())
}

JavaApp().stdout.on('data', (data) => {
   console.log('Java App sent data to stdout!')
   console.log('JavaApp says: ' + data.toString())
   buf.push(data)
})

JavaApp().stdout.on('end', () => {
   console.log('All data recieved')
   console.log(buf.join(''))
   buf = []
})

JavaApp().stderr.on('data', (data) => {
   process.stdout.write('.')
})

JavaApp().on('exit', (code, signal) => {
   JavaApp().IsAlive = false
   console.log('Java App exited with code ' + code)
   console.log('sig: ' + signal)
})

JavaApp().on('error', (err) => {
   console.log('Error occured...')
   console.log(err)
})

console.log('java process is... ' + (JavaApp().isAlive ? 'still ': 'not ')+ 'connected!')
console.log('listening...')

process.stdin.on('readable', () => {
   console.log('java process is... ' + (JavaApp().isAlive ? 'still ': 'not ')+ 'connected!')
   let chunk = process.stdin.read()
   if (chunk !== null) {
      JavaApp().stdin.write(chunk, 'utf8', (err) => {
         if (err) {
            console.error(err)
         }
         console.log('wrote to javaApp\'s stdin!')
      })
   }
})


