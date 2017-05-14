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
	public static Button button;//��ʼ��ť
	TextView textViewSybz;//ʹ�ò���
	TextView textViewLjcd;//·������
	public static Handler hd;//��Ϣ������
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mySurfaceView=new MySurfaceView(this);
		LinearLayout ll=(LinearLayout)findViewById(R.id.LinearLayout02);
		ll.addView(mySurfaceView);
		instance=this;
		button=(Button)findViewById(R.id.Button01);
		textViewSybz=(TextView)findViewById(R.id.TextView01);//ͨ��ID���ʹ�ò���TextView
        textViewLjcd=(TextView)findViewById(R.id.TextView02);//ͨ��ID���ʹ�ò���TextView
        Spinner spinnerSearch=(Spinner)findViewById(R.id.Spinner01);//����������������б�
		
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
				ll.setOrientation(LinearLayout.HORIZONTAL);		//���ó���	

				
				//��ʼ��TextView
				TextView tv=new TextView(MainActivity.this);
				tv.setText(" "+getResources().getText(targetId[position]));//��������
				tv.setTextSize(20);//���������С
				tv.setTextColor(Color.BLACK);//���������ɫ
				ll.addView(tv);//��ӵ�LinearLayout��
				
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
				case 1://����ʹ�ò���
			 	case 2:
			 	case 3:
			 	case 4:
			 	case 5:textViewSybz.setText("ʹ�ò�����"+mySurfaceView.game.tempCount);break;
			 	case 6:textViewLjcd.setText("·�����ȣ�"+mySurfaceView.game.LjcdCount);break;//����·������
				}
				
			}
			
		};
		
	}

}
