'use strict';

import Response = e.Response;
import Request = e.Request;
import e = require("express");

const express = require('express');
const morgan = require('morgan');
const bodyParser = require('body-parser');
const app = express();

app.use(express.static(__dirname));
app.use(morgan('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.get('/', (req: Request, res: Response) => {
  res.sendFile('index.html');
});


app.post('/api/authenticate', (req: Request, res: Response) => {
  res.end('oh hi');


});



app.use('', (req: Request, res: Response) => res.sendFile(__dirname + '/index.html'));

app.listen(3001);



