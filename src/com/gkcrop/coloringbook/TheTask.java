
package com.gkcrop.coloringbook;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.util.LinkedList;

class TheTask extends AsyncTask
{

    final Bitmap bmp;
    ImageView imageV;
    Point pt;
    int replacementColor;
    int targetColor;

    public TheTask(Bitmap bitmap, Point point, int i, int j, ImageView imageview)
    {
        bmp = bitmap;
        pt = point;
        replacementColor = j;
        targetColor = i;
        imageV = imageview;
    }

    private void floodFill(Bitmap bitmap, Point point, int i, int j)
    {
        int k = bitmap.getWidth();
        int l = bitmap.getHeight();
        if (i != j)
        {
            LinkedList linkedlist = new LinkedList();
            do
            {
                int i1 = point.x;
                int j1;
                for (j1 = point.y; i1 > 0 && !isBlack(bitmap.getPixel(i1 - 1, j1), j); i1--) { }
                boolean flag = false;
                boolean flag1 = false;
                while (i1 < k && !isBlack(bitmap.getPixel(i1, j1), j)) 
                {
                    bitmap.setPixel(i1, j1, j);
                    if (!flag && j1 > 0 && !isBlack(bitmap.getPixel(i1, j1 - 1), j))
                    {
                        linkedlist.add(new Point(i1, j1 - 1));
                        flag = true;
                    } else
                    if (flag && j1 > 0 && isBlack(bitmap.getPixel(i1, j1 - 1), j))
                    {
                        flag = false;
                    }
                    if (!flag1 && j1 < l - 1 && !isBlack(bitmap.getPixel(i1, j1 + 1), j))
                    {
                        linkedlist.add(new Point(i1, j1 + 1));
                        flag1 = true;
                    } else
                    if (flag1 && j1 < l - 1 && isBlack(bitmap.getPixel(i1, j1 + 1), j))
                    {
                        flag1 = false;
                    }
                    i1++;
                }
                point = (Point)linkedlist.poll();
            } while (point != null);
        }
    }

    private boolean isBlack(int i, int j)
    {
        while (Color.red(i) == Color.green(i) && Color.green(i) == Color.blue(i) && Color.red(i) < 150 || i == j) 
        {
            return true;
        }
        return false;
    }

    

    protected Void doInBackground(Void avoid[])
    {
        floodFill(bmp, pt, targetColor, replacementColor);
        return null;
    }

    protected void onPostExecute(Object obj)
    {
        onPostExecute((Void)obj);
    }

    protected void onPostExecute(Void void1)
    {
        imageV.setImageBitmap(bmp);
    }

	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		return doInBackground((Void[])params);
	}
}
