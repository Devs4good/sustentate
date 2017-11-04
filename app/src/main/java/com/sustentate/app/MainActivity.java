package com.sustentate.app;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
    private PhotoResult result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CameraView cameraView = findViewById(R.id.camera_view);
        ImageView cameraTrigger = findViewById(R.id.camera_trigger);
        final ImageView cameraPreview = findViewById(R.id.camera_preview);
        ProgressBar cameraLoading = findViewById(R.id.camera_loading);

        if (!KeySaver.isExist(this, Constants.CAMERA_SD_PERMISSION)) requestCameraAndSDPermission();

        photoApp = Fotoapparat.with(this)
                .into(cameraView)
                .previewScaleType(ScaleType.CENTER_CROP)
                .photoSize(biggestSize())
                .lensPosition(back())
                .focusMode(autoFocus())
                .flash(autoFlash())
                .build();

        cameraTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = photoApp.takePicture();
                result.saveToFile(saveResult());
                result.toBitmap(scaled(0.25f))
                        .whenAvailable(new PendingResult.Callback<BitmapPhoto>() {
                            @Override
                            public void onResult(BitmapPhoto result) {
                                cameraPreview.setVisibility(View.VISIBLE);
                                cameraPreview.setImageBitmap(result.bitmap);
                                cameraPreview.setRotation(-result.rotationDegrees);
                            }
                        });
            }
        });
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
    protected void onStart() {
        super.onStart();
        if (KeySaver.getPermission(this, Constants.CAMERA_SD_PERMISSION)) photoApp.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (photoApp != null) photoApp.stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        if (requestCode == PERMISSION_CAMERA_SD) {
            photoApp.start();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    private File saveResult() {
        return new File(getExternalFilesDir("Sustentate"), "sus_" + System.currentTimeMillis() + ".jpg");
    }

}
