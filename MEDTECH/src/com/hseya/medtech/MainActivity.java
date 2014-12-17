package com.hseya.medtech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	private Thread mSplashThread; 
	protected int _splashTime = 3000;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        
        final MainActivity thisActivity = this; 


		mSplashThread = new Thread() {
			@Override
			public void run() {
				try {
					synchronized(this){

						//wait 5 sec
						wait(_splashTime);
					}

				} catch(InterruptedException e) {}
				finally {
					finish();

					Intent i = new Intent();
					i.setClass(thisActivity, LoginActivity.class);
					startActivity(i);
					thisActivity.finish();
				}
			}
		};

		mSplashThread.start();
        
        
        
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
