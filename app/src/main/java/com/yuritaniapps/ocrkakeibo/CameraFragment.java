package com.yuritaniapps.ocrkakeibo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.googlecode.tesseract.android.TessBaseAPI;

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
            bitmap = data.getParcelableExtra("data");
            _imageViewCamera.setImageBitmap(bitmap);
        }
        if(bitmap != null){
            _imageRecognition.readImageFromBitmap(_parentActivity, bitmap);
        }

    }

    public class OnClickCameraIconListener implements View.OnClickListener {
        public void onClick(View view){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 200);
        }
    }
}