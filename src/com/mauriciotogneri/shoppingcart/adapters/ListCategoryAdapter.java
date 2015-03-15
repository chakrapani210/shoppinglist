package com.mauriciotogneri.shoppingcart.adapters;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.activeandroid.Model;
import com.mauriciotogneri.shoppingcart.R;
import com.mauriciotogneri.shoppingcart.model.Category;

public class ListCategoryAdapter extends ArrayAdapter<Category>
{
	private final LayoutInflater inflater;
	
	public ListCategoryAdapter(Context context)
	{
		super(context, android.R.layout.simple_list_item_1, new ArrayList<Category>());
		
		this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	@SuppressLint("InflateParams")
	public View getView(int position, View originalView, ViewGroup parent)
	{
		View convertView = originalView;
		Category category = getItem(position);
		
		if (convertView == null)
		{
			convertView = this.inflater.inflate(R.layout.list_category_row, null);
		}
		
		TextView name = (TextView)convertView.findViewById(R.id.name);
		name.setText(category.getName());
		
		return convertView;
	}
	
	public void refresh()
	{
		clear();
		
		List<Category> categories = Model.all(Category.class);
		addAll(categories);
		
		sort(new Comparator<Category>()
		{
			@Override
			public int compare(Category lhs, Category rhs)
			{
				return lhs.getName().compareTo(rhs.getName());
			}
		});
		
		notifyDataSetChanged();
	}
}