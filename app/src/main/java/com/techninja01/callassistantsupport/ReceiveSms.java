package com.techninja01.callassistantsupport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class ReceiveSms extends BroadcastReceiver implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {
    String msg_from;
    String msgBody;
    TextToSpeech textToSpeech;
    private TextToSpeech tts = null;
    private String msg = "";
    final String[] message1 = new String[1];
    String message;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Sms Received", Toast.LENGTH_SHORT).show();

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();
            SmsMessage[] smsMessages;
            tts = new TextToSpeech(MainActivity.context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status != TextToSpeech.ERROR){
                        tts.setLanguage(Locale.forLanguageTag("hin"));
                        tts.setPitch(0.8f);
                        tts.setSpeechRate(0.5f);
                    }
                }
            });
            Calendar c = Calendar.getInstance();
            int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
            if(timeOfDay >= 0 && timeOfDay < 12){
//                message = "Good Morning dear caller, the person you\'re calling is busy";
                message = "Suprabhat, Aapne Apoorva Mehta ko call kiya hai aur ve abhi dusra application bna rhe hai";
            }else if(timeOfDay >= 12 && timeOfDay < 16){
                message = "Good Afternoon dear caller, the person you\'re calling is busy";
            }else if(timeOfDay >= 16 && timeOfDay < 21){
                message = "Good Evening dear caller, the person you\'re calling is busy";
            }else if(timeOfDay >= 21 && timeOfDay < 24){
                message = "Good Night dear caller, the person you\'re calling is sleeping";
            }
            if(bundle!=null){
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    smsMessages = new SmsMessage[pdus.length];
                    for(int i=0;i<smsMessages.length;i++){
                        smsMessages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = smsMessages[i].getOriginatingAddress();
                        msgBody = smsMessages[i].getMessageBody();
                        if(msgBody.equalsIgnoreCase("0")){
                            Toast.makeText(MainActivity.context,"M",Toast.LENGTH_SHORT).show();
                            MainActivity.mediaPlayer = MediaPlayer.create(MainActivity.context,R.raw.msc1);
                            MainActivity.mediaPlayer.start();
                            msg = "Play Audio"+message;
                        }else if (msgBody.equalsIgnoreCase("1")){
                            MainActivity.mediaPlayer.pause();
                            msg = "Call Over";
                        }
//                        msg = msgBody;
                        Toast.makeText(context, "From: "+msg_from+"\tContent: "+msgBody, Toast.LENGTH_SHORT).show();
                        tts = new TextToSpeech(MainActivity.context,this);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    public void onInit(int status) {
        tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
    }
    public void onUtteranceCompleted(String utteranceId) {
        tts.shutdown();
        tts = null;
    }
}
