package com.yuritaniapps.ocrkakeibo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.file.FileSystemNotFoundException;

public class ImageRecognition {
    private String TESS_DATA_DIR_NAME = "tessdata";
    private String TRAINED_DATA_NAMES [] = { "eng.traineddata", "jpn.traineddata" };

    public String getTextFromBitmap(Activity parentActivity, Bitmap targetImage) {
        // Tess-twoのオブジェクトを取得。
        TessBaseAPI tessBaseAPI = new TessBaseAPI();
        // オブジェクトを初期化
        // 呼び出し元のActivityのコンテキストがほしい時は、メソッドの引数で受け取るようにしてやればいい？
        String dataPath = parentActivity.getFilesDir().toString();
        Log.d("debug", "The trained data is in: " + dataPath);

        tessBaseAPI.init(parentActivity.getFilesDir().toString(), "jpn");
        tessBaseAPI.setImage(targetImage);
        String gainedText = tessBaseAPI.getUTF8Text();
        Log.d("debug", "the gained text is: " + gainedText);
        return gainedText;
    }

    public void copyTrainedData(Activity parentActivity) {
        try{
            for(String s: TRAINED_DATA_NAMES){
                File trainedDataDir = new File(parentActivity.getFilesDir() + File.separator + TESS_DATA_DIR_NAME + File.separator);
                if(!trainedDataDir.exists()){
                    trainedDataDir.mkdir();
                }

                String filePath = parentActivity.getFilesDir() + File.separator + TESS_DATA_DIR_NAME  + File.separator + s;
                InputStream inputStream = parentActivity.getAssets().open(TESS_DATA_DIR_NAME + File.separator + s);
                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                byte [] buffer = new byte[1024];
                int read = inputStream.read(buffer);
                while(read != -1){
                    fileOutputStream.write(buffer, 0, read);
                    read = inputStream.read(buffer);
                }
                fileOutputStream.flush();
                File file = new File(filePath);
                if(!file.exists()) throw new FileNotFoundException();
            }
        }catch(FileNotFoundException e){
            Log.d("debug", "i/o file not found");
        }catch(IOException e){
            Log.d("debug", "something went wrong with i/o");
        }
    }
}
