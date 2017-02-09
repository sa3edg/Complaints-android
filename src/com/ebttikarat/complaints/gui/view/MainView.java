package com.ebttikarat.complaints.gui.view;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.ebttikarat.complaints.business.storage.FileStorageManger;
import com.ebttikarat.complaints.common.model.Complaint;
import com.ebttikarat.complaints.gui.R;
import com.ebttikarat.complaints.gui.adapter.NavDrawerListAdapter;
import com.ebttikarat.complaints.gui.model.FileBrowserItem;
import com.ebttikarat.complaints.gui.model.NavDrawerItem;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class MainView extends ActionBarActivity {
	private DrawerLayout mDrawerLayout;
	// private RelativeLayout mDrawerRelative;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	// navigation map(component index, component title)
	private SparseArray<String> navigationMap = new SparseArray<String>();
	private ArrayList<NavDrawerItem> navDrawerItems;
	private TextView vTitle;

	private AddNewComplaintView currentAddComplaint;
	private EditComplaintView currentEditComplaint;
	private AttachmentsView currentAttachmentView;
	private MyComplaintDetailsView myCOmplaintDetailsView;
	// UI components index
	public static final int ADD_COMPLAINT_VIEW = 1;
	public static final int MY_COMPLAINT_VIEW = 2;
	public static final int SAVED_COMPLAINT_VIEW = 3;
	public static final int SETTING_VIEW = 4;
	public static final int RATE_VIEW = 5;
	public static final int FILE_BROWSER_VIEW = 12;
	public static final int CAPTURE_PHOTO_VIEW = 13;
	public static final int CAPTURE_VIDEO_VIEW = 14;
	public static final int ATTACHMENT_VIEW = 15;
	public static final int ABOUT_VIEW = 16;
	public static final int UNIV_ABOUT_VIEW = 17;
	public static final int UNIV_RIGHTS_VIEW = 18;
	public static final int MY_COMPLAINT_DETAILS_VIEW = 19;
	public static final int EDIT_COMPLAINT_VIEW = 20;
	private Uri currentMediaURI;

	// Capture image/video activity request
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;

	//private Complaint editCOmplain;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view_layout);

		mTitle = mDrawerTitle = getTitle();
		Intent intent = getIntent();
		int targetFragment = intent.getIntExtra("targetFragment", 0);

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// mDrawerRelative = (RelativeLayout)
		// findViewById(R.id.relative_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		// set sliding meny footer if needed
		// View listFooter =
		// LayoutInflater.from(this).inflate(R.layout.drawer_list_footer, null);
		// mDrawerList.addFooterView(listFooter);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav title to navigation map
		navigationMap.put(ADD_COMPLAINT_VIEW, navMenuTitles[1]);
		navigationMap.put(FILE_BROWSER_VIEW,
				getResources().getString(R.string.file_browser_title));
		navigationMap.put(CAPTURE_PHOTO_VIEW,
				getResources().getString(R.string.capture_photo_title));
		navigationMap.put(CAPTURE_VIDEO_VIEW,
				getResources().getString(R.string.capture_video_title));
		navigationMap.put(ATTACHMENT_VIEW,
				getResources().getString(R.string.attachment_title));
		navigationMap.put(MY_COMPLAINT_VIEW, navMenuTitles[2]);
		navigationMap.put(SAVED_COMPLAINT_VIEW, navMenuTitles[3]);
		navigationMap.put(ABOUT_VIEW,
				getResources().getString(R.string.about_title));
		navigationMap.put(RATE_VIEW,
				navMenuTitles[5]);
		navigationMap.put(UNIV_ABOUT_VIEW,
				getResources().getString(R.string.univ_about_title));
		navigationMap.put(UNIV_RIGHTS_VIEW,
				getResources().getString(R.string.univ_rights_title));
		navigationMap.put(MY_COMPLAINT_DETAILS_VIEW,
				getResources().getString(R.string.complaint_details_title));
		navigationMap.put(EDIT_COMPLAINT_VIEW,
				getResources().getString(R.string.edit_complaint_title));
		navigationMap.put(SETTING_VIEW, navMenuTitles[4]);
		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		// new complaint
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		// my complaints
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));
		// saved complaints
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1)));
		// setting
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1)));
		// app rate
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();

		// set up the drawer's list view with items using a custom adapter and
		// click listener
		NavDrawerListAdapter adapter = new NavDrawerListAdapter(this,
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// enabling action bar app icon and behaving it as toggle button
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_drawer);
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.app_title_bar));
		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setCustomView(R.layout.main_view_header_layout);

		vTitle = (TextView) getSupportActionBar().getCustomView().findViewById(
				R.id.mainViewTitle);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				vTitle.setText(mTitle);

				// calling onPrepareOptionsMenu() to show action bar icons
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				vTitle.setText(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				supportInvalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// display target fragment view.
		displayView(targetFragment);
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);

			// Closing the drawer
			mDrawerLayout.closeDrawers();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	public void displayView(int position) {
		// update the main content by replacing fragments
		Object fragment = null;
		switch (position) {
		case 0:
			onBackPressed();
			break;
		case ADD_COMPLAINT_VIEW:
			fragment = new AddNewComplaintView(this);
			setCurrentAddComplaint((AddNewComplaintView) fragment);
			setCurrentAttachmentView(null);
			setCurrentEditComplaint(null);
			break;
		case SAVED_COMPLAINT_VIEW:
			fragment = new SavedComplaintsView(this);
			break;
		case MY_COMPLAINT_VIEW:
			fragment = new MyComplaintsView(this);
			break;
		case RATE_VIEW:
			fragment = new RateView(this);
			break;
		case SETTING_VIEW:
			fragment = new SettingView(this);
			break;
		default:
			break;
		}
		if(ABOUT_VIEW == position || UNIV_ABOUT_VIEW == position || UNIV_RIGHTS_VIEW == position){
			if(ABOUT_VIEW == position){
				displayTextContainerView(ABOUT_VIEW, "1");
			}else if(UNIV_ABOUT_VIEW == position){
				displayTextContainerView(UNIV_ABOUT_VIEW, "2");
				
			}else if(UNIV_RIGHTS_VIEW == position){
				displayTextContainerView(UNIV_RIGHTS_VIEW, "3");
			}
		}
		else if (fragment != null) {
			showTargetFragment(position, fragment);
		}
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	public void displayFileBrowser(AddNewComplaintView parentScreen,
			boolean isAttachmentView, boolean isEditMode) {
		setCurrentAddComplaint(parentScreen);
		// update the main content by replacing fragments
//		if (isAttachmentView) {
//			displayAttachmentView(parentScreen, isEditMode);
//		} else if (isEditMode) {
//			displayEditComplaintView(getEditCOmplain());
//		}else {
			Object fragment = new FileChooserView(this, isAttachmentView, isEditMode);
			if (fragment != null) {
				android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
				fm.beginTransaction()
						.replace(R.id.frame_container, (Fragment) fragment)
						.commit();

				// update selected item and title, then close the drawer
//				mDrawerList.setItemChecked(ADD_COMPLAINT_VIEW, true);
//				mDrawerList.setSelection(ADD_COMPLAINT_VIEW);
				setTitle(navigationMap.get(FILE_BROWSER_VIEW));
			}
//		}
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	public void displayAttachmentView(AddNewComplaintView parentScreen, boolean isEditMode) {
		// update the main content by replacing fragments
		setCurrentAddComplaint(parentScreen);
		final Object fragment = new AttachmentsView(this, isEditMode);
		setCurrentAttachmentView((AttachmentsView)fragment);
		if (fragment != null) {
			new Handler().post(new Runnable() {
				public void run() {
					android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
					fm.beginTransaction()
							.replace(R.id.frame_container, (Fragment) fragment)
							.commit();
					setTitle(navigationMap.get(ATTACHMENT_VIEW));
				}
			});
			
		}
	}
	
	public void displayAttachmentDetailsView(MyComplaintDetailsView parentScreen, Complaint complaint) {
		// update the main content by replacing fragments
		setMyCOmplaintDetailsView(parentScreen);
		Object fragment = new AttachmentsDetailsView(this, complaint);
		if (fragment != null) {
			android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
			fm.beginTransaction()
					.replace(R.id.frame_container, (Fragment) fragment)
					.commit();
			setTitle(navigationMap.get(ATTACHMENT_VIEW));
		}
	}
	
	public void displayMyComplaintDetailsView() {
		// update the main content by replacing fragments
		Object fragment = getMyCOmplaintDetailsView();
		if (fragment != null) {
			android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
			fm.beginTransaction()
					.replace(R.id.frame_container, (Fragment) fragment)
					.commit();
			setTitle(navigationMap.get(MY_COMPLAINT_DETAILS_VIEW));
		}
	}
	
	public void displayAttachView(FileBrowserItem attachment,boolean isAttachment, boolean isEditMode) {
		// update the main content by replacing fragments
		if(isEditMode){
		if (getCurrentEditComplaint() != null) {
			if (attachment != null) {
				getCurrentEditComplaint().getAttachments().add(attachment);
				getCurrentEditComplaint().getAttachmentCount().setText(
						String.valueOf(getCurrentEditComplaint().getAttachments().size()));
			}
		}
		}else {
			if (getCurrentAddComplaint() != null) {
				if (attachment != null) {
					getCurrentAddComplaint().getAttachments().add(attachment);
					getCurrentAddComplaint().getAttachmentCount().setText(
					String.valueOf(getCurrentAddComplaint().getAttachments().size()));
				}
			}
		}
			new Handler().post(new Runnable() {
				public void run() {
					android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
					fm.beginTransaction()
							.replace(R.id.frame_container,
									(Fragment) getCurrentAttachmentView()).commit();
					setTitle(navigationMap.get(ATTACHMENT_VIEW));
				}
			});
		
	}
//	/**
//	 * Diplaying fragment view for selected nav drawer list item
//	 * */
	public void displayTextContainerView(int titleIndex, String pageId) {
		// update the main content by replacing fragments
		Object fragment = new TextContainerView(this, pageId);
		if (fragment != null) {
			android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
			fm.beginTransaction()
					.replace(R.id.frame_container, (Fragment) fragment)
					.commit();
			// update selected item and title, then close the drawer
//			mDrawerList.setItemChecked(ADD_COMPLAINT_VIEW, true);
//			mDrawerList.setSelection(ADD_COMPLAINT_VIEW);
			setTitle(navigationMap.get(titleIndex));
		}
	}
	
	public void displayMyComplaintDetails(Complaint complaint) {
		// update the main content by replacing fragments
		Object fragment = new MyComplaintDetailsView(this, complaint);
		if (fragment != null) {
			android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
			fm.beginTransaction()
					.replace(R.id.frame_container, (Fragment) fragment)
					.commit();
			setTitle(navigationMap.get(MY_COMPLAINT_DETAILS_VIEW));
		}
	}
	
	public void displaySavedComplaintDetails(Complaint complaint) {
		// update the main content by replacing fragments
		Object fragment = new SavedComplaintDetailsView(this, complaint);
		if (fragment != null) {
			android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
			fm.beginTransaction()
					.replace(R.id.frame_container, (Fragment) fragment)
					.commit();
			setTitle(navigationMap.get(MY_COMPLAINT_DETAILS_VIEW));
		}
	}
	
	public void displayEditComplaintView(Complaint complaint) {
		// update the main content by replacing fragments
		Object fragment = new EditComplaintView(this, complaint);
		setCurrentEditComplaint((EditComplaintView) fragment);
		setCurrentAttachmentView(null);
		setCurrentAddComplaint(null);
		if (fragment != null) {
			android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
			fm.beginTransaction()
					.replace(R.id.frame_container, (Fragment) fragment)
					.commit();
			setTitle(navigationMap.get(EDIT_COMPLAINT_VIEW));
		}
	}

	public void displayCameraView(AddNewComplaintView parentScreen,
			AttachmentsView attachmentView, boolean isVideoMode) {
		// update the main content by replacing fragments
		// show camera view with out launching camera activity
		// Object fragment = new CameriaPreviewViewView(this, parentScreen,
		// isVideoMode);
		// if (fragment != null) {
		// android.support.v4.app.FragmentManager fm =
		// getSupportFragmentManager();
		// fm.beginTransaction()
		// .replace(R.id.frame_container, (Fragment)fragment).commit();
		//
		// // update selected item and title, then close the drawer
		// mDrawerList.setItemChecked(ADD_COMPLAINT_VIEW, true);
		// mDrawerList.setSelection(ADD_COMPLAINT_VIEW);
		// if(isVideoMode){
		// setTitle(navigationMap.get(CAPTURE_VIDEO_VIEW));
		// }else{
		// setTitle(navigationMap.get(CAPTURE_PHOTO_VIEW));
		// }
		// }
		// launch camera activity
		setCurrentAddComplaint(parentScreen);
		if (attachmentView != null) {
			setCurrentAttachmentView(attachmentView);
		}
		if (!isVideoMode) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			currentMediaURI = FileStorageManger.getOutputMediaFileUri(
					FileStorageManger.MEDIA_TYPE_IMAGE, "temp"); 
			intent.putExtra(MediaStore.EXTRA_OUTPUT, currentMediaURI); 
			startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		} else {
			Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			// create a file to save the video
			currentMediaURI = FileStorageManger.getOutputMediaFileUri(
					FileStorageManger.MEDIA_TYPE_VIDEO, "temp"); 
			// set the image file name
			intent.putExtra(MediaStore.EXTRA_OUTPUT, currentMediaURI); 
			// set the video image quality to high
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); 
			// start the Video Capture Intent
			startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
		}
	}
	
	public void displayCameraViewInEditMode(EditComplaintView parentScreen,
			AttachmentsView attachmentView, boolean isVideoMode) {
		setCurrentEditComplaint(parentScreen);
		if (attachmentView != null) {
			setCurrentAttachmentView(attachmentView);
		}
		if (!isVideoMode) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			currentMediaURI = FileStorageManger.getOutputMediaFileUri(
					FileStorageManger.MEDIA_TYPE_IMAGE, "temp"); 
			intent.putExtra(MediaStore.EXTRA_OUTPUT, currentMediaURI); 
			startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		} else {
			Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			// create a file to save the video
			currentMediaURI = FileStorageManger.getOutputMediaFileUri(
					FileStorageManger.MEDIA_TYPE_VIDEO, "temp"); 
			// set the image file name
			intent.putExtra(MediaStore.EXTRA_OUTPUT, currentMediaURI); 
			// set the video image quality to high
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); 
			// start the Video Capture Intent
			startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
		}
	}
	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	public void displayAddNewComplaintView(
			final AddNewComplaintView parentScreen, FileBrowserItem attachment) {
		// update the main content by replacing fragments
		setCurrentAttachmentView(null);
		if (parentScreen != null) {
			if (attachment != null) {
				parentScreen.getAttachments().add(attachment);
				parentScreen.getAttachmentCount().setText(
						String.valueOf(parentScreen.getAttachments().size()));
			}
			new Handler().post(new Runnable() {
				public void run() {
					android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
					fm.beginTransaction()
							.replace(R.id.frame_container,
									(Fragment) parentScreen).commit();

					// update selected item and title, then close the drawer
					mDrawerList.setItemChecked(ADD_COMPLAINT_VIEW, true);
					mDrawerList.setSelection(ADD_COMPLAINT_VIEW);
					setTitle(navigationMap.get(ADD_COMPLAINT_VIEW));
				}
			});

		}
	}
	public void displayEditComplaintView(
			final EditComplaintView parentScreen, FileBrowserItem attachment) {
		// update the main content by replacing fragments
		setCurrentAttachmentView(null);
		if (parentScreen != null) {
			if (attachment != null) {
				getCurrentEditComplaint().getAttachments().add(attachment);
				getCurrentEditComplaint().getAttachmentCount().setText(
						String.valueOf(getCurrentEditComplaint().getAttachments().size()));
			}
			new Handler().post(new Runnable() {
				public void run() {
					android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
					fm.beginTransaction()
							.replace(R.id.frame_container,
									(Fragment) parentScreen).commit();

					// update selected item and title, then close the drawer
//					mDrawerList.setItemChecked(ADD_COMPLAINT_VIEW, true);
//					mDrawerList.setSelection(ADD_COMPLAINT_VIEW);
					setTitle(navigationMap.get(EDIT_COMPLAINT_VIEW));
				}
			});

		}
	}

	private void showTargetFragment(int position, Object fragment) {
		{
			android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
			fm.beginTransaction()
					.replace(R.id.frame_container, (Fragment) fragment)
					.commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navigationMap.get(position));
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		vTitle.setText(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// Image captured and saved to fileUri specified in the Intent
				try{
					if(currentMediaURI != null){
					String path = com.ebttikarat.complaints.util.FileUtils.getRealImagePathFromURI(this, currentMediaURI) ;
					File ff = new File(path);
					byte[] fdata = FileUtils.readFileToByteArray(ff);
					String name = com.ebttikarat.complaints.util.FileUtils.getFileName(path);

					FileBrowserItem cupturedImage = new FileBrowserItem(name, "", "",
						path, "", fdata);
					if(getCurrentAttachmentView() != null && getCurrentEditComplaint() == null){
						getCurrentAddComplaint().getAttachments().add(cupturedImage);
						displayAttachmentView(getCurrentAddComplaint(), false);
					}else if(getCurrentAttachmentView() != null && getCurrentEditComplaint() != null){
						getCurrentEditComplaint().getAttachments().add(cupturedImage);
						displayAttachmentView(getCurrentAddComplaint(), true);
					}else if(getCurrentAttachmentView() == null && getCurrentEditComplaint() != null && getCurrentAddComplaint() == null){
						displayEditComplaintView(getCurrentEditComplaint(), cupturedImage);
					}else if(getCurrentAttachmentView() == null && getCurrentEditComplaint() == null && getCurrentAddComplaint() != null){
						displayAddNewComplaintView(getCurrentAddComplaint(), cupturedImage);
					}
					}
					}
					catch(Exception ex){
					}
			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
				if(getCurrentAttachmentView() != null && getCurrentEditComplaint() == null){
					displayAttachmentView(getCurrentAddComplaint(), false);
				}else if(getCurrentAttachmentView() != null && getCurrentEditComplaint() != null){
					displayAttachmentView(getCurrentAddComplaint(), true);
				}else if(getCurrentAttachmentView() == null && getCurrentEditComplaint() != null && getCurrentAddComplaint() == null){
					displayEditComplaintView(getCurrentEditComplaint(), null);
				}else if(getCurrentAttachmentView() == null && getCurrentEditComplaint() == null && getCurrentAddComplaint() != null){
					displayAddNewComplaintView(getCurrentAddComplaint(), null);
				}
			} else {
				// Image capture failed, advise user
				if(getCurrentAttachmentView() != null && getCurrentEditComplaint() == null){
					displayAttachmentView(getCurrentAddComplaint(), false);
				}else if(getCurrentAttachmentView() != null && getCurrentEditComplaint() != null){
					displayAttachmentView(getCurrentAddComplaint(), true);
				}else if(getCurrentAttachmentView() == null && getCurrentEditComplaint() != null && getCurrentAddComplaint() == null){
					displayEditComplaintView(getCurrentEditComplaint(), null);
				}else if(getCurrentAttachmentView() == null && getCurrentEditComplaint() == null && getCurrentAddComplaint() != null){
					displayAddNewComplaintView(getCurrentAddComplaint(), null);
				}
			}
		}

		if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// Video captured and saved to fileUri specified in the Intent
				try{
					if(currentMediaURI != null){
					String path = com.ebttikarat.complaints.util.FileUtils.getRealVideoPathFromURI(this, currentMediaURI) ;
					File ff = new File(path);
					byte[] fdata = FileUtils.readFileToByteArray(ff);
					String name = com.ebttikarat.complaints.util.FileUtils.getFileName(path);

					FileBrowserItem cupturedImage = new FileBrowserItem(name, "", "",
							path, "", fdata);
					cupturedImage.setVideo(true);
					if(getCurrentAttachmentView() != null && getCurrentEditComplaint() == null){
						getCurrentAddComplaint().getAttachments().add(cupturedImage);
						displayAttachmentView(getCurrentAddComplaint(), false);
					}else if(getCurrentAttachmentView() != null && getCurrentEditComplaint() != null){
						getCurrentEditComplaint().getAttachments().add(cupturedImage);
						displayAttachmentView(getCurrentAddComplaint(), true);
					}else if(getCurrentAttachmentView() == null && getCurrentEditComplaint() != null && getCurrentAddComplaint() == null){
						displayEditComplaintView(getCurrentEditComplaint(), cupturedImage);
					}else if(getCurrentAttachmentView() == null && getCurrentEditComplaint() == null && getCurrentAddComplaint() != null){
						displayAddNewComplaintView(getCurrentAddComplaint(), cupturedImage);
					}
					}
				    }
					catch(Exception ex){
						Log.i("", ex.getMessage());
					}
			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the video capture
				if(getCurrentAttachmentView() != null && getCurrentEditComplaint() == null){
					displayAttachmentView(getCurrentAddComplaint(), false);
				}else if(getCurrentAttachmentView() != null && getCurrentEditComplaint() != null){
					displayAttachmentView(getCurrentAddComplaint(), true);
				}else if(getCurrentAttachmentView() == null && getCurrentEditComplaint() != null && getCurrentAddComplaint() == null){
					displayEditComplaintView(getCurrentEditComplaint(), null);
				}else if(getCurrentAttachmentView() == null && getCurrentEditComplaint() == null && getCurrentAddComplaint() != null){
					displayAddNewComplaintView(getCurrentAddComplaint(), null);
				}
			} else {
				// Video capture failed, advise user
				if(getCurrentAttachmentView() != null && getCurrentEditComplaint() == null){
					displayAttachmentView(getCurrentAddComplaint(), false);
				}else if(getCurrentAttachmentView() != null && getCurrentEditComplaint() != null){
					displayAttachmentView(getCurrentAddComplaint(), true);
				}else if(getCurrentAttachmentView() == null && getCurrentEditComplaint() != null && getCurrentAddComplaint() == null){
					displayEditComplaintView(getCurrentEditComplaint(), null);
				}else if(getCurrentAttachmentView() == null && getCurrentEditComplaint() == null && getCurrentAddComplaint() != null){
					displayAddNewComplaintView(getCurrentAddComplaint(), null);
				}
			}
		}
	}

	public AddNewComplaintView getCurrentAddComplaint() {
		return currentAddComplaint;
	}

	public void setCurrentAddComplaint(AddNewComplaintView currentAddComplaint) {
		this.currentAddComplaint = currentAddComplaint;
	}

	public AttachmentsView getCurrentAttachmentView() {
		return currentAttachmentView;
	}

	public void setCurrentAttachmentView(AttachmentsView currentAttachmentView) {
		this.currentAttachmentView = currentAttachmentView;
	}

	public EditComplaintView getCurrentEditComplaint() {
		return currentEditComplaint;
	}

	public void setCurrentEditComplaint(EditComplaintView currentEditComplaint) {
		this.currentEditComplaint = currentEditComplaint;
	}

	public MyComplaintDetailsView getMyCOmplaintDetailsView() {
		return myCOmplaintDetailsView;
	}

	public void setMyCOmplaintDetailsView(MyComplaintDetailsView myCOmplaintDetailsView) {
		this.myCOmplaintDetailsView = myCOmplaintDetailsView;
	}

//	public Complaint getEditCOmplain() {
//		return editCOmplain;
//	}
//
//	public void setEditCOmplain(Complaint editCOmplain) {
//		this.editCOmplain = editCOmplain;
//	}

}
