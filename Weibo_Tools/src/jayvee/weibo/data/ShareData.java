package jayvee.weibo.data;

import java.util.ArrayList;

import android.app.Application;
import android.graphics.Bitmap;

public class ShareData extends Application {
	public long ID;
	public String text;
	public String username;
	public String created_at;
	public String profile_image_url;
	public Boolean favorited; 
	public Bitmap usericon;
	public Bitmap thumbnail_pic;
	public String thumbnail_pic_url;
	public String bmiddle_pic_url;
	public Bitmap bmiddle_pic;
	public Pic_Urls pic_urls = new Pic_Urls();
	public Re_Pic_Urls re_pic_urls = new Re_Pic_Urls();
	public Retweeted_Status retweeted_status = new Retweeted_Status();

	

	class Pic_Urls {
		private ArrayList<String> thumbnail_pics = new ArrayList<String>();

		// String

		public ArrayList<String> getThumbnail_pics() {
			return thumbnail_pics;
		}

		public void setThumbnail_pics(ArrayList<String> thumbnail_pics) {
			this.thumbnail_pics = thumbnail_pics;
		}
	}

	class Re_Pic_Urls {
		private ArrayList<String> retweeted_thumbnail_pics = new ArrayList<String>();

		// String

		public ArrayList<String> getretweeted_Thumbnail_pics() {
			return retweeted_thumbnail_pics;
		}

		public void setretweeted_Thumbnail_pics(
				ArrayList<String> retweeted_thumbnail_pics) {
			this.retweeted_thumbnail_pics = retweeted_thumbnail_pics;
		}
	}

	public void setShareData(weibo_statuses status) {
		this.ID = status.ID;
		this.text = status.text;
		this.username = status.username;
		this.usericon = status.usericon;
		this.created_at = status.created_at;
		this.favorited = status.favorited;
		this.retweeted_status = status.retweeted_status;
		this.bmiddle_pic_url = status.bmiddle_pic_url;
		this.thumbnail_pic = status.thumbnail_pic;
	}


}
