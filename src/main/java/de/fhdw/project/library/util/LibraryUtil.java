package de.fhdw.project.library.util;

import com.google.common.hash.Hashing;
import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.util.response.ErrorType;

import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class LibraryUtil {

    public static byte[] checkAvatar(final String image) throws LibraryException {
        return checkAvatar(image, 50000000);
    }

    public static byte[] checkAvatar(final String image, final int maxSize) throws LibraryException {
        if (!Base64.isBase64(image))
            throw new LibraryException(ErrorType.IMAGE_IS_NOT_IN_THE_RIGHT_FORMAT);
        final byte[] imageByte = Base64.decodeBase64(image);
        if (imageByte.length > maxSize)
            throw new LibraryException(ErrorType.IMAGE_IS_TOO_LARGE);
        try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageByte)){
            ImageIO.read(byteArrayInputStream);
        }catch (Exception e){
            throw new LibraryException(ErrorType.IMAGE_IS_NOT_IN_THE_RIGHT_FORMAT);
        }
        return imageByte;
    }

    public static String encryptPassword(final String password, final UUID salt) {
        return Hashing.sha512().hashString(password + password, StandardCharsets.UTF_8).toString();
    }

}
