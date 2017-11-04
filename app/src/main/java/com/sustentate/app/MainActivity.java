package com.sustentate.app;

import android.Manifest;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.sustentate.app.utils.Constants;
import com.sustentate.app.utils.KeySaver;

import java.io.File;
import java.util.List;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.photo.BitmapPhoto;
import io.fotoapparat.result.PendingResult;
import io.fotoapparat.result.PhotoResult;
import io.fotoapparat.view.CameraView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static io.fotoapparat.parameter.selector.FlashSelectors.autoFlash;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.autoFocus;
import static io.fotoapparat.parameter.selector.LensPositionSelectors.back;
import static io.fotoapparat.parameter.selector.SizeSelectors.biggestSize;
import static io.fotoapparat.result.transformer.SizeTransformers.scaled;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int PERMISSION_CAMERA_SD = 3030;
    private Fotoapparat photoApp;
    private ImageView cameraRetake;
    private ImageView cameraPreview;
    private ImageView cameraTrigger;
    private ProgressBar cameraLoading;

    private ViewGroup cameraRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraRoot = findViewById(R.id.camera_root);

        CameraView cameraView = findViewById(R.id.camera_view);
        cameraTrigger = findViewById(R.id.camera_trigger);
        cameraPreview = findViewById(R.id.camera_preview);
        cameraLoading = findViewById(R.id.camera_loading);
        cameraRetake = findViewById(R.id.camera_retake);

        requestPermissionMarshmallow();

        photoApp = Fotoapparat.with(this)
                .into(cameraView)
                .previewScaleType(ScaleType.CENTER_CROP)
                .photoSize(biggestSize())
                .lensPosition(back())
                .focusMode(autoFocus())
                .flash(autoFlash())
                .build();

        cameraTrigger.setOnClickListener(cameraListener);
        cameraRetake.setOnClickListener(retakeListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (KeySaver.getPermission(this, Constants.CAMERA_SD_PERMISSION)) photoApp.start();
        } else {
            photoApp.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (photoApp != null) {
            try {
                photoApp.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private File saveResult() {
        return new File(getExternalFilesDir("Sustentate"), "sus_" + System.currentTimeMillis() + ".jpg");
    }

    View.OnClickListener cameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            cameraLoading.setVisibility(View.VISIBLE);
            PhotoResult result = photoApp.takePicture();
            result.saveToFile(saveResult());
            result.toBitmap(scaled(0.3f))
                    .whenAvailable(new PendingResult.Callback<BitmapPhoto>() {
                        @Override
                        public void onResult(BitmapPhoto result) {
                            showResult(result);
                        }
                    });
        }
    };

    View.OnClickListener retakeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            retakePicture();
        }
    };

    private void showResult(BitmapPhoto result) {
        cameraLoading.setVisibility(View.GONE);
        cameraPreview.setVisibility(View.VISIBLE);
        cameraPreview.setImageBitmap(result.bitmap);
        cameraPreview.setRotation(-result.rotationDegrees);
    }

    private void retakePicture() {
        cameraPreview.setVisibility(View.GONE);
        cameraPreview.setImageBitmap(null);
    }

    @AfterPermissionGranted(PERMISSION_CAMERA_SD)
    private void requestCameraAndSDPermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, getString(R.string.camera_and_sd_permission), PERMISSION_CAMERA_SD, perms);
        } else {
            KeySaver.savePermission(this, Constants.CAMERA_SD_PERMISSION, true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        if (requestCode == PERMISSION_CAMERA_SD) {
            if (list.size() > 1 ) photoApp.start();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == PERMISSION_CAMERA_SD) {
            cameraRetake.setEnabled(false);
            cameraTrigger.setEnabled(false);
            Snackbar.make(cameraRoot, "Necesitamos que apruebes los permisos", Snackbar.LENGTH_SHORT).setAction("ACEPTAR", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestCameraAndSDPermission();
                }
            }).show();
        }
    }

    private void requestPermissionMarshmallow() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (!KeySaver.getPermission(this, Constants.CAMERA_SD_PERMISSION)) requestCameraAndSDPermission();
        }
    }
}
