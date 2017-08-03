package com.zhb.sinaspider.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * @author zhanghaobo
 * @date 2017年7月29日下午7:54:39
 * @todo TODO
 */

/**
 * 加密工具类
 * 
 * @author zhanghaobo
 *
 */
public class EncodeUtil {

	/**
	 * 对登陆用户名进行base64加密
	 * 
	 * @param username用户名
	 * @return 加密后的字符串
	 */
	public static String username2Base64(String username) {

		return Base64.getEncoder().encodeToString(username.getBytes());
	}

	private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };

	/**
	 * 返回经过RSA加密后的密码
	 * @param pubkey新浪公钥
	 * @param exponentHex
	 * @param pwd 要加密的密码
	 */
	public static String rsa(String pubkey, String exponentHex, String pwd)
			throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException {
		KeyFactory factory = KeyFactory.getInstance("RSA");

		BigInteger m = new BigInteger(pubkey, 16);
		BigInteger e = new BigInteger(exponentHex, 16);
		RSAPublicKeySpec spec = new RSAPublicKeySpec(m, e);
		// 创建公钥
		RSAPublicKey pub = (RSAPublicKey) factory.generatePublic(spec);
		Cipher enc = Cipher.getInstance("RSA");
		enc.init(Cipher.ENCRYPT_MODE, pub);

		byte[] encryptedContentKey = enc.doFinal(pwd.getBytes("UTF-8"));
		return new String(encodeHex(encryptedContentKey));
	}

	protected static char[] encodeHex(final byte[] data, final char[] toDigits) {
		final int l = data.length;
		final char[] out = new char[l << 1];

		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
			out[j++] = toDigits[0x0F & data[i]];
		}
		return out;
	}

	public static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
		return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
	}

	public static char[] encodeHex(final byte[] data) {
		return encodeHex(data, true);
	}
	
	/**
	 * 得到新浪加密密码的最终值
	 * @param pwd   用户密码
	 * @param pubkey  服务器返回的参数之一
	 * @param servertime   服务器返回的参数之一
	 * @param nonce  服务器返回的参数之一
	 * @return  新浪加密密码的最终值
	 */
	public static String getSP(String pwd, String pubkey, String servertime, String nonce) {
		String t = "10001";
		String message = servertime + "\t" + nonce + "\n" + pwd;
		String result = null;
		try {
			result = rsa(pubkey, t, message);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

}
