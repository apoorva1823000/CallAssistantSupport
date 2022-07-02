package com.techninja01.callassistantsupport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class SpeakTheMessage extends AppCompatActivity implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {
    private TextToSpeech tts = null;
    private String msg = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak_the_message);
//        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if(status != TextToSpeech.ERROR){
//                    textToSpeech.setLanguage(Locale.UK);
//                }
//            }
//        });
//        Intent intent = getIntent();
//        String msgBody = intent.getStringExtra("MESSAGE");
//        textToSpeech.speak(msgBody,TextToSpeech.QUEUE_FLUSH, null);
        Intent startingIntent = this.getIntent();
        msg = startingIntent.getStringExtra("MESSAGE");
        tts = new TextToSpeech(this,this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts!=null) {
            tts.shutdown();
        }
    }

    public void onInit(int status) {
        tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
    }

    // OnUtteranceCompletedListener impl
    public void onUtteranceCompleted(String utteranceId) {
        tts.shutdown();
        tts = null;
        finish();
    }
}