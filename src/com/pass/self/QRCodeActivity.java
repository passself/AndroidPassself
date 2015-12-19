package com.pass.self;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.pass.self.qrcode.QRCodeUtil;

/**
 * 二维码操作
 * @author jingoal_user
 *
 */
public class QRCodeActivity extends Activity implements OnClickListener{
	String TAG = "QRCodeActivity";
	Button addButton;
	CheckBox checkBox;
	EditText editText;
	ImageView qrImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qrcode_layout);
		initViews();
	}
	
	private void initViews(){
		addButton = (Button)findViewById(R.id.qrcode_btn_add);
		addButton.setOnClickListener(this);
		checkBox = (CheckBox)findViewById(R.id.qrcode_checkbox_logo);
		editText = (EditText)findViewById(R.id.qrcode_edit_content);
		qrImageView = (ImageView)findViewById(R.id.qrcode_show_img);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == addButton){
			addQRCode();
		}
	}
	
	private void addQRCode(){
		final String filePath = getFileRoot(this) + File.separator  
                + "qr_" + System.currentTimeMillis() + ".jpg";  
		Log.i(TAG, "filePath is:"+filePath);
		final Bitmap logo =  BitmapFactory.decodeResource(getResources(), R.drawable.jin_goal);
        //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中  
        new Thread(new Runnable() {  
            @Override  
            public void run() {  
                boolean success = QRCodeUtil.createQRImage(editText.getText().toString().trim(), 300, 300,  
                		checkBox.isChecked() ? logo : null,  
                        filePath);  
                Log.i(TAG, "success is:"+success);
                if (success) {  
                    runOnUiThread(new Runnable() {  
                        @Override  
                        public void run() {  
                        	//qrImageView.setImageBitmap(BitmapFactory.decodeFile(filePath));  
                        	try {
								qrImageView.setImageBitmap(QRCodeUtil.createCode(editText.getText().toString(), logo, BarcodeFormat.QR_CODE));
							} catch (WriterException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        }  
                    });  
                }  
            }  
        }).start();  
	}
	
	//文件存储根目录  
    private String getFileRoot(Context context) {  
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  
            File external = context.getExternalFilesDir(null);  
            if (external != null) {  
                return external.getAbsolutePath();  
            }  
        }  
        return context.getFilesDir().getAbsolutePath();  
    }  
}
