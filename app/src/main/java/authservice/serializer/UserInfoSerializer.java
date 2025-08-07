package authservice.serializer;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import authservice.eventProducers.UserInfoEvent;

/* Used to serialize the data before sending it to the kafka. */
public class UserInfoSerializer implements Serializer<UserInfoEvent> {

    @Override
    public byte[] serialize(String arg0, UserInfoEvent arg1) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(arg1).getBytes();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }
}
