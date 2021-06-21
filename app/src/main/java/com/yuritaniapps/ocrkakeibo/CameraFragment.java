package com.yuritaniapps.ocrkakeibo;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraFragment extends Fragment {

    private Activity _parentActivity;
    private ImageView _imageViewCamera;
    private ImageRecognition _imageRecognition;
    private Uri _imageUri;
    private TessBaseAPI tessBaseAPI;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        _parentActivity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        _imageViewCamera = view.findViewById(R.id.ImageViewCamera);
        _imageViewCamera.setOnClickListener(new OnClickCameraIconListener());

        _imageRecognition = new ImageRecognition();
        _imageRecognition.copyTrainedData(_parentActivity);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Bitmap bitmap = null;
        if(requestCode == 200 && resultCode == _parentActivity.RESULT_OK ){
            _imageViewCamera.setImageURI(_imageUri);

            bitmap = data.getParcelableExtra("data");
            _imageViewCamera.setImageBitmap(bitmap);
        }
        if(bitmap != null){
            _imageRecognition.readImageFromBitmap(_parentActivity, bitmap);
        }

    }

    public class OnClickCameraIconListener implements View.OnClickListener {
        public void onClick(View view){
            if(ActivityCompat.checkSelfPermission(_parentActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(_parentActivity, permissions, 2000);
                return;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date now = new Date(System.currentTimeMillis());

            String nowStr = dateFormat.format(now);

            String fileName = "UseCameraActivityPhoto_" + nowStr +".jpg";

            ContentValues values = new ContentValues();

            values.put(MediaStore.Images.Media.TITLE, fileName);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

            ContentResolver resolver = _parentActivity.getContentResolver();

            _imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, _imageUri);

            startActivityForResult(intent, 200);
        }
    }
}