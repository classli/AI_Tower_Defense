package com.ai_test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.Toast;

public class GameLogic{
	
	private int [][] map=MapList.map[0];//��Ҫ�����ĵ�ͼ
	public int [] source=MapList.source;
	public int [] target=MapList.targetA[0];
	public int algorithmId=0;//�㷨����
	private int row=map.length;
	private int col=map[0].length;
	private float span=Map.span;
	
	private boolean pathFlag=false;//·���Ҵ��־
	private int timeSpan=10;//ʱ����
	public  int tempCount;//�ܹ���Ҫ����
	
	public int[][] visited=new int[row][col];//0 δȥ�� 1 ��ȥ��
	private int[][] length=new int[row][col];//��¼·������ for Dijkstra
	HashMap<String, ArrayList<int[][]>> hmPath=new HashMap<String,ArrayList<int[][]>>();
	
	ArrayList<int [][]> searchProcess=new ArrayList<int[][]>();//��������
	Stack<int [][]> stack=new Stack<int[][]>();//�����������ջ
	LinkedList<int[][]> queue=new LinkedList<int [][]>();//����������ö���
	PriorityQueue<int[][]>astarQueue=new PriorityQueue<int[][]>(100,new AStarComparator(this));
	
	HashMap<String, int[][]> hm=new HashMap<String,int[][]>();//���·����¼
	
	private int [][]sequence={
			{0,1},{0,-1},
			{-1,0},{1,0},
			{-1,1},{-1,-1},
			{1,-1},{1,1}
	}; //�����İ˸�����
	
	final int DFS_COUNT=1;//�������ʹ�ò�����־
	final int BFS_COUNT=2;//�������ʹ�ò�����־
	final int LJCD_COUNT=6;//·������
	final int BFSASTAR_COUNT=3;//�������ʹ�ò�����־
	final int DIJKSTRA_COUNT=4;//Dijkstraʹ�ò�����־
	final int DIJKSTRASTAR_COUNT=5;//DijkstraA*ʹ�ò�����־
	public int LjcdCount;
	
	public GameLogic(){
		
		clearInit();
	}
	
	public void clearInit(){
		searchProcess.clear();
		stack.clear();
		queue.clear();
		hm.clear();
		visited=new int [row][col];
		pathFlag=false;
		span=Map.span;
		hmPath.clear();
		astarQueue.clear();
		for(int i=0;i<length.length;i++)
		{
			for(int j=0;j<length[0].length;j++)
			{
				length[i][j]=9999;//���ó�ʼ·���ĳ��ȣ���������ô��
			}
		}
	}
	
	
	
	public void logic(){
		clearInit();
		switch (algorithmId) {
		case 0:
			DFS();
			break;
		case 1:
			BFS();
			break;
		case 2:
			  BFSAStar();
			break;
		case 3:
			Dijkstra();
			break;
		case 4://DijkstraA*�㷨
			  DijkstraAStar();
			break;
		}
		
	}
	
	public void draw(Canvas canvas,Paint paint){
		
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(0);
		for(int k=0;k<searchProcess.size();k++){
			int[][]Edge=searchProcess.get(k);
			canvas.drawLine(Edge[0][0]*(span+1)+span/2+2, Edge[0][1]*(span+1)+span/2+2, Edge[1][0]*(span+1)+span/2+2, Edge[1][1]*(span+1)+span/2+2, paint);
		}
		if(algorithmId==0||algorithmId==1||algorithmId==2){
			if(pathFlag){
				int [] tempTa=target;
				int count=0;
				paint.setColor(Color.BLACK);//���û��ʺ�ɫ
				paint.setStrokeWidth(3);//���û��ʿ��
				while(true){
					int [][]tempA=hm.get(tempTa[0]+":"+tempTa[1]);
					canvas.drawLine(tempA[0][0]*(span+1)+span/2+2, tempA[0][1]*(span+1)+span/2+2, tempA[1][0]*(span+1)+span/2+2, tempA[1][1]*(span+1)+span/2+2, paint);
					count++;
					if(tempA[1][0]==source[0]&&tempA[1][1]==source[1]){
						break;
					}
					tempTa=tempA[1];
				}
				LjcdCount=count;//���㷨�е�����С1��ӦΪ�и��Ʊ�
				MainActivity.hd.sendEmptyMessage(LJCD_COUNT);
			}
		}
		else if(algorithmId==3||algorithmId==4){
			if(pathFlag){
				HashMap<String,ArrayList<int[][]>> hPath=hmPath;
				ArrayList<int[][]> alPath=hPath.get(target[0]+":"+target[1]);
				for(int [][]tempA:alPath){
					paint.setColor(Color.BLACK);	
					paint.setStrokeWidth(3);
					canvas.drawLine
				    (
				    	tempA[0][0]*(span+1)+span/2+2,tempA[0][1]*(span+1)+span/2+2,
						tempA[1][0]*(span+1)+span/2+2,tempA[1][1]*(span+1)+span/2+2,paint
				    );	
				}
				LjcdCount=alPath.size();
				MainActivity.hd.sendEmptyMessage(LJCD_COUNT);
			}
		}
		
	}
	
	private void DFS()//�������
	{
		new Thread(){
			@Override
			public void run() {
				super.run();
				boolean flag=true;
				int [][]start=//��ʼ������������,�˴������ǽ�start������Ϊһ��Ԫ�ط����ջ
				{
					{source[0],source[1]},
					{source[0],source[1]}
				};
				stack.push(start);
				int count=0;//������
				while(flag){
					if(stack.empty()){
						break;
					}
					int [][]currentEdge=stack.pop();
					int [] tempTarget=currentEdge[1];
					
					if(visited[tempTarget[1]][tempTarget[0]]==1){
						continue;
					}
					count++;
					visited[tempTarget[1]][tempTarget[0]]=1;//��ʾĿ�ĵ㱻���ʹ�
					
					searchProcess.add(currentEdge);//����ʱĿ����������������
					
 				    hm.put(tempTarget[0]+":"+tempTarget[1], new int[][]{currentEdge[1],currentEdge[0]});//��¼����ʱ�ڵ�ĸ��ڵ�

					
					try {
						Thread.sleep(timeSpan);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace(System.out);
					}
					
					if(tempTarget[0]==target[0]&&tempTarget[1]==target[1])//�ж��Ƿ񵽴�Ŀ�ĵ�
					{
						break;
					}
					
					int currCol=tempTarget[0];//ȡ�߽ڵ�
					int currRow=tempTarget[1];
					
					for(int [] rc:sequence){
						int i=rc[1];
						int j=rc[0];
						
						if(i==0&&j==0){continue;}
						if(currCol+j>=0&&currCol+j<col&&currRow+i>=0&&currRow+i<row&&map[currRow+i][currCol+j]!=1){
							int[][] tempEdge=
								{
									{tempTarget[0],tempTarget[1]},
									{currCol+j,currRow+i}
								};
								stack.push(tempEdge);//��ջ
						}	
					}			
				}
				pathFlag=true;
				tempCount=count;
				MainActivity.hd.sendEmptyMessage(DFS_COUNT);
				MainActivity.button.setClickable(true);
			}
			
		}.start();	
	}
	
	public void BFS()//�������
	{
		new Thread(){

			@Override
			public void run() {
				// TODO BFS-generated method stub
				super.run();
				int count=0;
				boolean flag=true;
				int [][]start=
					{
						{source[0],source[1]},
						{source[0],source[1]}
					};
				
				queue.offer(start);
				while(flag){
					if(queue.peek()==null){
						break;
					}
					int [][]currentEdge=queue.poll();
					int [] tempTarget=currentEdge[1];
					
					if(visited[tempTarget[1]][tempTarget[0]]==1){
						continue;
					}
					count++;
					visited[tempTarget[1]][tempTarget[0]]=1;
					
					searchProcess.add(currentEdge);
					
					hm.put(tempTarget[0]+":"+tempTarget[1], new int[][]{currentEdge[1],currentEdge[0]});
					
					try {
						Thread.sleep(timeSpan);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace(System.out);
					}
					
					if(tempTarget[0]==target[0]&&tempTarget[1]==target[1]){
						break;
					}
					
					int currCol=tempTarget[0];
					int currRow=tempTarget[1];
					
					for(int[] rc:sequence){
						int i= rc[1];
						int j= rc[0];
						
						if(i==0&&j==0){continue;}
						if(currCol+j>=0&&currCol+j<19&&currRow+i>=0&&currRow+i<19&&map[currRow+i][currCol+j]!=1){
							int [][]tempEdge={
									{tempTarget[0],tempTarget[1]},
									{currCol+j,currRow+i}
							};
							queue.offer(tempEdge);
						}
					}	
				}
				pathFlag=true;
				tempCount=count;
				MainActivity.hd.sendEmptyMessage(DFS_COUNT);
				MainActivity.button.setClickable(true);
			}
			
		}.start();
	}
	
	public void Dijkstra()//Dijkstra
	{
		new Thread(){

			@Override
			public void run() {
				super.run();
				int count=0;//���������
				boolean flag=true;//����ѭ������
				int [] start={source[0],source[1]};//col,row	
				visited[source[1]][source[0]]=1;
				//����˵����п��Ե�����·��������
				for(int [] rowcol:sequence){
					
					int trow=start[1]+rowcol[1];
					int tcol=start[0]+rowcol[0];
					
					if(trow<0||trow>18||tcol<0||tcol>18)continue;
					if(map[trow][tcol]!=0)continue;
					
					length[trow][tcol]=1;
					
					String key=tcol+":"+trow;
					ArrayList<int[][]>al=new ArrayList<int [][]>();
					al.add(new int[][]{{start[0],start[1]},{tcol,trow}});
					hmPath.put(key, al);
					
					//��ȥ���ĵ��¼		
					searchProcess.add(new int[][]{{start[0],start[1]},{tcol,trow}});
					count++;	
				}
				
				outer:while(flag){
					//�ҵ���ǰ��չ��K Ҫ����չ��KΪ�ӿ�ʼ�㵽�˵�Ŀǰ·����̣��Ҵ˵�δ�����
					int []k=new int [2];
					int minLine=9999;
					for(int i=0;i<visited.length;i++){
						for(int j=0;j<visited[0].length;j++){
							if(visited[i][j]==0){
								if(minLine>length[i][j]){
									k[0]=j;
									k[1]=i;
									minLine=length[i][j];
								}
							}
						}
					}
					
					visited[k[1]][k[0]]=1;
					int dk=length[k[1]][k[0]];
					//ȡ����ʼ�㵽K��·��
					ArrayList<int[][]> al=hmPath.get(k[0]+":"+k[1]);
					for(int []rc:sequence){
						int trow=k[1]+rc[1];
						int tcol=k[0]+rc[0];
						//��Ҫ����ĵ㳬����ͼ�߽���ͼ�ϴ�λ��Ϊ�ϰ�������������˵�
						if(trow<0||trow>18||tcol<0||tcol>18)continue;
						if(map[trow][tcol]!=0)continue;
						
						//ȡ����ʼ�㵽�˵��·������
						int dj=length[trow][tcol];
						//���㾭K�㵽�˵��·������				
						int dkPluskj=dk+1;
						
						if(dj>dkPluskj){
							
							String key=tcol+":"+trow;
							//��¡��ʼ�㵽K��·��
							ArrayList<int[][]>tempal=(ArrayList<int[][]>)al.clone();//���ǹؼ����֣���¡
							tempal.add(new int [][]{{k[0],k[1]},{tcol,trow}});
							hmPath.put(key,tempal);
							length[trow][tcol]=dkPluskj;
							if(dj==9999)
							{
								//��ȥ���ĵ��¼		
								searchProcess.add(new int[][]{{k[0],k[1]},{tcol,trow}});
								count++;
							}
						
						}
						
						if(tcol==target[0]&&trow==target[1]){
							pathFlag=true;
							tempCount=count;
							MainActivity.hd.sendEmptyMessage(DIJKSTRA_COUNT);
							MainActivity.button.setClickable(true);
							break outer;
						}
					}
					try{Thread.sleep(timeSpan);}catch(Exception e){e.printStackTrace(System.out);}
				}
				
			}
			
		}.start();
	}
	
	
	public void BFSAStar()//�������A*
	{
		new Thread(){

			@Override
			public void run() {
				// TODO BFSAStar-generated method stub
				super.run();
				boolean flag=true;
				int[][] start=//��ʼ״̬
				{
					{source[0],source[1]},
					{source[0],source[1]}
				};
				astarQueue.offer(start);
				int count=0;
				while(flag){
					if(astarQueue.peek()==null){
						break;
					}
					int [][]currentEdge=astarQueue.poll();
					int []tempTarget=currentEdge[1];
					
					if(visited[tempTarget[1]][tempTarget[0]]!=0){
						continue;
					}
					count++;
					visited[tempTarget[1]][tempTarget[0]]=1;
					searchProcess.add(currentEdge);
					hm.put(tempTarget[0]+":"+tempTarget[1], new int [][]{currentEdge[1],currentEdge[0]});
					try{Thread.sleep(timeSpan);}catch(Exception e){e.printStackTrace(System.out);}
					//�ж��Ƿ�ΪĿ���
					if(tempTarget[0]==target[0]&&tempTarget[1]==target[1])
					{
						break;
					}
					
					int currCol=tempTarget[0];
					int currRow=tempTarget[1];
					
					for(int[]rc:sequence){
						int i=rc[1];
						int j=rc[0];
						if(i==0&&j==0){continue;}
						if(currRow+i>=0&&currRow+i<19&&currCol+j>=0&&currCol+j<19&&
						map[currRow+i][currCol+j]!=1){
							int[][]tempEdge={
								{tempTarget[0],tempTarget[1]},
								{currCol+j,currRow+i}
							};
							astarQueue.offer(tempEdge);
						}
					}
					
				}
				pathFlag=true;
				tempCount=count;
				MainActivity.hd.sendEmptyMessage(BFSASTAR_COUNT);
				MainActivity.button.setClickable(true);
				
			}
			
		}.start();	
	}
	
	public void DijkstraAStar()//Dijkstra A*�㷨
	{
		new Thread(){

			@Override
			public void run() {
				// TODO DijkstraAStar-generated method stub
				super.run();
				int count =0;
				boolean flag=true;
				int[]start={source[0],source[1]};
				visited[source[1]][source[0]]=1;
				for(int []rc:sequence){
					int trow=start[1]+rc[1];
					int tcol=start[0]+rc[0];
					if(trow<0||trow>18||tcol<0||tcol>18)continue;
					if(map[trow][tcol]!=0)continue;
					
					length[trow][tcol]=1;
					
					String key=tcol+":"+trow;
					ArrayList<int[][]>al=new ArrayList<int [][]>();
					al.add(new int [][]{{start[0],start[1]},{tcol,trow}});
					hmPath.put(key, al);
					
					searchProcess.add(new int[][]{{start[0],start[1]},{tcol,trow}});
					count++;
					
				}
				
				outer:while(flag){
					int []k=new int[2];
					int minLine=9999;
					boolean iniFlag=true;
					for(int i=0;i<visited.length;i++){
						for(int j=0;j<visited[0].length;j++){
							if(visited[i][j]==0){
								if(length[i][j]!=9999){//����
//									if(iniFlag)
//									{
//									minLine=length[i][j]+(int)(Math.sqrt((j-target[0])*(j-target[0])+(i-target[1])*(i-target[1])));
//									k[0]=j;
//									k[1]=i;
//									iniFlag=!iniFlag;
//									}else{
									int tempLen=length[i][j]+
											(int)Math.sqrt((j-target[0])*(j-target[0])+(i-target[1])*(i-target[1]));
									if(minLine>tempLen)
									{
										minLine=tempLen;
										k[0]=j;//col
										k[1]=i;//row
									}
//								}
							}	
						  }
						}
					}
					
					visited[k[1]][k[0]]=1;
					ArrayList<int[][]>al=hmPath.get(k[0]+":"+k[1]);
					int dt=length[k[1]][k[0]];
					for(int[]rc:sequence){
						int tcol=k[0]+rc[0];
						int trow=k[1]+rc[1];
						
						if(trow<0||trow>18||tcol<0||tcol>18)continue;
						if(map[trow][tcol]!=0)continue;
						
						int dj=length[trow][tcol];
						int dkPluskj=dt+1;
						if(dj>dkPluskj){
							String key=tcol+":"+trow;
							ArrayList<int[][]>tempal=(ArrayList<int[][]>)al.clone();
							tempal.add(new int[][]{{k[0],k[1]},{tcol,trow}});
							hmPath.put(key, tempal);
							length[trow][tcol]=dkPluskj;
							if(dj==9999){
								searchProcess.add(new int[][]{{k[0],k[1]},{tcol,trow}});
								count++;
							}
						}
						if(tcol==target[0]&&trow==target[1]){
							pathFlag=true;
							tempCount=count;
							MainActivity.hd.sendEmptyMessage(DIJKSTRASTAR_COUNT);
							MainActivity.button.setClickable(true);
							break outer;
						}	
					}
					try{Thread.sleep(timeSpan);}catch(Exception e){e.printStackTrace(System.out);}	
				}
			}
			
		}.start();
	}
	
}