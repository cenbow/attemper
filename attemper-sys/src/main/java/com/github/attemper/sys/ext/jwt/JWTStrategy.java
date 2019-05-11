package com.github.attemper.sys.ext.jwt;

import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.sys.exception.JWTDecodedException;
import com.github.attemper.sys.exception.JWTExpiredException;
import org.apache.commons.lang.StringUtils;

public interface JWTStrategy {
    String SECRET = "1!2@3#4$5%6^7&8*";

    String KEY = "tenant";

    /**
     * create json web token by tenant
     * @param expireMills
     * @param tenant
     * @return
     */
    String createToken(long expireMills, Tenant tenant);

    /**
     * @param token
     * @return
     */
    Tenant parseToken(String token) throws JWTExpiredException, JWTDecodedException;

    /**
     * @param token
     * @param tenant
     * @return
     */
    default boolean verify(String token, Tenant tenant) {
        Tenant tokenUser = parseToken(token);
        return StringUtils.equalsIgnoreCase(tenant.getUserName(), tokenUser.getUserName());
    }
}