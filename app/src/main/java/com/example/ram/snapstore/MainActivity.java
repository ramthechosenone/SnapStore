package com.example.ram.snapstore;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.DropBoxManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends Activity implements View.OnClickListener {

    // public static final String TAG = MainActivity.class.getSimpleName();
    final static private String APP_KEY = "6wuzgbhwp7ysegs";
    final static private String APP_SECRET = "64e0viye5qaqvg9";

    public static final int MEDIA_TYPE_IMAGE = 1;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;
    private Uri fileUri;

    public static String fileName;
    public static File mediaFile;
    private DropboxAPI<AndroidAuthSession> mDBApi;
    private LinearLayout container;

    @InjectView(R.id.fileUploadButton)
    Button mFileUpload;
    @InjectView(R.id.Camera_button)
    Button mCameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        container = (LinearLayout) findViewById(R.id.containers);
        mCameraButton.setOnClickListener(this);

        //Authenticating the user
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        mDBApi.getSession().startOAuth2Authentication(MainActivity.this);

        //onClickListener for fileLister
        mFileUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilesLister filesLister = new FilesLister(mDBApi, handler);

                //calling an Async Task to list all files in the Dropbox
                filesLister.execute();
            }
        });
    }

    protected void onResume() {
        super.onResume();

        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                mDBApi.getSession().finishAuthentication();

                String accessToken = mDBApi.getSession().getOAuth2AccessToken();
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }


    }

    //handler for rendering the TextViews in which URLs of the photos are displayed
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ArrayList<String> result = msg.getData().getStringArrayList("data");
            for (String fileName : result) {
                Log.i("ListFiles", fileName);
                TextView tv = new TextView(MainActivity.this);
                tv.setText(fileName);
                Linkify.addLinks(tv, Linkify.WEB_URLS);
                container.addView(tv);
            }
        }
    };

    //onClickListener for the cameraButton

    @Override
    public void onClick(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    //for storing the image uri

    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        fileName = "IMG_" + timeStamp + ".jpg";

        File temp;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");

        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    protected void onActivityResult(int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE, int resultCode, Intent data) {
        // Auto-generated method stub
        super.onActivityResult(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE, resultCode, data);
        FileUpload fileUpload = new FileUpload(mDBApi, mediaFile, fileName);
        fileUpload.execute();
        Toast.makeText(this, "File has been uploaded !", Toast.LENGTH_LONG).show();




    }

}
