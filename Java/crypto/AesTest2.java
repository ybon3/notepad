package crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Stanley 提供的版本
 */
public class AesTest2 {

	private static final String KEY_VALUE = "BU2020010000211";

	public static void main(String args[]) throws Exception {
		try {
			String privateKey = "qwer1234";
			MessageDigest messageDigest = null;
			try {
				messageDigest = MessageDigest.getInstance("SHA-256");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			int maxLength = 16;
			System.out.println("maxLength: " + maxLength);
			messageDigest.update(privateKey.getBytes("UTF-8"));
			byte[] raw = new byte[maxLength];
			byte[] md = messageDigest.digest();
			for (int ii = 0; ii < maxLength; ii++) {
				raw[ii] = md[ii];
			}
			SecretKeySpec secretKey = new SecretKeySpec(raw, "AES");

			String encstr = encryptAES(KEY_VALUE, secretKey);
			System.out.println("Encrypt String: " + encstr);
			encstr = "lh52yxDlAurSyaW+R/Std2olkXt4di/OOqThGB+7IQ==";
			String decstr = decryptAES(encstr, secretKey);
			System.out.println("Decrypt String: " + decstr);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static String encryptAES(String oristr, SecretKeySpec secretKey) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] byteCipherText = cipher.doFinal(oristr.getBytes("UTF-8"));
			byte[] des  = new byte[cipher.getIV().length + byteCipherText.length];
			System.arraycopy(cipher.getIV(), 0, des, 0, cipher.getIV().length);
			System.arraycopy(byteCipherText, 0, des, cipher.getIV().length, byteCipherText.length);
			String res = Base64.getEncoder().encodeToString(des);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String decryptAES(String encstr, SecretKeySpec secretKey) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
			byte[] value = Base64.getDecoder().decode(encstr);
			byte[] iv = new byte[cipher.getBlockSize()];
			for (int ii = 0; ii < cipher.getBlockSize(); ii++) {
				iv[ii] = value[ii];
			}
			byte[] payload = new byte[value.length - cipher.getBlockSize()];
			for (int ii = 0; ii < payload.length; ii++) {
				payload[ii] = value[cipher.getBlockSize() + ii];
			}
			cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
			String strDecryptedText = new String(cipher.doFinal(payload));
			return strDecryptedText;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
