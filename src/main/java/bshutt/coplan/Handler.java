package bshutt.coplan;

public interface Handler {
    void handle(Request req, Response res) throws Exception;
}

