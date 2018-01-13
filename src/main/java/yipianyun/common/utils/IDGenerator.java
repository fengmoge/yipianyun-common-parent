package yipianyun.common.utils;
 
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

public class IDGenerator {
	
	public static String generate() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		byte[] bytes = new byte[16];
		for (int i=0; i<bytes.length; i++) {
			String sub = uuid.substring(2*i, 2*i+1);
			bytes[i] = (byte) (128 - Integer.parseInt(sub, 16));
		}
		return Base64.encodeBase64URLSafeString(bytes).toUpperCase();
	}

}
