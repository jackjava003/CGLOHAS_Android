package com.example.jack.cglohas._00_GlobalService;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class VerifyUtils{
	public static final String SITE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
	public static final String ADDRESS_VERIFY_URL = "https://maps.google.com/maps/api/geocode/json?sensor=false&language=zh-tw&region=tw&address=";
	private String result;

	public String verifyAdd(String add) {

		try {
			URL verifyUrl = new URL(ADDRESS_VERIFY_URL + add);
			Log.e("123123",add);
			// Open Connection to URL
			HttpsURLConnection conn = (HttpsURLConnection) verifyUrl.openConnection();

			// Add Request Header
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
			conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			// Send Request
			conn.setDoOutput(false);
			conn.connect();

			// Response code return from server.
			int responseCode = conn.getResponseCode();
//			System.out.println("responseCode=" + responseCode);

			// Get the InputStream from Connection to read data sent from the
			// server.



			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					if(line.indexOf("status")!=-1){
						sb.append(line);
					}
				}
				br.close();
				conn.disconnect();
				result = sb.toString().substring(sb.toString().indexOf(":")+1,sb.toString().lastIndexOf("\""));
				result = result.substring(result.indexOf("\"")+1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result =  null;
		}
		return  result;
	}

	public String getResult(){
		Log.e("123123",result);
		return result;}

	public static boolean verifyPicFile(String fileName) {

		int nameIndex = fileName.lastIndexOf(".");
		boolean result = false;
		if (nameIndex != -1) {
			fileName = fileName.substring(nameIndex);
			String[] types = { ".png", ".jpg", ".gif", ".jpeg" };
			for (String x : types) {
				if (fileName.equalsIgnoreCase(x)) {
					result = true;
				}
			}
		}
		return result;
	}

	public static boolean verifyCardNum(String cardNum) {

		if (cardNum.length() != 16) {
			return false;
		}
		int[] x = new int[16];
		int sum1 = 0, sum2 = 0;
		for (int i = 0; i < 16; i++) {
			try {
				x[i] = Integer.parseInt(cardNum.substring(i, i + 1));
			} catch (NumberFormatException nfe) {
				return false;
			}
			if (i % 2 == 0) {
				if (x[i] < 5)
					x[i] *= 2;
				else
					x[i] = x[i] * 2 - 10 + 1;
				sum2 += x[i];
			} else {
				sum1 += x[i];
			}
		}
		sum1 -= x[15];
		int y = 10 - ((sum1 + sum2) % 10);
		return (x[15] == y);
	}

}
