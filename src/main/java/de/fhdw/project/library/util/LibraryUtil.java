package net.bashbuddy.backend.util;

import com.google.common.hash.Hashing;
import net.bashbuddy.backend.exception.RandaloException;
import net.bashbuddy.backend.util.response.ErrorType;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class RandaloUtil {

    public static byte[] checkAvatar(final String image) throws RandaloException {
        return checkAvatar(image, 50000000);
    }

    public static byte[] checkAvatar(final String image, final int maxSize) throws RandaloException {
        if (!Base64.isBase64(image))
            throw new RandaloException(ErrorType.IMAGE_IS_NOT_IN_THE_RIGHT_FORMAT);
        final byte[] imageByte = Base64.decodeBase64(image);
        if (imageByte.length > maxSize)
            throw new RandaloException(ErrorType.IMAGE_IS_TOO_LARGE);
        try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageByte)){
            ImageIO.read(byteArrayInputStream);
        }catch (Exception e){
            throw new RandaloException(ErrorType.IMAGE_IS_NOT_IN_THE_RIGHT_FORMAT);
        }
        return imageByte;
    }

    public static String encryptPassword(final String password, final UUID salt) {
        return Hashing.sha512().hashString(password + password, StandardCharsets.UTF_8).toString();
    }

}
