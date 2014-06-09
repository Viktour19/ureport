package com.informaticsproject.ureport;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends TextView {

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        
    	super(context, attrs, defStyle);
    	if(isInEditMode())
    	{
    		return;
    	}
        
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
    	if(isInEditMode())
    	{
    		return;
    	}
        
    }

    public MyTextView(Context context) {
        super(context);
    	if(isInEditMode())
    	{
    		return;
    	}
        
    }
    
    
    @Override
	public void setTypeface(Typeface tf, int style) {
		// TODO Auto-generated method stub
    if(style  == Typeface.BOLD)
    {
    	if(isInEditMode())
    	{
    		return;
    	}
    			 tf = Typeface.createFromAsset(getContext().getAssets(), "font/MyriadPro-Bold.otf");
    }
    else if(style  == Typeface.NORMAL)
    {
    	if(isInEditMode())
    	{
    		return;
    	}
    	tf = Typeface.createFromAsset(getContext().getAssets(), "font/MyriadPro-Regular.otf");
    }
    else if(style  == Typeface.ITALIC)
    {
    	if(isInEditMode())
    	{
    		return;
    	}
    	tf = Typeface.createFromAsset(getContext().getAssets(), "font/MyriadPro-Regular.otf");
    }
    			
    	
		super.setTypeface(tf, 1);
	}


}