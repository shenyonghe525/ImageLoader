package com.syh.imageloader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends Activity {

	private static final String URL = "http://www.imooc.com/api/teacher?type=4&num=30";

	private ListView lv;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
	}

	private void initViews() {
		lv = (ListView) findViewById(R.id.lv_news);
		new NewsAsyncTask().execute(URL);
	}

	/**
	 * 后台从网络获取listview的数据
	 * 
	 * @author shenyonghe
	 * 
	 *         2015-6-2
	 */
	class NewsAsyncTask extends AsyncTask<String, Void, List<NewsDto>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!NetState.isNetAvailable(getBaseContext())) {

			}
		}

		@Override
		protected List<NewsDto> doInBackground(String... params) {
			return getJsonDataFromUrl(params[0]);
		}

		@Override
		protected void onPostExecute(List<NewsDto> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			MyAdapter myAdapter = new MyAdapter(getBaseContext(), result,
					R.layout.new_item,lv);
			lv.setAdapter(myAdapter);
		}
	}

	/**
	 * 通过url来获取is之后从is中获取字符串，并解析json Tile:getJsonDataFromUrl
	 * 
	 * @param url
	 * @return List<NewsDto>
	 */
	private List<NewsDto> getJsonDataFromUrl(String url) {
		List<NewsDto> list = new ArrayList<NewsDto>();
		try {
			String jsonString = readStringFromIS(new java.net.URL(url)
					.openStream());
			JSONObject jsonObject = new JSONObject(jsonString);
			NewsDto newsDto;
			JSONArray jsonArray = jsonObject.getJSONArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				newsDto = new NewsDto();
				newsDto.setImageUrl(jsonObject.getString("picSmall"));
				newsDto.setTitle(jsonObject.getString("name"));
				newsDto.setContent(jsonObject.getString("description"));
				list.add(newsDto);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 从is中获得json字符串 Tile:readStringFromIS
	 * 
	 * @param is
	 * @return String
	 */
	private String readStringFromIS(InputStream is) {
		String result = "";
		try {
			String line = "";
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(isr);
			while ((line = bufferedReader.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
