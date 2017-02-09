package com.ebttikarat.complaints.client.processing;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

import com.ebttikarat.complaints.client.common.Response;

public class HttpClientLocationProcessor extends AbstractHttpClientProcessor {
	protected static final String MAP_API = "http://maps.googleapis.com/maps/api/staticmap?";

	public void process() {
		AsyncUserLocationConnectionTask conn = new AsyncUserLocationConnectionTask(this);
		conn.execute(new String[] { MAP_API + request.getRequestURL() });
	}

	public void preprocess() {
		super.preprocess();
	}

	public void terminate() {
		super.terminate();
	}

	private class AsyncUserLocationConnectionTask extends
			AsyncTask<String, Void, Response> {
		private HttpClient httpclient = new DefaultHttpClient();
		private HttpGet httpGet;
		private IProcessor processor;

		public AsyncUserLocationConnectionTask(IProcessor processor) {
			this.processor = processor;
		}

		@Override
		protected Response doInBackground(String... params) {
			// TODO Auto-generated method stub
			Response res = new Response();

			String url = params[0];
			httpGet = new HttpGet(url);
			try {
				HttpResponse response = httpclient.execute(httpGet);
				byte[] mapImage = IOUtils.toByteArray(response.getEntity()
						.getContent());
				res.setResponseData(mapImage);

			} catch (Exception ex) {
				ex.printStackTrace();
				Log.e("error", Log.getStackTraceString(ex));
			}
			return res;
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
