package com.example.ram.snapstore;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Ram on 07-05-2015.
 */
public class FileUpload extends AsyncTask {


    MainActivity mainAct = new MainActivity();

    DropboxAPI<AndroidAuthSession> mDBApi;
    Context context;
    String path;

    public FileUpload(DropboxAPI<AndroidAuthSession> mDBApi, MainActivity mainActivity, String path) {
        this.context = mainActivity.getApplicationContext();
        this.mDBApi = mDBApi;
        this.path = path;
    }


//    public void FileUpload(DropboxAPI<AndroidAuthSession> mDBApi, Context context, String path) {
//
//        this.context = context.getApplicationContext();
//        this.mDBApi = mDBApi;
//        this.path = path;
//    }


    @Override
    protected Object doInBackground(Object[] params) {

        File file = new File(path, "IMG_20150508_031426.jpg");
        System.out.println("file object created");
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //DropboxAPI.Entry response = null;
        try {
            mDBApi.putFile("/Photos/magnum-opus.jpg", inputStream,
                    file.length(), null, null);
            System.out.println("uploaded !!");
            return true;
        } catch (DropboxException e) {
            System.out.println("failed");
            e.printStackTrace();

        }
        return false;

    }
}
