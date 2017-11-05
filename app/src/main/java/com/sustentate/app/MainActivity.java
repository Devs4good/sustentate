package com.sustentate.app;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ImageClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier;
import com.sustentate.app.utils.Constants;
import com.sustentate.app.utils.KeySaver;

import java.io.File;
import java.io.FileOutputStream;
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
    private ViewGroup cameraRoot;
    private ImageView cameraRetake;
    private ImageView cameraPreview;
    private ImageView cameraTrigger;
    private ProgressBar cameraLoading;

    private String fileName;

    private File finalFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        cameraRoot = findViewById(R.id.camera_root);

        CameraView cameraView = findViewById(R.id.camera_view);
        cameraTrigger = findViewById(R.id.camera_trigger);
        cameraPreview = findViewById(R.id.camera_preview);
        cameraLoading = findViewById(R.id.camera_loading);
        cameraRetake = findViewById(R.id.camera_retake);
        ImageView cameraUpload = findViewById(R.id.camera_upload);

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
        cameraUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = BitmapFactory.decodeFile(getFile().getPath());
                System.out.println("HOLAS " + bitmap);
                Bitmap.createScaledBitmap(bitmap, 500, 500, false);

                File file = null;
                if (bitmap != null) {
                    file = new File(fileName);
                    try {
                        FileOutputStream outputStream = null;
                        try {
                            outputStream = new FileOutputStream(file);

                            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (outputStream != null) {
                                    outputStream.flush();
                                    outputStream.close();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                finalFile = file;

                new WatsonTask().execute();
            }
        });
    }

    private class WatsonTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return isRecyclable();
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            Snackbar.make(cameraRoot, "ES: " + bool, Snackbar.LENGTH_SHORT).show();
        }
    }

    private boolean isRecyclable() {
        VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
        service.setApiKey(Constants.WATSON_API);

        ClassifyImagesOptions options = new ClassifyImagesOptions
                .Builder()
                .images(finalFile)
                .threshold(0.0001)
                .classifierIds("sustentable")
                .build();

        VisualClassification r2 = service.classify(options).execute();
        List<ImageClassification> classifications = r2.getImages();
        if (classifications.size() > 0) {
            List<VisualClassifier> classifiers = classifications.get(0).getClassifiers();
            if (classifiers.size() > 0) {
                List<VisualClassifier.VisualClass> classes = classifiers.get(0).getClasses();
                for (VisualClassifier.VisualClass items : classes) {
                    if (items != null && items.getName().equals("rec")) {
                        return items.getScore() > 0.7;
                    }
                }
            }
        }
        return false;
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

    private File getFile() {
        return new File(fileName);
    }

    private View.OnClickListener cameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            fileName = (Environment.getExternalStorageDirectory() + "/Sustentate/")
                    + "sus_"
                    + System.currentTimeMillis()
                    + ".jpg";
            cameraLoading.setVisibility(View.VISIBLE);
            PhotoResult result = photoApp.takePicture();
            result.saveToFile(getFile());
            result.toBitmap(scaled(0.3f))
                    .whenAvailable(new PendingResult.Callback<BitmapPhoto>() {
                        @Override
                        public void onResult(BitmapPhoto result) {
                            showResult(result);
                        }
                    });
        }
    };

    private boolean checkFolder() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/Sustentate");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        return success;
    }

    private View.OnClickListener retakeListener = new View.OnClickListener() {
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
            if (list.size() > 1) photoApp.start();
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
            if (!KeySaver.getPermission(this, Constants.CAMERA_SD_PERMISSION))
                requestCameraAndSDPermission();
        }
    }
}
