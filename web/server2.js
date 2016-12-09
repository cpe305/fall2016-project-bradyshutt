'use strict';
const express = require('express');
const morgan = require('morgan');
const bodyParser = require('body-parser');
const app = express();
app.use(express.static(__dirname));
app.use(morgan('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.get('/', (req, res) => {
    res.sendFile('index.html');
});
app.post('/api/authenticate', (req, res) => {
    res.end('oh hi');
});
app.use('', (req, res) => res.sendFile(__dirname + '/index.html'));
app.listen(3001);
//# sourceMappingURL=server2.js.map