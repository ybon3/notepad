package crypto;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesTest {

	public static final String ALGORITHM = "AES";
	public static final String SECRET = "qwer1234";

	public static void main(String[] args) throws Exception {

		SecretKeySpec secretKeySpec = secretKeyWithSha256(SECRET, ALGORITHM);

		//ENCRYPT
		String plainText = "BT2020020001233";

		byte[] enc = encryptAES(secretKeySpec, plainText.getBytes("UTF-8"));

		System.out.println(Base64.getEncoder().encodeToString(enc));


		//DECRYPT
		String cipherText = "lh52yxDlAurSyaW+R/Std2olkXt4di/OOqThGB+7IQ==";

		byte[] data = Base64.getDecoder().decode(cipherText.getBytes("UTF-8"));

		System.out.println(new String(decrypt(secretKeySpec, data), "UTF-8"));
	}

	public static byte[] encryptAES(SecretKeySpec secretKey, byte[] plainText) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] cipherText = cipher.doFinal(plainText);

		byte[] iv = cipher.getIV();
		byte[] des = new byte[iv.length + cipherText.length];
		System.arraycopy(iv, 0, des, 0, iv.length);
		System.arraycopy(cipherText, 0, des, iv.length, cipherText.length);
		return des;
	}

	public static byte[] decrypt(SecretKey secretKey, byte[] cipherText) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

		int blockSize = cipher.getBlockSize();
		byte[] iv = slice(cipherText, 0, blockSize);
		byte[] payload = slice(cipherText, blockSize, cipherText.length);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

		return cipher.doFinal(payload);
	}

	public static SecretKeySpec secretKeyWithSha256(String secret, String algorithm)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(secret.getBytes("utf-8"));
		int maxAllowedKeyLength = Cipher.getMaxAllowedKeyLength(algorithm) / 8;
		byte[] raw = slice(md.digest(), 0, maxAllowedKeyLength);
		return new SecretKeySpec(raw, algorithm);
	}

	public static byte[] slice(byte[] src, int s, int e) {
		if (s >= src.length) {
			return new byte[0];
		}

		if (e > src.length) {
			e = src.length;
		}

		return Arrays.copyOfRange(src, s, e);
	}
}
