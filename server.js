'use strict'

const http = require('http')
const fs = require('fs');
const conf = require('./config.js')

let server = http.createServer((req, res) => {
   console.log(req.url)
   fs.readFile('web/index.html', (err, data) => {
      if (err) console.error(err)
      else {
         res.writeHeader(200, {'ContentType': 'text/html'});
         res.end(data) 
      }
   })
})

server.listen(process.argv[2] || 80)




