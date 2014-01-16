package jayvee.weibo_tools;

import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboDialogException;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.HttpManager;

import jayvee.weibo_tools.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	// ���ó��ò���
	// private static final String CONSUMER_KEY = "3942639720";
	// private static final String REDIRECT_URL = "http://weibo.com";
	public WeiboAuth mWeiboAuth;
	// private Button authBtn, ssoBtn, cancelBtn, sendBtn, testBtn;
	private TextView mText;
	public static com.sina.weibo.sdk.auth.Oauth2AccessToken accessToken;
	public static final String TAG = "sinasdk";

	public static LoginActivity instance_LoginActivity = null;

	/**
	 * SsoHandler ����sdk֧��ssoʱ��Ч��
	 */
	SsoHandler mSsoHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		instance_LoginActivity = this;// 获取被activity实例
		mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY,
				Constants.REDIRECT_URL, Constants.SCOPE);
		Button btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					mWeiboAuth.authorize(new AuthDialogListener(),
							WeiboAuth.OBTAIN_AUTH_CODE);
				} catch (Exception e) {
					Log.e("认证", "认证失败");
				}

			}
		});
		Bundle bundle = getIntent().getExtras();
		// Boolean isCancleAuth = bundle.getBoolean("isCancleAuth");
		if (null == bundle) {// 即不是从取消授权而来
			Toast.makeText(LoginActivity.this, "创建成功", Toast.LENGTH_SHORT)
					.show();
			accessToken = AccessTokenKeeper.readAccessToken(this);
			if (0 == accessToken.getExpiresTime()) {
			} else {
				MainActivity.setToken(accessToken);
				startActivity(new Intent(LoginActivity.this, MainActivity.class));
			}
		} else {
			MainActivity.instance_MainActivity.finish();
			AccessTokenKeeper.clear(this);// 取消授权
		}
		// mWeibo = WeiboAuth.getInstance(CONSUMER_KEY, REDIRECT_URL);

	}

	public void useCodeGetToken(String code) {
		// String code = values.getString("code");
		if (code != null) {
			Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
			WeiboParameters params = new WeiboParameters();
			params.add("client_id", Constants.APP_KEY);
			params.add("client_secret", Constants.APP_SECRET);
			params.add("grant_type", "authorization_code");
			params.add("redirect_uri", Constants.REDIRECT_URL);
			params.add("code", code);
			String result = "";

			getResult(params);

		} else {
			Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
		}
	}

	private void getResult(final WeiboParameters params) {
		new Thread(new Runnable() {

			public void run() {
				try {
					String result = HttpManager.openUrl(
							"https://api.weibo.com/oauth2/access_token",
							"POST", params, null);
					JSONObject json = new JSONObject(result);
					String token = json.getString("access_token");
					String expires_in = json.getString("expires_in");
					accessToken = new Oauth2AccessToken(token, expires_in);
					MainActivity.setToken(accessToken);
					if (accessToken.isSessionValid()) {
						AccessTokenKeeper.writeAccessToken(LoginActivity.this,
								accessToken);
						try {
							// Intent intent = new Intent(LoginActivity.this,
							// MainActivity.class);
							// Toast.makeText(LoginActivity.this, "认证成功",
							// Toast.LENGTH_LONG).show();
							// Log.i("认证", "认证成功");
							startActivity(new Intent(LoginActivity.this,
									MainActivity.class));
						} catch (Exception e) {
							Toast.makeText(LoginActivity.this, "进入主界面失败",
									Toast.LENGTH_SHORT).show();
						}
					}
				} catch (WeiboException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	class AuthDialogListener implements WeiboAuthListener {

		public void onCancel() {
			// TODO Auto-generated method stub

		}

		public void onComplete(Bundle values) {
			// TODO Auto-generated method stub
			String code = values.getString("code");
			useCodeGetToken(code);
			// Log.i("认证", "认证成功");
			// Log.d(tag, msg)
		}

		public void onError(WeiboDialogException arg0) {
			// TODO Auto-generated method stub
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(LoginActivity.this, "认证失败",
							Toast.LENGTH_LONG).show();
				}
			});
			Log.e("认证", "认证失败");

		}

		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(LoginActivity.this, "认证异常",
							Toast.LENGTH_LONG).show();
				}
			});
			Log.e("认证", "认证异常");

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 可以根据多个请求代码来作相应的操作
		if (123123 == resultCode) {
			AccessTokenKeeper.clear(this);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	//
	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	//
	// /**
	// * ��������ע�͵��Ĵ��룬����sdk֧��ssoʱ��Ч��
	// */
	// if (mSsoHandler != null) {
	// mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
	// }
	// }
}
