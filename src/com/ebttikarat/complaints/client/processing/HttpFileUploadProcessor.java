package com.ebttikarat.complaints.client.processing;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

import com.ebttikarat.complaints.client.common.Response;
import com.ebttikarat.complaints.util.FileUtils;

public class HttpFileUploadProcessor extends AbstractHttpClientProcessor {

	public void process() {
		AsyncFileUploadTask conn = new AsyncFileUploadTask(this);
		conn.execute(new String[] {  request.getRequestURL() });
	}

	public void preprocess() {
		super.preprocess();
	}

	public void terminate() {
		super.terminate();
	}

	private class AsyncFileUploadTask extends AsyncTask<String, Void, Response> {
		private IProcessor processor;

		public AsyncFileUploadTask(IProcessor processor) {
			this.processor = processor;
		}

		@Override
		protected Response doInBackground(String... params) {
			// TODO Auto-generated method stub
			Response res = new Response();
			String url = params[0];
			try {
				BasicNameValuePair filePath = (BasicNameValuePair)processor.getRequest().getParameters().get(0);
				res = execute(url, filePath.getValue());

			} catch (Exception ex) {
				ex.printStackTrace();
				Log.e("error", Log.getStackTraceString(ex));
			}
			return res;
		}
		private Response execute(String url,String filePath) throws Exception {
			Response response = new Response();
			String exsistingFileName = FileUtils.getFileName(filePath);

			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";
			try {
				// Open a HTTP connection to the URL
				URL connectURL = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) connectURL
						.openConnection();

				// Allow Inputs
				conn.setDoInput(true);

				// Allow Outputs
				conn.setDoOutput(true);

				// Don't use a cached copy.
				conn.setUseCaches(false);

				// Use a post method.
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
						+ exsistingFileName + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				
				// create a buffer of maximum size
				FileInputStream fileInputStream = new FileInputStream(filePath);
				int bytesAvailable = fileInputStream.available();
				int maxBufferSize = 1024;
				int bufferSize = Math.min(bytesAvailable, maxBufferSize);
				byte[] buffer = new byte[bufferSize];

				// read file and write it into form...
				int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// close streams
				fileInputStream.close();
				dos.flush();

				InputStream is = conn.getInputStream();
				
				// retrieve the response from server
				int ch;

				StringBuffer b = new StringBuffer();
				while ((ch = is.read()) != -1) {
					b.append((char) ch);
				}
				String s = b.toString();
				Log.i("Response", s);
				response.setResponseBody(s);
				dos.close();

			} catch (MalformedURLException ex) {
				Log.e("File", "error: " + ex.getMessage(), ex);
			}

			catch (IOException ioe) {
				Log.e("File", "error: " + ioe.getMessage(), ioe);
			}
			return response;

		}

		@Override
		protected void onPostExecute(Response res) {
			this.processor.prepareResponse(res);
			this.processor.getActivity().postExecution(res);
		}

		@Override
		protected void onPreExecute() {
			this.processor.getActivity().preExecution();
		}
	}
}
