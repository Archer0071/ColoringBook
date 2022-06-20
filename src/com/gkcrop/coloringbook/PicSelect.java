package com.gkcrop.coloringbook;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.adapter.Itemadapter;
import com.example.model.Model;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class PicSelect extends SherlockActivity {

	private GridView photoGrid;
	private int mPhotoSize, mPhotoSpacing;
	private Itemadapter imageAdapter;
	private AdView mAdView;
	private InterstitialAd mInterstitial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picselct);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#c5d951")));
		mAdView = (AdView) findViewById(R.id.adViewad);
	    mAdView.loadAd(new AdRequest.Builder().build());
	    
	    mInterstitial = new InterstitialAd(getApplicationContext());
		 mInterstitial.setAdUnitId(getResources().getString(R.string.admob_intertestial_id));
		 mInterstitial.loadAd(new AdRequest.Builder().build());
		
		 mInterstitial.setAdListener(new AdListener() {
	    	  @Override
	    	public void onAdLoaded() {
	    		// TODO Auto-generated method stub
	    		super.onAdLoaded();
	    		if (mInterstitial.isLoaded()) {
		            mInterstitial.show();
			  }
	    	}
		});
	    
	    
		mPhotoSize = getResources().getDimensionPixelSize(R.dimen.photo_size);
		mPhotoSpacing = getResources().getDimensionPixelSize(R.dimen.photo_spacing);
		photoGrid = (GridView) findViewById(R.id.albumGrid);

		Model.LoadModel();
		String[] ids = new String[Model.Items.size()];
		for (int i= 0; i < ids.length; i++){
			ids[i] = Integer.toString(i+1);
		}
		
		imageAdapter=new Itemadapter(getApplicationContext(), R.layout.photo_item,ids,"CATIMAGE");
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
						Log.e("FolderName", Model.GetbyId(position+1).FolderName);
						
						String FolderName=Model.GetbyId(position+1).FolderName;
						String CategoryName=Model.GetbyId(position+1).Name;
						Intent i=new Intent(PicSelect.this,PicItem.class);
						i.putExtra("Folder", FolderName);
						i.putExtra("Category", CategoryName);
						startActivity(i);
						
					}
				});
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.home, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{       

		switch (menuItem.getItemId()) 
		{
			
		case R.id.rateapp:
			
			final String appName = getPackageName();//your application package name i.e play store application url
			try {
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse("market://details?id="
								+ appName)));
			} catch (android.content.ActivityNotFoundException anfe) {
				startActivity(new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("http://play.google.com/store/apps/details?id="
								+ appName)));
			}
			return true;

		case R.id.moreapp:
			
			startActivity(new Intent(
					Intent.ACTION_VIEW,
					Uri.parse(getString(R.string.play_more_apps))));

			return true;
			
		default:
			return super.onOptionsItemSelected(menuItem);
		}

	}

}
