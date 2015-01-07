/**
 * Project: Pandora
 * File it.zerocool.pandora.utility/RequestUtilities.java
 * @author Marco Battisti
 */
package it.zerocool.pandora.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.util.Log;

/**
 * Aux class containing Http request utility
 * 
 * @author Marco Battisti
 * 
 */
public class RequestUtilities {

	
	private static final int BUFFER_SIZE = 8;
	private static final String ENCODING = "UTF-8";

	/**
	 * Calls HTTP GET method for retrieving InputStream from web
	 * 
	 * @param uri
	 *            is the URI to call
	 * @return the InputStream containing infos, null in case of connection
	 *         error
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static InputStream requestInputStream(String uri)
			throws ClientProtocolException, IOException {
		InputStream is = null;
		HttpClient httpclient = new DefaultHttpClient();
		uri = uri.replaceAll(" ", "%20");
		HttpGet httppost = new HttpGet(uri);
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		is = entity.getContent();
		return is;
	}

	/**
	 * Obtains String from InputStream
	 * 
	 * @param is
	 *            is the InputStream to elaborate
	 * @return InputStream data in String format
	 */
	public static String inputStreamToString(InputStream is) {
		String result = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, ENCODING), BUFFER_SIZE);
			StringBuilder sb = new StringBuilder();
			String line = reader.readLine();
			while (line != null) {
				sb.append(line + "\n");
				line = reader.readLine();
			}
			is.close();
			result = sb.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Obtains a Bitmap object from specified URI
	 * 
	 * @param url
	 *            is the URI of Bitmap
	 * @return a Bitmap
	 */
	public static Bitmap downloadBitmap(String url) {
		final AndroidHttpClient client = AndroidHttpClient
				.newInstance("Android");
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					final Bitmap bitmap = BitmapFactory
							.decodeStream(inputStream);
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			getRequest.abort();
			Log.w("ImageDownloader", "Error while retrieving bitmap from "
					+ url, e);
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return null;
	}
	
	/**
	 * Private constructor
	 */
	private RequestUtilities() {}

}
