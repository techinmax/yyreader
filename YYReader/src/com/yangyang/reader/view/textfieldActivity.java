package com.yangyang.reader.view;

import com.yangyang.reader.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class textfieldActivity extends Activity{
	
	private EditText editText = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.textfield);
		
		editText = (EditText)findViewById(R.id.editText);
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("key");
		String value = bundle.getString("textValue");
		editText.setText(value);
		
		editText.setOnEditorActionListener(new OnEditorActionListener(){

			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if(actionId == EditorInfo.IME_NULL){
					Intent resIntent = new Intent();
					Bundle bundle = new Bundle();
					String value = editText.getText().toString();
					bundle.putString("ResultValue", value);
					resIntent.putExtra("Result", bundle);
					textfieldActivity.this.setResult(RESULT_OK, resIntent);
					finish();
					return true;
				}
				return false;
			}
			
		});
	}
	
}