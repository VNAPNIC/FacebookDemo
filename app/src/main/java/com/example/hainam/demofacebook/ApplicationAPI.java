package com.example.hainam.demofacebook;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hainam on 4/28/2015.
 */
public class ApplicationAPI extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PrintHashkey();
    }

    public void PrintHashkey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.hainam.demofacebook",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }
}
