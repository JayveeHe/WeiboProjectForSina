package jayvee.weibo.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import jayvee.weibo.timeline.TimelineListActivity;
import jayvee.weibo_tools.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.legacy.WeiboAPI.FEATURE;

import android.R;
import android.R.integer;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

public class TimelineData {
	// 存储的信息
	// private weibo_statuses wStatuses = new weibo_statuses();
	private ArrayList<weibo_statuses> statuses_list = new ArrayList<weibo_statuses>(
			MainActivity.count);
	private ArrayList<weibo_statuses> user_statuses_list = new ArrayList<weibo_statuses>(
			MainActivity.count);
	private boolean isReady = false;

	public boolean isDataReady() {
		return isReady();
	}

	public void getUserTimelineData(String UserName,
			com.sina.weibo.sdk.auth.Oauth2AccessToken acccessToken,
			long since_id, long max_id, final int count, int page) {
		StatusesAPI statusesAPI = new StatusesAPI(MainActivity.getToken());
		getUser_statuses_list().clear();
		setReady(false);// 重置ready值，直到读取完毕
		if (null != UserName) {// 读取指定的帐号微博

			statusesAPI.userTimeline(UserName, since_id, max_id, count, page,
					false, FEATURE.ALL, false, new RequestListener() {

						public void onIOException(IOException e) {
							// TODO Auto-generated method stub

						}

						public void onError(WeiboException e) {
							// TODO Auto-generated method stub

						}

						public void onComplete4binary(
								ByteArrayOutputStream responseOS) {
							// TODO Auto-generated method stub

						}

						public void onComplete(String response) {
							// TODO Auto-generated method stub
							ProcessWeibo(response, getUser_statuses_list());
						}
					});
		} else {// 读取当前登陆的帐号微博
			statusesAPI.userTimeline(since_id, max_id, count, page, false,
					FEATURE.ALL, false, new RequestListener() {

						public void onIOException(IOException e) {
							// TODO Auto-generated method stub

						}

						public void onError(WeiboException e) {
							// TODO Auto-generated method stub

						}

						public void onComplete4binary(
								ByteArrayOutputStream responseOS) {
							// TODO Auto-generated method stub

						}

						public void onComplete(String response) {
							// TODO Auto-generated method stub
							ProcessWeibo(response, getUser_statuses_list());
						}
					});
		}
	}

	public void getTimelineData(
			com.sina.weibo.sdk.auth.Oauth2AccessToken acccessToken,
			long since_id, long max_id, final int count, int page) {
		StatusesAPI statusesAPI = new StatusesAPI(MainActivity.getToken());
		// final MainActivity mainActivity = new MainActivity();
		getStatuses_list().clear();
		setReady(false);// 重置ready值，直到读取完毕
		statusesAPI.homeTimeline(since_id, max_id, count, page, false,
				FEATURE.ALL, false, myListener
		// new RequestListener() {
		//
		// public void onComplete(String response) {
		// // TODO Auto-generated method stub
		// ProcessWeibo(response);
		// }
		//
		// public void onComplete4binary(
		// ByteArrayOutputStream responseOS) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// public void onIOException(IOException e) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// public void onError(WeiboException e) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// }
				);

	}

	public ArrayList<weibo_statuses> getStatuses_list() {
		return statuses_list;
	}

	public void refreshTimelineData(
			com.sina.weibo.sdk.auth.Oauth2AccessToken acccessToken,
			long since_id, long max_id, final int count, int page) {
		StatusesAPI statusesAPI = new StatusesAPI(MainActivity.getToken());
		// final MainActivity mainActivity = new MainActivity();
		setReady(false);// 重置ready值，直到读取完毕
		getStatuses_list().clear();
		statusesAPI.homeTimeline(since_id, max_id, count, page, false,
				FEATURE.ALL, false, myListener
		// new RequestListener() {
		//
		// public void onComplete(String response) {
		// // TODO Auto-generated method stub
		// ProcessWeibo(response);
		// }
		//
		// public void onComplete4binary(
		// ByteArrayOutputStream responseOS) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// public void onIOException(IOException e) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// public void onError(WeiboException e) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// }
				);

	}

	public void LoadOldTimelineData(
			com.sina.weibo.sdk.auth.Oauth2AccessToken acccessToken,
			long since_id, long max_id, final int count, int page) {
		StatusesAPI statusesAPI = new StatusesAPI(MainActivity.getToken());
		// final MainActivity mainActivity = new MainActivity();
		setReady(false);// 重置ready值，直到读取完毕
		getStatuses_list().clear();
		statusesAPI.homeTimeline(since_id, max_id, count, page, false,
				FEATURE.ALL, false, myListener
		// new RequestListener() {
		//
		// public void onComplete(String response) {
		// // TODO Auto-generated method stub
		// ProcessWeibo(response);
		// }
		//
		// public void onComplete4binary(
		// ByteArrayOutputStream responseOS) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// public void onIOException(IOException e) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// public void onError(WeiboException e) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// }
				);

	}

	private RequestListener myListener = new RequestListener() {

		public void onIOException(IOException e) {
			// TODO Auto-generated method stub

		}

		public void onError(WeiboException e) {
			// TODO Auto-generated method stub

		}

		public void onComplete4binary(ByteArrayOutputStream responseOS) {
			// TODO Auto-generated method stub

		}

		public void onComplete(String response) {
			// TODO Auto-generated method stub
			ProcessWeibo(response, getStatuses_list());
		}
	};

	@SuppressWarnings("unused")
	public Bitmap getNetBitmap(String bitmapUrl) {
		try {
			URL url = new URL(bitmapUrl);
			String responseCode = url.openConnection().getHeaderField(0);

			// if (responseCode.indexOf("200") < 0) {
			// throw new Exception("图片文件不存在或路径错误，错误代码：" + responseCode);
			// }
			// ////、注意！！！！此处应该加入异常检测，容我慢慢研究这个代码返回的是什么
			Bitmap temp = BitmapFactory.decodeStream(url.openStream());
			return temp;

		} catch (Exception e) {
			// TODO: handle exception
			Log.e("获取图片", "获取图片异常");
			return null;
		}
	}

	public void ProcessWeibo(String response, ArrayList<weibo_statuses> Datalist) {
		Log.i("时间线", "读取成功");
		try {
			JSONTokener jsonTokener = new JSONTokener(response);
			JSONObject timeline = (JSONObject) jsonTokener.nextValue();
			JSONArray statuses_array = timeline.getJSONArray("statuses");
			for (int i = 0; i < statuses_array.length(); i++) {
				JSONObject status = statuses_array.getJSONObject(i);
				weibo_statuses wStatuses = new weibo_statuses();
				wStatuses.favorited = (Boolean) status.getBoolean("favorited");
				wStatuses.text = (String) status.getString("text");// 填充微博内容
				wStatuses.ID = Long.parseLong(status.getString("id"));// 获取该条微博的id
				wStatuses.created_at = (String) status.getString("created_at");
				// 获取微博配图，此处为了方便，不考虑多副图的情况
				JSONArray pic_urls = status.getJSONArray("pic_urls");
				if (0 != pic_urls.length()) {
					for (int j = 0; j < pic_urls.length(); j++) {
						wStatuses.pic_urls.getThumbnail_pics().add(
								pic_urls.getJSONObject(j).getString(
										"thumbnail_pic"));
						// 填充weibo_status里的pic_urls
					}
					// 暂时只读取微博配图的第一个
					wStatuses.bmiddle_pic_url = status.getString("bmiddle_pic");
					// wStatuses.bmiddle_pic =
					// getNetBitmap(wStatuses.bmiddle_pic_url);
					wStatuses.thumbnail_pic = getNetBitmap(wStatuses.pic_urls
							.getThumbnail_pics().get(0));
				}
				// getNetBitmap(status
				// .getString("thumbnail_pic"));//获取微博配图的缩略版本
				JSONObject user = status.getJSONObject("user");
				wStatuses.username = (String) user.getString("screen_name");
				wStatuses.profile_image_url = (String) user
						.getString("profile_image_url");// 头像image的地址
				wStatuses.usericon = getNetBitmap(wStatuses.profile_image_url);
				JSONObject re_status = null;
				// 检查是否转发了微博
				try {
					re_status = status.getJSONObject("retweeted_status");
				} catch (Exception e) {
					// TODO: handle exception
					Log.d("调试", "没有转发微博");
				}
				if (null != re_status)// 即有转发微博
				{
					wStatuses.retweeted_status.retweeted_text = (String) re_status
							.getString("text");// 填充转发微博内容
					wStatuses.retweeted_status.retweeted_ID = Long
							.parseLong(re_status.getString("id"));// 获取该条微博的id
					wStatuses.retweeted_status.retweeted_created_at = (String) re_status
							.getString("created_at");
					// 获取微博配图，此处为了方便，不考虑多副图的情况
					JSONArray re_pic_urls = re_status.getJSONArray("pic_urls");
					if (0 != re_pic_urls.length()) {
						for (int j = 0; j < re_pic_urls.length(); j++) {
							wStatuses.re_pic_urls.getretweeted_Thumbnail_pics()
									.add(re_pic_urls.getJSONObject(j)
											.getString("thumbnail_pic"));
							// 填充weibo_status里的pic_urls
						}
						// 暂时只读取微博配图的第一个
						wStatuses.retweeted_status.retweeted_bmiddle_pic_url = re_status
								.getString("bmiddle_pic");
						// wStatuses.bmiddle_pic =
						// getNetBitmap(wStatuses.bmiddle_pic_url);
						wStatuses.retweeted_status.retweeted_thumbnail_pic = getNetBitmap(wStatuses.re_pic_urls
								.getretweeted_Thumbnail_pics().get(0));
					}
					// getNetBitmap(status
					// .getString("thumbnail_pic"));//获取微博配图的缩略版本
					JSONObject re_user = re_status.getJSONObject("user");
					wStatuses.retweeted_status.retweeted_username = (String) re_user
							.getString("screen_name");
					wStatuses.retweeted_status.retweeted_profile_image_url = (String) re_user
							.getString("profile_image_url");// 头像image的地址
					// wStatuses.retweeted_status.retweeted_usericon =
					// getNetBitmap(wStatuses.profile_image_url);
				}
				Datalist.add(wStatuses);
				// getStatuses_list().add(wStatuses);
				// Log.i("调试", "第" + i + "个用户名："
				// + getStatuses_list().get(i).username + "	\n微博："
				// + getStatuses_list().get(i).text);
				Log.i("调试", "第" + i + "个用户名：" + Datalist.get(i).username
						+ "	\n微博：" + Datalist.get(i).text);
			}

			System.out.println(Datalist.size());
			Log.i("时间线", "填充完毕");
			// Intent intent = new Intent("timeline_ready");
			// mainActivity.sendready();
			setReady(true);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}

	public ArrayList<weibo_statuses> getUser_statuses_list() {
		return user_statuses_list;
	}

	public void setUser_statuses_list(
			ArrayList<weibo_statuses> user_statuses_list) {
		this.user_statuses_list = user_statuses_list;
	}

}
