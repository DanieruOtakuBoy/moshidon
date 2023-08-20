package org.joinmastodon.android.ui.displayitems;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.joinmastodon.android.GlobalUserPreferences;
import org.joinmastodon.android.R;
import org.joinmastodon.android.fragments.BaseStatusListFragment;
import org.joinmastodon.android.model.Poll;
import org.joinmastodon.android.model.Status;
import org.joinmastodon.android.ui.utils.UiUtils;

public class PollFooterStatusDisplayItem extends StatusDisplayItem{
	public final Poll poll;
	public final Status status;

	public PollFooterStatusDisplayItem(String parentID, BaseStatusListFragment parentFragment, Poll poll, Status status){
		super(parentID, parentFragment);
		this.poll=poll;
		this.status=status;
	}

	@Override
	public Type getType(){
		return Type.POLL_FOOTER;
	}

	public static class Holder extends StatusDisplayItem.Holder<PollFooterStatusDisplayItem>{
		private TextView text;
		private Button button;

		public Holder(Activity activity, ViewGroup parent){
			super(activity, R.layout.display_item_poll_footer, parent);
			text=findViewById(R.id.text);
			button=findViewById(R.id.vote_btn);
			button.setOnClickListener(v->item.parentFragment.onPollVoteButtonClick(this));
		}

		@Override
		public void onBind(PollFooterStatusDisplayItem item){
			String text=item.parentFragment.getResources().getQuantityString(R.plurals.x_votes, item.poll.votesCount, item.poll.votesCount);
			String sep=item.parentFragment.getString(R.string.sk_separator);
			if(item.poll.expiresAt!=null && !item.poll.isExpired()){
				text+=" "+sep+" "+UiUtils.formatTimeLeft(itemView.getContext(), item.poll.expiresAt);
				if(item.poll.multiple)
					text+=" "+sep+" "+item.parentFragment.getString(R.string.poll_multiple_choice);
			}else if(item.poll.isExpired()){
				text+=" "+sep+" "+item.parentFragment.getString(R.string.poll_closed);
			}
			this.text.setText(text);
			button.setVisibility(item.poll.isExpired() || item.poll.voted || (!item.poll.multiple && !GlobalUserPreferences.voteButtonForSingleChoice) ? View.GONE : View.VISIBLE);
			button.setEnabled(item.poll.selectedOptions!=null && !item.poll.selectedOptions.isEmpty());
		}
	}
}
