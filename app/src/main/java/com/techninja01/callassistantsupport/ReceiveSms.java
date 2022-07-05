package com.techninja01.callassistantsupport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Locale;

public class ReceiveSms extends BroadcastReceiver implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {
    String msg_from;
    String msgBody;
    TextToSpeech textToSpeech;
    private TextToSpeech tts = null;
    private String msg = "";
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
            if(bundle!=null){
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    smsMessages = new SmsMessage[pdus.length];
                    for(int i=0;i<smsMessages.length;i++){
                        smsMessages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = smsMessages[i].getOriginatingAddress();
                        msgBody = smsMessages[i].getMessageBody();
                        msg = msgBody;
                        Toast.makeText(context, "From: "+msg_from+"\tContent: "+msgBody, Toast.LENGTH_SHORT).show();
//                        Intent speechIntent = new Intent();
//                        speechIntent.setClass(context, SpeakTheMessage.class);
//                        speechIntent.putExtra("MESSAGE", msgBody);
//                        speechIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                        context.startActivity(speechIntent);
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

    // OnUtteranceCompletedListener impl
    public void onUtteranceCompleted(String utteranceId) {
        tts.shutdown();
        tts = null;
    }
}
