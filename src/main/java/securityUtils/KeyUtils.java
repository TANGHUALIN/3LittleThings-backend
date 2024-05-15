package securityUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;

public class KeyUtils {
    private static final Properties properties = new Properties();

    static {
        //外部のファイルを読み込む、/ はsrc
        try (InputStream asStream = KeyUtils.class.getResourceAsStream("/application.properties")) {
            //Propertiesオブジェクトを通してStreamを解釈
            properties.load(asStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SecretKey getKeyForJwtToken() {
        byte[] secretBytes = Base64.getDecoder().decode(properties.getProperty("jwt.secret.key"));
        return new SecretKeySpec(secretBytes, 0, secretBytes.length, "HmacSHA256");
    }
}
