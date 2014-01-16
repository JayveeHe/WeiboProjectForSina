package jayvee.weibo.status_detail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.CommentsAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.legacy.WeiboAPI;

import jayvee.weibo_tools.MainActivity;
import jayvee.weibo_tools.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class RepostActivity extends Activity {
	// 相关参数
	Long ID;
	Long CID;
	private Boolean isRepost;
	private Boolean isCommentOri = false;// 是否评论原微博
	RepostActivity repostActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		repostActivity = this;
		setContentView(R.layout.repost_activity);
		Bundle bundle = getIntent().getExtras();
		ID = bundle.getLong("repost_weibo_id");
		// Boolean isCommentOri = false;// 是否评论原微博
		System.out.println("repost weiboID=" + ID);
		CheckBox comment_ori = (CheckBox) findViewById(R.id.repost_checkpost);
		comment_ori.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				setIsCommentOri(isChecked);
				System.out.println(getIsCommentOri());
			}
		});
		Button btn_comment = (Button) findViewById(R.id.btn_repost);
		btn_comment.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				EditText editText = (EditText) findViewById(R.id.addrepost_edittext);
				String commentText = editText.getText().toString();
				// if (!isRepost)// 非转发，即是评论的时候
				// {
				// CommentsAPI commentsAPI = new CommentsAPI(MainActivity
				// .getToken());
				// commentsAPI.reply(CID, ID, commentText, false,
				// getIsCommentOri(), myListener);
				// } else {
				StatusesAPI statusesAPI = new StatusesAPI(MainActivity
						.getToken());
				if (isCommentOri)
					statusesAPI.repost(ID, commentText,
							WeiboAPI.COMMENTS_TYPE.BOTH, myListener);
				else
					statusesAPI.repost(ID, commentText,
							WeiboAPI.COMMENTS_TYPE.NONE, myListener);

			}
			// }
		});

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
			runOnUiThread(new Runnable() {

				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(RepostActivity.this, "转发成功！",
							Toast.LENGTH_SHORT).show();

				}
			});
			repostActivity.finish();
		}

	};

	public Boolean getIsCommentOri() {
		return isCommentOri;
	}

	public void setIsCommentOri(Boolean isCommentOri) {
		this.isCommentOri = isCommentOri;
	}

	public Boolean getIsRepost() {
		return isRepost;
	}

	public void setIsRepost(Boolean isRepost) {
		this.isRepost = isRepost;
	}
}
