package com.example.hainam.demofacebook;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment{

    //Facebook
    private CallbackManager callbackManager;
    //Google+


    public MainActivityFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fecebook
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        //Google+

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }





    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Facebook
        final LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
       final TextView view1 = (TextView) view.findViewById(R.id.txtView);
//        loginButton.setReadPermissions(Arrays.asList("user_friends","user_location","user_birthday","user_likes"));

        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("user_friends", "user_location", "user_birthday", "user_likes", "user_photos"));
                Toast.makeText(getActivity().getApplicationContext(), "Login Success.!", Toast.LENGTH_LONG).show();
//                AccessToken accessToken = loginResult.getAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        try {
                            String localtion = jsonObject.getJSONObject("location").getString("name");
                            String birthday = jsonObject.getString("birthday");
                            String gender = jsonObject.getString("gender");
                            String locale = jsonObject.getString("locale");
                            String link = jsonObject.getString("link");
                            String name = jsonObject.getString("name");
                            String id = jsonObject.getString("id");
                            Log.d("id", id);
                            Log.d("name", name);
                            Log.d("link", link);
                            Log.d("locale", locale);
                            Log.d("gender", gender);
                            Log.d("birthday", birthday);
                            Log.d("localtion", localtion);
                            ProfilePictureView pictureView = (ProfilePictureView) view.findViewById(R.id.profilePicture);
                            try {
                                URL image_path = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                                System.out.println("image::> " + image_path);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            pictureView.setProfileId(id);

                            //  imageView.setImageBitmap(bitmap);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,gender,locale,birthday,location");
                request.setParameters(parameters);
                request.executeAsync();
//====================================
                GraphRequestBatch batch = new GraphRequestBatch(
                        GraphRequest.newMeRequest(
                                AccessToken.getCurrentAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject jsonObject,
                                            GraphResponse response) {
                                        // Application code for user
                                    }
                                }),
                        GraphRequest.newMyFriendsRequest(
                                AccessToken.getCurrentAccessToken(),
                                new GraphRequest.GraphJSONArrayCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONArray jsonArray,
                                            GraphResponse response) {

                                        System.out.println("getFriendsData onCompleted : jsonArray " + jsonArray);
                                        System.out.println("getFriendsData onCompleted : response " + response);
                                        try {
                                            JSONObject jsonObject = response.getJSONObject();
                                            System.out.println("getFriendsData onCompleted : jsonObject " + jsonObject);
                                            JSONObject summary = jsonObject.getJSONObject("summary");
                                            System.out.println("getFriendsData onCompleted : summary total_count - " + summary.getString("total_count"));
                                            JSONArray array = jsonObject.getJSONArray("data");
                                            System.out.println("getFriendsData onCompleted : jsonArray " + array.length());
                                            System.out.println("getFriendsData onCompleted : jsonArray Item " + array.getJSONObject(0).getString("name"));

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                );
                batch.addCallback(new GraphRequestBatch.Callback() {
                    @Override
                    public void onBatchCompleted(GraphRequestBatch graphRequests) {
                        // Application code for when the batch finishes
                    }
                });
                batch.executeAsync();

            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity().getApplicationContext(), "Login Cancel.!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Login Error.!", Toast.LENGTH_LONG).show();
            }
        });


    //check Login
        if(AccessToken.getCurrentAccessToken()!=null){
            view1.setText("Login");
        }else{
            view1.setText("Logout");
        }

        //google+


    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(getActivity().getApplicationContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(getActivity().getApplicationContext());
    }




}
