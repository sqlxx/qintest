package ind.sq.android.todo;

import ind.sq.android.todo.dbhelper.ActivityDB;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ToDoActivity extends Activity {
	private ListView activityList;
	private EditText inputEdit;
	private Cursor myCursor;
	private ActivityDB db;
	private int id = -1;
	
	private static final int MENU_ADD = Menu.FIRST;
	private static final int MENU_EDIT = Menu.FIRST +1;
	private static final int MENU_DEL = Menu.FIRST + 2;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent notifyIntent = new Intent(this, ToDoActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notifyIntent, 0);
        Notification noti = new Notification();
        noti.tickerText = "hellow";
        noti.defaults = 0;
        noti.icon = android.R.drawable.ic_secure;
        noti.setLatestEventInfo(this, "状态", "任务列表", contentIntent);
        nm.notify(0, noti);
        
        activityList = (ListView) findViewById(R.id.activityList);
        inputEdit = (EditText) findViewById(R.id.editText);
        
        db = new ActivityDB(this);
        myCursor = db.select();
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, R.layout.list, myCursor, new String[] {ActivityDB.FIELD_TEXT}, new int[]{R.id.listTextView});
        activityList.setAdapter(sca);
        activityList.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				Toast.makeText(ToDoActivity.this, "Item clicked", Toast.LENGTH_SHORT).show();
				setSelected(position);
			}
        
        });
        
        activityList.setOnItemSelectedListener(new ListView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//				Toast.makeText(ToDoActivity.this, "Item selected", Toast.LENGTH_SHORT).show();
				setSelected(position);				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				id = 0;
				
			}
        	
        });
    }
    
    private void setSelected(int position) {
    	myCursor.moveToPosition(position);
    	id = myCursor.getInt(0);
    	inputEdit.setText(myCursor.getString(1));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	super.onOptionsItemSelected(item);
    	
    	switch(item.getItemId()) {
    	case MENU_ADD: addActivity(); break;
    	case MENU_EDIT: editActivity(); break;
    	case MENU_DEL: delActivity(); break;
    	}
    	
    	return true;
    }
    
    private void delActivity() {
		if (id == -1) {
			Toast.makeText(this, "请选择一个事项", Toast.LENGTH_LONG).show();
		} else {
			db.delete(id);
			myCursor.requery();
			activityList.invalidateViews();
			inputEdit.setText("");
			id = -1;
		}
		
	}
	private void editActivity() {
		if (id == -1){
			Toast.makeText(this, "请选择一个事项", Toast.LENGTH_LONG).show();
		} else if (inputEdit.getText().toString().trim().length() == 0) {
			Toast.makeText(this, "请输入新的事项名称", Toast.LENGTH_LONG).show();
		} else {
			db.update(id, inputEdit.getText().toString());
			myCursor.requery();
			activityList.invalidateViews();
			inputEdit.setText("");
			id = -1;
		}
		
	}
	private void addActivity() {
		if (inputEdit.getText().toString().trim().length() == 0) {
			Toast.makeText(this, "请输入事项名称", Toast.LENGTH_LONG).show();
		} else {
			db.insert(inputEdit.getText().toString());
			myCursor.requery();
			activityList.invalidateViews();
//			activityList.setSelection(id);
			inputEdit.setText("");
			id = -1;
		}
		
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	menu.add(Menu.NONE, MENU_ADD, 0, R.string.menu_add);
    	menu.add(Menu.NONE, MENU_EDIT, 0, R.string.menu_edit);
    	menu.add(Menu.NONE, MENU_DEL, 0, R.string.menu_del);
    	return true;
    }
}