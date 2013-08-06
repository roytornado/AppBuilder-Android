package com.midland.base.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class SimpleCrypto {
	public static String encrypt(String cleartext) {
		try {
			return encrypt(getSecretKey("midland", "1200"), cleartext);
		} catch (Exception e) {
			Common.e(e);
			return "";
		}
	}
	
	public static String decrypt(String encrypted) {
		try {
			return decrypt(getSecretKey("midland", "1200"), encrypted);
		} catch (Exception e) {
			Common.e(e);
			return "";
		}
	}
	
	private static final String PROVIDER = "BC";
	private static final int SALT_LENGTH = 20;
	private static final int IV_LENGTH = 16;
	private static final int PBE_ITERATION_COUNT = 100;
	
	private static final String RANDOM_ALGORITHM = "SHA1PRNG";
	private static final String HASH_ALGORITHM = "SHA-512";
	private static final String PBE_ALGORITHM = "PBEWithSHA256And256BitAES-CBC-BC";
	private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
	private static final String SECRET_KEY_ALGORITHM = "AES";
	
	private static String encrypt(SecretKey secret, String cleartext) throws Exception {
		try {
			
			byte[] iv = generateIv();
			String ivHex = toHex(iv);
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			
			Cipher encryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM, PROVIDER);
			encryptionCipher.init(Cipher.ENCRYPT_MODE, secret, ivspec);
			byte[] encryptedText = encryptionCipher.doFinal(cleartext.getBytes("UTF-8"));
			String encryptedHex = toHex(encryptedText);
			
			return ivHex + encryptedHex;
			
		} catch (Exception e) {
			throw new Exception("Unable to encrypt", e);
		}
	}
	
	private static String decrypt(SecretKey secret, String encrypted) throws Exception {
		try {
			Cipher decryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM, PROVIDER);
			String ivHex = encrypted.substring(0, IV_LENGTH * 2);
			String encryptedHex = encrypted.substring(IV_LENGTH * 2);
			IvParameterSpec ivspec = new IvParameterSpec(toByte(ivHex));
			decryptionCipher.init(Cipher.DECRYPT_MODE, secret, ivspec);
			byte[] decryptedText = decryptionCipher.doFinal(toByte(encryptedHex));
			String decrypted = new String(decryptedText, "UTF-8");
			return decrypted;
		} catch (Exception e) {
			throw new Exception("Unable to decrypt", e);
		}
	}
	
	private static SecretKey getSecretKey(String password, String salt) throws Exception {
		try {
			PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), toByte(salt), PBE_ITERATION_COUNT, 256);
			SecretKeyFactory factory = SecretKeyFactory.getInstance(PBE_ALGORITHM, PROVIDER);
			SecretKey tmp = factory.generateSecret(pbeKeySpec);
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), SECRET_KEY_ALGORITHM);
			return secret;
		} catch (Exception e) {
			throw new Exception("Unable to get secret key", e);
		}
	}
	
	private static String getHash(String password, String salt) throws Exception {
		try {
			String input = password + salt;
			MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM, PROVIDER);
			byte[] out = md.digest(input.getBytes("UTF-8"));
			return toHex(out);
		} catch (Exception e) {
			throw new Exception("Unable to get hash", e);
		}
	}
	
	private static String generateSalt() throws Exception {
		try {
			SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
			byte[] salt = new byte[SALT_LENGTH];
			random.nextBytes(salt);
			String saltHex = toHex(salt);
			return saltHex;
		} catch (Exception e) {
			throw new Exception("Unable to generate salt", e);
		}
	}
	
	private static byte[] generateIv() throws NoSuchAlgorithmException, NoSuchProviderException {
		SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
		byte[] iv = new byte[IV_LENGTH];
		random.nextBytes(iv);
		return iv;
	}
	
	private static String toHex(String txt) {
		return toHex(txt.getBytes());
	}
	
	private static String fromHex(String hex) {
		return new String(toByte(hex));
	}
	
	private static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
		return result;
	}
	
	private static String toHex(byte[] buf) {
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}
	
	private final static String HEX = "0123456789ABCDEF";
	
	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}
}
