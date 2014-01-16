package jayvee.weibo.status_detail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.CommentsAPI;
import com.sina.weibo.sdk.openapi.legacy.FavoritesAPI;
import com.sina.weibo.sdk.openapi.legacy.WeiboAPI;

import jayvee.weibo.data.ShareData;
import jayvee.weibo.data.TimelineData;
import jayvee.weibo.timeline.TimelineListActivity;
import jayvee.weibo.timeline.UserTimelineListActivity;
import jayvee.weibo_tools.MainActivity;
import jayvee.weibo_tools.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class StatusDetailActivity extends Activity {
	protected StatusDetailActivity statusDetailActivity;
	TimelineData tlData = new TimelineData();
	Bitmap test;
	DetailViewHolder holder = new DetailViewHolder();
	// 相关参数
	long thisid;
	long since_id = 0;// 起始微博的ID
	long max_id = 0;// 最大微博ID
	int count = 30;// 每页的记录条数
	int page = 1;// 读取的页数

	ArrayList<CommentslistData> commentslistDatas = new ArrayList<CommentslistData>();
	MyAdapter myAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		statusDetailActivity = this;
		setContentView(R.layout.weibodetail);
		ShareData receiveStatus = (ShareData) getApplication();
		final long thisid = receiveStatus.ID;
		// Intent intent = getIntent();
		// transObject receiveStatuses = (transObject) intent
		// .getParcelableExtra("clicked");
		setDetailView(receiveStatus);
		Toast.makeText(StatusDetailActivity.this, "正在读取评论", Toast.LENGTH_SHORT)
				.show();
		MyListView myListView = (MyListView) findViewById(R.id.mylistview);
		myListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				CommentslistData comment_stat = (CommentslistData) arg0
						.getItemAtPosition(arg2);
				// comment_stat.
				Intent it = new Intent(StatusDetailActivity.this,
						ReplyActivity.class);
				Bundle bundle = new Bundle();
				bundle.putLong("repost_weibo_id", comment_stat.ID);
				bundle.putLong("repost_comment_id", comment_stat.CID);
				bundle.putString("toreply_text", comment_stat.text);
				it.putExtras(bundle);
				startActivity(it);
			}
		});

		// 转发按钮
		Button btn_torepost = (Button) findViewById(R.id.btn_torepost);
		btn_torepost.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(StatusDetailActivity.this,
						RepostActivity.class);
				Bundle bundle = new Bundle();
				// final long commentID =
				bundle.putLong("repost_weibo_id", thisid);
				it.putExtras(bundle);
				startActivity(it);
			}
		});

		// 评论按钮
		Button btn_tocomment = (Button) findViewById(R.id.btn_tocomment);
		btn_tocomment.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(StatusDetailActivity.this,
						CommentActivity.class);
				Bundle bundle = new Bundle();
				// final long commentID =
				bundle.putLong("comment_weibo_id", thisid);
				it.putExtras(bundle);
				startActivity(it);
			}
		});

		// // 收藏按钮
		// Log.d("微博详情", receiveStatus.favorited.toString());
		// final Button btn_favorite = (Button) findViewById(R.id.btn_favorite);
		// if (receiveStatus.favorited) {
		// btn_favorite.setClickable(false);
		// btn_favorite.setText("已收藏");
		// Log.d("微博详情", "已经收藏");
		// } else {
		// btn_favorite.setClickable(true);
		// btn_favorite.setText("收藏");
		// Log.d("微博详情", "未收藏");
		// }
		// final Long weiboID = receiveStatus.ID;
		// btn_favorite.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		//
		// FavoritesAPI favoritesAPI = new FavoritesAPI(MainActivity
		// .getToken());
		// favoritesAPI.create(weiboID, new RequestListener() {
		//
		// public void onIOException(IOException e) {
		// // TODO Auto-generated method stub
		// new Runnable() {
		// public void run() {
		// Toast.makeText(statusDetailActivity, "收藏IO异常！",
		// Toast.LENGTH_SHORT).show();
		//
		// }
		// };
		// }
		//
		// public void onError(WeiboException e) {
		// // TODO Auto-generated method stub
		// new Runnable() {
		// public void run() {
		// Toast.makeText(statusDetailActivity, "收藏失败！",
		// Toast.LENGTH_SHORT).show();
		//
		// }
		// };
		// }
		//
		// public void onComplete4binary(
		// ByteArrayOutputStream responseOS) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// public void onComplete(String response) {
		// // TODO Auto-generated method stub
		// new Runnable() {
		// public void run() {
		// Toast.makeText(statusDetailActivity, "收藏成功！",
		// Toast.LENGTH_SHORT).show();
		// btn_favorite.setClickable(false);
		// btn_favorite.setText("已收藏");
		// Log.d("微博详情", "已经收藏");
		// }
		// };
		//
		// }
		// });
		// }
		// });
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 111) {

				if (test.getWidth() > holder.re_layout.getWidth()) {
					test = Bitmap
							.createScaledBitmap(test,
									holder.re_layout.getWidth(),
									test.getHeight(), true);
				}
				holder.retweeted_image.setImageBitmap(test);
			}
			if (msg.what == 222) {
				if (test.getWidth() > holder.main_Layout.getWidth()) {
					test = Bitmap.createScaledBitmap(test,
							holder.main_Layout.getWidth(), test.getHeight(),
							true);
				}
				holder.weibo_image.setImageBitmap(test);
			}
			if (msg.what == 333) {
				TextView title_text = (TextView) findViewById(R.id.titlepanel_text);
				title_text.setText("评论读取中……");
				if (0 != commentslistDatas.size()) {
					System.out.println("--------------------"
							+ commentslistDatas.size());
					title_text.setText("以下为评论");
					MyListView myListView = (MyListView) findViewById(R.id.mylistview);
					// ListView comlist = (ListView)
					// findViewById(R.id.comment_list);
					setMyAdapter(new MyAdapter());
					// comlist.setAdapter(getMyAdapter());
					myListView.setAdapter(myAdapter);
					// setListViewHeightBasedOnChildren(comlist);
					setListViewHeightBasedOnChildren(myListView);
					Toast.makeText(StatusDetailActivity.this,
							"共读取了" + commentslistDatas.size() + "条评论",
							Toast.LENGTH_SHORT).show();
				} else {
					title_text.setText("当前没有评论！");
					Toast.makeText(StatusDetailActivity.this, "当前没有评论！",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	};

	private void setDetailView(ShareData receiveStatus) {
		/** 得到各个控件的对象 */
		holder.username = (TextView) findViewById(R.id.username);
		holder.weibo_text = (TextView) findViewById(R.id.weibo_text);
		holder.usericon = (ImageView) findViewById(R.id.usericon);
		holder.created_at = (TextView) findViewById(R.id.created_at);
		holder.weibo_image = (ImageView) findViewById(R.id.weibo_image);
		// 转发的微博控件
		holder.retweeted_username = (TextView) findViewById(R.id.re_username);
		holder.retweeted_text = (TextView) findViewById(R.id.re_text);
		holder.retweeted_image = (ImageView) findViewById(R.id.re_image);
		holder.re_layout = (LinearLayout) findViewById(R.id.re_layout);
		holder.main_Layout = (LinearLayout) findViewById(R.id.main_layout);
		// 评论的控件
		// holder.listView = (ListView) findViewById(R.id.comment_list);
		holder.myListView = (MyListView) findViewById(R.id.mylistview);
		final long commentID = receiveStatus.ID;
		new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				CommentsAPI commentsAPI = new CommentsAPI(
						MainActivity.getToken());// 获取评论api
				commentsAPI.show(commentID, since_id, max_id, count, page,
						WeiboAPI.AUTHOR_FILTER.ALL, new RequestListener() {

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
								processComments(response);
							}
						});
			}
		}).start();
		// 相关细节的设置
		holder.username.setText(receiveStatus.username.toString());
		holder.weibo_text.setText(receiveStatus.text.toString());
		holder.usericon.setImageBitmap(receiveStatus.usericon);
		holder.created_at.setText("发布于：" + receiveStatus.created_at);
		if (null != receiveStatus.thumbnail_pic) {
			holder.weibo_image.setImageBitmap(receiveStatus.thumbnail_pic);
			holder.weibo_image.setVisibility(ImageView.VISIBLE);
			holder.weibo_image.setClickable(true);// 如果有图片则进行点击放大处理
			final String Url = receiveStatus.bmiddle_pic_url;
			// Bitmap temp = null;
			// new Thread(new Runnable() {
			// //进行异步读取图片
			// public void run() {
			// // TODO Auto-generated method stub
			// Bitmap temp = tlData.getNetBitmap(Url);
			// }
			// }).start();
			// if (null!=temp)
			holder.weibo_image.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (null == test) {

						new Thread(new Runnable() {

							public void run() {
								// TODO Auto-generated method stub
								test = tlData.getNetBitmap(Url);
								// holder.retweeted_image
								// .setImageBitmap(test);
								handler.sendEmptyMessage(222);
							}
						}).start();
						Toast.makeText(StatusDetailActivity.this, "正在读取图片……",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(StatusDetailActivity.this, "图片已经读取成功！",
								Toast.LENGTH_SHORT).show();
					}
					// holder.weibo_image.setImageBitmap(tlData.getNetBitmap(Url));
				}
			});
		} else
			holder.weibo_image.setVisibility(ImageView.GONE);// 没有图就滚粗
		// /////判断是否有转发微博
		if (0 != receiveStatus.retweeted_status.retweeted_ID) {
			holder.re_layout.setVisibility(LinearLayout.VISIBLE);
			holder.re_layout.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(StatusDetailActivity.this, "你点到了转发微博！",
							Toast.LENGTH_SHORT).show();

				}
			});

			holder.retweeted_username
					.setText(receiveStatus.retweeted_status.retweeted_username
							.toString());
			holder.retweeted_text
					.setText(receiveStatus.retweeted_status.retweeted_text
							.toString());
			// 设置转发微博的内容
			if (null != receiveStatus.re_pic_urls) {
				holder.retweeted_image
						.setImageBitmap(receiveStatus.retweeted_status.retweeted_thumbnail_pic);
				holder.retweeted_image.setVisibility(ImageView.VISIBLE);
				holder.retweeted_image.setClickable(true);// 如果有图片则进行点击放大处理
				final String Url = receiveStatus.retweeted_status.retweeted_bmiddle_pic_url;
				holder.retweeted_image
						.setOnClickListener(new OnClickListener() {

							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								if (null == test) {
									new Thread(new Runnable() {

										public void run() {
											// TODO Auto-generated method stub
											test = tlData.getNetBitmap(Url);
											// holder.retweeted_image
											// .setImageBitmap(test);
											handler.sendEmptyMessage(111);
										}
									}).start();
									Toast.makeText(StatusDetailActivity.this,
											"正在读取图片……", Toast.LENGTH_SHORT)
											.show();
								} else {
									Toast.makeText(StatusDetailActivity.this,
											"图片已经读取成功！", Toast.LENGTH_SHORT)
											.show();

								}
							}
						});
			} else
				holder.retweeted_image.setVisibility(ImageView.GONE);// 没有图就滚粗
		} else
			holder.re_layout.setVisibility(LinearLayout.GONE);// 没有转发微博就滚粗
	}

	public final class DetailViewHolder {
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
		public LinearLayout main_Layout;
		// 评论列表
		public ListView listView;
		public MyListView myListView;
	}

	// 处理评论列表信息
	public void processComments(String response) {
		Log.i("微博评论", "得到响应");

		JSONTokener jsonTokener = new JSONTokener(response);
		try {
			JSONObject commentslist = (JSONObject) jsonTokener.nextValue();
			JSONArray commentArray = commentslist.getJSONArray("comments");
			for (int i = 0; i < commentArray.length(); i++) {
				JSONObject comment = commentArray.getJSONObject(i);
				CommentslistData comment_to_add = new CommentslistData();
				comment_to_add.created_at = comment.getString("created_at");
				comment_to_add.CID = Long.parseLong(comment.getString("id"));
				comment_to_add.text = comment.getString("text");
				JSONObject user = comment.getJSONObject("user");
				comment_to_add.userID = Long.parseLong(user.getString("id"));
				comment_to_add.username = user.getString("screen_name");
				comment_to_add.usericon = tlData.getNetBitmap(user
						.getString("profile_image_url"));
				JSONObject status = comment.getJSONObject("status");// 获取所评论的微博
				comment_to_add.ID = status.getLong("id");
				commentslistDatas.add(comment_to_add);
				Log.i("微博评论", "读取第" + i + "个评论成功！");
			}
			Log.i("微博评论", "读取成功！");
			// 读取完毕
			handler.sendEmptyMessage(333);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e("微博评论", "读取list错误！");
			e.printStackTrace();
		}

	}

	public MyAdapter getMyAdapter() {
		return myAdapter;
	}

	public void setMyAdapter(MyAdapter myAdapter) {
		this.myAdapter = myAdapter;
	}

	public final class CommentViewHolder {
		public TextView comment_text;
		public TextView username;
		public ImageView usericon;
		public TextView created_at;

	}

	// 微博评论的界面设置
	private class MyAdapter extends BaseAdapter {

		public int getCount() {
			// TODO Auto-generated method stub
			// System.out.println("当前列表容量=" + commentslistDatas.size());
			return commentslistDatas.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return commentslistDatas.get(position);// 返回某处的微博信息类
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			CommentViewHolder holder;

			if (null == convertView) {
				convertView = View.inflate(StatusDetailActivity.this,
						R.layout.commentlist, null);
				holder = new CommentViewHolder();
				/** 得到各个控件的对象 */
				holder.username = (TextView) convertView
						.findViewById(R.id.username);
				holder.comment_text = (TextView) convertView
						.findViewById(R.id.weibo_text);
				holder.usericon = (ImageView) convertView
						.findViewById(R.id.usericon);
				holder.created_at = (TextView) convertView
						.findViewById(R.id.created_at);
				convertView.setTag(holder);// 绑定ViewHolder对象

			} else
				holder = (CommentViewHolder) convertView.getTag();

			holder.username.setText(commentslistDatas.get(position).username
					.toString());
			holder.comment_text.setText(commentslistDatas.get(position).text
					.toString());
			holder.usericon
					.setImageBitmap(commentslistDatas.get(position).usericon);
			// final int curPosition = position;
			// holder.usericon.setOnClickListener(new OnClickListener() {
			// //点击头像进入指定用户的微博
			// //由于新浪更新了api，非审核应用无法读取其他用户的微博，所以暂时停用该段代码
			// public void onClick(View arg0) {
			// // TODO Auto-generated method stub
			// Intent it = new Intent(StatusDetailActivity.this,
			// UserTimelineListActivity.class);
			// Bundle bundle = new Bundle();
			// System.out.println(curPosition+"+++++++"+commentslistDatas.get(curPosition).username);
			// bundle.putString("UserName",
			// commentslistDatas.get(curPosition).username);
			// it.putExtras(bundle);
			// startActivity(it);
			// }
			// });
			holder.created_at.setText("发布于："
					+ commentslistDatas.get(position).created_at);

			return convertView;
		}
	}

	// ///////根据listview子项的个数计算其相应的高度
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
			Log.i("微博评论调试",
					"第" + i + "个item高度为：" + listItem.getMeasuredHeight());
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// statusDetailActivity.finish();
		// Log.i("微博评论", "成功关闭");
	}
}
