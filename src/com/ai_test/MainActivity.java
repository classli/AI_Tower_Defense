package com.ai_test;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

	int [] searchMsgId={
			R.string.depthFirstSearch,R.string.breadthFirstSearch,
			R.string.breadthFirstSearchA,R.string.Dijkstra,
			R.string.DijkstraA
	};
	
	int [] targetId={
		R.string.tA,
		R.string.tB,
		R.string.tC,
		R.string.tD,
		R.string.tE,
	};
	static MainActivity instance;
	MySurfaceView mySurfaceView;
	public static Button button;//开始按钮
	TextView textViewSybz;//使用步数
	TextView textViewLjcd;//路径长度
	public static Handler hd;//消息处理器
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mySurfaceView=new MySurfaceView(this);
		LinearLayout ll=(LinearLayout)findViewById(R.id.LinearLayout02);
		ll.addView(mySurfaceView);
		instance=this;
		button=(Button)findViewById(R.id.Button01);
		textViewSybz=(TextView)findViewById(R.id.TextView01);//通过ID获得使用步骤TextView
        textViewLjcd=(TextView)findViewById(R.id.TextView02);//通过ID获得使用步骤TextView
        Spinner spinnerSearch=(Spinner)findViewById(R.id.Spinner01);//获得搜索方法下拉列表
		
        BaseAdapter ba=new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				LinearLayout ll=new LinearLayout(MainActivity.this);
				ll.setOrientation(LinearLayout.HORIZONTAL);
				
				TextView tx=new TextView(MainActivity.this);
				tx.setText(" "+getResources().getText(searchMsgId[position]));
				tx.setTextSize(20);
				tx.setTextColor(Color.BLACK);
				ll.addView(tx);
				return ll;
			}
			
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 5;
			}
		};
		
		spinnerSearch.setAdapter(ba);
		spinnerSearch.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mySurfaceView.game.algorithmId=position;
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		Spinner spinnerTarget=(Spinner)findViewById(R.id.Spinner02);
		
		BaseAdapter bas=new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				LinearLayout ll=new LinearLayout(MainActivity.this);
				ll.setOrientation(LinearLayout.HORIZONTAL);		//设置朝向	

				
				//初始化TextView
				TextView tv=new TextView(MainActivity.this);
				tv.setText(" "+getResources().getText(targetId[position]));//设置内容
				tv.setTextSize(20);//设置字体大小
				tv.setTextColor(Color.BLACK);//设置字体黑色
				ll.addView(tv);//添加到LinearLayout中
				
				return ll;
			}
			
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 5;
			}
		};
		
		spinnerTarget.setAdapter(bas);
		spinnerTarget.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mySurfaceView.game.target=MapList.targetA[position];
				mySurfaceView.game.clearInit();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO button-generated method stub
				mySurfaceView.game.logic();
				button.setClickable(false);
				
			}
		});
		
		hd=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Handler-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 1://更改使用步数
			 	case 2:
			 	case 3:
			 	case 4:
			 	case 5:textViewSybz.setText("使用步数："+mySurfaceView.game.tempCount);break;
			 	case 6:textViewLjcd.setText("路径长度："+mySurfaceView.game.LjcdCount);break;//更改路径长度
				}
				
			}
			
		};
		
	}

}
