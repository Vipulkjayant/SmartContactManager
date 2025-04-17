package smartcontact.manager.JWT;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64.Decoder;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    

    private String secretkey="";

    private JwtService()
    {
        
        try {
           KeyGenerator key= KeyGenerator.getInstance("HmacSHA256"); //key bnanae wala mil gya
           SecretKey k=key.generateKey();//key bna de
           secretkey=Base64.getEncoder().encodeToString(k.getEncoded());
 
        } catch (Exception e) {
          System.out.println(e);
        }
    }

    public String getToken(String userName)
    {
        Map<String,Object> claims=new HashMap<>();

        return Jwts.builder()
        .claims()
        .add(claims)
        .subject(userName)

                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()*60*60*10))
                .and()
                .signWith(getKey())
                 .compact();

                 
        }



        private SecretKey getKey()
       {
 
          byte[] key= Decoders.BASE64.decode(secretkey);

          return Keys.hmacShaKeyFor(key);
        }


        public String extractUserName(String token) {
      

          return extractClaims(token,Claims::getSubject);
     }
 
private <T> T extractClaims(String token,Function<Claims,T> claimResolver) {
    final Claims claims=extractAllClaims(token);
          return claimResolver.apply(claims);
        }
                    
     private Claims extractAllClaims(String token)
      {
       
         return Jwts.parser()
         .verifyWith(getKey())
         .build()
         .parseSignedClaims(token)
         .getPayload();
             
    }
        
    public boolean validate(String token, UserDetails userDetails) {
     try {
         // Extract the username from the token
         String usernameFromToken = extractUserName(token);

         // Check if the username matches and if the token is not expired
         return (usernameFromToken.equals(userDetails.getUsername()) && !isTokenExpired(token));
     } catch (Exception e) {
         // Handle token validation errors
         System.err.println("Token validation failed: " + e.getMessage());
         return false;
     }
 }

 private boolean isTokenExpired(String token) {
     Claims claims = extractAllClaims(token);
     return claims.getExpiration().before(new java.util.Date());
 }


}
