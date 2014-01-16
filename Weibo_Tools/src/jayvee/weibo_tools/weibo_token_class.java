package jayvee.weibo_tools;

import java.io.Serializable;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class weibo_token_class implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2526074077942728906L;
	com.sina.weibo.sdk.auth.Oauth2AccessToken token;

	public weibo_token_class(Oauth2AccessToken token) {
		// TODO Auto-generated constructor stub
		this.token = token;
	}
	public Oauth2AccessToken gettoken(){
		return token;
	}

}
