package org.joinmastodon.android.ui.text;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.view.View;

import org.joinmastodon.android.ui.utils.UiUtils;

public class LinkSpan extends CharacterStyle {

	private int color=0xFF00FF00;
	private OnLinkClickListener listener;
	private String link;
	private Type type;
	private String accountID;
	private String text;

	public LinkSpan(String link, OnLinkClickListener listener, Type type, String accountID){
		this(link, listener, type, accountID, null);
	}

	public LinkSpan(String link, OnLinkClickListener listener, Type type, String accountID, String text){
		this.listener=listener;
		this.link=link;
		this.type=type;
		this.accountID=accountID;
		this.text=text;
	}

	public int getColor(){
		return color;
	}

	@Override
	public void updateDrawState(TextPaint tp) {
		tp.setColor(color=tp.linkColor);
		tp.setUnderlineText(true);
	}

	public void onClick(Context context){
		switch(getType()){
			case URL -> UiUtils.openURL(context, accountID, link);
			case MENTION -> UiUtils.openProfileByID(context, accountID, link);
			case HASHTAG -> UiUtils.openHashtagTimeline(context, accountID, link, null);
			case CUSTOM -> listener.onLinkClick(this);
		}
	}

	public void onLongClick(View view) {
		UiUtils.copyText(view, getType() == Type.URL ? link : text);
	}

	public String getLink(){
		return link;
	}

	public String getText() {
		return text;
	}

	public Type getType(){
		return type;
	}

	public void setListener(OnLinkClickListener listener){
		this.listener=listener;
	}

	public interface OnLinkClickListener{
		void onLinkClick(LinkSpan span);
	}

	public enum Type{
		URL,
		MENTION,
		HASHTAG,
		CUSTOM
	}
}
