package com.ebttikarat.complaints.gui.view;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList; 
import java.util.Collections;
import java.util.List;
import java.text.DateFormat; 

import org.apache.commons.io.FileUtils;

import com.ebttikarat.complaints.gui.R;
import com.ebttikarat.complaints.gui.adapter.FileArrayAdapter;
import com.ebttikarat.complaints.gui.model.FileBrowserItem;

import android.os.Bundle; 
import android.os.Environment;
import android.graphics.Color;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView; 
import android.widget.AdapterView.OnItemLongClickListener;

public class FileChooserView extends ListFragment {

	private File currentDir;
    private FileArrayAdapter adapter;
    private MainView mView;
    private boolean isAttachment;
    private boolean isEditMode;
    public FileChooserView(MainView mView, boolean isAttachment, boolean isEditMode){
    	this.mView = mView;
    	this.isAttachment = isAttachment;
    	this.isEditMode = isEditMode;
    }
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);

	}
    
    @Override
	public void onStart() {
		super.onStart();
		/** Setting the multiselect choice mode for the listview */
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		getListView().setCacheColorHint(Color.TRANSPARENT);
	}
    @Override
	public void onActivityCreated(Bundle savedState) {
    	super.onActivityCreated(savedState);
        currentDir = Environment.getExternalStorageDirectory();
        fill(currentDir);
        getListView().setOnItemClickListener(
				new ListView.OnItemClickListener() {
					// @Override
					public void onItemClick(AdapterView<?> a, View v, int i,
							long l) {
						// do click action
						FileBrowserItem o = adapter.getItem(i);
						if(o.getImage().equalsIgnoreCase("directory_icon")||o.getImage().equalsIgnoreCase("directory_up")){
								currentDir = new File(o.getPath());
								fill(currentDir);
						}
						else
						{
							onFileClick(o);
						}
					}
				});

		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			// @Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// edit event here

				return true;
			}
		});
    }
    private void fill(File f)
    {
    	File[]dirs = f.listFiles(); 
		 //this.setTitle("Current Dir: "+f.getName());
		 List<FileBrowserItem>dir = new ArrayList<FileBrowserItem>();
		 List<FileBrowserItem>fls = new ArrayList<FileBrowserItem>();
		 try{
			 for(File ff: dirs)
			 { 
				Date lastModDate = new Date(ff.lastModified()); 
				DateFormat formater = DateFormat.getDateTimeInstance();
				String date_modify = formater.format(lastModDate);
				if(ff.isDirectory()){
					
					
					File[] fbuf = ff.listFiles(); 
					int buf = 0;
					if(fbuf != null){ 
						buf = fbuf.length;
					} 
					else buf = 0; 
					String num_item = String.valueOf(buf);
					if(buf == 0) num_item = num_item + " item";
					else num_item = num_item + " items";
					
					//String formated = lastModDate.toString();
					dir.add(new FileBrowserItem(ff.getName(),num_item,date_modify,ff.getAbsolutePath(),"directory_icon", null)); 
				}
				else
				{
					byte[] data = FileUtils.readFileToByteArray(ff);
					if(com.ebttikarat.complaints.util.FileUtils.isImage(ff.getAbsolutePath()) || com.ebttikarat.complaints.util.FileUtils.isVideo(ff.getAbsolutePath()))
					{
						fls.add(new FileBrowserItem(ff.getName(),ff.length() + " Byte", date_modify, ff.getAbsolutePath(),"file_icon", data));
					}
				}
			 }
		 }catch(Exception e)
		 {    
			 
		 }
		 Collections.sort(dir);
		 Collections.sort(fls);
		 dir.addAll(fls);
		 if(!f.getName().equalsIgnoreCase(Environment.getExternalStorageDirectory().getName())){
			 dir.add(0,new FileBrowserItem("..","Parent Directory","",f.getParent(),"directory_up", null));
		 }
		 adapter = new FileArrayAdapter(mView,R.layout.file_view,dir);
		 this.setListAdapter(adapter); 
    }
   
    private void onFileClick(FileBrowserItem o)
    {
    	if(com.ebttikarat.complaints.util.FileUtils.isVideo(o.getPath())){
    		o.setVideo(true);
    	}
    	if(isAttachment){
    		mView.displayAttachView(o,isAttachment, isEditMode);
    		
    	}else if(isEditMode){
    		mView.displayEditComplaintView(mView.getCurrentEditComplaint(), o);
    	}else{
    		mView.displayAddNewComplaintView(mView.getCurrentAddComplaint(), o);
    	}
    }
}
