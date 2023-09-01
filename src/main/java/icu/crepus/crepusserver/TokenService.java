package icu.crepus.crepusserver;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TokenService {
    private static final String secretKey="Dian_Wei_Chun_Chun_Chao_Biao_Ying_Xiong";//这是咒语，随便写一个就行，扔到配置文件里应该更安全

    public String getToken(String userID) {
        return JWT
                .create()
                .withClaim("userID", userID)
                .withClaim("timeStamp", System.currentTimeMillis())
                .sign(Algorithm.HMAC256(secretKey));
    }

    public  HashMap<String, String> parseToken(String token) {
        HashMap<String, String> map = new HashMap<String, String>();
        DecodedJWT decodedJWT = JWT
                .require(Algorithm.HMAC256((secretKey)))
                .build().verify(token);

        Claim userID = decodedJWT.getClaim("userID");
        Claim timeStamp = decodedJWT.getClaim("timeStamp");

        map.put("userID", userID.asString());
        map.put("timeStamp", timeStamp.asLong().toString());

        return map;
    }
}