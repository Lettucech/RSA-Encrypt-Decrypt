import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class Main {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		EncryptionHelper test = EncryptionHelper.newInstance();
		
		try {
			String data = RandomString.randomString(300);
			String encrypted = test.encryptWithBuffer(data);
			System.out.println(encrypted.substring(0,10) + " + " + String.valueOf(encrypted.length() - 10));
			String decrypted = test.decryptWithBuffer(encrypted);
			System.out.println(decrypted);
		} catch (InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | IOException e) {
			System.out.println(e.toString());
		}
	}
}
