import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class ChessPlayMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainFrame mainframe=new MainFrame();
		mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainframe.setSize(700,680);
		mainframe.setLocationRelativeTo(null);
		mainframe.setVisible(true);
	}
}
class MainFrame extends JFrame {
	public MainFrame(){
		setTitle("Welcome to the world of Chinese Chess!");
		
		selSearch=new JMenu("搜索策略");
		grp=new ButtonGroup();
		minmax=new JRadioButtonMenuItem("最大最小值搜索",true);
		alphabeta=new JRadioButtonMenuItem("alpha-beta搜索");
		bar=new JMenuBar();
		grp.add(minmax);
		grp.add(alphabeta);
		selSearch.add(minmax);
		selSearch.add(alphabeta);
		bar.add(selSearch);
		this.setJMenuBar(bar);
		
		//this.set("image/background");
		chessPanel=new ChessPanel();
		//chessPanel.initGame();
		add(chessPanel);		
	}	
	static public ChessPanel chessPanel;
	private ButtonGroup grp;
	static public JRadioButtonMenuItem minmax;
	static public JRadioButtonMenuItem alphabeta;
	private JMenuBar bar;
	private JMenu selSearch;
}
   
class ChessPanel extends JPanel{
	public ChessPanel(){
		//side=0;//初始轮到红方走棋
		this.setLayout(null);
		for(int i=0;i<48;i++){
			cman[i]=new JLabel(new ImageIcon());
		   // cman[i].setText(" ");
		   // add(cman[i]);
		}
		//GenBoard(RulesGen.board);
		//preSel=new JLabel();
		//initGame();
	    rules=new RulesGen();
	    role=new JLabel(new ImageIcon("image/role.jpg"));
	    role.setBounds(540, 50, 140, 100);
	    start=new JButton(new ImageIcon("image/startGame.jpg"));
	    start.setBounds(540, 155, 130, 70);
	
	    start.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
			    start.setIcon(new ImageIcon("image/turnHong.jpg"));
			    ChessPanel.mes.setText("");
			    //new ChessPanel().GenBoard(RulesGen.board);
			    //startFlag=true;
			    for(short i=48;i<208;i++){
					if(rules.board[i]!=0){
						int pix=(int) posToPixel(i).getWidth();
						int piy=(int) posToPixel(i).getHeight();
						cman[rules.board[i]].setBounds(pix, piy, 55, 55);
				    	MainFrame.chessPanel.add(cman[rules.board[i]]);
					}
				}
			    //int j=0;
				for(short i=48;i<208;i++){
					if(rules.board[i]!=0){
						Chessman man=new Chessman(rules.board[i],i);
						cman[rules.board[i]].setIcon(man.getPic());
						cman[rules.board[i]].setName(i+","+man.getId());//标签的名字格式：位置下标，棋子Id
				    	if((man.getId() & 16)!=0){
				    		cman[rules.board[i]].addMouseListener(new SelectListener());
				    	}
					}
				}
			}
	    });

	    addMouseListener(new clickPanelListener());//面板添加点击事件
	    
	    sign=new JLabel("走棋信息：");
	    sign.setBounds(540, 240, 150, 50);
	    mes=new JTextArea(10,10);
	    mes.setBackground(new Color(220,190,140));
	    scroll=new JScrollPane(mes);
	    scroll.setBounds(540, 280, 140, 200);
	    
	    add(role);
	    add(sign);
	    add(scroll);
	    add(start);

	}
	
    
	
	//将board数组中的位置下标转换成棋盘中的像素坐标（棋子左上角位置）
	static public Dimension  posToPixel(short aiTo){
	   	int a=aiTo/16-3;
    	int b=aiTo%16-3;//a,b分别表示棋盘的行和列数
    	int pix=b*57+25;
    	int piy=a*57+25;//棋盘原点在（50,50）像素处，棋盘美格57像素
		Dimension xy=new Dimension(pix,piy);
		return xy;
	}
	
	/*static public void removeLabel(JLabel label){
		this.remove(label);	
		//label.
    }*/
	
	/*public void initGame(){
		//rule=new RulesGen();
		
		for(short i=48;i<208;i++){
			if(rules.board[i]!=0){
				int pix=(int) posToPixel(i).getWidth();
				int piy=(int) posToPixel(i).getHeight();
				cman[RulesGen.board[i]].setBounds(pix, piy, 55, 55);
		    	this.add(cman[RulesGen.board[i]]);
			}
		}
	}*/
		
   static public ImageIcon changePic(String name){//name 为标签原来的图像名称
	  // Chessman man=new Chessman((short)id,(short)0);
	   //String name=man.getPic().toString();
	   String newName;
	   if(name.endsWith("-sel.gif")){
		 newName=name.substring(0, name.length()-8)+".gif";
	   }else{
		   newName=name.substring(0,name.length()-4)+"-sel.gif";
	   }
	   ImageIcon ico=new ImageIcon(newName);
	   return ico;
   }
	/*
    public void drawAtPos(Chessman man,short pos){
    	//JLabel cman=new JLabel(man.getPic());
    	
    	int j=0;
    	cman[j].setIcon(man.getPic());
    	int a=pos/16-3;
    	int b=pos%16-3;//a,b分别表示棋盘的行和列数
    	int pix=b*57+25;
    	int piy=a*57+25;//棋盘原点在（50,50）像素处，棋盘美格57像素
    	System.out.println("位置坐标："+pix+","+piy+"    ");
    	cman[j].setBounds(pix, piy, 50, 50);
    	System.out.println("id : "+ (int)man.getId());
//    	if((man.getId() & 16)!=0){
//    		cman.addMouseListener(new SelectListener());
//    	}
    	this.add(cman[j]);
    }
    
	public void GenBoard(short [] board){
		Chessman man;

		for(short i=48;i<208;i++){
			if(board[i]!=0){
				man=new Chessman(board[i],i);
				drawAtPos(man,i);
			}
		}
	}
	
	*/

	//设置面板背景
	ImageIcon background = new ImageIcon("image/background.jpg");//加载图片  
	Image im=Toolkit.getDefaultToolkit().getImage("image/background.jpg"); 
	public void paintComponent(Graphics g) { 
		g.drawImage(im, 0, 0, null); 
/*		for(short i=48;i<208;i++){
			if(rules.board[i]!=0){
				int pix=(int) posToPixel(i).getWidth();
				int piy=(int) posToPixel(i).getHeight();
				cman[RulesGen.board[i]].setBounds(pix, piy, 55, 55);
		    	this.add(cman[RulesGen.board[i]]);
			}
		}*/
		
	}
	static public  JButton start;
	private JLabel role; //显示棋局角色
	private JLabel sign;
	static public JTextArea mes;
	private JScrollPane scroll;
   // static boolean startFlag=false;//是否开始游戏
    static public JLabel [] cman=new JLabel[48];
    private RulesGen rule;
    static  public JLabel preSel=null;//记录选择棋子前一个棋子的标签
    static public JLabel aiPreSel=null;//记录电脑前一次move的棋子
    static public int preSelId=0;//记录选择的棋子的Id
    //static int side=0;
    static RulesGen rules;
   // static boolean chooseOne=false;
//    public JRadioButton maxmin;
//    public JRadioButton albe;
//   public ButtonGroup grp;
    
    
}

class SelectListener implements  MouseListener{

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	    JLabel tmp=(JLabel) e.getSource();
		boolean isSelected =false;
	    if(isSelected==false){
	    	 String imageName=tmp.getIcon().toString();
	    	 String newimageName=imageName.substring(0, imageName.length()-4)+"-sel.gif";
	    	 tmp.setIcon(new ImageIcon(newimageName));
	         //tmp.setVisible(false);
	    	 isSelected=true;
	    	 //preSel=tmp;
	    	 System.out.println(imageName+","+newimageName);
	    	 if(ChessPanel.preSel!=null){
	    		 //设置选择状态
	    		 JLabel pre=ChessPanel.preSel;
	    		 String preName=pre.getIcon().toString();
	    		 String newPreName=preName.substring(0, preName.length()-8)+".gif";
	    		 pre.setIcon(new ImageIcon(newPreName));
	    		 System.out.println("persel:"+preName+","+newPreName);
	    		 //如果已选择自己棋子并且此时单击对方棋子，则判断是否可将对方棋子吃掉
				/* String[] name=pre.getName().split(",");
				 String [] nameto=tmp.getName().split(",");
				int fromId=Integer.parseInt(name[1]);
				int fromPos=Integer.parseInt(name[0]);
				int to=Integer.parseInt(name[0]);
				int newx=(int) ChessPanel.posToPixel((short) to).getWidth();
				int newy=(int) ChessPanel.posToPixel((short) to).getHeight();
				System.out.println("persel id:"+fromId+",pos"+fromPos);
					//RulesGen rule=new RulesGen();
				int isValued=ChessPanel.rules.posIsValid(fromId, fromPos,to );
				if(fromId==25 ||fromId==26){
					if(isValued==1){
						ChessPanel.rules.board[to]=(short) fromId;
						ChessPanel.rules.board[fromId]=0;
						ChessPanel.rules.piece[fromId]=0;
						ChessPanel.rules.piece[ChessPanel.rules.board[to]]=(short) fromPos;
						pre.setBounds(newx, newy, 55, 55);
						tmp.setVisible(false);
					}
				}else{
					if(isValued==0){
						ChessPanel.rules.board[to]=(short) fromId;
						ChessPanel.rules.board[fromId]=0;
						ChessPanel.rules.piece[fromId]=0;
						ChessPanel.rules.piece[ChessPanel.rules.board[to]]=(short) fromPos;
						pre.setBounds(newx, newy, 55, 55);
						tmp.setVisible(false);
					}
				}*/
	    	 } 
	    	 ChessPanel.preSel=tmp;
	    	 ChessPanel.preSel.setName(tmp.getName());
	    	 System.out.println(tmp.getName());
	    	 //ChessPanel.preSelId=
	    }		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}
}

class clickPanelListener implements MouseListener{

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		int x=arg0.getX();
		int y=arg0.getY();
		if(x>30 && x<530 && y>30 && y<580){//判断点击处是否在棋盘区域
			
					//ChessPanel.side=1;
			PlayerThread player=new PlayerThread(x,y);
			player.start();
	        System.out.println("player+ai");
			//AiThread ai=new AiThread();
			//ai.start();
		 }
	   
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	
}

class PlayerThread extends Thread{
	private int x,y;
	public PlayerThread(int x,int y){
		this.x=x;
		this.y=y;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("轮到红棋走，side="+ChessPanel.rules.side);
		int a,b;//a行b列
		a=  (int) Math.round((y-50)/57.0);
		b= (int) Math.round((x-50)/57.0);
		int to=(a+3)*16+b+3;//目标位置在board数组中的下标
		int newx=(int) ChessPanel.posToPixel((short) to).getWidth();
		int newy=(int) ChessPanel.posToPixel((short) to).getHeight();
		//System.out.println("test "+"("+x+","+y+")"+a+"hang "+b+"lie "+RulesGen.board[to]);
		//System.out.println("test "+"("+newx+","+newy+")");
		if(ChessPanel.preSel!=null){
			JLabel sel=ChessPanel.preSel;
		    String[] name=sel.getName().split(",");
			int fromId=Integer.parseInt(name[1]);
			int fromPos=Integer.parseInt(name[0]);
			System.out.println("persel id:"+fromId+",pos"+fromPos+" ,to"+to);
			//RulesGen rule=new RulesGen();
			int isValued=ChessPanel.rules.posIsValid(fromId, fromPos, to);
			System.out.println("isvalued:"+isValued);
			if(isValued==0 || isValued==1){
				if(isValued==1) System.out.println("fanguoyige"+ChessPanel.rules.board[to]);
			
				if((isValued==0 ) || (isValued==1 && (fromId==25 || fromId==26))){
					if(ChessPanel.rules.board[to]==0){
						sel.setBounds(newx, newy, 55, 55);
					}else{
						System.out.println("toid:"+ChessPanel.rules.board[to]);
						if((ChessPanel.rules.board[to] & 0x10)==0){//是对方棋子
							System.out.println("是对方棋子");
							//if(isValued==1) System.out.println("chidiao");
							ChessPanel.cman[ChessPanel.rules.board[to]].setBounds(0, 0, 0, 0);
							sel.setBounds(newx, newy, 55, 55);
							//ChessPanel.cman[ChessPanel.rules.board[to]].setVisible(false);
						}
					}
					
					short capture=ChessPanel.rules.board[to];
					sel.setName(to+","+fromId);
//					ChessPanel.rules.board[to]=(short) fromId;
//					ChessPanel.rules.board[fromPos]=0;
//					ChessPanel.rules.piece[fromId]=0;
//					ChessPanel.rules.piece[ChessPanel.rules.board[to]]=(short) fromPos;
					Move pMove=new Move((short)fromPos,(short)to,capture);
					String inf="";
				   // int step=0;
					if(fromPos/16 == to/16){
						inf="平";
					//	step=Math.abs(fromPos%16-to%16);
					}else if(fromPos>to){
						inf="进";
					//	step=fromPos/16-to/16;
					}else {
						inf="退";
					//	step=to/16-fromPos/16;
					}
					ChessPanel.mes.append("玩家： "+ChessPanel.rules.IdToChar(fromId)+" "+(12-fromPos%16)+" "+inf+" "+(12-to%16)+"\n");
					boolean isWin;
					isWin=ChessPanel.rules.makeMove(pMove);
					
					if(isWin){
						JOptionPane.showMessageDialog(null, "恭喜！你赢了！");
						ChessPanel.start.setIcon(new ImageIcon("image/startGame.jpg"));
						ChessPanel.rules.InitBoard();
						//MainFrame.chessPanel.repaint();
						ChessPanel.aiPreSel=null;
						ChessPanel.preSel=null;
						
						this.stop();
						return;
					}
					AiThread ai=new AiThread();
					ai.start();
				}
				
			}
		}

	}
	
}
class AiThread extends Thread{

	@Override
	 public void run() {
		// TODO Auto-generated method stub
		System.out.println("轮到黑棋走，side="+ChessPanel.rules.side);
		ChessPanel.start.setIcon(new ImageIcon("image/turnHei.jpg"));
		if(MainFrame.minmax.isSelected()){
			ChessPanel.rules.negaMaxSearch(ChessPanel.rules.maxDepth);
		}else if(MainFrame.alphabeta.isSelected()){
			ChessPanel.rules.alphaBetaSearch(ChessPanel.rules.maxDepth,-ChessPanel.rules.maxValue, ChessPanel.rules.maxValue);
		}
		if(ChessPanel.rules.HasLegalMove()){
			Move best=ChessPanel.rules.bestMove;
			//ChessPanel.rules.bestMove=null;
			//System.out.println("ai zuidazhi move,from:"+best.getFrom()+",to:"+best.getTo());
			if(best==null){
				JOptionPane.showMessageDialog(null, "恭喜！你赢了！");
				ChessPanel.start.setIcon(new ImageIcon("image/startGame.jpg"));
				ChessPanel.rules.InitBoard();
				//MainFrame.chessPanel.repaint();
				ChessPanel.aiPreSel=null;
				ChessPanel.preSel=null;
				
				this.stop();
				return;
			}
			int aiFrom=best.getFrom();
			int aiTo=best.getTo();
			//int aiCap=best.getCapture();
			int destId=ChessPanel.rules.board[aiTo];
			int aiFromId=ChessPanel.rules.board[aiFrom];
			
			//best.setCapture(ChessPanel.rules.board[aiTo]);
			System.out.println("aiid:"+aiFromId+"aifom:"+aiFrom+",aito:"+aiTo+",cap:"+best.getCapture());
			if(destId!=0){
				ChessPanel.cman[destId].setBounds(0, 0, 0, 0);
				//ChessPanel.cman[dest].setVisible(false);
			}
			//ChessPanel.cman[ChessPanel.rules.board[aiTo]].setVisible(false);
			Dimension dim=ChessPanel.posToPixel((short) aiTo);
			//System.out.println("ai zuobiao x:"+dim.getWidth()+";y:"+dim.getHeight());
	        if(ChessPanel.aiPreSel!=null){
	        	JLabel sel=ChessPanel.aiPreSel;
			    String[] name=sel.getName().split(",");
				int preFromId=Integer.parseInt(name[1]);
				int preFromPos=Integer.parseInt(name[0]);
				System.out.println("aipersel id:"+preFromId+",pos"+preFromPos);
				//if(preFromId!= aiFromId){
		        	ChessPanel.aiPreSel.setIcon(ChessPanel.changePic(ChessPanel.aiPreSel.getIcon().toString()));
		       // }
	        }
			
	        
	        ChessPanel.aiPreSel=ChessPanel.cman[aiFromId];
	        ChessPanel.aiPreSel.setName(aiTo+","+aiFromId);
	        
			ChessPanel.cman[aiFromId].setBounds((int)dim.getWidth(),(int) dim.getHeight(), 55,55 );
			ChessPanel.cman[aiFromId].setIcon(ChessPanel.changePic(ChessPanel.cman[aiFromId].getIcon().toString()));
			ChessPanel.preSel.setIcon(ChessPanel.changePic(ChessPanel.preSel.getIcon().toString()));
			//System.out.println(ChessPanel.preSel.getIcon().toString());
			ChessPanel.start.setIcon(new ImageIcon("image/turnHong.jpg"));
			ChessPanel.preSel=null;
			//ChessPanel.cman[dest].setIcon("image/box2.ico");
			String inf="";
		    //int step=0;
			if(aiFrom/16 == aiTo/16){
				inf="平";
			//	step=Math.abs(aiFrom%16-aiTo%16);
			}else if(aiFrom>aiTo){
				inf="退";
			//	step=aiFrom/16-aiTo/16;
			}else {
				inf="进";
			//	step=aiTo/16-aiFrom/16;
			}
			ChessPanel.mes.append("电脑： "+ChessPanel.rules.IdToChar(aiFromId)+" "+(aiFrom%16-2)+" "+inf+" "+(aiTo%16-2)+"\n");
			boolean isWin=ChessPanel.rules.makeMove(best);
			ChessPanel.rules.bestMove=null;
			System.out.println("AI走后board数组为：");
			for(int i=0;i<256;i++){
				if(i%16==0) System.out.println();
				System.out.print(ChessPanel.rules.board[i]+",");
			}
			System.out.println("AI走后piece数组为：");
			for(int i=0;i<48;i++){
				if(i%16==0) System.out.println();
				System.out.print(ChessPanel.rules.piece[i]+",");
			}
			//ChessPanel.mes.append(""+aiFromId+(aiFrom%16-3)+);
			if(isWin){
				JOptionPane.showMessageDialog(null, "胜败乃兵家常事，大侠请重新来过！");
				//初始化棋盘 重新开始
				ChessPanel.rules.InitBoard();
				ChessPanel.start.setIcon(new ImageIcon("image/startGame.jpg"));
				ChessPanel.aiPreSel=null;
				ChessPanel.preSel=null;
				
				this.stop();
			   // MainFrame.chessPanel.repaint();
				return ;
			}
		}

	}
	
	
	
}