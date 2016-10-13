'use strict'

const http = require('http')

const conf = require('./config.js')
const spawn = require('child_process').spawn

const javaApp = spawn('java', ['-cp', conf.base + 'src/main/java/com/bshutt/coursemanager/', 'App'])
let childIsAlive = true

console.log('java process is... ' + (childIsAlive ? 'still ': 'not ')+ 'connected!')

let buf = []

javaApp.stdout.on('data', (data) => {
   console.log('Java App sent data to stdout!')
   console.log('JavaApp says: ' + data.toString())
   buf.push(data)
})

javaApp.stdout.on('end', () => {
   console.log('All data recieved')
   console.log(buf.join(''))
   buf = []
})

javaApp.stderr.on('data', (data) => {
   process.stdout.write('.')
})

javaApp.on('exit', (code, signal) => {
   childIsAlive = false
   console.log('Java App exited with code ' + code)
   console.log('sig: ' + signal)
})

javaApp.on('error', (err) => {
   console.log('Error occured...')
   console.log(err)
})

console.log('java process is... ' + (childIsAlive ? 'still ': 'not ')+ 'connected!')
console.log('listening...')

process.stdin.on('readable', () => {
   console.log('java process is... ' + (childIsAlive ? 'still ': 'not ')+ 'connected!')
   let chunk = process.stdin.read()
   if (chunk !== null) {
      javaApp.stdin.write(chunk, 'utf8', (err) => {
         if (err) {
            console.error(err)
         }
         console.log('wrote to javaApp\'s stdin!')
      })
   }
})


//http.createServer((req, res) => {
//
//
//
//   
//}).listen(8080)
