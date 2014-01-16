package jayvee.weibo.timeline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.legacy.WeiboAPI.FEATURE;

import jayvee.weibo.data.ShareData;
import jayvee.weibo.data.TimelineData;
import jayvee.weibo.data.weibo_statuses;
import jayvee.weibo.status_detail.StatusDetailActivity;
import jayvee.weibo_tools.MainActivity;
import jayvee.weibo_tools.R;
import jayvee.weibo_tools.R.id;
import jayvee.weibo_tools.R.layout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.drawable.ScaleDrawable;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TimelineListActivity extends Activity {
	// 相关参数
	long since_id = 0;// 起始微博的ID
	long max_id = 0;// 最大微博ID
	int count = 30;// 每页的记录条数
	int page = 1;// 读取的页数
	public static TimelineListActivity instance_TimelineListActivity;
	boolean isLoadingNew = true;
	private AlertDialog dialog;
	private ProgressDialog progressDialog;
	
	TimelineData tlData = new TimelineData();
	private ArrayList<weibo_statuses> Statuses_list = null;
	MyAdapter myAdapter = null;

	// public ArrayList<weibo_statuses> statuses_list = new
	// ArrayList<weibo_statuses>(
	// count);

	public ProgressDialog getProgressDialog() {
		return progressDialog;
	}

	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline_activity);
		instance_TimelineListActivity = this;
		final ShareData shareData = (ShareData) getApplication();
		Log.i("时间线", "创建完毕");
		since_id = MainActivity.since_id;// 起始微博的ID
		max_id = MainActivity.max_id;// 最大微博ID
		count = MainActivity.count;// 每页的记录条数
		page = MainActivity.page;// 读取的页数
		tlData.getTimelineData(MainActivity.getToken(), since_id, max_id,
				count, page);
		wait_for_loading();
		ListView listView = (ListView) findViewById(R.id.ListView);
		listView.setOnItemClickListener(new OnItemClickListener() {
			// 点击list内的item则进入微博详情界面
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				weibo_statuses clickStatuses = (weibo_statuses) arg0
						.getItemAtPosition(arg2);
				shareData.setShareData(clickStatuses);
				//
				// transObject tObject = new transObject();
				// tObject.setID(clickStatuses.ID);
				// tObject.setText(clickStatuses.text);
				// tObject.setUsername(clickStatuses.username);
				// Toast.makeText(
				// TimelineListActivity.this,
				// "你点到了第" + (arg2 + 1) + "个！" + ""
				// + clickStatuses.username, Toast.LENGTH_SHORT)
				// .show();

				Intent it = new Intent(TimelineListActivity.this,
						StatusDetailActivity.class);
				// Bundle bundle = new Bundle();
				// bundle.putParcelable("clicked", tObject);
				// it.putExtras(bundle);
				startActivity(it); // startActivityForResult(it,REQUEST_CODE);
			}
		});
		// listView

		// Log.d("测试", Statuses_list.toString());
		// Statuses_list = (ArrayList<weibo_statuses>)
		// tlData.getStatuses_list();
		Button btn_refresh = (Button) findViewById(R.id.menu_refresh);

		btn_refresh.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(TimelineListActivity.this, "正在刷新……",
						Toast.LENGTH_SHORT).show();
				tlData.getTimelineData(MainActivity.getToken(),
						getStatuses_list().get(0).ID, max_id, count, page);
				// 取出微博ID比原list的第一个微博id更大的微博（即最新的微博）
				wait_for_loading();
				// ListView listView = (ListView) findViewById(R.id.ListView);
				// listView.setAdapter(new MyAdapter());
				//
				// new MyAdapter().notifyDataSetChanged();
			}
		});

		Button btn_loadmore = (Button) findViewById(R.id.menu_loadmore);
		btn_loadmore.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(TimelineListActivity.this, "正在读取……",
						Toast.LENGTH_SHORT).show();
				tlData.LoadOldTimelineData(
						MainActivity.getToken(),
						since_id,
						getStatuses_list().get(getStatuses_list().size() - 1).ID - 1,
						count, page);
				// 取出微博ID比原list的最后一个微博id更小的微博（即更旧的微博）
				isLoadingNew = false;
				wait_for_loading();
				// ListView listView = (ListView) findViewById(R.id.ListView);
				// listView.setAdapter(new MyAdapter());
				// myAdapter.notifyDataSetChanged();
			}
		});

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// Builder builder = new Builder(TimelineListActivity.this);
		// dialog = builder.setTitle("————请稍等————").setMessage("正在读取……")
		// .setCancelable(false).create();
		// dialog.show();
		// new Thread(new Runnable() {
		//
		// public void run() {
		// // TODO Auto-generated method stub
		// while (tlData.isDataReady() == false)
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// handler.sendEmptyMessage(111);
		// }
		// }).start();
	}

	private class MyAdapter extends BaseAdapter {

		public int getCount() {
			// TODO Auto-generated method stub
			return getStatuses_list().size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return getStatuses_list().get(position);// 返回某处的微博信息类
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (null == convertView) {
				convertView = View.inflate(TimelineListActivity.this,
						R.layout.weibolist, null);
				holder = new ViewHolder();
				/** 得到各个控件的对象 */
				holder.username = (TextView) convertView
						.findViewById(R.id.username);
				holder.weibo_text = (TextView) convertView
						.findViewById(R.id.weibo_text);
				holder.usericon = (ImageView) convertView
						.findViewById(R.id.usericon);
				holder.created_at = (TextView) convertView
						.findViewById(R.id.created_at);
				holder.weibo_image = (ImageView) convertView
						.findViewById(R.id.weibo_image);
				// 转发的微博控件
				holder.retweeted_username = (TextView) convertView
						.findViewById(R.id.re_username);
				holder.retweeted_text = (TextView) convertView
						.findViewById(R.id.re_text);
				holder.retweeted_image = (ImageView) convertView
						.findViewById(R.id.re_image);
				holder.re_layout = (LinearLayout) convertView
						.findViewById(R.id.re_layout);
				convertView.setTag(holder);// 绑定ViewHolder对象

			} else
				holder = (ViewHolder) convertView.getTag();

			holder.username.setText(getStatuses_list().get(position).username
					.toString());
			holder.weibo_text.setText(getStatuses_list().get(position).text
					.toString());
			holder.usericon
					.setImageBitmap(getStatuses_list().get(position).usericon);
			holder.created_at.setText("发布于："
					+ getStatuses_list().get(position).created_at);
			if (null != getStatuses_list().get(position).thumbnail_pic) {
				holder.weibo_image.setImageBitmap(getStatuses_list().get(
						position).thumbnail_pic);
				holder.weibo_image.setVisibility(ImageView.VISIBLE);
				holder.weibo_image.setClickable(true);// 如果有图片则进行点击放大处理
				holder.weibo_image.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(TimelineListActivity.this, "你点到了图片",
								Toast.LENGTH_SHORT).show();
					}
				});
			} else
				holder.weibo_image.setVisibility(ImageView.GONE);// 没有图就滚粗
			// /////判断是否有转发微博
			if (0 != getStatuses_list().get(position).retweeted_status.retweeted_ID) {
				holder.re_layout.setVisibility(LinearLayout.VISIBLE);
				holder.re_layout.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(TimelineListActivity.this, "你点到了转发微博！",
								Toast.LENGTH_SHORT).show();

					}
				});
				holder.retweeted_username.setText(getStatuses_list().get(
						position).retweeted_status.retweeted_username
						.toString());
				holder.retweeted_text
						.setText(getStatuses_list().get(position).retweeted_status.retweeted_text
								.toString());
				// 设置转发微博的内容
				if (null != getStatuses_list().get(position).re_pic_urls) {
					holder.retweeted_image
							.setImageBitmap(getStatuses_list().get(position).retweeted_status.retweeted_thumbnail_pic);
					holder.retweeted_image.setVisibility(ImageView.VISIBLE);
					holder.retweeted_image.setClickable(true);// 如果有图片则进行点击放大处理
					holder.retweeted_image
							.setOnClickListener(new OnClickListener() {

								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									Toast.makeText(TimelineListActivity.this,
											"你点到了转发的图片", Toast.LENGTH_SHORT)
											.show();
								}
							});
				} else
					holder.retweeted_image.setVisibility(ImageView.GONE);// 没有图就滚粗
			} else
				holder.re_layout.setVisibility(LinearLayout.GONE);// 没有转发微博就滚粗
			return convertView;
		}
	}

	public final class ViewHolder {
		public TextView weibo_text;
		public TextView username;
		public ImageView usericon;
		public TextView created_at;
		public ImageView weibo_image;
		// 转发的微博
		public TextView retweeted_text;
		public TextView retweeted_username;
		public ImageView retweeted_image;
		public LinearLayout re_layout;
	}

	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 111) {
				dialog.dismiss();
				if (null == getStatuses_list()) {
					setStatuses_list((ArrayList<weibo_statuses>) tlData
							.getStatuses_list().clone());
					ListView listView = (ListView) findViewById(R.id.ListView);
					setMyAdapter(new MyAdapter());
					listView.setAdapter(getMyAdapter());
					Toast.makeText(TimelineListActivity.this,
							"本次共读取" + tlData.getStatuses_list().size() + "条微博",
							Toast.LENGTH_SHORT).show();
				} else if (isLoadingNew) {
					Toast.makeText(TimelineListActivity.this,
							"本次共更新" + tlData.getStatuses_list().size() + "条微博",
							Toast.LENGTH_SHORT).show();
					getStatuses_list().addAll(0, tlData.getStatuses_list());// 新微博添加到原list头
					// ListView listView = (ListView)
					// findViewById(R.id.ListView);
					// listView.setAdapter(new MyAdapter());
					myAdapter.notifyDataSetChanged();

				} else {
					Toast.makeText(TimelineListActivity.this,
							"本次共读取" + tlData.getStatuses_list().size() + "条微博",
							Toast.LENGTH_SHORT).show();
					getStatuses_list().addAll(tlData.getStatuses_list());// 旧微博添加到原list尾
					isLoadingNew = true;
					// ListView listView = (ListView)
					// findViewById(R.id.ListView);
					// listView.setAdapter(new MyAdapter());
					myAdapter.notifyDataSetChanged();
					// listView.setSelection(getStatuses_list().size()
					// - tlData.getStatuses_list().size());
				}
			}
			;
		};
	};

	private void wait_for_loading() {
		 Builder builder = new Builder(this);
		// 进度窗口的设置
//		setProgressDialog(new ProgressDialog(this));
//		getProgressDialog().setTitle("请稍等");
//		getProgressDialog().setMessage("正在读取……");
//		getProgressDialog().setIndeterminate(true);
//		getProgressDialog().setCancelable(true);
//		getProgressDialog().setOnCancelListener(new OnCancelListener() {
//
//			public void onCancel(DialogInterface arg0) {
//				// TODO Auto-generated method stub
//				startActivity(new Intent(TimelineListActivity.this,
//						MainActivity.class));
//			}
//		});
//		getProgressDialog().show();
		// dialog.setOnCancelListener(new OnCancelListener() {
		//
		// public void onCancel(DialogInterface arg0) {
		// // TODO Auto-generated method stub
		// tlData.getStatuses_list().clear();
		// }
		// });
		if (null == getStatuses_list()) {
//			progressDialog.show();
			 dialog = builder.setTitle("————请稍等————").setMessage("正在读取……")
			 .setCancelable(false).create();
			 dialog.show();

		}
		// else
		// dialog.setCancelable(true);
		new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				int timeoutcount = 0;
				while (tlData.isDataReady() == false) {

					try {
						timeoutcount++;
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (timeoutcount > 60)// 读取时间大于30秒则超时
						break;
				}
				if (tlData.isDataReady()) {
					Message msg = new Message();
					msg.obtain();
					msg.what = 4213;
					tlData.setReady(false);
					handler.sendEmptyMessage(111);
				} else {
					// Toast.makeText(TimelineListActivity.this,
					// "连接超时！",
					// Toast.LENGTH_SHORT).show();
					TimelineListActivity.instance_TimelineListActivity.finish();
				}
			}
		}).start();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public ArrayList<weibo_statuses> getStatuses_list() {
		return Statuses_list;
	}

	public void setStatuses_list(ArrayList<weibo_statuses> statuses_list) {
		Statuses_list = statuses_list;
	}

	public MyAdapter getMyAdapter() {
		return myAdapter;
	}

	public void setMyAdapter(MyAdapter myAdapter) {
		this.myAdapter = myAdapter;
	}

}
