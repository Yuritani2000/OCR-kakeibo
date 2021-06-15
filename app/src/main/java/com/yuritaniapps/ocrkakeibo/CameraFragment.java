package com.yuritaniapps.ocrkakeibo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class CameraFragment extends Fragment {

    private Activity _parentActivity;
    private ImageView _imageViewCamera;

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

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 200 && resultCode == _parentActivity.RESULT_OK ){
            Bitmap bitmap = data.getParcelableExtra("data");
            _imageViewCamera.setImageBitmap(bitmap);
        }
    }

    public class OnClickCameraIconListener implements View.OnClickListener {
        public void onClick(View view){

        }
    }
}