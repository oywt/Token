package entity;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import net.minidev.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建Token,验证Token,定义Token的三种状态
 */
public class JWT {

    /**
     * token格式
     * header
     *    {
     *         “alg”:"HS256"
     *         "type":"JWT"
     *    }
     *
     *   payLoad
     *      {
     *          "uid":""//用户id
     *          "iat":""//签发时间
     *          "exp":""//过期时间
     *      }
     */

    /**
     * 秘钥
     */
    private static final byte[] SCRICT = "3d990d2276917dfac04467df11fff26d".getBytes();

    /**
     * 签名
     */
    private static  JWSSigner signer=null;

    /**
     * 验证器
     */
    private static  JWSVerifier verifier = null;

    static {
        try {
            signer = new MACSigner(SCRICT);
            verifier = new MACVerifier(SCRICT);
        } catch (KeyLengthException e) {
            e.printStackTrace();
        } catch (JOSEException e) {
            e.printStackTrace();
        }
    }

    /**
     * 过期
     */
    public static final String EXPIRED = "EXPIRED";

    /**
     * 无效
     */
    public static final String INVALID = "INVALID";

    /**
     * 有效
     */
    public static final String VALID = "VALID";

    /**
     * token是null值
     */
    public static final String TOKENISNULL="NOTHAVATOKEN";

    /**
     *初始化的header头数据
     * {
     *     “alg”:"HS256"
     *     "type":"JWT"
     * }
     */
    public static final JWSHeader header = new JWSHeader(JWSAlgorithm.HS256, JOSEObjectType.JWT,null,null, null, null, null, null, null, null, null, null, null);

    /**
     * 创建Token
     * @param payLoad Mpa类型的payLoad引用
     * @return 返回签名后的Token
     */
    public static String createToke(Map<String,String> payLoad){
        String toke = null;
        JWSObject jwsObject = new JWSObject(header,new Payload(new JSONObject(payLoad)));
        //应用HS256签名
        try {
            jwsObject.sign(signer);
            toke = jwsObject.serialize();
        } catch (JOSEException e) {

            System.out.println("签名失败");
        }
        return toke;
    }

    /**
     *验证token
     * @param token
     * @return
     */
    public static Map<String,Object> validToken(String token){
        Map<String,Object> result=new HashMap<String, Object>();

        if(null==token&&token.length()==0){
            result.put("state",TOKENISNULL);
            return result;
        }

        try {
            //将token解析成JWSObject
            JWSObject parse = JWSObject.parse(token);
            Payload payload = parse.getPayload();
            if(parse.verify(verifier)){
                 //验证token有效期
                JSONObject jsonObject = payload.toJSONObject();
                if(jsonObject.containsKey("exp")){
                    long date = new Date().getTime();
                    long exp = Long.valueOf(jsonObject.get("exp").toString());
                   if(date>exp)
                        result.put("state",EXPIRED);

                }
                result.put("data",jsonObject);
            }
        } catch (ParseException e) {
            //解析token失败,格式不正确
            result.put("state",INVALID);

        } catch (JOSEException e) {
           //校验失败
            result.put("state",INVALID);

        }

        return result;
    }
}
