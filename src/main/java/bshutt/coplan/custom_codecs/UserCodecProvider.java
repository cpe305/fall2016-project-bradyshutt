//package bshutt.coplan.custom_codecs;
//
//import bshutt.coplan.models.User;
//import org.bson.codecs.Codec;
//import org.bson.codecs.configuration.CodecProvider;
//import org.bson.codecs.configuration.CodecRegistry;
//
//public class UserCodecProvider implements CodecProvider {
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public <T> Codec<T> getData(Class<T> clazz, CodecRegistry registry) {
//        if (clazz == User.class) {
//            return (Codec<T>) new UserCodec(registry);
//        }
//        return null;
//    }
//}
