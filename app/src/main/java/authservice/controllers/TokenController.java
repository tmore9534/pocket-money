package authservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import authservice.entities.RefreshToken;
import authservice.reponse.JWTResponseDTO;
import authservice.request.AuthRequestDTO;
import authservice.request.RefreshTokenRequestDTO;
import authservice.services.JwtService;
import authservice.services.RefreshTokenService;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class TokenController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("auth/v1/login")
    public ResponseEntity<?> AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));

        if (authentication.isAuthenticated()) {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());

            return new ResponseEntity<>(JWTResponseDTO.builder()
                    .accessToken(jwtService.generateToken(authRequestDTO.getUsername()))
                    .token(refreshToken.getToken())
                    .build(),
                    HttpStatus.OK);

        } else {
            return new ResponseEntity<>("Exception in user service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("auth/v1/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenDTO) {
        return refreshTokenService.findByToken(refreshTokenDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo.getUsername());
                    return new ResponseEntity<>(JWTResponseDTO.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenDTO.getToken())
                            .build(),
                            HttpStatus.OK);
                }).orElseThrow(() -> new RuntimeException("RefreshToken is not in DB."));
    }

}
