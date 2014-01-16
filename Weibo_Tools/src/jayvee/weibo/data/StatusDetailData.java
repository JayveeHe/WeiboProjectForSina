package jayvee.weibo.data;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class StatusDetailData {
	public long ID;
	public String text;
	public String username;
	public String created_at;
	public String profile_image_url;
	public Bitmap usericon;
	public Bitmap thumbnail_pic;
	public String thumbnail_pic_url;
	public String bmiddle_pic_url;
	public Bitmap bmiddle_pic;
	public Pic_Urls pic_urls = new Pic_Urls();
}
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
