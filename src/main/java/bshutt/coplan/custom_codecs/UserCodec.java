//package bshutt.coplan.custom_codecs;
//
//import bshutt.coplan.exceptions.DatabaseException;
//import bshutt.coplan.models.User;
//import org.bson.BsonReader;
//import org.bson.BsonType;
//import org.bson.BsonWriter;
//import org.bson.codecs.Codec;
//import org.bson.codecs.DecoderContext;
//import org.bson.codecs.EncoderContext;
//import org.bson.codecs.configuration.CodecRegistry;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class UserCodec implements Codec<User> {
//
//    private CodecRegistry codecRegistry;
//
//    public UserCodec(CodecRegistry codecRegistry) {
//        this.codecRegistry = codecRegistry;
//    }
//
//    @Override
//    public User decode(BsonReader reader, DecoderContext decoderContext) {
//        reader.readStartDocument();
//        String username = reader.readString("username");
//        String firstName = reader.readString("firstName");
//        String lastName = reader.readString("lastName");
//        String email = reader.readString("email");
//        String jwt = reader.readString("jwt");
//        String hashedPassword = reader.readString("hashedPassword");
//        ArrayList<String> courses = new ArrayList<>();
//        reader.readStartArray();
//        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
//            String course = reader.readString();
//            courses.add(course);
//        }
//        reader.readEndArray();
//        reader.readEndDocument();
//
//        User user = new User();
//        user.setUsername(username);
//        user.setFirstName(firstName);
//        user.setLastName(lastName);
//        user.setEmail(email);
//        user.setJwt(jwt);
//        user.setHashedPassword(hashedPassword);
//        user.setCourses(courses);
//        return user;
//    }
//
//    @Override
//    public void encode(BsonWriter writer, User user, EncoderContext encoderContext) {
//        writer.writeStartDocument();
//
//        writer.writeName("username");
//        writer.writeString(user.getUsername());
//
//        writer.writeName("firstName");
//        writer.writeString(user.getFirstName());
//
//        writer.writeName("lastName");
//        writer.writeString(user.getLastName());
//
//        writer.writeName("email");
//        writer.writeString(user.getEmail());
//
//        writer.writeName("jwt");
//        writer.writeString(user.getJwt());
//
//        writer.writeName("hashedPassword");
//        writer.writeString(user.getHashedPassword());
//
//        writer.writeStartArray("courses");
//        for (String course : user.getCourses()) {
//            encoderContext.encodeWithChildContext(codecRegistry.getData(String.class), writer, course);
//        }
//        writer.writeEndArray();
//        writer.writeEndDocument();
//    }
//
//    @Override
//    public Class<User> getEncoderClass() {
//        return User.class;
//    }
//}
