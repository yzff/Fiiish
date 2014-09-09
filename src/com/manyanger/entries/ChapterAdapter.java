package com.manyanger.entries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.manyounger.fiiish.R;

import java.util.List;

public class ChapterAdapter extends BaseAdapter {

	private final List<ChapterItem> chapterList;
	private final LayoutInflater mInflater;
	private boolean showPayed;
	private final Context context;
	
	private int chapterOffset = 1;

	public ChapterAdapter(Context _context, List<ChapterItem> _chapterList) {
		chapterList = _chapterList;
		context = _context;
		mInflater = LayoutInflater.from(_context);
		showPayed = true;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return chapterList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return chapterList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mViewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.chapter_item, null);
			mViewHolder = new ViewHolder();
			mViewHolder.tv_chapter = (TextView) convertView
					.findViewById(R.id.tv_chapter);
			mViewHolder.iv_payed = (ImageView) convertView.findViewById(R.id.iv_payed);
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

//		mViewHolder.tv_chapter.setText(chapterList.get(position)
//				.getTitle());
		mViewHolder.tv_chapter.setText(context.getString(R.string.chapter_name, position+chapterOffset));
		
		if(isShowPayed())
		{
			if(chapterList.get(position).isPayed()){
				mViewHolder.iv_payed.setImageResource(R.drawable.payed);
			} else {
				mViewHolder.iv_payed.setImageResource(R.drawable.pay_tip);
			}
		} else {
			mViewHolder.iv_payed.setVisibility(View.GONE);
		}
		return convertView;
	}

	public boolean isShowPayed() {
		return showPayed;
	}

	public void setShowPayed(boolean showPayed) {
		this.showPayed = showPayed;
	}

	public int getChapterOffset() {
		return chapterOffset;
	}

	public void setChapterOffset(int chapterOffset) {
		this.chapterOffset = chapterOffset;
	}

	static class ViewHolder {
		private TextView tv_chapter;
		private ImageView iv_payed;
	}

}
