package com.liyu.itester.utils;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class MountListener {
public Context con;

public MountListener(Context context)
{
	this.con = context;
}

public void init()
{
	IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
    //intentFilter.setPriority(1000);  
    intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
    intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
    intentFilter.addAction(Intent.ACTION_MEDIA_SHARED);
    intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL); 
    intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
    intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED); 
    intentFilter.addAction(Intent.ACTION_MEDIA_CHECKING);
    intentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
    intentFilter.addAction(Intent.ACTION_MEDIA_NOFS);
    intentFilter.addAction(Intent.ACTION_MEDIA_BUTTON);
    intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    intentFilter.addDataScheme("file");
    con.registerReceiver(broadcastRec, intentFilter);
}

private final BroadcastReceiver broadcastRec = new BroadcastReceiver() {  
    @Override  
    public void onReceive(Context context, Intent intent) {  
        String action = intent.getAction(); 
        Log.d("MediaAction", action);
        if (action.equals("android.intent.action.MEDIA_MOUNTED"))
        {  
        	mOnStateChangedListener.onMounted();
        } else if (action.equals(Intent.ACTION_MEDIA_REMOVED)    
                || action.equals(Intent.ACTION_MEDIA_BAD_REMOVAL)
                ||action.equals(Intent.ACTION_MEDIA_UNMOUNTED)) 
        {  
        	mOnStateChangedListener.onUnMounted();
        }else if (action.equals(Intent.ACTION_MEDIA_SCANNER_STARTED)){
        }else if (action.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED)){
        }else if (action.equals(Intent.ACTION_MEDIA_SHARED)){
        }else { 
        }  
    }  
}; 

public interface OnStateChangedListener
{
	public void onMounted();
	public void onUnMounted();
}

OnStateChangedListener mOnStateChangedListener = null;

public void setOnStateChangedListener(OnStateChangedListener listener) {
    mOnStateChangedListener = listener;
    init();
}

public void removeListener()
{
	con.unregisterReceiver(broadcastRec);
	
}

}
