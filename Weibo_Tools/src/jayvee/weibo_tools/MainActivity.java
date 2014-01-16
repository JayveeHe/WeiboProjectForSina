package jayvee.weibo_tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

import jayvee.weibo.data.weibo_statuses;
import jayvee.weibo.timeline.TimelineListActivity;
import jayvee.weibo.timeline.UserTimelineListActivity;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.UsersAPI;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	// // ���ó��ò���
	// private static final String CONSUMER_KEY = "3942639720";
	// private static final String REDIRECT_URL = "http://weibo.com";
	// private Weibo mWeibo;
	private Button authBtn, ssoBtn, cancelBtn, sendBtn, myhomeBtn;
	private TextView mText;
	private static com.sina.weibo.sdk.auth.Oauth2AccessToken accessToken;
	public static MainActivity instance_MainActivity;

	// LoginActivity loginActivity;

	public static void setToken(com.sina.weibo.sdk.auth.Oauth2AccessToken Token) {
		accessToken = Token;
	}

	public static com.sina.weibo.sdk.auth.Oauth2AccessToken getToken() {
		return accessToken;
	}

	public static final String TAG = "sinasdk";
	// /**
	// * SsoHandler ����sdk֧��ssoʱ��Ч��
	// */
	com.sina.weibo.sdk.auth.sso.SsoHandler mSsoHandler;

	// 相关参数
	public static final long since_id = 0l;// 起始微博的ID
	public static final long max_id = 0l;// 最大微博ID
	public static final int count = 5;// 每页的记录条数
	public static final int page = 1;// 读取的页数

	public static ArrayList<weibo_statuses> statuses_list = new ArrayList<weibo_statuses>();

	// MyReceiver myReceiver = new MyReceiver();
	// IntentFilter filter = new IntentFilter("timeline_ready");

	// public void sendready() {
	// sendBroadcast(new Intent("timeline_ready"));
	// }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		instance_MainActivity = this;

		// 进入主时间线
		
		
		UsersAPI uApi = new UsersAPI(accessToken);
		uApi.show(accessToken.getUid(), new RequestListener() {
			
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
				try {
					 JSONObject data = new JSONObject(response);
					 TextView loginusername = (TextView)findViewById(R.id.loginuser);
					 
					 loginusername.setText(data.getString("screen_name").toString());
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		Button btn_swt = (Button) findViewById(R.id.btn_switch);
		btn_swt.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				// TimelineData tlData = new TimelineData();
				try {
					// while (statuses_list.isEmpty())
					// tlData.getTimelineData(MainActivity.getToken(), since_id,
					// max_id,
					// count, page, statuses_list);
					Intent intent = new Intent(MainActivity.this,
							TimelineListActivity.class);
					startActivity(intent);

				} catch (Exception e) {
					Toast.makeText(MainActivity.this, "进入时间线失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		// 发送微博
		mText = (TextView) findViewById(R.id.edit_content);
		sendBtn = (Button) findViewById(R.id.btn_send);
		sendBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				com.sina.weibo.sdk.openapi.legacy.StatusesAPI api = new com.sina.weibo.sdk.openapi.legacy.StatusesAPI(
						MainActivity.accessToken);
				String content = null;
				try {
					content = mText.getText().toString();
					mText.setText("");
				} catch (Exception e) {
					// Toast.makeText(getApplicationContext(), "������������ݣ�",
					// Toast.LENGTH_SHORT).show();
					// runOnUiThread(new Runnable() {
					// public void run() {
					// Toast.makeText(MainActivity.this, "������������ݣ�",
					// Toast.LENGTH_LONG).show();
					// }
					// });
					// return;
				}
				api.update(content, "0", "0", new RequestListener() {

					public void onIOException(IOException arg0) {
						// TODO Auto-generated method stub
						// Toast.makeText(getApplicationContext(), "IO���󣡣�",
						// Toast.LENGTH_SHORT).show();
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(MainActivity.this, "IO错误",
										Toast.LENGTH_LONG).show();
							}
						});
						Log.e("io", "io错误");
					}

					public void onError(WeiboException arg0) {
						// TODO Auto-generated method stub
						// Toast.makeText(getApplicationContext(), "�����쳣��",
						// Toast.LENGTH_SHORT).show();
						Log.e("主界面", "微博发送错误");
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(MainActivity.this,
										"微博发送错误", Toast.LENGTH_LONG)
										.show();
							}
						});
					}

					public void onComplete(String arg0) {
						// TODO Auto-generated method stub
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(MainActivity.this, "发送成功！",
										Toast.LENGTH_LONG).show();
							}
						});
						// Toast.makeText(getApplicationContext(), "���ͳɹ���",
						// Toast.LENGTH_SHORT).show();
						Log.i("主界面", "微博发送成功");
					}

					public void onComplete4binary(
							ByteArrayOutputStream responseOS) {
						// TODO Auto-generated method stub

					}
				});

			}
		});

		// 进入我的首页
		myhomeBtn = (Button) findViewById(R.id.btn_myhome);
		myhomeBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(MainActivity.this,
						UserTimelineListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("UserName", null);
				it.putExtras(bundle);
				startActivity(it);
			}
			// UsersAPI uApi = new UsersAPI(accessToken);
			// uApi.show("Jayvee_He", new RequestListener() {
			//
			// public void onIOException(IOException arg0) {
			// // TODO Auto-generated method stub
			//
			// }
			//
			// public void onError(WeiboException arg0) {
			// // TODO Auto-generated method stub
			// arg0.printStackTrace();
			// Log.e("error", "test error!");
			// }
			//
			// public void onComplete(String response) {
			// // TODO Auto-generated method stub
			// // Log.v("chenggong", "�ɹ�");
			// try {
			// JSONObject data = new JSONObject(response);
			// // System.out.println(data.get("status"));
			// JSONObject status = (JSONObject) data.get("status");
			// final String text = status.getString("text");
			// // System.out.println(text);
			// // final JSONObject info = status.getJSONObject(0);
			// // String location;
			// // location = info.getString("location");
			// // final String text = data.get("text").toString();
			// runOnUiThread(new Runnable() {
			// public void run() {
			// Toast.makeText(MainActivity.this, text,
			// Toast.LENGTH_LONG).show();
			// }
			// });
			// // System.out.println(location);
			// // Log.i("��ȡ����Ϣ", info.toString());
			// } catch (JSONException e) {
			// e.printStackTrace();
			// }
			// }
			//
			// public void onComplete4binary(
			// ByteArrayOutputStream responseOS) {
			// // TODO Auto-generated method stub
			//
			// }
			// });
			// // sApi.friendsTimeline(0, 0, 0, 10, false, 0, false, null);
			// // Toast.makeText(getApplicationContext(), "��������Ϣ��",
			// // Toast.LENGTH_SHORT).show();
			//
			// }
		});

		// 取消授权
		authBtn = (Button) findViewById(R.id.btn_auth);
		authBtn.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// AlertDialog.Builder builder = new AlertDialog.Builder(this);
				Builder builder = new Builder(MainActivity.this);
				final AlertDialog alertDialog = builder.create();
				alertDialog.setTitle("是否确定注销？");
				alertDialog.setButton("确定",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								// setResult(123123);
								// MainActivity.instance_MainActivity.finish();
								Intent intent = new Intent(MainActivity.this,
										LoginActivity.class);
								Bundle bundle = new Bundle();
								bundle.putBoolean("isCancleAuth", true);
								intent.putExtras(bundle);
								startActivity(intent);
								MainActivity.instance_MainActivity.finish();
							}

						});
				alertDialog.setButton2("取消",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								alertDialog.dismiss();
							}

						});
				alertDialog.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	// public class MyReceiver extends BroadcastReceiver {
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// // TODO Auto-generated method stub
	// startActivity(new Intent(MainActivity.this,
	// TimelineListActivity.class));
	// }
	// }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainActivity.instance_MainActivity.finish();
		// unregisterReceiver(myReceiver);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LoginActivity.instance_LoginActivity.finish();
		Log.i("主界面", "登陆界面成功关闭");

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
}
