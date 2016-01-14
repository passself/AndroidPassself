package com.pass.self.utils;

import java.util.regex.Pattern;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.EditText;

public class IpEditText extends EditText {
	
	
	public IpEditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public IpEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public IpEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setFilters(InputFilter[] filters) {
		// TODO Auto-generated method stub
		super.setFilters(addIpFilter());
	}
	
	private InputFilter[] addIpFilter(){
		InputFilter[] filters = new InputFilter[1];
	    filters[0] = new InputFilter() {
	        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
	            if (end > start) {
	                String destTxt = dest.toString();
	                String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
	                if (!resultingTxt.matches ("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) { 
	                    return "";
	                } else {
	                    String[] splits = resultingTxt.split("\\.");
	                    for (int i=0; i<splits.length; i++) {
	                        if (Integer.valueOf(splits[i]) > 255) {
	                            return "";
	                        }
	                    }
	                }
	            }
	        return null;
	        }
	    };
		return filters;
	}
	
	/**
	 * 验证是否合法
	 * @param str
	 * @return
	 */
	public boolean checkIP(String str) {
		Pattern pattern = Pattern
                .compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]"
                        + "|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
        return pattern.matcher(str).matches();
	}
}
