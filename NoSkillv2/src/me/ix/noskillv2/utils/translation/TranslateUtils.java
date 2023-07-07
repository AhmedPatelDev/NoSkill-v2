package me.ix.noskillv2.utils.translation;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TranslateUtils {

	private static String urlToText(URL url) throws IOException {
		URLConnection urlConn = url.openConnection();
		urlConn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:2.0) Gecko/20100101 Firefox/4.0");
		Reader r = new java.io.InputStreamReader(urlConn.getInputStream(), Charset.forName("UTF-8"));
		StringBuilder buf = new StringBuilder();
		while (true) {
			int ch = r.read();
			if (ch < 0)
				break;
			buf.append((char) ch);
		}
		String str = buf.toString();
		return str;
	}
	
	public static String[] translate(String languageCode, String text) throws IOException {
		String urlText = generateURL("auto", languageCode, text);
		URL url = new URL(urlText);
		String rawData = urlToText(url);
		if (rawData == null) {
			return null;
		}
		String[] raw = rawData.split("\"");
		if (raw.length < 2) {
			return null;
		}
		
		String[] toReturn = {raw[1], raw[5]};
		
		return toReturn;
	}
	
	
	private static String generateURL(String sourceLanguage , String targetLanguage , String text) throws UnsupportedEncodingException {
		String encoded = URLEncoder.encode(text, "UTF-8");
		StringBuilder sb = new StringBuilder();
		sb.append("http://translate.google.com/translate_a/single");
		sb.append("?client=webapp");
		sb.append("&hl=en");
		sb.append("&sl=");
		sb.append(sourceLanguage);
		sb.append("&tl=");
		sb.append(targetLanguage);
		sb.append("&q=");
		sb.append(encoded);
		sb.append("&multires=1");
		sb.append("&otf=0");
		sb.append("&pc=0");
		sb.append("&trs=1");
		sb.append("&ssel=0");
		sb.append("&tsel=0");
		sb.append("&kc=1");
		sb.append("&dt=t");
		sb.append("&ie=UTF-8");
		sb.append("&oe=UTF-8");
		sb.append("&tk=");
		sb.append(generateToken(text));
		return sb.toString();
	}
	
	private static String generateToken(String text) {
		int tkk[] = TKK();
		int b = tkk[0];
		int e = 0;
		int f = 0;
		List<Integer> d = new ArrayList<Integer>();
		for (; f < text.length(); f++) {
			int g = text.charAt(f);
			if (0x80 > g) {
				d.add(e++, g);
			} else {
				if (0x800 > g) {
					d.add(e++, g >> 6 | 0xC0);
				} else {
					if (0xD800 == ( g & 0xFC00 ) && f + 1 < text.length() && 0xDC00 == ( text.charAt(f + 1) & 0xFC00 )) {
						g = 0x10000 + ( ( g & 0x3FF ) << 10 ) + ( text.charAt(++f) & 0x3FF );
						d.add(e++, g >> 18 | 0xF0);
						d.add(e++, g >> 12 & 0x3F | 0x80);
					} else {
						d.add(e++, g >> 12 | 0xE0);
						d.add(e++, g >> 6 & 0x3F | 0x80);
					}
				}
				d.add(e++, g & 63 | 128);
			}
		}
		
		int a_i = b;
		for (e = 0; e < d.size(); e++) {
			a_i += d.get(e);
			a_i = RL(a_i, "+-a^+6");
		}
		a_i = RL(a_i, "+-3^+b+-f");
		a_i ^= tkk[1];
		long a_l;
		if (0 > a_i) {
			a_l = 0x80000000l + ( a_i & 0x7FFFFFFF );
		} else {
			a_l = a_i;
		}
		a_l %= Math.pow(10, 6);
		return String.format(Locale.US, "%d.%d", a_l, a_l ^ b);
	}
	
	private static int[] TKK() {
		return new int[]{ 0x6337E , 0x217A58DC + 0x5AF91132 };
	}
	
	private static int RL(int a , String b) {
		for (int c = 0; c < b.length() - 2; c += 3) {
			int d = b.charAt(c + 2);
			d = d >= 65 ? d - 87 : d - 48;
			d = b.charAt(c + 1) == '+' ? shr32(a, d) : ( a << d );
			a = b.charAt(c) == '+' ? ( a + ( d & 0xFFFFFFFF ) ) : a ^ d;
		}
		return a;
	}
	
	private static int shr32(int x , int bits) {
		if (x < 0) {
			long x_l = 0xffffffffl + x + 1;
			return (int) ( x_l >> bits );
		}
		return x >> bits;
	}
	
	public static String getLangFromCode(String code) {
		String languages = 
				"Amharic-am,"
				+ "Arabic-ar,"
				+ "Basque-eu,"
				+ "Bengali-bn,"
				+ "English-en-GB,"
				+ "Portuguese-pt-BR,"
				+ "Bulgarian-bg,"
				+ "Catalan-ca,"
				+ "Cherokee-chr,"
				+ "Croatian-hr,"
				+ "Czech-cs,"
				+ "Danish-da,"
				+ "Dutch-nl,"
				+ "English-en,"
				+ "Estonian-et,"
				+ "Filipino-fil,"
				+ "Finnish-fi,"
				+ "French-fr,"
				+ "German-de,"
				+ "Greek-el,"
				+ "Gujarati-gu,"
				+ "Hebrew-iw,"
				+ "Hindi-hi,"
				+ "Hungarian-hu,"
				+ "Icelandic-is,"
				+ "Indonesian-id,"
				+ "Italian-it,"
				+ "Japanese-ja,"
				+ "Kannada-kn,"
				+ "Korean-ko,"
				+ "Latvian-lv,"
				+ "Lithuanian-lt,"
				+ "Malay-ms,"
				+ "Malayalam-ml,"
				+ "Marathi-mr,"
				+ "Norwegian-no,"
				+ "Polish-pl,"
				+ "Portuguese-pt-PT,"
				+ "Romanian-ro,"
				+ "Russian-ru,"
				+ "Serbian-sr,"
				+ "Chinese-zh-CN,"
				+ "Slovak-sk,"
				+ "Slovenian-sl,"
				+ "Spanish-es,"
				+ "Swahili-sw,"
				+ "Swedish-sv,"
				+ "Tamil-ta,"
				+ "Telugu-te,"
				+ "Thai-th,"
				+ "Chinese-zh-TW,"
				+ "Turkish-tr,"
				+ "Urdu-ur,"
				+ "Ukrainian-uk,"
				+ "Vietnamese-vi,"
				+ "Welsh-cy";
		
		for(String language : languages.split(",")) {
			if(language.split("-")[1].equals(code)) {
				return language.split("-")[0];
			}
		}
		return "n/a";
	}
}