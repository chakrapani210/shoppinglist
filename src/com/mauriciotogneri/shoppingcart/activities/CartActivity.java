package com.mauriciotogneri.shoppingcart.activities;

import java.io.ByteArrayOutputStream;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.mauriciotogneri.shoppingcart.R;
import com.mauriciotogneri.shoppingcart.adapters.ListCartItemAdapter;
import com.mauriciotogneri.shoppingcart.adapters.ListCartItemAdapter.CartItemSeparator;
import com.mauriciotogneri.shoppingcart.model.CartItem;
import com.mauriciotogneri.shoppingcart.model.Category;
import com.mauriciotogneri.shoppingcart.model.Product;
import com.mauriciotogneri.shoppingcart.widgets.CustomDialog;
import com.mauriciotogneri.shoppingcart.widgets.ProductImage;

public class CartActivity extends BaseActivity
{
	private ListCartItemAdapter listCartItemAdapter;
	
	@Override
	protected void init()
	{
		setContentView(R.layout.activity_cart);
		
		this.listCartItemAdapter = new ListCartItemAdapter(this);
		
		ListView listView = getListView(R.id.cart_list);
		listView.setAdapter(this.listCartItemAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				CartItem cartItem = (CartItem)parent.getItemAtPosition(position);
				
				if (!(cartItem instanceof CartItemSeparator))
				{
					selectCartItem(cartItem);
				}
			}
		});
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
			{
				CartItem cartItem = (CartItem)parent.getItemAtPosition(position);
				
				if (!cartItem.isSelected() && (!(cartItem instanceof CartItemSeparator)))
				{
					displayCartItem(cartItem);
				}
				
				return true;
			}
		});
		
		setButtonAction(R.id.share_cart, new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				shareCart();
			}
		});
		
		setButtonAction(R.id.add_product, new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				addProduct();
			}
		});
		
		// initDatabase();
	}
	
	private void initDatabase()
	{
		Category drinks = new Category("Drinks", Category.COLOR_1);
		drinks.save();
		
		Category food = new Category("Food", Category.COLOR_2);
		food.save();
		
		Category kitchen = new Category("Kitchen", Category.COLOR_3);
		kitchen.save();
		
		// --------------------------------
		
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.product_generic);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] bitmapdata = stream.toByteArray();
		
		Product cocaCola = new Product("Coca-Cola", drinks, bitmapdata);
		cocaCola.save();
		
		Product milk = new Product("Milk", drinks, bitmapdata);
		milk.save();
		
		Product bananas = new Product("Bananas", food, bitmapdata);
		bananas.save();
		
		Product cereals = new Product("Cereals", food, bitmapdata);
		cereals.save();
		
		Product bag = new Product("Bag", kitchen, bitmapdata);
		bag.save();
		
		// --------------------------------
		
		CartItem bananas1 = new CartItem(bananas, 12, false);
		bananas1.save();
		
		CartItem bag1 = new CartItem(bag, 34, false);
		bag1.save();
	}
	
	private void shareCart()
	{
		if (!this.listCartItemAdapter.isEmpty())
		{
			share(R.string.label_share_cart, this.listCartItemAdapter.getShareContent());
		}
		else
		{
			showToast(R.string.error_cart_empty);
		}
	}
	
	private void selectCartItem(CartItem cartItem)
	{
		cartItem.invertSelection();
		cartItem.save();
		
		updateList(true);
	}
	
	private void updateList(boolean sort)
	{
		this.listCartItemAdapter.refresh(sort);
		
		ListView listView = getListView(R.id.cart_list);
		TextView emptyLabel = getCustomTextView(R.id.empty_label);
		
		if (this.listCartItemAdapter.getCount() > 0)
		{
			listView.setVisibility(View.VISIBLE);
			emptyLabel.setVisibility(View.GONE);
		}
		else
		{
			listView.setVisibility(View.GONE);
			emptyLabel.setVisibility(View.VISIBLE);
		}
	}
	
	@SuppressLint("InflateParams")
	private void displayCartItem(final CartItem cartItem)
	{
		CustomDialog dialog = new CustomDialog(this, cartItem.getName());
		dialog.setLayout(R.layout.dialog_cart_item);
		
		ProductImage productImage = dialog.getProductImage(R.id.thumbnail);
		productImage.setImage(cartItem.getImage());
		
		final NumberPicker quantity = dialog.getNumberPicker(R.id.quantity);
		quantity.setMinValue(1);
		quantity.setMaxValue(100);
		quantity.setValue(cartItem.getQuantity());
		
		dialog.setPositiveButton(R.string.button_accept, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				updateQuantity(cartItem, quantity.getValue());
			}
		});
		
		dialog.setNegativeButton(R.string.button_cancel, null);
		
		dialog.setNeutralButton(R.string.button_remove, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				removeCartItem(cartItem);
			}
		});
		
		dialog.display();
	}
	
	private void updateQuantity(CartItem cartItem, int quantity)
	{
		cartItem.setQuantity(quantity);
		cartItem.save();
		
		updateList(false);
	}
	
	private void removeCartItem(CartItem cartItem)
	{
		cartItem.delete();
		
		this.listCartItemAdapter.remove(cartItem);
		updateList(false);
	}
	
	private void addProduct()
	{
		startActivity(AddProductActivity.class);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		updateList(true);
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		this.listCartItemAdapter.removeSelectedItems();
	}
}