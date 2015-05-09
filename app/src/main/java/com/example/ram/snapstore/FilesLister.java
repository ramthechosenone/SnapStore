package com.example.ram.snapstore;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by Ram on 08-05-2015.
 */
public class FilesLister extends AsyncTask<Void, Void, ArrayList<String>> {


    private DropboxAPI<AndroidAuthSession> mDBApi;
    private String path = "/Photos";
    //private int i=0;
    private Handler handler;
    ArrayList<String> files = new ArrayList<String>();


    public FilesLister (DropboxAPI<AndroidAuthSession> mDBApi, Handler handler)
    {
        this.mDBApi = mDBApi;
        this.handler = handler;
    }


        //DropboxAPI dropboxAPI = new DropboxAPI();

    @Override
    protected ArrayList doInBackground(Void... params)

    {
        ArrayList files = new ArrayList();

        String shareAddress = null;

        DropboxAPI.Entry entries;
        //DropboxAPI.DropboxLink shareLink = null;

//        ArrayList<String> files = new ArrayList<String>();

        try {
            entries = mDBApi.metadata(path, 100, null, true, null);
            for (DropboxAPI.Entry e : entries.contents) {
                if (!e.isDeleted) {

                    DropboxAPI.DropboxLink shareLink = mDBApi.share(e.path);
                    shareAddress = shareLink.url;

                    //shareAddress = shareAddress.replaceFirst("https://www", "https://db");

                    Log.d("Dropbox","dropbox share link " + shareAddress);

                    //String shareAddress_f= shareAddress.replaceFirst("https://www.dropbox.com/", "https://dl.dropboxusercontent.com/");



                    files.add(shareAddress);
                    System.out.println(files);
                }
            }

        } catch (DropboxException e) {
            e.printStackTrace();

        }

        return files;

    }

//    ArrayList<String> List_dropBox_images = new ArrayList<String>();
//    private void getFile()
//    {
//        for (int i = 0; i < files.size(); i++) {
//            try {
//                System.gc();
//                DropboxAPI.DropboxLink shareLink = mDBApi.share(files.path);
//
//
//                Log.e("", "" + shareLink.url);
////                String shareAddress = getLocation(shareLink.url);
//                String shareAddress = shareLink.url;
//                if(shareAddress==null) continue;
//                String shareAddress_f= shareAddress.replaceFirst("https://www.dropbox.com/", "https://dl.dropboxusercontent.com/");
//                Log.e(""," address "+shareAddress_f);
//                List_dropBox_images.add(shareAddress_f);
//            }catch (Exception e) {
//                System.gc();
//                e.printStackTrace();
//            }
//        }
//
//
//    }


//    private String getLocation(String link)  {
//        try {
//            final URL url = new URL(link);
//            final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setInstanceFollowRedirects(false);
//
//            return urlConnection.getHeaderField("location");
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return link;
//    }



    @Override
    protected void onPostExecute(ArrayList<String> result) {
        Message msgObj = handler.obtainMessage();
        Bundle b = new Bundle();
        b.putStringArrayList("data", result);
        msgObj.setData(b);
        System.out.println("handling on post excecute");
        handler.sendMessage(msgObj);

    }
}
