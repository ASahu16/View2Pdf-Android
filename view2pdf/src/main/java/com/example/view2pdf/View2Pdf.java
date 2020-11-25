package com.example.view2pdf;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class View2Pdf {

    private static final String TAG = "View2Pdf";
    public Context context;

    public View2Pdf(Context context) {
        this.context = context;
    }

    public static void d(String message) {
        Log.d(TAG, message);
    }

    public void create_pdf(NestedScrollView nestedScrollView) {
        Bitmap bitmap = getBitmapFromView(nestedScrollView, nestedScrollView.getChildAt(0).getHeight(), nestedScrollView.getChildAt(0).getWidth());
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        Log.i("life", "" + file);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();


            Document document = new Document();
            document.setPageSize(PageSize.A4);
            String directoryPath = android.os.Environment.getExternalStorageDirectory().toString();
            String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
            String pdfpath = directoryPath + "/" + currentDateTimeString + ".pdf";
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfpath)); //  Change pdf's name.
            writer.setPageSize(PageSize.A4);
            document.open();

            Image image;  // Change image's name and extension.


            //To store all the small image chunks in bitmap format in this list
            Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            //xCoord and yCoord are the pixel positions of the image chunks
            int yCoord = 0;
            int rows = (int) nestedScrollView.getChildAt(0).getHeight() / size.y;
            double last_chunk_height = nestedScrollView.getChildAt(0).getHeight() % size.y;

            for (int i = 0; i < rows; i++) {

                String chunkfname = currentDateTimeString + "_" + i + fname; // full image name with extension
                File chunkfile = new File(myDir, chunkfname);  // files gets loaded in app. myDir is the path for file. chunkfname is file name
                Log.i("life", "" + chunkfile);
                if (chunkfile.exists())
                    chunkfile.delete();
                try {

                    FileOutputStream chunkout = new FileOutputStream(chunkfile);
                    (Bitmap.createBitmap(bitmap, 0, yCoord, size.x, size.y)).compress(Bitmap.CompressFormat.JPEG, 90, chunkout);
                    chunkout.flush();
                    chunkout.close();

                    image = Image.getInstance(root + "/" + chunkfname);
                    image.scaleAbsolute(PageSize.A4);
                    image.setAbsolutePosition(0, 0);
                    document.newPage();
                    document.add(image);
                    File fdelete = new File(root + "/" + chunkfname);
                    if (fdelete.exists())
                        if (fdelete.delete()) {
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                yCoord += size.y;
            }
//            yCoord -= size.y+last_chunk_height;
            String lastChunkFname = currentDateTimeString + "__1" + fname;
            File lastchunkfile = new File(myDir, lastChunkFname);
            Log.i("life", "" + lastchunkfile);
            FileOutputStream lastchunkout = new FileOutputStream(lastchunkfile);
            (Bitmap.createBitmap(bitmap, 0, yCoord, size.x, (int) last_chunk_height)).compress(Bitmap.CompressFormat.JPEG, 90, lastchunkout);
            lastchunkout.flush();
            lastchunkout.close();

            image = Image.getInstance(root + "/" + lastChunkFname);
            image.scaleAbsolute(PageSize.A4);
            image.setAbsolutePosition(0, 0);
            document.newPage();
            document.add(image);
            File fdelete = new File(root + "/" + lastChunkFname);
            if (fdelete.exists())
                if (fdelete.delete()) {
                }
            File finaldelete = new File(root + "/" + fname);
            if (finaldelete.exists())
                if (finaldelete.delete()) {
                }

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

}