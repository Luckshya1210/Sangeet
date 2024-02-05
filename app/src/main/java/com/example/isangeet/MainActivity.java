package com.example.isangeet;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listView);
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                        Toast.makeText(MainActivity.this, "Runtime permission given", Toast.LENGTH_SHORT).show();
                        ArrayList<File> arrayList=fetchsongs(Environment.getExternalStorageDirectory());
                        String []song=new String[arrayList.size()];
                        for(int i=0;i<arrayList.size();i++){
                            song[i]=arrayList.get(i).getName().replace(".mp3","");
                        }
                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,song);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent=new Intent(MainActivity.this,PlaySong.class);
                                String currsong=listView.getItemAtPosition(i).toString();
                                intent.putExtra("songlist",arrayList);
                                intent.putExtra("currsong",currsong);
                                intent.putExtra("position",i);
                                startActivity(intent);
                            }
                        });

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }
    public ArrayList<File> fetchsongs(File file){
        ArrayList arr=new ArrayList();
        File []songs=file.listFiles();
        if(songs!=null){
            for(File myfile:songs){
                if(!myfile.isHidden() && myfile.isDirectory()){
                    arr.addAll(fetchsongs(myfile));
                }
                else{
                    if(myfile.getName().endsWith(".mp3") && !myfile.getName().startsWith(".")){
                        arr.add(myfile);
                    }
                }
            }
        }
        return arr;
    }

}