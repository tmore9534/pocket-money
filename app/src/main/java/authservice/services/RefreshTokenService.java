package authservice.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import authservice.entities.RefreshToken;
import authservice.entities.UserInfo;
import authservice.repository.RefreshTokenRepository;
import authservice.repository.UserRepository;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    // public RefreshToken createRefreshToken(String username) {
    // UserInfo userInfoExtracted = userRepository.findByUsername(username);
    // RefreshToken refreshToken = RefreshToken.builder()
    // .userInfo(userInfoExtracted)
    // .token(UUID.randomUUID().toString())
    // .expiryDate(Instant.now().plusMillis(600000))
    // .build();
    // return refreshTokenRepository.save(refreshToken);
    // }

    public RefreshToken createRefreshToken(String username) {
        UserInfo user = userRepository.findByUsername(username);

        return refreshTokenRepository.findByUserInfo(user)
                .map(existing -> {
                    existing.setToken(UUID.randomUUID().toString());
                    existing.setExpiryDate(Instant.now().plusMillis(600000));
                    return refreshTokenRepository.save(existing);
                })
                .orElseGet(() -> {
                    RefreshToken refreshToken = RefreshToken.builder()
                            .userInfo(user)
                            .token(UUID.randomUUID().toString())
                            .expiryDate(Instant.now().plusMillis(600000))
                            .build();
                    return refreshTokenRepository.save(refreshToken);
                });
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }
}
