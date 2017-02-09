package com.ebttikarat.complaints.gui.view;

import com.ebttikarat.complaints.gui.AbstractScreen;
import com.ebttikarat.complaints.gui.R;
import com.ebttikarat.complaints.util.FontUtil;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainMenuView extends AbstractScreen {

	public MainMenuView(FragmentActivity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void loadScreen() {
		// TODO Auto-generated method stub
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity.setContentView(R.layout.main_menu_layout);
      
      /**
       * Creating all buttons instances
       * */
      
	  TextView mTitle = (TextView) activity.findViewById(R.id.titleTxt);
	  Typeface mFont = FontUtil.get(activity, FontUtil.BOLD_FONT);
	  mTitle.setTypeface(mFont);
      //my complaints button
      Button myComplaintsBtn = (Button) activity.findViewById(R.id.btn_my_complaint);
      //new complaint button
      Button newComplaintBtn = (Button) activity.findViewById(R.id.btn_new_complaint);
      //setting button
      Button settingBtn= (Button) activity.findViewById(R.id.btn_setting);
      //saved complaints button
      Button savedComplaintsBtn = (Button) activity.findViewById(R.id.btn_saved_complaint);
      //about button
      Button aboutBtn = (Button) activity.findViewById(R.id.btn_about);
      //rate button
      Button rateBtn = (Button) activity.findViewById(R.id.btn_rate);
      
      Button aboutUnv = (Button) activity.findViewById(R.id.aboutUnv);
      
      Button unvRights = (Button) activity.findViewById(R.id.unvRIghts);
      /**
       * Handling all button click events
       * */
      
     // Listening my complaints click
      myComplaintsBtn.setOnClickListener(new View.OnClickListener() {
			
			//@Override
			public void onClick(View view) {
				// Launching my complaints Screen
				Intent mainView = new Intent(activity.getApplicationContext(), MainView.class);
				mainView.putExtra("targetFragment", 2);
				activity.startActivity(mainView);
			}
		});
      
      // Listening new complaint click
      newComplaintBtn.setOnClickListener(new View.OnClickListener() {
			
			//@Override
			public void onClick(View view) {
				// Launching new complaints Screen
//				Intent mainView = new Intent(activity.getApplicationContext(), MainActivity.class);
//				activity.startActivity(mainView);
				Intent mainView = new Intent(activity.getApplicationContext(), MainView.class);
				mainView.putExtra("targetFragment", 1);
				activity.startActivity(mainView);
			}
		});
      
      // Listening setting click
      settingBtn.setOnClickListener(new View.OnClickListener() {
			
			//@Override
			public void onClick(View view) {
				// Launching setting Screen
				Intent mainView = new Intent(activity.getApplicationContext(), MainView.class);
				mainView.putExtra("targetFragment", 4);
				activity.startActivity(mainView);
			}
		});
      
      // Listening saved complaints click
      savedComplaintsBtn.setOnClickListener(new View.OnClickListener() {
			
			//@Override
			public void onClick(View view) {
				// Launching saved complaints Screen
				Intent mainView = new Intent(activity.getApplicationContext(), MainView.class);
				mainView.putExtra("targetFragment", 3);
				activity.startActivity(mainView);
			}
		});
      
      // Listening about click
      aboutBtn.setOnClickListener(new View.OnClickListener() {
			
			//@Override
			public void onClick(View view) {
				// Launching about Screen
				Intent mainView = new Intent(activity.getApplicationContext(), MainView.class);
				mainView.putExtra("targetFragment", MainView.ABOUT_VIEW);
				activity.startActivity(mainView);
			}
		});
      
      // Listening rate click
      rateBtn.setOnClickListener(new View.OnClickListener() {
			
			//@Override
			public void onClick(View view) {
				// Launching rate Screen
				Intent mainView = new Intent(activity.getApplicationContext(), MainView.class);
				mainView.putExtra("targetFragment", 5);
				activity.startActivity(mainView);
			}
		});
      
      aboutUnv.setOnClickListener(new View.OnClickListener() {
			
			//@Override
			public void onClick(View view) {
				// Launching about university Screen
				Intent mainView = new Intent(activity.getApplicationContext(), MainView.class);
				mainView.putExtra("targetFragment", MainView.UNIV_ABOUT_VIEW);
				activity.startActivity(mainView);
			}
		});
      
      unvRights.setOnClickListener(new View.OnClickListener() {
			
			//@Override
			public void onClick(View view) {
				// Launching university rights Screen
				Intent mainView = new Intent(activity.getApplicationContext(), MainView.class);
				mainView.putExtra("targetFragment", MainView.UNIV_RIGHTS_VIEW);
				activity.startActivity(mainView);
			}
		});
		
	}
}
