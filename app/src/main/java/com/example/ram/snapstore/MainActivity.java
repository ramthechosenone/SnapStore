package com.example.ram.snapstore;

import android.app.Activity;
import android.nfc.Tag;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends Activity implements View.OnClickListener{

    public static final String TAG = MainActivity.class.getSimpleName();
    final static private String APP_KEY = "6wuzgbhwp7ysegs";
    final static private String APP_SECRET = "64e0viye5qaqvg9";
    final String path = Environment.getExternalStorageDirectory().toString() + "/";
   // final String ImagePath = Environment.getExternalStorageDirectory().toString() + "/" + IMAGE_DIRECTORY + "/";
    final String imagePath = "/storage/emulated/0/DCIM/Camera/";

    // In the class declaration section:
    private DropboxAPI<AndroidAuthSession> mDBApi;

    @InjectView(R.id.fileUploadButton) Button mFileUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

       // mfileupload.setOnClickListener(this);
        mFileUpload.setOnClickListener(this);

        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);



//        if (mDBApi != null) {
//            System.out.println("inside if");
//            Toast.makeText(this, "Sup man, its working", Toast.LENGTH_LONG).show();
//        } else {
//            System.out.println("inside end");
//            Toast.makeText(this, "Sup man, its  not working", Toast.LENGTH_LONG).show();
//        }

        System.out.println("authenticating");

        mDBApi.getSession().startOAuth2Authentication(MainActivity.this);
        System.out.println("auth'd");

        System.out.println(mFileUpload);

//        mFileUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //uploader();
//                FileUpload FileUpload = new FileUpload(mDBApi,this,path);
//                FileUpload.execute();
//            }
//
//        });




//        mDBApi.getSession().startOAuth2Authentication(MainActivity.this);
//        System.out.println("auth'd");
        //onResume();


      //  final FileUpload fileUpload = new FileUpload();

        // System.out.println("fileupload object created");

        //final String path = "/storage/emulated/0/hey.txt";


        // putFile(fileUpload);


        //Log.v(TAG, mDBApi.toString() );


        //mDBApi.getSession().startOAuth2Authentication(MainActivity.this);
//        try {
//            upload(mDBApi);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("calling putfile inside runnable");
//                try {
//                    upload(mDBApi);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        });


    }

//    public void puttingFile(FileUpload fileUpload) {
//
//        System.out.println("putting file");
//        try {
//            System.out.println("in putfile in try");
//            upload(mDBApi);
//        } catch (FileNotFoundException e) {
//            System.out.println("filentfound in putfilefn");
//            e.printStackTrace();
//        }
//    }

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

    @Override
    public void onClick(View v) {
        FileUpload fileUpload = new FileUpload(mDBApi,this,imagePath);
        fileUpload.execute();
    }


//    public void upload(final DropboxAPI<AndroidAuthSession> mDBApi) throws FileNotFoundException {
//
//
//
//
//        //File file = new File(path);
//
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                uploader(mDBApi);
//                //Log.i("DbExampleLog", "The uploaded file's rev is: " + response.rev);
//            }
//
//        });
//    }

//    public void uploader() {
//
//
//        System.out.println("uploading");
//        String path = Environment.getExternalStorageDirectory().toString() + "/";
//        File file = new File(path, "hey.txt");
//        System.out.println("file object created");
//        FileInputStream inputStream = null;
//        try {
//            inputStream = new FileInputStream(file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        //DropboxAPI.Entry response = null;
//        try {
//            mDBApi.putFile("/magnum-opus.txt", inputStream,
//                    file.length(), null, null);
//            System.out.println("uploaded !!");
//        } catch (DropboxException e) {
//            System.out.println("failed");
//            e.printStackTrace();
//        }
//    }

//    @Override
//    public void onClick(View v) {
//        uploader(mDBApi);
//    }
}



