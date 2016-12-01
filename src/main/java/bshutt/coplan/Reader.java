package bshutt.coplan;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Reader {

    private InputStreamReader isr;
    private BufferedReader bufferReader;
    private ReaderStrategy readerStrategy;

    public Reader(ReaderStrategy readerStrategy) {
        this.readerStrategy = readerStrategy;

        isr = new InputStreamReader(System.in);
        bufferReader = new BufferedReader(isr);
    }

    void listen(Callback cb) {
        String nextInput;
        while (true) {
            try {
                if ((nextInput = bufferReader.readLine()) != null) {
                    Request req = this.readerStrategy.interpret(nextInput);
                    cb.cb(req);
                }
            } catch (Exception exception) {
                System.err.println(exception);
                //exception.printStackTrace();
            }
        }
    }

}
