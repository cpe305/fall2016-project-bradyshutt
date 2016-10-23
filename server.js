'use strict'

const http = require('http')
const fs = require('fs');
const conf = require('./config.js')
const express = require('express')
const app = express()
//const spawn = require('child_process').spawn

const bridge = require('./bridge.js')

let java = bridge((msg) => {
   console.log('MESSAGE RECEIVED')
   console.log('['+msg.toString()+']')
})



process.stdin.on('readable', () => {
   let msg = process.stdin.read()
   java.status()
   console.log('<NODE> msg: '+msg)
   if (msg !== null)
      java.send(msg)
})


app.get('/', (req, res) => {
   console.log(req.url)
   fs.readFile('web/index.html', (err, data) => {
      if (err) console.error(err)
      else {
         res.writeHeader(200, {'ContentType': 'text/html'});
         res.end(data) 
      }
   })
})

app.listen(8000)


//let server = http.createServer((req, res) => {
//   console.log(req.url)
//   fs.readFile('web/index.html', (err, data) => {
//      if (err) console.error(err)
//      else {
//         res.writeHeader(200, {'ContentType': 'text/html'});
//         res.end(data) 
//      }
//   })
//})

//server.listen(process.argv[2] || 80)




