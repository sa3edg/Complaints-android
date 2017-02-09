package com.ebttikarat.complaints.client.processing;

import static com.ebttikarat.complaints.common.config.Constants.SERVER_URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

import com.ebttikarat.complaints.client.common.Request;
import com.ebttikarat.complaints.client.common.Response;
import com.ebttikarat.complaints.gui.IActivity;

public abstract class AbstractHttpClientProcessor implements IProcessor {

	protected Request request;
	protected Response response;
	protected IActivity activity;

	public void process() throws Exception {
		AsynConnectionTask conn = new AsynConnectionTask(this);
		conn.execute(new String[] { SERVER_URL + request.getRequestURL() });
	}

	public void preprocess() {

		if (!request.isAsync()) {
			if (activity != null) {
				activity.preExecution();
			}
		}
	}

	public void prepareResponse(Response res) {
		setResponse(res);
		response.setRequestName(getRequest().getRequestName());
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;

	}

	public void terminate() {

	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public void setActivity(IActivity activity) {
		// TODO Auto-generated method stub
		this.activity = activity;
	}

	public IActivity getActivity() {
		// TODO Auto-generated method stub
		return this.activity;
	}

	private class AsynConnectionTask extends
			AsyncTask<String, Void, Response> {
		private HttpClient httpclient = new DefaultHttpClient();
		private IProcessor processor;

		public AsynConnectionTask(IProcessor processor) {
			this.processor = processor;
		}

		@Override
		protected Response doInBackground(String... params) {
			// TODO Auto-generated method stub
			Response res = new Response();
			try {
				HttpResponse response = execute(params[0]);
				String responseBody = EntityUtils.toString(
						response.getEntity(), HTTP.UTF_8);
				res.setResponseBody(responseBody);
				Log.i("in background", (String) res.getResponseBody());

			} catch (Exception ex) {
				ex.printStackTrace();
				Log.e("error", Log.getStackTraceString(ex));
			}
			return res;
		}

		private HttpResponse execute(String url) throws Exception {
			HttpResponse response = null;
			switch (processor.getRequest().getRequestMethod()) {
			case Request.POST_METHOD:
				HttpPost httppost = new HttpPost(url);
				httppost.addHeader("Accept", Request.MIME_TYPE);
				httppost.addHeader("Content-type", Request.CONTENT_TYPE);
				StringEntity entity = new StringEntity(processor.getRequest()
						.getRequestBody(), HTTP.UTF_8);
				// httppost.addHeader("Accept", "application/json");
				// httppost.addHeader("Content-type",
				// "application/json;charset=UTF-8");
				// StringEntity entity = new StringEntity(processor.getRequest()
				// .getRequestBody());
				httppost.setEntity(entity);
				response = httpclient.execute(httppost);
				break;
			case Request.GET_METHOD:
				HttpGet httpGet = new HttpGet(url);
				httpGet.addHeader("Accept", Request.MIME_TYPE);
				httpGet.addHeader("Content-type", Request.CONTENT_TYPE);
				response = httpclient.execute(httpGet);
				break;
			case Request.PUT_METHOD:
				HttpPut httpPut = new HttpPut(url);
				httpPut.addHeader("Accept", Request.MIME_TYPE);
				httpPut.addHeader("Content-type", Request.CONTENT_TYPE);
				StringEntity resEntity = new StringEntity(processor
						.getRequest().getRequestBody(), HTTP.UTF_8);
				httpPut.setEntity(resEntity);
				response = httpclient.execute(httpPut);
				break;
			default:
				HttpPost deafultHttppost = new HttpPost(url);
				deafultHttppost.addHeader("Accept", Request.MIME_TYPE);
				deafultHttppost.addHeader("Content-type", Request.CONTENT_TYPE);
				StringEntity responseEntity = new StringEntity(processor
						.getRequest().getRequestBody(), HTTP.UTF_8);
				deafultHttppost.setEntity(responseEntity);
				response = httpclient.execute(deafultHttppost);
				break;
			}
			return response;

		}

		@Override
		protected void onPostExecute(Response res) {
			Log.i("in post execute", (String) res.getResponseBody());
			this.processor.prepareResponse(res);
			if (!this.processor.getRequest().isAsync()) {
				this.processor.getActivity().postExecution(
						this.processor.getResponse());
			}
		}
	}
}
