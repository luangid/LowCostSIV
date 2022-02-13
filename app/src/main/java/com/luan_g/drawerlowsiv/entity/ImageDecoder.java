package com.luan_g.drawerlowsiv.entity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.media.Image;
import android.util.Base64;

public class ImageDecoder {
    public static Bitmap decodeString(String base64){
        byte[] decodedArray = Base64.decode(base64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedArray, 0, decodedArray.length);
        return bitmap;
    }
}
