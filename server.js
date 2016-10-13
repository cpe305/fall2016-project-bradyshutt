'use strict'

const http = require('http')
const fs = require('fs');

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

server.listen(80)
