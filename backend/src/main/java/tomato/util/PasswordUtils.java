package tomato.util;

import tomato.common.BusinessException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码工具类
 * <p>
 * 提供密码加密和验证功能，使用 SHA-256 算法
 * </p>
 */
public final class PasswordUtils {

    /**
     * 私有构造函数，防止实例化
     */
    private PasswordUtils() {
    }

    /**
     * 使用 SHA-256 算法加密密码
     *
     * @param rawPassword 原始密码
     * @return 加密后的十六进制字符串
     */
    public static String encode(String rawPassword) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] digest = messageDigest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte item : digest) {
                builder.append(String.format("%02x", item));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new BusinessException(500, "密码加密失败");
        }
    }

    /**
     * 验证密码是否匹配
     *
     * @param rawPassword      原始密码
     * @param encodedPassword 加密后的密码
     * @return 密码是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
