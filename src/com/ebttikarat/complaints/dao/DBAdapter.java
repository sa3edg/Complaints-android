package com.ebttikarat.complaints.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ebttikarat.complaints.common.model.Attachment;
import com.ebttikarat.complaints.common.model.Complaint;
import com.ebttikarat.complaints.gui.model.PhoneNumber;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class DBAdapter {

	/**
	 * if debug is set true then it will show all Logcat message
	 **/
	public static final boolean DEBUG = true;

	/**
	 * Logcat TAG
	 */
	public static final String LOG_TAG = "DBAdapter";

	/**
	 * Complaint table fields
	 */
	public static final String KEY_COMPLAINT_ID = "COMPLAINT_ID";
	public static final String KEY_COMPLAINT_TITLE = "COMPLAINT_TITLE";
	public static final String KEY_COMPLAINT_STATUS = "COMPLAINT_STATUS";
	public static final String KEY_COMPLAINT_CLOSE_REASON = "COMPLAINT_CLOSE_REASON";
	public static final String KEY_COMPLAINT_CREATION_DATE = "CREATION_DATE";
	public static final String KEY_COMPLAINT_LAST_UPDATE_DATE = "LAST_UPDATE_DATE";
	public static final String KEY_COMPLAINT_CREATION_DATE_HIJRI = "CREATION_DATE_HIJRI";
	public static final String KEY_COMPLAINT_LAST_UPDATE_DATE_HIJRI = "LAST_UPDATE_DATE_HIJRI";
	public static final String KEY_COMPLAINT_DESCRIPTION = "OMPLAINT_DESCRIPTION";
	public static final String KEY_COMPLAINT_LATITUDE = "COMPLAINT_LATITUDE";
	public static final String KEY_COMPLAINT_LONGITUDE = "COMPLAINT_LONGITUDE";
	public static final String KEY_COMPLAINT_LOC_DESC = "COMPLAINT_LOC_DESC";
	public static final String KEY_COMPLAINT_DEST_UNIT = "COMPLAINT_DEST_UNIT";
	public static final String KEY_COMPLAINT_DEST_UNIT_ID = "COMPLAINT_DEST_UNIT_ID";

	/**
	 * attachment table fields
	 */
	public static final String KEY_ATTACHMENT_ID = "ATTACHMENT_ID";
	public static final String KEY_ATTACHMENT_NAME = "ATTACHMENT_NAME";
	public static final String KEY_ATTACHMENT_TYPE = "ATTACHMENT_TYPE";
	public static final String KEY_ATTACHMENT_PATH = "ATTACHMENT_PATH";

	/**
	 * User table fields
	 */
	public static final String MOBILE_NUMBER = "MOBILE_NUMBER";
	public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
	public static final String IS_VERIFIED = "VERIFIED";
	public static final String IS_PREFERIED = "PREFERIED";
	
	/**
	 * Database Name
	 */
	public static final String DATABASE_NAME = "complaints_db_5.db";

	/**
	 * Database Version (Increase one if want to also upgrade your database)
	 */
	public static final int DATABASE_VERSION = 1;// started at 1

	/**
	 * Tables names
	 */
	public static final String SAVED_COMPLAINTS_TABLE = "saved_complaints";
	public static final String COMPLAINTS_ATTACHMENT_TABLE = "complaint_attchment";
	public static final String USER_TABLE = "PHONE_NUMBER";

	/**
	 * Set all table with comma seperated like SAVED_COMPLAINTS_TABLE,ABC_TABLE
	 */
	private static final String[] ALL_TABLES = { SAVED_COMPLAINTS_TABLE,
			COMPLAINTS_ATTACHMENT_TABLE , USER_TABLE};

	/**
	 * Used to open database in syncronized way
	 */
	private static DataBaseHelper DBHelper = null;

	protected DBAdapter() {

	}

	/**
	 * Initialize database
	 */
	public static void init(Context context) {
		if (DBHelper == null) {
			if (DEBUG)
				Log.i("DBAdapter", context.toString());
			DBHelper = new DataBaseHelper(context);
		}
	}

	/**
	 * Main Database creation INNER class
	 */
	private static class DataBaseHelper extends SQLiteOpenHelper {
		public DataBaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			if (DEBUG)
				Log.i(LOG_TAG, "new create");
			try {
				String complaintsCreate = "create table "
						+ SAVED_COMPLAINTS_TABLE + " ( " + KEY_COMPLAINT_ID
						+ " text primary key, " + KEY_COMPLAINT_TITLE
						+ " text," + KEY_COMPLAINT_STATUS + " text,"
						+ KEY_COMPLAINT_CLOSE_REASON + " text,"
						+ KEY_COMPLAINT_CREATION_DATE + " text,"
						+ KEY_COMPLAINT_LAST_UPDATE_DATE + " text,"
						+ KEY_COMPLAINT_CREATION_DATE_HIJRI + " text,"
						+ KEY_COMPLAINT_LAST_UPDATE_DATE_HIJRI + " text,"
						+ KEY_COMPLAINT_DESCRIPTION + " text, "
						+ KEY_COMPLAINT_LATITUDE + " text,"
						+ KEY_COMPLAINT_LONGITUDE + " text,"
						+ KEY_COMPLAINT_LOC_DESC + " text,"
						+ KEY_COMPLAINT_DEST_UNIT + " text,"
						+ KEY_COMPLAINT_DEST_UNIT_ID + " text);";
				String attachmentCreate = "create table "
						+ COMPLAINTS_ATTACHMENT_TABLE + " ( "
						+ KEY_ATTACHMENT_ID
						+ " text primary key, "
						+ KEY_COMPLAINT_ID + " text," + KEY_ATTACHMENT_NAME
						+ " text," + KEY_ATTACHMENT_TYPE + " text,"
						+ KEY_ATTACHMENT_PATH + " text);";
				String userCreate = "create table "
						+ USER_TABLE + " ( "
						+ MOBILE_NUMBER
						+ " text primary key , "
						+ ACCESS_TOKEN + " text," + IS_VERIFIED
						+ " text," + IS_PREFERIED + " text);";
				db.execSQL(complaintsCreate);
				db.execSQL(attachmentCreate);
				db.execSQL(userCreate);

			} catch (Exception exception) {
				if (DEBUG)
					Log.i(LOG_TAG, "Exception onCreate() exception");
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if (DEBUG)
				Log.w(LOG_TAG, "Upgrading database from version" + oldVersion
						+ "to" + newVersion + "...");

			for (String table : ALL_TABLES) {
				db.execSQL("DROP TABLE IF EXISTS " + table);
			}
			onCreate(db);
		}

	} // Inner class closed

	/**
	 * Open database for insert,update,delete in syncronized manner
	 */
	private static synchronized SQLiteDatabase open() throws SQLException {
		return DBHelper.getWritableDatabase();
	}

	/**
	 * Escape string for single quotes (Insert,Update)
	 */
	private static String sqlEscapeString(String aString) {
		String aReturn = "";

		if (null != aString) {
			// aReturn = aString.replace(", );
			aReturn = DatabaseUtils.sqlEscapeString(aString);
			// Remove the enclosing single quotes ...
			aReturn = aReturn.substring(1, aReturn.length() - 1);
		}

		return aReturn;
	}

	public static void addPhoneNumber(PhoneNumber uData) {

		// Open database for Read / Write

		final SQLiteDatabase db = open();

		String pNumber = sqlEscapeString(uData.getPhoneNumber());
		String aToken = sqlEscapeString(uData.getAccessToken());
		String isVerified = sqlEscapeString(uData.getIsVerified());
		String isPrefered = sqlEscapeString(uData.getIsPrefered());
	
		ContentValues cVal = new ContentValues();
		cVal.put(MOBILE_NUMBER, pNumber);
		cVal.put(ACCESS_TOKEN, aToken);
		cVal.put(IS_VERIFIED, isVerified);
		cVal.put(IS_PREFERIED, isPrefered);
		// Insert user values in database
		db.insert(USER_TABLE, null, cVal);
		db.close(); // Closing database connection
	}
	

	// Updating single data
	public static int updatePhoneNumber(PhoneNumber uData) {

		final SQLiteDatabase db = open();

		ContentValues cVal = new ContentValues();
		cVal.put(MOBILE_NUMBER, uData.getPhoneNumber());
		cVal.put(ACCESS_TOKEN, uData.getAccessToken());
		cVal.put(IS_VERIFIED, uData.getIsVerified());
		cVal.put(IS_PREFERIED, uData.getIsPrefered());

		// updating row
		int count =  db.update(USER_TABLE, cVal, MOBILE_NUMBER
				+ " = ?", new String[] { uData.getPhoneNumber() });
		db.close();
		return count;
	}
	
	public static int setPreferedNumber(PhoneNumber uData) {

		final SQLiteDatabase db = open();

		ContentValues cVal = new ContentValues();
		cVal.put(IS_PREFERIED, "0");

		// updating row
		int count =  db.update(USER_TABLE, cVal, MOBILE_NUMBER
				+ " != ?", new String[] { uData.getPhoneNumber() });
		db.close();
		return count;
	}

	// Getting single attachment
	public static PhoneNumber getPhoneNumber(String phoneNumber) {

		// Open database for Read / Write
		final SQLiteDatabase db = open();

		Cursor cursor = db.query(USER_TABLE, new String[] {
				MOBILE_NUMBER, ACCESS_TOKEN, IS_VERIFIED,
				IS_PREFERIED }, MOBILE_NUMBER + "=?",
				new String[] { phoneNumber }, null, null, null, null);

		PhoneNumber data = new PhoneNumber();
		if (cursor != null && cursor.moveToFirst()){
			data.setPhoneNumber(cursor.getString(0));
			data.setAccessToken(cursor.getString(1));
			data.setIsVerified(cursor.getString(2));
			data.setIsPrefered(cursor.getString(3));
			db.close();
		}
		else{
			return null;
		}
		return data;
	}
	public static PhoneNumber getPreferedPhoneNumber() {

		// Open database for Read / Write
		final SQLiteDatabase db = open();

		Cursor cursor = db.query(USER_TABLE, new String[] {
				MOBILE_NUMBER, ACCESS_TOKEN, IS_VERIFIED,
				IS_PREFERIED }, IS_PREFERIED + "=?",
				new String[] { "1" }, null, null, null, null);

		if (cursor != null && cursor.moveToFirst()){
			PhoneNumber data = new PhoneNumber();
			data.setPhoneNumber(cursor.getString(0));
			data.setAccessToken(cursor.getString(1));
			data.setIsVerified(cursor.getString(2));
			data.setIsPrefered(cursor.getString(3));
			db.close();
			return data;
		}else{
			return null;
		}
	}
	public static List<PhoneNumber> getPhoneNumbers() {

		List<PhoneNumber> contactList = new ArrayList<PhoneNumber>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + USER_TABLE + " where " + IS_VERIFIED + "=?";

		String[] args={"1"};
		// Open database for Read / Write
		final SQLiteDatabase db = open();
		Cursor cursor = db.rawQuery(selectQuery, args);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				PhoneNumber data = new PhoneNumber();
				data.setPhoneNumber(cursor.getString(0));
				data.setAccessToken(cursor.getString(1));
				data.setIsVerified(cursor.getString(2));
				data.setIsPrefered(cursor.getString(3));
				
				// Adding complaints to list
				contactList.add(data);
			} while (cursor.moveToNext());
		}

		db.close();
		// return user list
		return contactList;
	}
	public static void addAttachment(Attachment uData) {

		// Open database for Read / Write

		final SQLiteDatabase db = open();

		String id = sqlEscapeString(uData.getFileId());
		String complaintId = sqlEscapeString(uData.getComplaintId());
		String name = sqlEscapeString(uData.getFileName());
		String type = sqlEscapeString(uData.getFileType());
		String path = sqlEscapeString(uData.getFilePath());
	
		ContentValues cVal = new ContentValues();
		cVal.put(KEY_ATTACHMENT_ID, id);
		cVal.put(KEY_COMPLAINT_ID, complaintId);
		cVal.put(KEY_ATTACHMENT_NAME, name);
		cVal.put(KEY_ATTACHMENT_TYPE, type);
		cVal.put(KEY_ATTACHMENT_PATH, path);
		// Insert user values in database
		db.insert(COMPLAINTS_ATTACHMENT_TABLE, null, cVal);
		db.close(); // Closing database connection
	}
	

	// Updating single data
	public static int updateAttachment(Attachment attachment) {

		final SQLiteDatabase db = open();

		ContentValues cVal = new ContentValues();
		cVal.put(KEY_ATTACHMENT_ID, attachment.getFileId());
		cVal.put(KEY_COMPLAINT_ID, attachment.getComplaintId());
		cVal.put(KEY_ATTACHMENT_NAME, attachment.getFileName());
		cVal.put(KEY_ATTACHMENT_TYPE, attachment.getFileType());
		cVal.put(KEY_ATTACHMENT_PATH, attachment.getFilePath());

		// updating row
		int count = db.update(COMPLAINTS_ATTACHMENT_TABLE, cVal, KEY_ATTACHMENT_ID
				+ " = ?", new String[] { attachment.getFileId() });
		db.close();
		return count;
	}

	// Getting single attachment
	public static Attachment getAttachment(String attachmentId) {

		// Open database for Read / Write
		final SQLiteDatabase db = open();

		Cursor cursor = db.query(COMPLAINTS_ATTACHMENT_TABLE, new String[] {
				KEY_ATTACHMENT_ID, KEY_COMPLAINT_ID, KEY_ATTACHMENT_NAME,
				KEY_ATTACHMENT_TYPE, KEY_ATTACHMENT_PATH }, KEY_ATTACHMENT_ID + "=?",
				new String[] { attachmentId }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		Attachment data = new Attachment();
		data.setFileId(cursor.getString(0));
		data.setComplaintId(cursor.getString(1));
		data.setFileName(cursor.getString(2));
		data.setFileType(cursor.getString(3));
		data.setFilePath(cursor.getString(4));
		db.close();
		return data;
	}

	// Getting All User data
	public static List<Attachment> getComplaintAttachment(String complaintId) {

		List<Attachment> contactList = new ArrayList<Attachment>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + COMPLAINTS_ATTACHMENT_TABLE + " where " + KEY_COMPLAINT_ID + "=?";

		String[] args={complaintId};
		// Open database for Read / Write
		final SQLiteDatabase db = open();
		Cursor cursor = db.rawQuery(selectQuery, args);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Attachment data = new Attachment();
				data.setFileId(cursor.getString(0));
				data.setComplaintId(cursor.getString(1));
				data.setFileName(cursor.getString(2));
				data.setFileType(cursor.getString(3));
				data.setFilePath(cursor.getString(4));

				// Adding complaints to list
				contactList.add(data);
			} while (cursor.moveToNext());
		}
		db.close();
		// return user list
		return contactList;
	}

	// Deleting single contact
	public static void deleteAttachment(Attachment data) {
		final SQLiteDatabase db = open();
		db.delete(COMPLAINTS_ATTACHMENT_TABLE, KEY_ATTACHMENT_ID + " = ?",
				new String[] { data.getFileId() });
		db.close();
	}

	public static void addComplaintWIthAttachment(Complaint uData) {
		addComplaint(uData);
		for(Attachment file : uData.getFiles()){
			file.setComplaintId(uData.getId());
			addAttachment(file);
		}
		
	}
	public static void updateComplaintWIthAttachment(Complaint uData) {
		updateComplaint(uData);
//		deleteComplaint(uData);
		deleteComplaintAttachment(uData);
		//deleteComplaintAttachment(uData);
		//addComplaintWIthAttachment(uData);
		for(Attachment file : uData.getFiles()){
			file.setComplaintId(uData.getId());
			addAttachment(file);
		}
		
	}
	public static void addComplaint(Complaint uData) {

		// Open database for Read / Write

		final SQLiteDatabase db = open();

		String id = sqlEscapeString(uData.getId());
		String title = sqlEscapeString(uData.getTitle());
		String status = sqlEscapeString(uData.getStatus());
		String closeReason = sqlEscapeString(uData.getCloseReason());
		String creationDate = sqlEscapeString(uData.getCreationDate());
		String lastUpdateDate = sqlEscapeString(uData.getLastUpdateDate());
		String creationDateHijri = sqlEscapeString(uData.getCreationDateHijri());
		String lastUpdateDateHijri = sqlEscapeString(uData
				.getLastUpdateDateHijri());
		String description = sqlEscapeString(uData.getDescription());
		String latitude = sqlEscapeString(uData.getLatitude());
		String longitude = sqlEscapeString(uData.getLongitude());
		String locationDescription = sqlEscapeString(uData
				.getLocationDescription());
		String destinationUnit = sqlEscapeString(uData.getDestinationUnit());
		String destinationUnitId = sqlEscapeString(uData.getUnitId());
		ContentValues cVal = new ContentValues();
		cVal.put(KEY_COMPLAINT_ID, id);
		cVal.put(KEY_COMPLAINT_TITLE, title);
		cVal.put(KEY_COMPLAINT_STATUS, status);
		cVal.put(KEY_COMPLAINT_CLOSE_REASON, closeReason);
		cVal.put(KEY_COMPLAINT_CREATION_DATE, creationDate);
		cVal.put(KEY_COMPLAINT_LAST_UPDATE_DATE, lastUpdateDate);
		cVal.put(KEY_COMPLAINT_CREATION_DATE_HIJRI, creationDateHijri);
		cVal.put(KEY_COMPLAINT_LAST_UPDATE_DATE_HIJRI, lastUpdateDateHijri);
		cVal.put(KEY_COMPLAINT_DESCRIPTION, description);
		cVal.put(KEY_COMPLAINT_LATITUDE, latitude);
		cVal.put(KEY_COMPLAINT_LONGITUDE, longitude);
		cVal.put(KEY_COMPLAINT_LOC_DESC, locationDescription);
		cVal.put(KEY_COMPLAINT_DEST_UNIT, destinationUnit);
		cVal.put(KEY_COMPLAINT_DEST_UNIT_ID, destinationUnitId);
		// Insert user values in database
		db.insert(SAVED_COMPLAINTS_TABLE, null, cVal);
		db.close(); // Closing database connection
	}

	// Updating single data
	public static int updateComplaint(Complaint complaint) {

		final SQLiteDatabase db = open();

		ContentValues cVal = new ContentValues();
		cVal.put(KEY_COMPLAINT_ID, complaint.getId());
		cVal.put(KEY_COMPLAINT_TITLE, complaint.getTitle());
		cVal.put(KEY_COMPLAINT_STATUS, complaint.getStatus());
		cVal.put(KEY_COMPLAINT_CLOSE_REASON, complaint.getCloseReason());
		cVal.put(KEY_COMPLAINT_CREATION_DATE, complaint.getCreationDate());
		cVal.put(KEY_COMPLAINT_LAST_UPDATE_DATE, complaint.getLastUpdateDate());
		cVal.put(KEY_COMPLAINT_CREATION_DATE_HIJRI,
				complaint.getCreationDateHijri());
		cVal.put(KEY_COMPLAINT_LAST_UPDATE_DATE_HIJRI,
				complaint.getLastUpdateDateHijri());
		cVal.put(KEY_COMPLAINT_DESCRIPTION, complaint.getDescription());
		cVal.put(KEY_COMPLAINT_LATITUDE, complaint.getLatitude());
		cVal.put(KEY_COMPLAINT_LONGITUDE, complaint.getLongitude());
		cVal.put(KEY_COMPLAINT_LOC_DESC, complaint.getLocationDescription());
		cVal.put(KEY_COMPLAINT_DEST_UNIT, complaint.getDestinationUnit());
		cVal.put(KEY_COMPLAINT_DEST_UNIT_ID, complaint.getUnitId());

		// updating row
		int count =  db.update(SAVED_COMPLAINTS_TABLE, cVal, KEY_COMPLAINT_ID
				+ " = ?", new String[] { complaint.getId() });
		db.close();
		return count;
	}

	// Getting single contact
	public static Complaint getComplaint(int id) {

		// Open database for Read / Write
		final SQLiteDatabase db = open();

		Cursor cursor = db.query(SAVED_COMPLAINTS_TABLE, new String[] {
				KEY_COMPLAINT_ID, KEY_COMPLAINT_TITLE, KEY_COMPLAINT_STATUS,
				KEY_COMPLAINT_CLOSE_REASON, KEY_COMPLAINT_CREATION_DATE,
				KEY_COMPLAINT_LAST_UPDATE_DATE,
				KEY_COMPLAINT_CREATION_DATE_HIJRI,
				KEY_COMPLAINT_LAST_UPDATE_DATE_HIJRI,
				KEY_COMPLAINT_DESCRIPTION, KEY_COMPLAINT_LATITUDE,
				KEY_COMPLAINT_LONGITUDE, KEY_COMPLAINT_LOC_DESC,
				KEY_COMPLAINT_DEST_UNIT, KEY_COMPLAINT_DEST_UNIT_ID }, KEY_COMPLAINT_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		Complaint data = new Complaint();
		data.setId( cursor.getString(0));
		data.setTitle( cursor.getString(1));
		data.setStatus( cursor.getString(2));
		data.setCloseReason( cursor.getString(3));
		data.setCreationDate( cursor.getString(4));
		data.setLastUpdateDate( cursor.getString(5));
		data.setCreationDateHijri( cursor.getString(6));
		data.setLastUpdateDateHijri( cursor.getString(7));
		data.setDescription( cursor.getString(8));
		data.setLatitude( cursor.getString(9));
		data.setLongitude( cursor.getString(10));
		data.setLocationDescription( cursor.getString(11));
		data.setDestinationUnit( cursor.getString(12));
		data.setUnitId( cursor.getString(13));
		db.close();
		return data;
	}

	// Getting All User data
	public static List<Complaint> getAllComplaint() {

		List<Complaint> contactList = new ArrayList<Complaint>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + SAVED_COMPLAINTS_TABLE;

		// Open database for Read / Write
		final SQLiteDatabase db = open();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Complaint data = new Complaint();
				data.setId( cursor.getString(0));
				data.setTitle( cursor.getString(1));
				data.setStatus( cursor.getString(2));
				data.setCloseReason( cursor.getString(3));
				data.setCreationDate( cursor.getString(4));
				data.setLastUpdateDate( cursor.getString(5));
				data.setCreationDateHijri( cursor.getString(6));
				data.setLastUpdateDateHijri( cursor.getString(7));
				data.setDescription( cursor.getString(8));
				data.setLatitude( cursor.getString(9));
				data.setLongitude( cursor.getString(10));
				data.setLocationDescription( cursor.getString(11));
				data.setDestinationUnit( cursor.getString(12));
				data.setUnitId( cursor.getString(13));

				// Adding complaints to list
				contactList.add(data);
			} while (cursor.moveToNext());
		}
		db.close();
		// return user list
		Collections.reverse(contactList);
		return contactList;
	}

	// Deleting single contact
	public static void deleteComplaint(Complaint data) {
		final SQLiteDatabase db = open();
		db.delete(SAVED_COMPLAINTS_TABLE, KEY_COMPLAINT_ID + " = ?",
				new String[] { data.getId() });
		db.close();
	}
	public static void deleteComplaintAttachment(Complaint data) {
		final SQLiteDatabase db = open();
		db.delete(COMPLAINTS_ATTACHMENT_TABLE, KEY_COMPLAINT_ID + " = ?",
				new String[] { data.getId() });
		db.close();
	}

	// Getting dataCount

	public static int getComplaintCount() {

		final SQLiteDatabase db = open();

		String countQuery = "SELECT  * FROM " + SAVED_COMPLAINTS_TABLE;
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		db.close();

		// return count
		return cursor.getCount();
	}

	public static Bitmap getImage(int i) {

		final SQLiteDatabase db = open();
		String qu = "select img  from table where feedid=" + i;
		Cursor cur = db.rawQuery(qu, null);

		if (cur.moveToFirst()) {
			byte[] imgByte = cur.getBlob(0);
			cur.close();
			db.close();
			return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
		}
		if (cur != null && !cur.isClosed()) {
			cur.close();
		}
		db.close();
		return null;
	}

} // CLASS CLOSED
