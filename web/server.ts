'use strict';

import { Request, Response } from 'express';

const express = require('express');
const morgan = require('morgan');
const fs = require('fs');
const bodyParser = require('body-parser');
const app = express();

const javaBridge = require('./java-bridge')();

app.use(express.static(__dirname));
app.use(morgan('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.get('/', (req: Request, res: Response) => {
  res.sendFile('index.html');
});

app.post('/api/authenticate', (req: Request, res: Response) => {
  let msg = {
    route: 'login',
    data: {
      username: req.body.username,
      password: req.body.password
    }
  };

  javaBridge.send(JSON.stringify(msg), (javaResponse) => {
    console.log('javaResponse', javaResponse);
    let clientRes = {
      status: javaResponse.status,
      jwt: javaResponse.jwt || null,
      user: javaResponse.user || null
    };
    res.setHeader('Content-Type', 'text/json');
    res.send(JSON.stringify(clientRes));
  });
});

app.post('/api/endpoint', (req: Request, res: Response) => {
  let msg = req.body.message;
  console.log('msg:', msg);
  javaBridge.send(JSON.stringify(msg), (javaResponse) => {
    //console.log('javaResponse', javaResponse);
    res.setHeader('Content-Type', 'text/json');
    res.json(JSON.stringify(javaResponse));
  });
});

app.use('', (req: Request, res: Response) => res.sendFile(__dirname + '/index.html'));
app.listen(3000);
