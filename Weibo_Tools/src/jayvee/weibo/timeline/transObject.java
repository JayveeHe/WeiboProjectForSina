package jayvee.weibo.timeline;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import jayvee.weibo.data.weibo_statuses;

public class transObject implements Parcelable {

	private long ID;
	private String text;
	private String username;
	private String created_at;
	private String profile_image_url;
	private Bitmap usericon;
	private Bitmap thumbnail_pic;
	private String thumbnail_pic_url;
	private String bmiddle_pic_url;
	private Bitmap bmiddle_pic;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private weibo_statuses transStatuses;

	public weibo_statuses getTransStatuses() {
		return transStatuses;
	}

	public void setTransStatuses(weibo_statuses transStatuses) {
		this.transStatuses = transStatuses;
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		arg0.writeLong(getID());
		arg0.writeString(getText());
		arg0.writeString(getUsername());
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	public Bitmap getUsericon() {
		return usericon;
	}

	public void setUsericon(Bitmap usericon) {
		this.usericon = usericon;
	}

	public Bitmap getThumbnail_pic() {
		return thumbnail_pic;
	}

	public void setThumbnail_pic(Bitmap thumbnail_pic) {
		this.thumbnail_pic = thumbnail_pic;
	}

	public String getThumbnail_pic_url() {
		return thumbnail_pic_url;
	}

	public void setThumbnail_pic_url(String thumbnail_pic_url) {
		this.thumbnail_pic_url = thumbnail_pic_url;
	}

	public String getBmiddle_pic_url() {
		return bmiddle_pic_url;
	}

	public void setBmiddle_pic_url(String bmiddle_pic_url) {
		this.bmiddle_pic_url = bmiddle_pic_url;
	}

	public Bitmap getBmiddle_pic() {
		return bmiddle_pic;
	}

	public void setBmiddle_pic(Bitmap bmiddle_pic) {
		this.bmiddle_pic = bmiddle_pic;
	}

	public static final Parcelable.Creator<transObject> CREATOR = new Creator<transObject>() {

		public transObject createFromParcel(Parcel arg0) {
			// TODO Auto-generated method stub
			transObject tObject = new transObject();
			tObject.ID = arg0.readLong();
			tObject.text = arg0.readString();
			tObject.username = arg0.readString();
			return tObject;
		}

		public transObject[] newArray(int arg0) {
			// TODO Auto-generated method stub
			return new transObject[arg0];
		}

	};
}
