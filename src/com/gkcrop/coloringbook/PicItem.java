package com.gkcrop.coloringbook;

import java.io.IOException;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.example.adapter.PicItemadapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class PicItem extends SherlockActivity {
	
	private GridView photoGrid;
	private int mPhotoSize, mPhotoSpacing;
	private PicItemadapter imageAdapter;
	private String[] arrImagesStrings;
	String Foldername,CategoryName;
	private AdView mAdView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picselct);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#c5d951")));
		mAdView = (AdView) findViewById(R.id.adViewad);
	    mAdView.loadAd(new AdRequest.Builder().build());
		mPhotoSize = getResources().getDimensionPixelSize(R.dimen.photo_size);
		mPhotoSpacing = getResources().getDimensionPixelSize(R.dimen.photo_spacing);
		photoGrid = (GridView) findViewById(R.id.albumGrid);
		
		Intent i=getIntent();
		Foldername=i.getStringExtra("Folder");
		CategoryName=i.getStringExtra("Category");
		arrImagesStrings = listAssetFiles(Foldername);  
		getSupportActionBar().setTitle(CategoryName);
		
		imageAdapter=new PicItemadapter(getApplicationContext(), R.layout.picphoto_item,arrImagesStrings,Foldername);
		photoGrid.setAdapter(imageAdapter);
		
		// get the view tree observer of the grid and set the height and numcols dynamically
				photoGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (imageAdapter.getNumColumns() == 0) {
							final int numColumns = (int) Math.floor(photoGrid.getWidth() / (mPhotoSize + mPhotoSpacing));
							if (numColumns > 0) {
								final int columnWidth = (photoGrid.getWidth() / numColumns) - mPhotoSpacing;
								imageAdapter.setNumColumns(numColumns);
								imageAdapter.setItemHeight(columnWidth);

							}
						}
					}
				});
				
				photoGrid.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(PicItem.this, FloodFillActivity.class);
		                intent.putExtra(FloodFillActivity.IMG, Foldername+"/"+arrImagesStrings[position]);
		                intent.setFlags(0x40000000);
		                startActivity(intent);
					}
				});
		
		
		
	}
	
	 private String [] listAssetFiles(String path)  
	  {  
	   String [] list;  
	   try 
	   {  
	     list = PicItem.this.getAssets().list(path);  
	      if (list.length > 0)  
	      {  
	        System.out.println(list.length);  
	       return list;  
	       }  
	   }catch (IOException e)  
	   {  
	   }  
	  return null;  
	 }
	 
	 
	 @Override
		public boolean onOptionsItemSelected(MenuItem menuItem)
		{       
			switch (menuItem.getItemId()) 
	        {
	        case android.R.id.home: 
	            onBackPressed();
	            break;

	        default:
	            return super.onOptionsItemSelected(menuItem);
	        }
	        return true;
		}
}
