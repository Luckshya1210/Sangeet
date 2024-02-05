package com.example.isangeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateseek.interrupt();
    }

    TextView textView;
    ImageView play,next,prev;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String textContent;
    int position;
    SeekBar seekBar;
    Thread updateseek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        textView=findViewById(R.id.textView);
        play=findViewById(R.id.play);
        prev=findViewById(R.id.prev);
        next=findViewById(R.id.next);
        seekBar=findViewById(R.id.seekBar);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        songs=(ArrayList) bundle.getParcelableArrayList("songlist");
        textContent=intent.getStringExtra("currsong");
        textView.setText(textContent);
        textView.setSelected(true);
        position=intent.getIntExtra("position",0);
        Uri uri=Uri.parse(songs.get(position).toString());
        mediaPlayer=MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        updateseek=new Thread(){
            @Override
            public void run() {
                super.run();
                int currpos=0;
                try{
                    while (currpos<mediaPlayer.getDuration()){
                        currpos=mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currpos);
                        sleep(800);
                    }
                    if(currpos==mediaPlayer.getDuration()){
                        play.setImageResource(R.drawable.play);
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateseek.start();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }else{
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
//                onDestroy();
                if (position != 0) {
                    position = position - 1;
                } else {
                    position = songs.size() - 1;
                }

                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                updateseek.start();
                textContent=songs.get(position).getName().toString();
                textView.setText(textContent);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
//                onDestroy();
                if(position!=songs.size()-1){
                    position=position+1;
                }
                else{
                    position=0;
                }
                Uri uri=Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);

                mediaPlayer.start();

                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                updateseek.start();
                textContent=songs.get(position).getName().toString();
                textView.setText(textContent);
            }
        });
    }
}