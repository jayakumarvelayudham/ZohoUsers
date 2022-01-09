package com.example.quickapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.quickapp.dao.RandomUserDao;
import com.example.quickapp.database.InMobiDB;
import com.example.quickapp.entity.RandomUser;
import com.example.quickapp.fragments.RandomUserList;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    FrameLayout fragment_container;
    private static int LOCATION_PERMISSIONS_REQUEST = 1;

    private RandomUserDao randomUserDao;
    private InMobiDB inMobiDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inMobiDB = InMobiDB.getDatabase(getApplicationContext());
        randomUserDao = inMobiDB.randomUserDao();
//        randomUserDao.delete();
        new DeleteAsyncTask(randomUserDao).execute();

        fragment_container = (FrameLayout) findViewById(R.id.fragment_container);
        askPermissions();
        statusCheck();
    }

    public void statusCheck(){
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, Need to enable to view Weather Report")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        statusCheck();
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    protected void askPermissions() {

        LOCATION_PERMISSIONS_REQUEST = 1;

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSIONS_REQUEST);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSIONS_REQUEST) {

            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                RandomUserList randomUserList = new RandomUserList();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(fragment_container.getId(),randomUserList,"HomeFragment").addToBackStack(null).commit();
            } else {
                Toast.makeText(this, "Access Location permission denied, PLEASE ACCEPT IT.", Toast.LENGTH_SHORT).show();
                askPermissions();
            }
        } else {

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if(Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag("HomeFragment")).isVisible()){
            showExitDialog();
        }else if(fragmentList.size()>1){
            getSupportFragmentManager().popBackStack();
        }
    }

    public void showExitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private class DeleteAsyncTask extends AsyncTask<RandomUser,Void,Void> {

        RandomUserDao randomUserDao;

        public DeleteAsyncTask(RandomUserDao randomUserDao){
            this.randomUserDao = randomUserDao;
        }

        @Override
        protected Void doInBackground(RandomUser... randomUsers) {
//            randomUserDao.insert(randomUsers[0]);
            randomUserDao.delete();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
//            super.onPostExecute(unused);
        }
    }
}