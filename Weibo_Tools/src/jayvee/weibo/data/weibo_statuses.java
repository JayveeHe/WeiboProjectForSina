package jayvee.weibo.data;

import java.util.ArrayList;
import java.util.jar.Pack200.Packer;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class weibo_statuses{
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
	public Retweeted_Status retweeted_status= new Retweeted_Status();

//	public class Retweeted_Status{
//		public long retweeted_ID;
//		public String retweeted_text;
//		public String retweeted_username;
//		public String retweeted_created_at;
//		public String retweeted_profile_image_url;
//		public Bitmap retweeted_thumbnail_pic;
//		public String retweeted_thumbnail_pic_url;
//		public String retweeted_bmiddle_pic_url;
//	}
	
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

		public void setretweeted_Thumbnail_pics(ArrayList<String> retweeted_thumbnail_pics) {
			this.retweeted_thumbnail_pics = retweeted_thumbnail_pics;
		}
	}

}