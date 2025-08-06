package canhxuan.quanlybanhang.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void saveToken(String token, String username, int timeoutMinutes){
        redisTemplate.opsForValue().set(token, username, timeoutMinutes, TimeUnit.MINUTES);
    }

    public void saveAccessToken(String token, String username, int timeoutMinutes){
        redisTemplate.opsForValue().set("Access:" + token, username, timeoutMinutes, TimeUnit.MINUTES);
    }

    public void saveRefreshToken(String token, String username, int timeoutMinutes){
        redisTemplate.opsForValue().set("Refresh:" + token, username, timeoutMinutes, TimeUnit.MINUTES);
    }

    public void blacklistToken(String token, long ttlMinutes) {
        redisTemplate.opsForValue().set("blacklist:" + token, "true", ttlMinutes, TimeUnit.MINUTES);
    }

    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey("blacklist:" + token);
    }


    public String getUsernameFromToken(String token){
        return redisTemplate.opsForValue().get(token);
    }

    public void deleteToken(String token){
        redisTemplate.delete(token);
    }
}
