import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EncryptionHelper {
	private static final int KEY_SIZE = 2048;
	private KeyPair keyPair;
	private Cipher cipher;

	public static EncryptionHelper newInstance() {
		return new EncryptionHelper();
	}

	public EncryptionHelper() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(KEY_SIZE);
			keyPair = generator.generateKeyPair();
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}
	}

	public String encrypt(String data)
			throws InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
		byte[] dataBytes = data.getBytes("UTF-8");
		byte[] encrypted = cipher.doFinal(dataBytes);
		return new String(Base64.getEncoder().encode(encrypted));
	}

	public String encryptWithBuffer(String data)
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
		cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
		int blockSize = KEY_SIZE / 8 - 11;
		ByteArrayInputStream dataBytes = new ByteArrayInputStream(data.getBytes("UTF-8"));
		ByteArrayOutputStream encrypted = new ByteArrayOutputStream();

		System.out.println("Bytes to be encrypted: " + dataBytes.available());
		
		while (dataBytes.available() > 0) {
			byte[] buffer = new byte[blockSize];
			dataBytes.read(buffer);
			encrypted.write(cipher.doFinal(buffer));
		}

		System.out.println("Encrypted bytes: " + encrypted.toByteArray().length);
		return new String(Base64.getEncoder().encode(encrypted.toByteArray()));
	}

	public String decrypt(String data)
			throws InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
		byte[] bytes = Base64.getDecoder().decode(data);
		byte[] decrypted = cipher.doFinal(bytes);
		return new String(decrypted);
	}

	public String decryptWithBuffer(String data)
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
		cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
		int blockSize = KEY_SIZE / 8;
		ByteArrayInputStream dataBytes = new ByteArrayInputStream(Base64.getDecoder().decode(data));
		ByteArrayOutputStream decrypted = new ByteArrayOutputStream();

		System.out.println("Bytes to be decrypted: " + dataBytes.available());
		
		while (dataBytes.available() > 0) {
			byte[] buffer = new byte[blockSize];
			dataBytes.read(buffer, 0, blockSize);
			for (byte b: cipher.doFinal(buffer)) {
				if (b != 0x00) {
					decrypted.write(b);
				}
			}
		}

		System.out.println("Decrypted bytes: " + decrypted.toByteArray().length);
		return new String(decrypted.toByteArray(), "UTF-8");
	}
}
