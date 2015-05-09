package com.example.ram.snapstore;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;


import java.util.ArrayList;

/**
 * Created by Ram on 08-05-2015.
 */
public class FilesLister extends AsyncTask<Void, Void, ArrayList<String>> {


    private DropboxAPI<AndroidAuthSession> mDBApi;
    private String path = "/Photos";
    private Handler handler;


    public FilesLister(DropboxAPI<AndroidAuthSession> mDBApi, Handler handler) {
        this.mDBApi = mDBApi;
        this.handler = handler;
    }

    /**
     * Retrieving list of files stored in Dropbox and saving it into an ArrayList
     */
    @Override
    protected ArrayList doInBackground(Void... params)
    {
        ArrayList files = new ArrayList();
        String shareAddress = null;
        DropboxAPI.Entry entries;
        try {
            entries = mDBApi.metadata(path, 100, null, true, null);
            for (DropboxAPI.Entry e : entries.contents) {
                if (!e.isDeleted) {
                    DropboxAPI.DropboxLink shareLink = mDBApi.share(e.path);
                    shareAddress = shareLink.url;
                    Log.d("Dropbox", "dropbox share link " + shareAddress);

                    files.add(shareAddress);
                    System.out.println(files);
                }
            }
        } catch (DropboxException e) {
            e.printStackTrace();

        }
        return files;
    }

    /**
     * Bundling the result and sending it to the handler in MainActivity
     */
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
