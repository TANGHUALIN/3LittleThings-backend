package securityUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;



public class VerificationTokenUtils {
	
	    public static String generateJwtToken(String userId, long expirationMillis, Key signingKey) {
	        // Get the current time
	        long nowMillis = System.currentTimeMillis();
	        // Calculate the expiration time
	        long expMillis = nowMillis + expirationMillis;

	        // Build the JWT
	        return Jwts.builder()
	                .setSubject(userId)
	                .setIssuedAt(new Date(nowMillis))
	                .setExpiration(new Date(expMillis))
	                .signWith(signingKey)
	                .compact();
	    }

    public static String createVerificationLink(String url, String token) {

		return "https://www.3littlethings.top/" + url + "/" + token;
	    }

	private static Integer getUidFromToken(String token, Key signingKey) {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
		return Integer.parseInt(claims.getSubject());

    }

	public static Integer getUidFromRequest(HttpServletRequest req) {
		String authHeader = req.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring("Bearer ".length());
            System.out.println(token);
			return VerificationTokenUtils.getUidFromToken(token, KeyUtils.getKeyForJwtToken());
		} else {
			return -1;
		}
	}
}
