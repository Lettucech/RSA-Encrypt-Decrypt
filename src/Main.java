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
//			String data = RandomString.randomString(300);
			String data = new String();
			
			for (int i = 0; i < 300; i++) {
				data += "A";
			}
			
			System.out.println("Start Encrypt (" + data.length() + "char): " + data);
			
			String encrypted = test.encryptWithBuffer(data);
			System.out.println("Encrpted String: " + encrypted);
			
			String decrypted = test.decryptWithBuffer(encrypted);
			System.out.println("Decrypted String (" + decrypted.length() + "char): " + decrypted);
			
			for(int i = 0; i < decrypted.length(); i++) {
				char c = decrypted.charAt(i);
				if (c != 'A') {
					System.out.println("Non-A char is found: " + String.valueOf(c));
				}
			}
			
			System.out.println("Is result correct? " + data.equals(decrypted));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
