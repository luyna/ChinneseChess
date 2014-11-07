import java.util.ArrayList;


public class RulesGen {
//	void initGen(){
//		moveList.clear();
//	}
	boolean saveMove(short from,short to,ArrayList<Move> list){//,ArrayList<Move> moveList
		short p=board[to];
		piece[board[from]]=to;
		if(p!=0) piece[p]=0;
		board[to]=board[from];
		board[from]=0;
		//移动棋子后判断是否处于被将军状态，如果是则该条走法舍弃
		boolean r=Check(side);
		//判断之后要将移动还原，因为还要判断其他走法
		board[from]=board[to];
		board[to]=p;
		piece[board[from]]=from;
		if(p!=0) piece[p]=to;
		
		if(!r){
			Move tmp=new Move(from,to,p);
			tmp.setCapture(board[to]);
			list.add(tmp);
			return true;
		}
		return false;
	}
    //生成并保存马的走法
	void maMove(short p,ArrayList<Move> list){//p是当前马所在位置
		short n,m;//下一步可走位置和马腿位置
		int sideTag=16+side*16;//side=0,1;红方16，黑方32
		for(int k=0;k<8;k++ ){
			n=(short) (p+maDir[k]);
			if(n>=0 && n<=256&&legalPosition[n]==1){ //是否在棋盘上
				m=(short) (p+maCheck[k]);
				if(board[m]==0){
					if((board[n] & sideTag) ==0){
						saveMove(p,n,list);
					}
				}
			}
		}
	}
	//帅的走法
	void kingMove(short p,ArrayList<Move> list){
		short n;
		int sideTag=16+side*16;
		for(int k=0;k<4;k++){
			n=(short) (p+kingDir[k]);
			if(n>=0 && n<=256&&kingPosition[n]!=0){
				if((board[n]&sideTag)==0){
					saveMove(p,n,list);
				}
			}
		}
	}
	//士的走法
	void shiMove(short p,ArrayList<Move> list){
		short n;
		int sideTag=16+side*16;
		for(int k=0;k<4;k++){
			n=(short) (p+shiDir[k]);
			if(n>=0 && n<=256&&shiPosition[n]!=0){
				if((board[n] & sideTag)==0){ //目标位置上不是本方棋子
					saveMove(p,n,list);
				}
			}
		}
	}
	//相的走法
	void xiangMove(short p,ArrayList<Move> list){
		short m,n;
		int sideTag=16+side*16;
		for(int k=0;k<4;k++){
			n=(short) (p+xiangDir[k]);
			if(n>=0 && n<=256&&xiangPosition[n]!=0){
				m=(short) (p+xiangCheck[k]);
				if(board[m]==0){
					if((board[n]&sideTag)==0){
						saveMove(p,n,list);
					}
				}
			}
		}
	}
	//车的走法
	void juMove(short p,ArrayList<Move> list){
		short n;
		int sideTag=16+side*16;
		for(int k=0;k<4;k++){
			for(int j=1;j<10;j++){//横的最多有8个可走位置，纵向最多有9个可走位置
				n=(short) (p+j*juDir[k]);
				if(n>=0 && n<=256){
					if(legalPosition[n]==0){//不合理的位置
						break;
					}
					if(board[n]==0){//目标位置上无子
						saveMove(p,n,list);
					}else if((board[n]&sideTag)!=0){//目标位置上有本方棋子
						break;
					}else{              //目标位置上有对方棋子
						saveMove(p,n,list);
						break;
					}
				}
				
			}
		}
	}
	//炮的走法
	void paoMove(short p,ArrayList<Move> list){
		short n;
		int sideTag=16+side*16;
		int overFlag;
		for(int k=0;k<4;k++){
			overFlag=0;//表示未翻山
			for(int j=1;j<10;j++){
				n=(short) (p+j*paoDir[k]);
				if(n>=0 && n<=256){
					if(legalPosition[n]==0){
						break;
					}
					if(board[n]==0){//目标位置上无子
						if(overFlag==0)
							saveMove(p,n,list);//若已翻山则不作处理，自动考察下一个位置
					}else{//目标位置上有子
						if(overFlag==0){
							overFlag=1;
						}else{
							if((board[n]&sideTag)==0){
								saveMove(p,n,list);
							}
							break;
						}
					}
				}
				
			}
		}
	}
	
	//兵的走法
	void bingMove(short p,ArrayList<Move> list){
		short n;
		int sideTag=16+16*side;
		for(int k=0;k<3;k++){
			n=(short) (p+bingDir[side][k]);
			if(n>=0 && n<=256&&bingPosition[side][n]!=0){
				if((board[n]&sideTag)==0){//目标位置上没有本方棋子
					saveMove(p,n,list);
				}
			}
		}
	}
	
	/*
	 * 寻找当前棋盘所有可行走法
	 * 两个方法：1，遍历棋盘所有位置，如果有棋子的话则按照规则生成走法，效率较低
	 * 2，利用棋子数组，找到还在棋盘中的棋子，生成可行走法
	 * 
	 */
	//一、遍历棋盘得到所有走法
	int genAllMove1(ArrayList<Move> list){
		short p;
		int sideTag=16+side*16;//走棋方，红方16，黑方32
		for(int i=3;i<13;i++){
			for(int j=3;j<12;j++){
				p=(short) ((i<<4)+j);
				if((board[p]&sideTag)==0) continue;
				switch(board[p]-sideTag){
				case 0:kingMove(p,list);break;
				case 1:
				case 2:shiMove(p,list);break;
				case 3:
				case 4:xiangMove(p,list);break;
				case 5:
				case 6:maMove(p,list);break;
				case 7:
				case 8:juMove(p,list);break;
				case 9:
				case 10:paoMove(p,list);break;
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:bingMove(p,list);break;	
				}
			}
		}
		return list.size();
	}
	//二、根据棋子数组，即棋盘中的棋子存在状态遍历棋子而不是遍历棋盘生成走法
	int genAllMove2(ArrayList<Move> list){
		short p;
		list.clear();
		int sideTag=16+16*side;
		int pc;//棋子ID
		for(int i=0;i<16;i++){
			pc=sideTag+i;
			p=piece[pc];
			if(p==0 ){
				if(i==0) return 0;
				continue;
			}
			switch(i)
			{
			case 0: kingMove(p,list);break;
			case 1:
			case 2:shiMove(p,list);break;
			case 3:
			case 4:xiangMove(p,list);break;
			case 5:
			case 6:maMove(p,list);break;
			case 7:
			case 8:juMove(p,list);break;
			case 9:
			case 10:paoMove(p,list);break;
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:bingMove(p,list);break;
			}
		}
		return list.size();
	}
	
	
	
	int GenAllMove(ArrayList<Move> list){
		short p,n,m;//棋子位置、下一步可能行走的位置、马腿或象眼位置
		int sideTag;
		int overFlag;//翻山标志
		sideTag=16+16*side;
		p=piece[sideTag];
		if(p==0) return 0;

		//帅的走法
		for(int k=0;k<4;k++){
			n=(short) (p+kingDir[k]);
			if(n>=0 && n<=256&&kingPosition[n]!=0){
				if((board[n]&sideTag)==0){
					saveMove(p,n,list);
				}
			}
		}
		
		//士的走法	
		for(int i=1;i<=2;i++){
			p=piece[sideTag+i];
			if(p==0) continue;
		    for(int k=0;k<4;k++){
		    	n=(short) (p+shiDir[k]);
		    	if(n>=0 && n<=256&&shiPosition[n]!=0){
		    		if((board[n] & sideTag)==0){ //目标位置上不是本方棋子
		    			saveMove(p,n,list);
		    		}
		    	}
			}
		}

		//相的走法
		for(int i=3;i<=4;i++){
			p=piece[sideTag+i];
			if(p==0) continue;
			for(int k=0;k<4;k++){
				n=(short) (p+xiangDir[k]);
				if(n>=0 && n<=256&&xiangPosition[n]!=0){
					m=(short) (p+xiangCheck[k]);
					if(board[m]==0){
						if((board[n]&sideTag)==0){
							saveMove(p,n,list);
						}
					}
				}
			}
		}
		
		for(int i=5;i<=6;i++){
			p=piece[sideTag+i];
			if(p==0) continue;
			for(int k=0;k<8;k++ ){
				n=(short) (p+maDir[k]);
				if(n>=0 && n<=256&&legalPosition[n]==1){ //是否在棋盘上
					m=(short) (p+maCheck[k]);
					if(board[m]==0){
						if((board[n] & sideTag) ==0){
							saveMove(p,n,list);
						}
					}
				}
			}
		}
			
		//车的走法
		for(int i=7;i<=8;i++){
			p=piece[sideTag+i];
			if(p==0) continue;
			for(int k=0;k<4;k++){
				for(int j=1;j<10;j++){//横的最多有8个可走位置，纵向最多有9个可走位置
					n=(short) (p+j*juDir[k]);
					if(n>=0 && n<=256){
						if(legalPosition[n]==0){//不合理的位置
							break;
						}
						if(board[n]==0){//目标位置上无子
							saveMove(p,n,list);
						}else if((board[n]&sideTag)!=0){//目标位置上有本方棋子
							break;
						}else{              //目标位置上有对方棋子
							saveMove(p,n,list);
							break;
						}
					}					
				}
			}	
		}
		
		//炮的走法
			for(int i=9;i<=10;i++){
				p=piece[sideTag+i];
				if(p==0) continue;
				for(int k=0;k<4;k++){
					overFlag=0;//表示未翻山
					for(int j=1;j<10;j++){
						n=(short) (p+j*paoDir[k]);
						if(n>=0 && n<=256){
							if(legalPosition[n]==0){
								break;
							}
							if(board[n]==0){//目标位置上无子
								if(overFlag==0)
									saveMove(p,n,list);//若已翻山则不作处理，自动考察下一个位置
							}else{//目标位置上有子
								if(overFlag==0){
									overFlag=1;
								}else{
									if((board[n]&sideTag)==0){
										saveMove(p,n,list);
									}
									break;
								}
							}
						}	
					}
				}
			}
		//兵的走法
		for(int i=11;i<=15;i++){
			p=piece[sideTag+i];
			if(p==0) continue;	
			for(int k=0;k<3;k++){
				n=(short) (p+bingDir[side][k]);
				if(n>=0 && n<=256&&bingPosition[side][n]!=0){
					if((board[n]&sideTag)==0){//目标位置上没有本方棋子
						saveMove(p,n,list);
					}
				}
			}	
		}
		return list.size();
	}
	
	public  boolean HasLegalMove()		//判断当前局面是否有合理走法，没有则判输
	{
		ArrayList<Move> list=new ArrayList<Move>();
		int num;
		num = GenAllMove(list);
		return num>0;
	}
	
	//清除棋盘信息和piece信息；
	void InitBoard(){
		side=0;
		short board1[]={
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,39,37,35,33,32,34,36,38,40,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,41,0,0,0,0,0,42,0,0,0,0,0,
				0,0,0,43,0,44,0,45,0,46,0,47,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,27,0,28,0,29,0,30,0,31,0,0,0,0,
				0,0,0,0,25,0,0,0,0,0,26,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,23,21,19,17,16,18,20,22,24,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			};
		board=board1;
		short piece1[]={
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				199,198,200,197,201,196,202,195,203,164,170,147,149,151,153,155,
				55,54,56,53,57,52,58,51,59,84,90,99,101,103,105,107
		};
		this.piece=piece1;
//		for(int i=0;i<256;i++){
//			board[i]=0;
//		}
//		for(int j=0;j<48;j++){
//			piece[j]=0;
//		}
	}
	//pc:棋子在piece表中的代表数字，即chessman的ID，pos指该棋子放在一维棋盘中的位置下标
	void addPiece(short pos,short pc){
		board[pos]= pc;
		piece[pc]=pos;
	}
	
	/*lside方走过之后检测
	 * 将军检测：从具有攻击性的兵，马，车，炮进行分别检测，+将帅照面
	 */
	boolean Check(int lside){//检测lside一方是否被将军，是被将军返回true，否则返回false
		short rking,bking;//红黑双方将帅的位置
		short p;
		boolean r;//r=true表示将军
		int sideTag=32-lside*16;//sideTag表示lside的对方将的值
		int posadd;//位置增量
		int fside=1-lside;//对方标志
		rking=piece[16];
		bking=piece[32];
		if(rking==0 || bking==0) return false;
		r=true;
		//检测将帅是否照面
		if(rking%16 == bking%16){
			for(rking=(short) (rking-16);rking!=bking;rking=(short) (rking-16)){
				if(board[rking]!=0){
					r=false;
					break;
				}
			}
			if(r) return r;
		}
		//检测是否被马将军
		short q=piece[48-sideTag];//lside方将的位置
		short n,m;//下一步可能行走的位置和马腿位置
		for(int i=5;i<=6;i++){
			p=piece[sideTag+i];//对方马所在的位置
			if(p==0) continue;
			for(int k=0;k<8;k++){
				n=(short) (p+maDir[k]);
				if(n!=q) continue;
				if(legalPosition[n]!=0){
					m=(short) (p+maCheck[k]);
					if(board[m]==0) return true;
				}
			}
		}
		
		//检测是否被车将军
		r=true;
		for(int i=7;i<=8;i++){
			p=piece[sideTag+i];
			if(p==0) continue;
			if(p%16 == q%16){
				posadd=(p>q?-16:16);
				for(p=(short) (p+posadd);p!=q;p=(short) (p+posadd)){
					if(board[p]!=0){
						r=false;
						break;
					}
				}
				if(r) return r;
			}else if(p/16 ==q/16){
				posadd=(p>q?-1:1);
				for(p=(short) (p+posadd);p!=q;p=(short) (p+posadd)){
					if(board[p]!=0){
						r=false;
						break;
					}
				}
				if(r) return r;
			}
		}
		//检测是否被炮将军
		int overFlag=0;//翻山标志
		for(int i=9;i<=10;i++){
			p=piece[sideTag+i];
			if(p==0) continue;
			if(p%16 ==q%16){
				posadd=(p>q?-16:16);
				for(p=(short) (p+posadd);p!=q;p=(short) (p+posadd)){
					if(board[p]!=0){
						if(overFlag==0)
							overFlag=1;
						else {
							overFlag=2;
							break;
						}
					}
				}
				if(overFlag==1) return true;
			}else if(p/16 ==q/16){
				posadd=(p>q?-1:1);
				for(p=(short) (p+posadd);p!=q;p=(short) (p+posadd)){
					if(board[p]!=0){
						if(overFlag==0) 
							overFlag=1;
						else {
							overFlag=2;
							break;
						}
					}
				}
				if(overFlag==1) return true;
			}
		}
		//检测是否被兵将军
		for(int i=11;i<=15;i++){
			p=piece[sideTag+i];
			if(p==0) continue;
			for(int k=0;k<3;k++){
				n=(short) (p+bingDir[fside][k]);
				if((n==q)&& legalPosition[n]!=0){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//评估函数
	int Evalue(){
		int bvalue=0,rvalue=0;
		short p,n,m;//棋子位置，下一步可走位置，马腿或象眼位置
		int sideTag;
		int overFlag;//翻山标志
		int fvalue[]={0,0};//灵活性分值
		//估算当前棋盘中存在棋子的固定位置价值
		for(int i=16;i<32;i++){
			if(piece[i]>0){
				rvalue=rvalue+positionValues[0][pieceNumToType[i]][piece[i]];
			}
		}
		for(int j=32;j<48;j++){
			if(piece[j]>0){
				bvalue +=positionValues[1][pieceNumToType[j]][piece[j]];
			}
		}
		//计算灵活性分值
		int k;
		for(int r=0;r<=1;r++){
			sideTag=16+16*r;
			//将的灵活性
			p=piece[sideTag];
//			if(p==0){
//				fvalue[r]=-maxValue;
//				return fvalue[1]-fvalue[0];
//			}
			
			for(k=0;k<4;k++ ){
				n=(short) (p+kingDir[k]);
				if(n>=0 && n<=256 && kingPosition[n]!=0){
					if((board[n]&sideTag)==0){
						fvalue[r] +=2;
					}
				}
			}
			
			//士的灵活性
			for(int i=1;i<=2;i++){
				p=piece[sideTag+i];
				if(p==0) continue;
				for(k=0;k<4;k++){
					n=(short) (p+shiDir[k]);
					if(n>=0 && n<=256&&shiPosition[n]!=0){
						if((board[n]&sideTag)==0){
							fvalue[r] +=2;
						}
					}
				}
			}
			//象的走法的灵活性
			for(int i=3;i<=4;i++){
				p=piece[sideTag+i];
				if(p==0){
					continue;
				}
				for(k=0;k<4;k++){
					n=(short) (p+xiangDir[k]);
					if(n>=0 && n<=256 && xiangPosition[n]!=0){
						m=(short) (p+xiangCheck[k]);
						if(board[m]==0){
							if((board[n]&sideTag)==0){
								fvalue[r] +=2;
							}
						}
					}
				}
			}
			
			//马的灵活性
			for(int i=5;i<=6;i++){
				p=piece[sideTag+i];
				if(p==0) continue;
				for(k=0;k<8;k++){
					n=(short) (p+maDir[k]);
					if(n>=0 && n<=256&&legalPosition[n]!=0){
						m=(short) (p+maCheck[k]);
						if(board[m]==0){
							if((board[n]&sideTag)==0){
								fvalue[r] +=5;
							}
						}
					}
				}
			}
			
			//车的灵活性
			for(int i=7;i<=8;i++){
				p=piece[sideTag+i];
				if(p==0) continue;
				for(k=0;k<4;k++){
					for(int j=1;j<10;j++){
						n=(short) (p+j*juDir[k]);
						if(n>=0 && n<=256){
							if(legalPosition[n]==0) break;
							if(board[n]==0) fvalue[r] +=4;
							else if((board[n]&sideTag)!=0) break;
							else {
								fvalue[r] +=4;
								break;
							}
						}
						
					}
				}
			}
			
			//炮的灵活性
			for(int i=9;i<=10;i++){
				p=piece[sideTag+i];
				if(p==0) continue;
				for(k=0;k<4;k++){
					overFlag=0;
					for(int j=1;j<10;j++){
						n=(short) (p+j*paoDir[k]);
						if(n>=0 && n<=256){
							if(legalPosition[n]==0) break;
							if(board[n]==0){
								if(overFlag==0){
									fvalue[r] +=3;//若已翻山则不作任何处理，自动考察下一个位置
								}
							}else{
								if(overFlag==0) overFlag=1;
								else {
									if((board[n]&sideTag)==0){
										fvalue[r] +=3;
									}
									break;
								}
							}
						}
						
					}
				}
			}
			//卒的灵活性
			for(int i=11;i<=15;i++){
				p=piece[i+sideTag];
				if(p==0) continue;
				for(k=0;k<3;k++){
					n=(short) (p+bingDir[r][k]);
					if(n>=0 && n<=256 && bingPosition[r][n]!=0){
						if((board[n]&sideTag)==0)
							fvalue[r]+=2;
					}
				}
			}
		}
		return fvalue[1]-fvalue[0]+bvalue-rvalue;
	}

	
	//搜索过程
	public ArrayList<Move> moveStack=new ArrayList<Move>();//执行的走法栈
	//int stacktop;//栈顶指针，指向栈顶元素的下一位置，=0表示栈空
	static Move bestMove;//搜索得到的最佳走法
	int ply;//当前搜索深度
	static final int maxDepth=4;//最大搜索深度
	final int maxValue=900000;//固zhi最大值
	public void minMaxSearch(int depth){//极大极小搜索算法
		moveStack.clear();
		ArrayList<Move> tmp=new ArrayList<Move>();
		if(side==0)
			minSearch(depth);
		else maxSearch(depth);
	}
	int maxSearch(int depth){//极大点搜索算法
		int best=-maxValue;
		ArrayList<Move> list=new ArrayList<Move>();
		Move mv;
		int value;
		if(depth==0) return Evalue();
		int num=GenAllMove(list);
		
		for(int i=0;i<num;i++){			
			mv=list.get(i);
			makeMove(mv);
			value=minSearch(depth-1);
			unMakeMove();
			if(value>best){
				best=value;
				if(depth==maxDepth){
					bestMove=new Move(mv.getFrom(),mv.getTo(),mv.getCapture());
					System.out.println("zuidazhi:"+best+",from："+bestMove.getFrom()+",to:"+bestMove.getTo());
					//System.out.println(list.toString());
					//System.out.println("num:"+num);
				}
			}
		}
		//System.out.println("zuidazhi:"+best);
		return best;
	}
	
	int minSearch(int depth){//极小点搜索算法
		int best=maxValue;
		ArrayList<Move> list=new ArrayList<Move>();
		int value;
		Move mv;
		if(depth==0) return Evalue();
		int num=GenAllMove(list);
		for(int i=0;i<num;i++){
			mv=list.get(i);
			makeMove(mv);
			value=maxSearch(depth-1);
			unMakeMove();
			if(value<best){
				best=value;
				if(depth==maxDepth)
					bestMove=new Move(mv.getFrom(),mv.getTo(),mv.getCapture());
			
			}
		}
		return best;
	}
	
	int negaMaxSearch(int depth){
		int best,value;
		ArrayList<Move> list=new ArrayList<Move>();
		Move mv;
		best=-maxValue;
		if(depth==0) return Evalue();
		int num=GenAllMove(list);
		for(int i=0;i<num;i++){
			mv=list.get(i);
			//moveStack.clear();
			makeMove(mv);
			value=-negaMaxSearch(depth-1);
			unMakeMove();
			if(value>best){
				best=value;
				if(depth==maxDepth){
					bestMove=mv;
					System.out.println("zuidazhi:"+best+",from："+bestMove.getFrom()+",to:"+bestMove.getTo());
				}
				
			}
		}
		return best;
	}
	//α-β搜索，α是本方的
	int alphaBetaSearch(int depth,int alpha,int beta){
		int value;
		ArrayList<Move> list=new ArrayList<Move>();
		Move mv;
		if(depth==0) return Evalue();
		int num=GenAllMove(list);
		
		for(int i=0;i<num ;i++){
			mv=list.get(i);
			makeMove(mv);
			//if(piece[16]==0 ) System.out.println("depth:"+depth+" value:"+alpha);
			value=-alphaBetaSearch(depth-1,-beta,-alpha);
			
			unMakeMove();
			if(value>beta) return beta;      //注意此处没有等号
			if(value>alpha){
				alpha=value;
				//System.out.println("depth:"+depth+" value:"+value);
				if(depth==maxDepth){
					bestMove=mv;
					System.out.println("alpha:"+alpha+"beta:"+beta+"value:"+value+"move:from"+mv.getFrom()+"to:"+mv.getTo()+"cap:"+mv.getCapture());
					//System.out.println(list.toString());
					//System.out.println("test"+num);
				}
			}
		}		
		//list.clear();
		return alpha;
	}
	//棋子整数值转换成字符表示
	public char IdToChar(int a)
	{
		if(a <32)
		{
			switch(a)
			{
			case 16:	return '帅';
			case 17:
			case 18:	return '仕';
			case 19:
			case 20:	return '相';
			case 21:
			case 22:	return '马';
			case 23:
			case 24:	return '车';
			case 25:
			case 26:	return '炮';
			case 27:
			case 28:
			case 29:
			case 30:
			case 31:	return '兵';
			default:	return 0;
			}
		}
		else
		{
			a = a-16;
			switch(a)
			{
			case 16:	return '将';
			case 17:
			case 18:	return '士';
			case 19:
			case 20:	return '象';
			case 21:
			case 22:	return '马';
			case 23:
			case 24:	return '车';
			case 25:
			case 26:	return '炮';
			case 27:
			case 28:
			case 29:
			case 30:
			case 31:	return '卒';
			default:	return 0;
			}
		}
	}
	
	boolean makeMove(Move m){         //执行走法,执行后如果已经将军，则返回true，否则返回false
		short from,dest,p;
		int sideTag=(side==0?32:16);//此处表示对方将帅的值
		from=m.getFrom();
		dest=m.getTo();
		p=board[dest];
		//System.out.println("jianzao...1");
		Move newMove=new Move(from,dest,p);
		//System.out.println("jianzao...2");
		moveStack.add(newMove);
		//System.out.println("makemove.."+ply);
		//设置棋子数组
		if(p>0){
			piece[p]=0;
		}
		piece[board[from]]=dest;
		//设置棋盘数组
		board[dest]=board[from];
		board[from]=0;
		
		ply++;
		changeSide();
		return p==sideTag;
	}
	
	void unMakeMove(){//撤销走法
		short from,dest,p;
		ply--;
		changeSide();
		int last=moveStack.size();
		Move tmp=moveStack.get(last-1);
		from=tmp.getFrom();
		dest=tmp.getTo();
		p=tmp.getCapture();
		//设置棋盘数组
		board[from]=board[dest];
		board[dest]=p;
		//设置棋子数组
		if(p>0) piece[p]=dest;
		piece[board[from]]=from;
		
		moveStack.remove(last-1);
		
	}
	void changeSide(){//变换走棋方
		side=(short) (1-side);
	}
	//player方：判断位置pos是否符合行走规则，
	int posIsValid(int id,int from,int to){ //返回-1，则不合法，返回0合法，返回1表示炮中间夹一个棋子
		if(id==16){
			for(int i=0;i<4;i++){
				if((to-from) == kingDir[i]){
					if(kingPosition[to]!=0) return 0;
					else return -1;
				}
			}	
		}
		else if(id==17 || id==18){
			for(int i=0;i<4;i++){
				if(to-from==shiDir[i]){
					if(shiPosition[to]!=0) return 0;
					else return -1;
				}
			}
		}
		
		else if(id==19 || id==20){
			for(int i=0;i<4;i++){
				if(to-from==xiangDir[i]){
					if(xiangPosition[to]!=0){
						if(board[xiangCheck[i]+from]==0) return 0;
					}
					return -1;
				}
			}
		}
		else if(id==21 ||id==22){
			for(int i=0;i<8;i++){
				if((to-from)==maDir[i]){
					if(legalPosition[to]!=0){
						if(board[maCheck[i]+from]==0) return 0;
					}
					return -1;
				}
			}
		}
		else if(id==23 || id==24){
				int add;
				if(Math.abs(to-from)%16==0 ){
					add=(to-from)>0?16:-16;
					if(legalPosition[to]!=0){
						int count=0;
						for(int j=from+add;j!=to;j=j+add){
							if(board[j]!=0) count++;
						}
						if(count==0) return 0;
					}
				}else if(Math.abs(to-from)/16==0){
						add=(to-from)>0?1:-1;
						if(legalPosition[to]!=0){
							int count=0;
							for(int j=from+add;j!=to;j=j+add){
								if(board[j]!=0) count++;
							}
							if(count==0) return 0;
					    }	
				}
				return -1;
		}
		else if(id==25 || id==26){
				int add;
				if(Math.abs(to-from) %16==0 ){
					add=(to-from)>0?16:-16;
					if(legalPosition[to]!=0){
						int count =0;
						for(int j=from+add;j!=to;j=j+add){
							if(board[j]!=0) count++;
						}
						if(count==0 && (board[to]==0))return 0;
						if(count==1) return 1;
						else return -1;
					}
				}
				if(Math.abs(to-from) /16==0 ){
					add=(to-from)>0?1:-1;
					if(legalPosition[to]!=0){
						int count =0;
						for(int j=from+add;j!=to;j=j+add){
							if(board[j]!=0) count++;
						}
						if(count==0 && (board[to]==0)) return 0;
						if(count==1) return 1;
						else return -1;
					}
				}	
				return -1;
		}
		else if(id==27 || id==28 ||id==29 || id==30 || id==31){
			for(int i=0;i<3;i++){
				if(to-from == bingDir[0][i]){
					if(bingPosition[0][to]!=0){
						return 0;
					}
				}
			}
			return -1;
		}
		//return false;
		return -1;
	}
	
	//ArrayList<Move> moveList;//走法数组
	//int moveNum;//走法总数
	//马的八个可行方向的数组表示和马腿位置表示
	final short maDir[]={+0x0e,-0x12,-0x21,-0x1f,-0x0e,+0x12,+0x1f,+0x21};
	final short maCheck[]={-0x01,-0x01,-0x10,-0x10,+0x01,+0x01,+0x10,+0x10};
	
	//将帅的四个可行方向数组表示
	final short kingDir[]={-0x10,+0x01,+0x10,-0x01};
	//士的四个可行方向数组
	final short shiDir[]={-0x11,-0x0f,+0x11,+0x0f};
	final short xiangDir[]={-0x22,-0x1e,+0x22,+0x1e};
	final short xiangCheck[]={-0x11,-0x0f,+0x11,+0x0f};//相眼位置
	final short juDir[]={-0x01,-0x10,+0x01,+0x10};
	final short paoDir[]={-0x01,+0x01,-0x10,+0x10};
	final short bingDir[][]={{-0x01,+0x01,-0x10},{-0x01,+0x01,+0x10}};
	//保存双方每一个棋子的位置，如果棋子被吃掉，则其值为0，下标16-31保存红方棋子的位置，
	//32-47保存黑方棋子的位置，下标0-15没有实际意义，与chessman类中的棋子ID保持一致
	static short piece[]={
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			199,198,200,197,201,196,202,195,203,164,170,147,149,151,153,155,
			55,54,56,53,57,52,58,51,59,84,90,99,101,103,105,107
	};
	//保存每个棋子的价值
	final short pieceValue[]={
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			1000,20,20,20,20,40,40,90,90,45,45,10,10,10,10,10,
			1000,20,20,20,20,40,40,90,90,45,45,10,10,10,10,10
	};
	//辅助数组
	final short pieceNumToType[]={
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,1,1,2,2,3,3,4,4,5,5,6,6,6,6,6,
			0,1,1,2,2,3,3,4,4,5,5,6,6,6,6,6,
	};
	//保存不同棋子在不同位置的分值，第一维表示走方，0红方1黑方，第二维表示棋子种类
	//0-6分别表示将士象马车炮兵，第三位表示位置
	static final short positionValues[][][]={
		{
			{//红帅
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,	
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,	
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,	
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,
				0,0,0,0,0,0,10,10,10,0,0,0,0,0,0,0,
				0,0,0,0,0,0,15,20,15,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,	
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,	
			},{//红士
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,	
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,	
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,	
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,30,0,30,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,22,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,30,0,30,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,	
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,	
			},{//hongxiang
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,	
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,	
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,25,0,0,0,25,0,0,0,0,0,0,	
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,20,0,0,0,35,0,0,0,20,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,30,0,0,0,30,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,	
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			},{//hongma
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,70,80,90,80,70,80,90,80,70,0,0,0,0,
				0,0,0,80,110,125,90,70,90,125,110,80,0,0,0,0,
				0,0,0,90,100,120,125,120,125,120,100,90,0,0,0,0,
				0,0,0,90,100,120,130,110,130,120,100,90,0,0,0,0,
				0,0,0,90,110,110,120,100,120,110,110,90,0,0,0,0,
				0,0,0,90,100,100,110,100,100,110,100,100,0,0,0,0,
				0,0,0,80,90,100,100,90,100,100,90,80,0,0,0,0,
				0,0,0,80,80,90,90,80,90,90,80,80,0,0,0,0,
				0,0,0,70,75,75,70,50,70,75,75,70,0,0,0,0,
				0,0,0,60,70,75,70,60,70,75,70,60,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			},{//hongju
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,160,170,160,150,150,150,160,170,160,0,0,0,0,
				0,0,0,170,180,170,190,250,190,170,180,170,0,0,0,0,
				0,0,0,170,190,200,220,240,220,200,190,170,0,0,0,0,
				0,0,0,180,220,210,240,250,240,210,220,180,0,0,0,0,
				0,0,0,180,220,210,240,250,240,210,220,180,0,0,0,0,
				0,0,0,180,220,210,240,250,240,210,2200,180,0,0,0,0,
				0,0,0,170,190,180,220,240,220,200,190,170,0,0,0,0,
				0,0,0,170,180,170,170,160,170,170,180,170,0,0,0,0,
				0,0,0,160,170,160,160,150,160,160,170,1600,0,0,0,0,
				0,0,0,150,160,150,160,150,160,150,160,150,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
			},{//hongpao
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,125,130,100,70,60,70,100,130,125,0,0,0,0,
				0,0,0,110,125,100,70,60,70,100,125,110,0,0,0,0,
				0,0,0,100,120,90,80,80,80,90,120,100,0,0,0,0,
				0,0,0,90,110,90,110,130,110,90,110,90,0,0,0,0,
				0,0,0,90,110,90,110,130,110,90,110,90,0,0,0,0,
				0,0,0,90,100,90,110,130,110,90,100,90,0,0,0,0,
				0,0,0,90,100,90,90,110,90,90,100,90,0,0,0,0,
				0,0,0,90,100,80,80,70,80,80,100,90,0,0,0,0,
				0,0,0,80,90,80,70,65,70,80,90,80,0,0,0,0,
				0,0,0,80,90,80,70,60,70,80,90,80,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0	
			},{//hongbing
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,10,10,10,20,25,20,10,10,10,0,0,0,0,
				0,0,0,25,30,40,50,60,50,40,30,25,0,0,0,0,
				0,0,0,25,30,30,40,40,40,30,30,25,0,0,0,0,
				0,0,0,20,25,25,30,30,30,25,25,20,0,0,0,0,
				0,0,0,15,20,20,20,20,20,20,20,20,0,0,0,0,
				0,0,0,10,0,15,0,15,0,15,0,10,0,0,0,0,
				0,0,0,10,0,10,0,15,0,10,0,10,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
			}
		},
		{
			{//heijiang
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,15,20,15,0,0,0,0,0,0,0,
				0,0,0,0,0,0,10,10,10,0,0,0,0,0,0,0,
				0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			},{//heishi
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,30,0,30,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,22,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,30,0,30,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			},{//heixiang
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,30,0,0,0,30,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,20,0,0,0,35,0,0,0,20,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,25,0,0,0,25,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
			},{//heima
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,60,70,75,70,60,70,75,70,60,0,0,0,0,
				0,0,0,70,75,75,70,50,70,75,75,70,0,0,0,0,
				0,0,0,80,80,90,90,80,90,90,80,80,0,0,0,0,
				0,0,0,80,90,100,100,90,100,100,90,80,0,0,0,0,
				0,0,0,90,100,100,110,100,100,110,100,100,0,0,0,0,
				0,0,0,90,110,110,120,100,120,110,110,90,0,0,0,0,
				0,0,0,90,100,120,130,110,130,120,100,90,0,0,0,0,
				0,0,0,90,100,120,125,120,125,120,100,90,0,0,0,0,
				0,0,0,80,110,125,90,70,90,125,110,80,0,0,0,0,
				0,0,0,70,80,90,80,70,80,90,80,70,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			},{//heiju
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,150,160,150,160,150,160,150,160,150,0,0,0,0,
				0,0,0,160,170,160,160,150,160,160,170,1600,0,0,0,0,
				0,0,0,170,180,170,170,160,170,170,180,170,0,0,0,0,
				0,0,0,170,190,180,220,240,220,200,190,170,0,0,0,0,
				0,0,0,180,220,210,240,250,240,210,2200,180,0,0,0,0,
				0,0,0,180,220,210,240,250,240,210,220,180,0,0,0,0,
				0,0,0,180,220,210,240,250,240,210,220,180,0,0,0,0,
				0,0,0,170,190,200,220,240,220,200,190,170,0,0,0,0,
				0,0,0,170,180,170,190,250,190,170,180,170,0,0,0,0,
				0,0,0,160,170,160,150,150,150,160,170,160,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			},{//heipao
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,80,90,80,70,60,70,80,90,80,0,0,0,0,
				0,0,0,80,90,80,70,65,70,80,90,80,0,0,0,0,
				0,0,0,90,100,80,80,70,80,80,100,90,0,0,0,0,
				0,0,0,90,100,90,90,110,90,90,100,90,0,0,0,0,
				0,0,0,90,100,90,110,130,110,90,100,90,0,0,0,0,
				0,0,0,90,110,90,110,130,110,90,110,90,0,0,0,0,
				0,0,0,90,110,90,110,130,110,90,110,90,0,0,0,0,
				0,0,0,100,120,90,80,80,80,90,120,100,0,0,0,0,
				0,0,0,110,125,100,70,60,70,100,125,110,0,0,0,0,
				0,0,0,125,130,100,70,60,70,100,130,125,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			},{//heibing
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,10,0,10,0,15,0,10,0,10,0,0,0,0,
				0,0,0,10,0,15,0,15,0,15,0,10,0,0,0,0,
				0,0,0,15,20,20,20,20,20,20,20,20,0,0,0,0,
				0,0,0,20,25,25,30,30,30,25,25,20,0,0,0,0,
				0,0,0,25,30,30,40,40,40,30,30,25,0,0,0,0,
				0,0,0,25,30,40,50,60,50,40,30,25,0,0,0,0,
				0,0,0,10,10,10,20,25,20,10,10,10,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
			}
		}
	};
	
	static short side=0;//side=0轮到红旗，
	final char legalPosition[]={
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,
			0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,
			0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,
			0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,
			0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,
			0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,
			0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,
			0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,
			0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,
			0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,	
	};
	
	public static short board[]={
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,0,39,37,35,33,32,34,36,38,40,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,41,0,0,0,0,0,42,0,0,0,0,0,
		0,0,0,43,0,44,0,45,0,46,0,47,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,0,27,0,28,0,29,0,30,0,31,0,0,0,0,
		0,0,0,0,25,0,0,0,0,0,26,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,0,23,21,19,17,16,18,20,22,24,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
	};//棋盘中棋子摆放位置
	final char kingPosition[]={
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,
			0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,
			0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,
			0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,
			0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
	};
	
	final char shiPosition[]={
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,	
	};
	
	final char xiangPosition[]={
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,
			0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,	
	};
	
	final char bingPosition[][]={
			{//红旗
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,
				0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,
				0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,
				0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,
				0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,
				0,0,0,1,0,1,0,1,0,1,0,1,0,0,0,0,
				0,0,0,1,0,1,0,1,0,1,0,1,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,	
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,	
			},{//黑棋
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,	
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,1,0,1,0,1,0,1,0,1,0,0,0,0,
				0,0,0,1,0,1,0,1,0,1,0,1,0,0,0,0,
				0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,
				0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,
				0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,
				0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,
				0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,	
			}
	};
}

class Move{
	/*public Move(short from,short to){
		this.from=from;
		this.to=to;
		capture=0;
	}*/
	
	public Move(short from,short to,short capture){
		this.from=from;
		this.to=to;
		this.capture=capture;
	}
	public short getFrom() {
		return from;
	}
	public short getTo() {
		return to;
	}
	public short getCapture(){
		return capture;
	}
	
	public void setFrom(short from) {
		this.from = from;
	}

	public void setTo(short to) {
		this.to = to;
	}

	public void setCapture(short capture) {
		this.capture = capture;
	}

	private short from,to ;
	private short capture;//保存被该步吃掉的棋子id
}