package com.techninja01.callassistantsupport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Locale;

public class ReceiveSms extends BroadcastReceiver {
    String msg_from;
    String msgBody;
    TextToSpeech textToSpeech;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Sms Received", Toast.LENGTH_SHORT).show();

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();
            SmsMessage[] smsMessages;
            if(bundle!=null){
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    smsMessages = new SmsMessage[pdus.length];
                    for(int i=0;i<smsMessages.length;i++){
                        smsMessages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = smsMessages[i].getOriginatingAddress();
                        msgBody = smsMessages[i].getMessageBody();
                        Toast.makeText(context, "From: "+msg_from+"\tContent: "+msgBody, Toast.LENGTH_SHORT).show();
                        Intent speechIntent = new Intent();
                        speechIntent.setClass(context, SpeakTheMessage.class);
                        speechIntent.putExtra("MESSAGE", msgBody);
                        speechIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        context.startActivity(speechIntent);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
