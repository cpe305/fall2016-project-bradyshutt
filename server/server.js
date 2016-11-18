'use strict'

const http = require('http')
const fs = require('fs');

const conf = require('./config.js')
const express = require('express')
const app = express()
const bodyParser = require('body-parser')
const bridge = require('./bridge.js')

const tests = require('./tests.js')
const JavaApp = require('./javaApp.js')
const colors = require('colors')

let java = bridge()

app.use(express.static('web'))

//process.stdin.on('readable', () => {
//   let msg = process.stdin.read()
//   java.status()
//   //console.log('sending: ' + JSON.stringify(cmd))
//   //java.send(JSON.stringify(cmd) + "\n")
//   java.send(JSON.stringify(tests.getUser) + "\n")
//})

//fs.watchFile('server/demoJson.js', {
//   persistant: true,
//   interval: 1000,
//}, (curr, prev) => {
//   fs.readFile('server/demoJson.js', (err, file) => {
//      let jsonStr = file
//         .toString('utf8')
//         .replace(/\s/g, '')
//      let reqJson = JSON.parse(jsonStr)
//      let req = JSON.stringify(reqJson)
//      let pretty = JSON.stringify(reqJson, null, 4)
//
//      console.log('Requesting...\n'+ pretty.blue + '\n')
//      java.send(req)
//   })
//})



function onExit(opts, err) {
   if (err) console.log(err.stack)
   java.kill()
   process.exit()
}

process.on('exit', onExit.bind(null))
process.on('SIGINT', onExit.bind(null))
process.on('uncaughtException', (e) => {
   console.log(e.stack)
   onExit()
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

app.use(bodyParser.json())
app.use(bodyParser.urlencoded())
app.post('/endpoint', (req, res) => {
   console.log('received endpoint req')
   let jsonMsgStr = JSON.stringify(req.body)
   java.send(jsonMsgStr, (javaResponse) => {
      console.log('sending response to client')
      res.end(JSON.stringify(javaResponse))
   })
})

app.listen(8080)
//app.listen(process.argv[2] || 80)







