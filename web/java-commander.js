'use strict';
const kue = require("kue");
module.exports = function (javaBridge) {
    let queue = kue.createQueue();
    queue.process('javaTransaction', function (job, done) {
        doTransaction({ msg: job.data.msg, cb: job.data.cb }, done);
    });
    function doTransaction(args, done) {
        console.log('processing...');
        javaBridge.send(args.msg, (javaResponse) => {
            console.log('...done');
            args.cb(javaResponse);
            done();
        });
    }
    return {
        send(msg, cb) {
            let job = queue.create('javaTransaction', { msg, cb })
                .save(err => {
                if (err)
                    console.log('err with job ' + job.id);
            });
            //queue.add(msg, cb);
            //javaBridge.send(msg, cb);
            //let args = queue.next();
        },
    };
};
//# sourceMappingURL=java-commander.js.map