package authservice.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import authservice.entities.UserInfo;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserInfoDto extends UserInfo {

    private String firstName; // first_name

    private String lastName; // last_name

    private Long phoneNumber;

    private String email; // email
}
