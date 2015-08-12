package com.hseya.medtech.jobcard.unfinished.activity;

import java.util.List;

import com.hseya.medtech.R;
import com.hseya.medtech.jobcard.unfinished.bean.UnfinishedDataBean;


import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewsRowAdapter extends ArrayAdapter<UnfinishedDataBean> {

	private Activity activity;
	private List<UnfinishedDataBean> items;
	private UnfinishedDataBean objBean;
	private int row;
	

	public NewsRowAdapter(Activity act, int resource, List<UnfinishedDataBean> arrayList) {
		super(act, resource, arrayList);
		this.activity = act;
		this.row = resource;
		this.items = arrayList;

	

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(row, null);

			holder = new ViewHolder();
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if ((items == null) || ((position + 1) > items.size()))
			return view;

		objBean = items.get(position);

		holder.tvTitle = (TextView) view.findViewById(R.id.listcardID);
		holder.tvDesc = (TextView) view.findViewById(R.id.listCusName);
		holder.tvDate = (TextView) view.findViewById(R.id.listcardDate);
	

		if (holder.tvTitle != null && null != objBean.getID()
				&& objBean.getID().trim().length() > 0) {
			holder.tvTitle.setText(Html.fromHtml(objBean.getID()));
		}
		if (holder.tvDesc != null && null != objBean.getCustomerName()
				&& objBean.getCustomerName().trim().length() > 0) {
			holder.tvDesc.setText(Html.fromHtml(objBean.getCustomerName()));
		}
		if (holder.tvDate != null && null != objBean.getDate()
				&& objBean.getDate().trim().length() > 0) {
			holder.tvDate.setText(Html.fromHtml(objBean.getDate()));
		}
		

		return view;
	}

	public class ViewHolder {

		public TextView tvTitle, tvDesc, tvDate;
		

	}

}