package com.liyu.itester;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class ResultsActivity extends Activity implements OnClickListener{
	TextView tv_pass,tv_fail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);
		init();
	}

	public void init()
	{
		findViewById(R.id.button_upload).setOnClickListener(this);
		findViewById(R.id.button_exit).setOnClickListener(this);
		tv_pass = (TextView)findViewById(R.id.textView_pass);
		tv_fail = (TextView)findViewById(R.id.textView_fail);
		for(int i = 0;i<XmlUtils.passList.size();i++)
		{
			tv_pass.append("\n"+XmlUtils.passList.get(i));
		}
		for(int i = 0;i<XmlUtils.failList.size();i++)
		{
			tv_fail.append("\n"+XmlUtils.failList.get(i));
		}
		XmlUtils.activityClasses.clear();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(jumpUtils.isFastDoubleClick())
			return;
		switch(v.getId())
		{
		case R.id.button_upload:
			Toast.makeText(getApplicationContext(), "暂未开放该功能!", Toast.LENGTH_SHORT).show();
			break;
		case R.id.button_exit:
			ResultsActivity.this.finish();
			System.exit(0);
			break;
		}
	}
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    	if (keyCode == KeyEvent.KEYCODE_BACK)
	    	{
	    		new AlertDialog.Builder(ResultsActivity.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.label_confirmexit)
	    		.setPositiveButton(R.string.label_exit, new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				ResultsActivity.this.finish();
	    				System.exit(0);
	    			}
	    		})
	    		.show();
	    		return true;
	    	}
			return false;
	    }

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		YoYo.with(Techniques.Shake)
        .duration(1000)
        .playOn(findViewById(R.id.textView_fail));
	}
	 
	 
}
