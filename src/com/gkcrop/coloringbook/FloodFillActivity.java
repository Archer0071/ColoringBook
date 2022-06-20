package com.gkcrop.coloringbook;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class FloodFillActivity extends SherlockActivity{

	public static final String IMG = null;
	Context con;
	public Bitmap currentbmp;
	private String imgfile;
	public int replacecolor;
	public ImageView showcolor;
	private AdView mAdView;
	//	ImageButton btnsave,btnshare,btnreset;


	public FloodFillActivity()
	{
		replacecolor = 0xffff0000;
		imgfile = null;
		currentbmp = null;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.floodfill);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#c5d951")));
		con = this;
		mAdView = (AdView) findViewById(R.id.adViewad);
		mAdView.loadAd(new AdRequest.Builder().build());
		imgfile = getIntent().getStringExtra(IMG);
		//		btnsave=(ImageButton)findViewById(R.id.ibtnSave);
		//		btnshare=(ImageButton)findViewById(R.id.ibtnShare);
		//		btnreset=(ImageButton)findViewById(R.id.ibtnReset);

		ImageView imageview = (ImageView)findViewById(R.id.floodfill);
		ImageView imageview1 = (ImageView)findViewById(R.id.colorpal);
		showcolor = (ImageView)findViewById(R.id.showcolor);
		showcolor.setBackgroundColor(replacecolor);

		try
		{
			imageview.setImageDrawable(Drawable.createFromStream(getAssets().open(imgfile), null));
			imageview1.setImageDrawable(Drawable.createFromStream(getAssets().open("CATIMAGE/color.png"), null));
			imageview1.setOnTouchListener(new android.view.View.OnTouchListener() {

				Bitmap pmap;
				public boolean onTouch(View view, MotionEvent motionevent)
				{
					try
					{
						Point point = new Point();
						point.x = (int)motionevent.getX();
						point.y = (int)motionevent.getY();
						ImageView imageview2 = (ImageView)findViewById(R.id.colorpal);
						imageview2.buildDrawingCache();
						pmap = imageview2.getDrawingCache();
						replacecolor = pmap.getPixel(point.x, point.y);
						showcolor.setBackgroundColor(replacecolor);
					}
					catch (Exception exception) { }
					return true;
				}



			});


		} catch (Exception exception) { }


		imageview.setOnTouchListener(new OnTouchListener() {

			Bitmap bmap;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				if(event.getAction()==MotionEvent.ACTION_DOWN)
				{
					Point point = new Point();
					point.x = (int)event.getX();
					point.y = (int)event.getY();
					ImageView imageview2 = (ImageView)findViewById(R.id.floodfill);
					if (bmap == null)
					{
						imageview2.buildDrawingCache();
						bmap = imageview2.getDrawingCache();
					}
					int i = bmap.getPixel(point.x, point.y);
					int j = replacecolor;
					(new TheTask(bmap, point, i, j, imageview2)).execute(new Void[0]);

				}
				return true;
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{       

		ImageView imageview2 = (ImageView)findViewById(R.id.floodfill);
		final String s ="Image_"+String.valueOf(System.currentTimeMillis());
		switch (menuItem.getItemId()) 
		{
		case android.R.id.home: 
			onBackPressed();
			return true;
		case R.id.save:
			if (currentbmp == null)
			{
				imageview2.buildDrawingCache();
				currentbmp = imageview2.getDrawingCache();
			}
			Media.insertImage(getContentResolver(), currentbmp, s, null);
			Toast.makeText(getApplicationContext(), "Save to Gallery", Toast.LENGTH_SHORT).show();
			return true;

		case R.id.share:

			if (currentbmp == null)
			{
				imageview2.buildDrawingCache();
				currentbmp = imageview2.getDrawingCache();
			}

			String share =Media.insertImage(getContentResolver(), currentbmp, s, null);
			Intent intent = new Intent("android.intent.action.SEND");
			intent.putExtra("android.intent.extra.SUBJECT", "Check out the holiday card I made");
			intent.putExtra("android.intent.extra.STREAM", Uri.parse(share));
			intent.setType("image/jpeg");
			startActivity(Intent.createChooser(intent, "Choose an app to share with"));

			return true;

		case R.id.repeat:
			try
			{
				Drawable drawable = Drawable.createFromStream(getAssets().open(imgfile), null);
				imageview2.setImageDrawable(drawable);
				imageview2.buildDrawingCache();
			}
			catch (IOException ioexception1)
			{
				ioexception1.printStackTrace();
			}
			return true;

		default:
			return super.onOptionsItemSelected(menuItem);
		}

	}
}
