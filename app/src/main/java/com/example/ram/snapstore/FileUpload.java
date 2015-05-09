package com.example.ram.snapstore;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
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




    DropboxAPI<AndroidAuthSession> mDBApi;
   // Context context;

    File file;

    String fileName;



    public FileUpload(DropboxAPI<AndroidAuthSession> mDBApi, File b, String fileName) {
       // this.context = mainActivity.getApplicationContext();
        this.mDBApi = mDBApi;
        this.file =  b;
        this.fileName = fileName;

    }





    @Override
    protected Object doInBackground(Object[] params) {


//        String fileName = mediaFile;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            mDBApi.putFile("/Photos/"+fileName, inputStream,
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
