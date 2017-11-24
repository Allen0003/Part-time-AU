package Office365;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import Const.Const;
import properties.UserCalendar;

public class JavaToPhp {

	public void sendPost(UserCalendar calendar) {
		OutputStreamWriter out = null;
		try {
			URL url1 = new URL(Const.URL);
			HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
			connection.setConnectTimeout(5000);// 5 secs
			connection.setReadTimeout(5000);// 5 secs

			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");

			out = new OutputStreamWriter(connection.getOutputStream());
			String urlParameters = "{\"id\":\"" + calendar.getId() + "\", \"subject\":\"" + calendar.getSubject()
					+ "\",\"startTime\":\"" + calendar.getStartTime() + "\", \"endTime\":\"" + calendar.getEndTime()
					+ "\"}";

			out.write(urlParameters);
			out.flush();
			out.close();
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			connection.disconnect();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// test code
	public static void main(String[] args) throws Exception {
		OutputStreamWriter out = null;
		try {
			URL url = new URL("http://localhost/test/psAdviser.php");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(5000);// 5 secs
			connection.setReadTimeout(5000);// 5 secs

			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");

			out = new OutputStreamWriter(connection.getOutputStream());
			String urlParameters = "{\"id\":\"QooGYY\", \"subject\":998877,\"startTime\":\"QooGYY\", \"endTime\":99}";
			out.write(urlParameters);
			out.flush();
			out.close();
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			connection.disconnect();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
