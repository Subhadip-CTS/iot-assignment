package com.training.vehicleservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    //check expiration time

    //alternate way to generate secret way (properties file)
    private static final String SECRET_KEY="C0C4603F79267DFB5D56868F08250AB8C4D2CFC2DC20645CEFB08A14AC81E4C1420B80F2DF6EBC551F814D6B567C63900C46A99485D5F91E95549267F17CFC47C65D20670278503A3F87A7D520165B66D5D3402716D4895890AEF52D5BFDB7F55258DEA390A3AF56A4B949B37FC0AF30F53BBEB3304066701260F7E29EAA93B5";
    public String extractUserName(String token)
    {
        return extractClaim(token,Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails)
    {
        return generateToken(Map.of("role",userDetails.getAuthorities()),userDetails);
    }

    public boolean isTokenValid(String token,UserDetails userDetails)
    {
        final String userName=extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails)
    {

        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(getSingnKey(), SignatureAlgorithm.HS256).compact();
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimResolver)
    {
        final Claims claims=extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Claims extractAllClaims(String token)
    {
        return Jwts.parserBuilder().setSigningKey(getSingnKey())
                .build().parseClaimsJws(token)
                .getBody();
    }

    private Key getSingnKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
