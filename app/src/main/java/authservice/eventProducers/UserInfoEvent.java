package authservice.eventProducers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/* Define a separate dto userInfoEvent for sending the data to kafka.*/

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserInfoEvent {
    private String firstName;

    private String lastName;

    private String email;

    private Long phoneNumber;

    private String userId;

    // profile photo should be added explicity by another service.
}
