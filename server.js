'use strict'

const http = require('http')

const conf = require('./config.js')
let server = http.createServer((req, res) => {
   console.log(req.url)
   res.end('Coplan!')
})

server.listen(80)




