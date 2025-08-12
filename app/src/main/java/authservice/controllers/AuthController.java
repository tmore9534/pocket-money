package authservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import authservice.entities.RefreshToken;
import authservice.model.UserInfoDto;
import authservice.reponse.JWTResponseDTO;
import authservice.services.JwtService;
import authservice.services.RefreshTokenService;
import authservice.services.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("auth/v1/signup")
    public ResponseEntity<?> Signup(@RequestBody UserInfoDto userInfoDto) {
        try {
            Boolean isSignunUped = userDetailsService.signupUser(userInfoDto);
            if (Boolean.FALSE.equals(isSignunUped)) {
                return new ResponseEntity<>("Aleady Exists", HttpStatus.BAD_REQUEST);
            }
            // if user is newly signed up, create a refreshtoken as well as JWTToken
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUsername());
            String jwtToken = jwtService.generateToken(userInfoDto.getUsername());
            return new ResponseEntity<>(
                    JWTResponseDTO.builder().accessToken(jwtToken).token(refreshToken.getToken()).build(),
                    HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
