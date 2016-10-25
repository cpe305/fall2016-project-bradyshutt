package coplan;

public interface CRUD {
   boolean create();
   Object read();
   boolean update(Object o);
   boolean destroy();
}
