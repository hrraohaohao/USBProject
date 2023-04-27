package com.hao.usbproject.print;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.hao.usbproject.print.MPOS;

import java.io.IOException;
import java.io.InputStream;

public class Prints {

    public static void PrintTicket(Context ctx, MPOS pos) {
        pos.POS_Reset();
        pos.POS_FeedLine();
        pos.POS_TextOut("電子發票證明聯\r\n", 3, 24, 1, 1, 0, 0);
        pos.POS_TextOut("\r\n", 3, 24, 0, 0, 0, 0);
        pos.POS_TextOut("105年05-06月\r\n", 3, 48, 1, 1, 0, 0);
        pos.POS_TextOut("\r\n", 3, 24, 0, 0, 0, 0);
        pos.POS_TextOut("TW-56321497\r\n", 3, 60, 1, 1, 0, 0);
        pos.POS_TextOut("2016-05-26 09:43:29\r\n", 3, 0, 0, 0, 0, 0);
        pos.POS_TextOut("隨機碼：2887   總計：418\r\n", 3, 0, 0, 0, 0, 0);

        pos.POS_TextOut("賣方：12345678                   \r\n", 3, 0, 0, 0, 0, 0);
        pos.POS_FeedLine();
        pos.POS_S_SetBarcode("46661366725", 0, 69, 2, 50, 0, 0);
        pos.POS_FeedLine();
        pos.POS_S_SetQRcode("46661366725", 5, 5, 4);
        pos.POS_FeedLine();

        pos.POS_DoubleQRCode("乾電池:1:105", 20, 4, 5, "口罩:1:210:牛奶:1:25", 230, 4, 5, 4);
        pos.POS_FeedLine();
        pos.POS_FeedLine();
        pos.POS_FeedLine();
        pos.POS_FeedLine();
        pos.POS_FeedLine();
        pos.POS_FeedLine();
        pos.POS_TextOut("电子发票证明联\r\n", 0, 96, 1, 1, 0, 0);
        pos.POS_FeedLine();
        pos.POS_TextOut("小票：270500027719 收银员：010121212122121\r\n", 0, 0, 0, 0, 0, 0);
        pos.POS_TextOut("------------------------------------------\r\n", 0, 0, 0, 0, 0, 0);
        pos.POS_TextOut("   商品编码        单价  数量       小计\r\n", 0, 0, 0, 0, 0, 0);
        pos.POS_TextOut("01.9940228004700    3.98   1.181  20080616\r\n", 0, 0, 0, 0, 0, 0);
        pos.POS_TextOut("   番石榴     小计：4.70   小计： 4.70小计\r\n", 0, 0, 0, 0, 0, 0);
        pos.POS_TextOut("02.996100800220     6.00   0.376  20080617\r\n", 0, 0, 0, 0, 0, 0);
        pos.POS_TextOut("   白面条     小计：2.20          4.70小计\r\n", 0, 0, 0, 0, 0, 0);
        pos.POS_TextOut("03.6921644701204    3.50   1(包)  20080617\r\n", 0, 0, 0, 0, 0, 0);
        pos.POS_TextOut("   恒源德调味 小计：3.50          3.50小计\r\n", 0, 0, 0, 0, 0, 0);
        pos.POS_TextOut("04.9940316000602    5.16   0.116  20080617\r\n", 0, 0, 0, 0, 0, 0);
        pos.POS_TextOut("   生葱       小计：0.60          0.60小计   \r\n", 0, 0, 0, 0, 0, 0);
        pos.POS_TextOut("------------------------------------------\r\n", 0, 0, 0, 0, 0, 0);
        pos.POS_TextOut("购货总额：                         11.00   \r\n", 0, 0, 0, 0, 0, 0);
        pos.POS_TextOut("付款：   现金       人民币         101.00\r\n", 0, 0, 0, 0, 0, 0);
        pos.POS_TextOut("找零：   现金       人民币         90.00  \r\n", 0, 0, 0, 0, 0, 0);
        pos.POS_TextOut("            售出商品数量：4件         \r\n", 0, 0, 0, 0, 0, 0);
        pos.POS_TextOut("           2005-09-13  16:50:19\r\n", 0, 0, 0, 0, 0, 0);
        pos.POS_TextOut("            欢迎光临   多谢惠顾\r\n", 0, 0, 0, 0, 0, 0);
        pos.POS_TextOut("             （开发票当月有效）\r\n", 0, 0, 0, 0, 0, 0);
        pos.POS_TextOut("              满家福百货南邮店\r\n", 0, 0, 0, 0, 0, 0);
        pos.POS_TextOut("小票：270500027721           收银员：01012\r\n", 0, 0, 0, 0, 0, 0);
        pos.POS_FeedLine();
        pos.POS_FeedLine();
        pos.POS_FeedLine();
        pos.POS_FeedLine();
        pos.POS_FeedLine();
    }


    /**
     * 从Assets中读取图片
     */
    public static Bitmap getImageFromAssetsFile(Context ctx, String fileName) {
        Bitmap image = null;
        AssetManager am = ctx.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        // load the origial Bitmap
        Bitmap BitmapOrg = bitmap;

        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        // calculate the scale
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);

        // make a Drawable from Bitmap to allow to set the Bitmap
        // to the ImageView, ImageButton or what ever
        return resizedBitmap;
    }

    public static Bitmap getTestImage1(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, 4, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, width, 4, paint);

//		paint.setColor(Color.BLACK);
//		for(int i = 0; i < 8; ++i)
//		{
//			for(int x = i; x < width; x += 8)
//			{
//				for(int y = i; y < height; y += 8)
//				{
//					canvas.drawPoint(x, y, paint);
//				}
//			}
//		}
        return bitmap;
    }

    public static Bitmap getTestImage2(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, width, height, paint);

        paint.setColor(Color.BLACK);
        for (int y = 0; y < height; y += 4) {
            for (int x = y % 32; x < width; x += 32) {
                canvas.drawRect(x, y, x + 4, y + 4, paint);
            }
        }
        return bitmap;
    }
}
