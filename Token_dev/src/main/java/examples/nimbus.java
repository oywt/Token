package examples;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import net.minidev.json.JSONObject;

import java.security.SecureRandom;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class nimbus {

    /**
     * 秘钥
     */
    private static final byte[] SCRICT="3d990d2276917dfac04467df11fff26d".getBytes();

    public static void main(String[] args) {
        createToke();
       //voidToken("eyJhbGciOiJIUzI1NiJ9.aGVsbG8sd29yZA.vCF8nOMCwvDdqH3qKR0peCf6jeQ-JRtbXq619_RiKBs");
    }
    public static void createToke()  {

        //随机获取一个32bit的秘钥
        SecureRandom random = new SecureRandom();
        byte[] secret = new byte[256];
        random.nextBytes(secret);
        System.out.println("秘钥是==================="+secret.toString());

        //使用秘钥创建HMAC签名
        try {
            JWSSigner signer = new MACSigner(SCRICT);
            JWSObject jwsObject=new JWSObject(new JWSHeader(JWSAlgorithm.HS256),new Payload("hello,word"));

            try {
                //应用签名
                jwsObject.sign(signer);
                String token = jwsObject.serialize();
                System.out.println("token is ============="+token);
            } catch (JOSEException e) {
                e.printStackTrace();
            }
        } catch (KeyLengthException e) {
            e.printStackTrace();
        }

    }

    /**
     * 验证token
     * @param token token的值
     */
    public static void voidToken(String token,Map<String, Object> payload){
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            JWSVerifier verifier = new MACVerifier(SCRICT);
            if(jwsObject.verify(verifier))
                System.out.println("token合法");
            JWSHeader header = jwsObject.getHeader();
           //JWSObject构造函数可用接受Mpa引用 Payload接受JWSObject参数

            Payload payload1 = jwsObject.getPayload();



        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("token格式不合法");
        } catch (JOSEException e) {
            e.printStackTrace();
        }
    }
}
