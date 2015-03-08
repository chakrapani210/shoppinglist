package com.mauriciotogneri.shoppingcart.adapters;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.mauriciotogneri.shoppingcart.R;
import com.mauriciotogneri.shoppingcart.model.Category;

public class CategoryAdapter extends ArrayAdapter<Category>
{
	private final LayoutInflater inflater;
	
	public CategoryAdapter(Context context, List<Category> list)
	{
		super(context, R.layout.spinner_category, list);
		
		this.inflater = LayoutInflater.from(context);
		
		setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}
	
	@Override
	@SuppressLint("InflateParams")
	public View getView(int position, View originalView, ViewGroup parent)
	{
		View convertView = originalView;
		Category category = getItem(position);
		
		if (convertView == null)
		{
			convertView = this.inflater.inflate(R.layout.spinner_category, null);
		}
		
		TextView name = (TextView)convertView.findViewById(R.id.name);
		name.setText(category.getName());
		
		return convertView;
	}
	
	@Override
	@SuppressLint("InflateParams")
	public View getDropDownView(int position, View originalView, ViewGroup parent)
	{
		View convertView = originalView;
		Category category = getItem(position);
		
		if (convertView == null)
		{
			convertView = this.inflater.inflate(R.layout.spinner_category, null);
		}
		
		TextView name = (TextView)convertView.findViewById(R.id.name);
		name.setText(category.getName());
		
		return convertView;
	}
}