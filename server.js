'use strict'

const http = require('http')

let server = http.createServer((req, res) => {
   console.log(req.url)
   res.end('Coplan!')
})

server.listen(80)
