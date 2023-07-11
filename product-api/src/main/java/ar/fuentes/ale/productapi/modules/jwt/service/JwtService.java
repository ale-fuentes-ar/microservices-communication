package ar.fuentes.ale.productapi.modules.jwt.service;

import ar.fuentes.ale.productapi.config.exception.AuthenticationException;
import ar.fuentes.ale.productapi.modules.jwt.dto.JwtResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class JwtService {

    private static final String TXT_EMPTY_SPACE = " ";
    private static final Integer TOKEN_INDEX = 1;

    @Value("${app-config.secrets.api-secret}")
    private String apiSecret;

    public void validationAuthorization(String token){
        var accessToken = extractToken(token);
        int bitLenght = apiSecret.getBytes().length * 8;

        try{
            var claims = Jwts
                    .parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(apiSecret.getBytes()))
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

            var user = JwtResponse.getUser(claims);
            if(isEmpty(user) || isEmpty(user.getId())){
                throw new AuthenticationException("The user is not valid.");
            }
        }catch (Exception ex){
            ex.printStackTrace();
            throw new AuthenticationException("Error while trying to proccess the Access Token.");
        }
    }

    private String extractToken(String token){

        if(token == null || token.isEmpty()){
            throw new AuthenticationException("The access token was not informed");
        }

        if(token.contains(TXT_EMPTY_SPACE)){
            token = token.split(TXT_EMPTY_SPACE)[TOKEN_INDEX];
        }

        return token;
    }
}

