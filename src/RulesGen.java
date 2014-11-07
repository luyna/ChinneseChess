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
		//�ƶ����Ӻ��ж��Ƿ��ڱ�����״̬�������������߷�����
		boolean r=Check(side);
		//�ж�֮��Ҫ���ƶ���ԭ����Ϊ��Ҫ�ж������߷�
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
    //���ɲ���������߷�
	void maMove(short p,ArrayList<Move> list){//p�ǵ�ǰ������λ��
		short n,m;//��һ������λ�ú�����λ��
		int sideTag=16+side*16;//side=0,1;�췽16���ڷ�32
		for(int k=0;k<8;k++ ){
			n=(short) (p+maDir[k]);
			if(n>=0 && n<=256&&legalPosition[n]==1){ //�Ƿ���������
				m=(short) (p+maCheck[k]);
				if(board[m]==0){
					if((board[n] & sideTag) ==0){
						saveMove(p,n,list);
					}
				}
			}
		}
	}
	//˧���߷�
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
	//ʿ���߷�
	void shiMove(short p,ArrayList<Move> list){
		short n;
		int sideTag=16+side*16;
		for(int k=0;k<4;k++){
			n=(short) (p+shiDir[k]);
			if(n>=0 && n<=256&&shiPosition[n]!=0){
				if((board[n] & sideTag)==0){ //Ŀ��λ���ϲ��Ǳ�������
					saveMove(p,n,list);
				}
			}
		}
	}
	//����߷�
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
	//�����߷�
	void juMove(short p,ArrayList<Move> list){
		short n;
		int sideTag=16+side*16;
		for(int k=0;k<4;k++){
			for(int j=1;j<10;j++){//��������8������λ�ã����������9������λ��
				n=(short) (p+j*juDir[k]);
				if(n>=0 && n<=256){
					if(legalPosition[n]==0){//�������λ��
						break;
					}
					if(board[n]==0){//Ŀ��λ��������
						saveMove(p,n,list);
					}else if((board[n]&sideTag)!=0){//Ŀ��λ�����б�������
						break;
					}else{              //Ŀ��λ�����жԷ�����
						saveMove(p,n,list);
						break;
					}
				}
				
			}
		}
	}
	//�ڵ��߷�
	void paoMove(short p,ArrayList<Move> list){
		short n;
		int sideTag=16+side*16;
		int overFlag;
		for(int k=0;k<4;k++){
			overFlag=0;//��ʾδ��ɽ
			for(int j=1;j<10;j++){
				n=(short) (p+j*paoDir[k]);
				if(n>=0 && n<=256){
					if(legalPosition[n]==0){
						break;
					}
					if(board[n]==0){//Ŀ��λ��������
						if(overFlag==0)
							saveMove(p,n,list);//���ѷ�ɽ���������Զ�������һ��λ��
					}else{//Ŀ��λ��������
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
	
	//�����߷�
	void bingMove(short p,ArrayList<Move> list){
		short n;
		int sideTag=16+16*side;
		for(int k=0;k<3;k++){
			n=(short) (p+bingDir[side][k]);
			if(n>=0 && n<=256&&bingPosition[side][n]!=0){
				if((board[n]&sideTag)==0){//Ŀ��λ����û�б�������
					saveMove(p,n,list);
				}
			}
		}
	}
	
	/*
	 * Ѱ�ҵ�ǰ�������п����߷�
	 * ����������1��������������λ�ã���������ӵĻ����չ��������߷���Ч�ʽϵ�
	 * 2�������������飬�ҵ����������е����ӣ����ɿ����߷�
	 * 
	 */
	//һ���������̵õ������߷�
	int genAllMove1(ArrayList<Move> list){
		short p;
		int sideTag=16+side*16;//���巽���췽16���ڷ�32
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
	//���������������飬�������е����Ӵ���״̬�������Ӷ����Ǳ������������߷�
	int genAllMove2(ArrayList<Move> list){
		short p;
		list.clear();
		int sideTag=16+16*side;
		int pc;//����ID
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
		short p,n,m;//����λ�á���һ���������ߵ�λ�á����Ȼ�����λ��
		int sideTag;
		int overFlag;//��ɽ��־
		sideTag=16+16*side;
		p=piece[sideTag];
		if(p==0) return 0;

		//˧���߷�
		for(int k=0;k<4;k++){
			n=(short) (p+kingDir[k]);
			if(n>=0 && n<=256&&kingPosition[n]!=0){
				if((board[n]&sideTag)==0){
					saveMove(p,n,list);
				}
			}
		}
		
		//ʿ���߷�	
		for(int i=1;i<=2;i++){
			p=piece[sideTag+i];
			if(p==0) continue;
		    for(int k=0;k<4;k++){
		    	n=(short) (p+shiDir[k]);
		    	if(n>=0 && n<=256&&shiPosition[n]!=0){
		    		if((board[n] & sideTag)==0){ //Ŀ��λ���ϲ��Ǳ�������
		    			saveMove(p,n,list);
		    		}
		    	}
			}
		}

		//����߷�
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
				if(n>=0 && n<=256&&legalPosition[n]==1){ //�Ƿ���������
					m=(short) (p+maCheck[k]);
					if(board[m]==0){
						if((board[n] & sideTag) ==0){
							saveMove(p,n,list);
						}
					}
				}
			}
		}
			
		//�����߷�
		for(int i=7;i<=8;i++){
			p=piece[sideTag+i];
			if(p==0) continue;
			for(int k=0;k<4;k++){
				for(int j=1;j<10;j++){//��������8������λ�ã����������9������λ��
					n=(short) (p+j*juDir[k]);
					if(n>=0 && n<=256){
						if(legalPosition[n]==0){//�������λ��
							break;
						}
						if(board[n]==0){//Ŀ��λ��������
							saveMove(p,n,list);
						}else if((board[n]&sideTag)!=0){//Ŀ��λ�����б�������
							break;
						}else{              //Ŀ��λ�����жԷ�����
							saveMove(p,n,list);
							break;
						}
					}					
				}
			}	
		}
		
		//�ڵ��߷�
			for(int i=9;i<=10;i++){
				p=piece[sideTag+i];
				if(p==0) continue;
				for(int k=0;k<4;k++){
					overFlag=0;//��ʾδ��ɽ
					for(int j=1;j<10;j++){
						n=(short) (p+j*paoDir[k]);
						if(n>=0 && n<=256){
							if(legalPosition[n]==0){
								break;
							}
							if(board[n]==0){//Ŀ��λ��������
								if(overFlag==0)
									saveMove(p,n,list);//���ѷ�ɽ���������Զ�������һ��λ��
							}else{//Ŀ��λ��������
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
		//�����߷�
		for(int i=11;i<=15;i++){
			p=piece[sideTag+i];
			if(p==0) continue;	
			for(int k=0;k<3;k++){
				n=(short) (p+bingDir[side][k]);
				if(n>=0 && n<=256&&bingPosition[side][n]!=0){
					if((board[n]&sideTag)==0){//Ŀ��λ����û�б�������
						saveMove(p,n,list);
					}
				}
			}	
		}
		return list.size();
	}
	
	public  boolean HasLegalMove()		//�жϵ�ǰ�����Ƿ��к����߷���û��������
	{
		ArrayList<Move> list=new ArrayList<Move>();
		int num;
		num = GenAllMove(list);
		return num>0;
	}
	
	//���������Ϣ��piece��Ϣ��
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
	//pc:������piece���еĴ������֣���chessman��ID��posָ�����ӷ���һά�����е�λ���±�
	void addPiece(short pos,short pc){
		board[pos]= pc;
		piece[pc]=pos;
	}
	
	/*lside���߹�֮����
	 * ������⣺�Ӿ��й����Եı����������ڽ��зֱ��⣬+��˧����
	 */
	boolean Check(int lside){//���lsideһ���Ƿ񱻽������Ǳ���������true�����򷵻�false
		short rking,bking;//���˫����˧��λ��
		short p;
		boolean r;//r=true��ʾ����
		int sideTag=32-lside*16;//sideTag��ʾlside�ĶԷ�����ֵ
		int posadd;//λ������
		int fside=1-lside;//�Է���־
		rking=piece[16];
		bking=piece[32];
		if(rking==0 || bking==0) return false;
		r=true;
		//��⽫˧�Ƿ�����
		if(rking%16 == bking%16){
			for(rking=(short) (rking-16);rking!=bking;rking=(short) (rking-16)){
				if(board[rking]!=0){
					r=false;
					break;
				}
			}
			if(r) return r;
		}
		//����Ƿ�����
		short q=piece[48-sideTag];//lside������λ��
		short n,m;//��һ���������ߵ�λ�ú�����λ��
		for(int i=5;i<=6;i++){
			p=piece[sideTag+i];//�Է������ڵ�λ��
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
		
		//����Ƿ񱻳�����
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
		//����Ƿ��ڽ���
		int overFlag=0;//��ɽ��־
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
		//����Ƿ񱻱�����
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
	
	//��������
	int Evalue(){
		int bvalue=0,rvalue=0;
		short p,n,m;//����λ�ã���һ������λ�ã����Ȼ�����λ��
		int sideTag;
		int overFlag;//��ɽ��־
		int fvalue[]={0,0};//����Է�ֵ
		//���㵱ǰ�����д������ӵĹ̶�λ�ü�ֵ
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
		//��������Է�ֵ
		int k;
		for(int r=0;r<=1;r++){
			sideTag=16+16*r;
			//���������
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
			
			//ʿ�������
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
			//����߷��������
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
			
			//��������
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
			
			//���������
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
			
			//�ڵ������
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
									fvalue[r] +=3;//���ѷ�ɽ�����κδ����Զ�������һ��λ��
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
			//��������
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

	
	//��������
	public ArrayList<Move> moveStack=new ArrayList<Move>();//ִ�е��߷�ջ
	//int stacktop;//ջ��ָ�룬ָ��ջ��Ԫ�ص���һλ�ã�=0��ʾջ��
	static Move bestMove;//�����õ�������߷�
	int ply;//��ǰ�������
	static final int maxDepth=4;//����������
	final int maxValue=900000;//��zhi���ֵ
	public void minMaxSearch(int depth){//����С�����㷨
		moveStack.clear();
		ArrayList<Move> tmp=new ArrayList<Move>();
		if(side==0)
			minSearch(depth);
		else maxSearch(depth);
	}
	int maxSearch(int depth){//����������㷨
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
					System.out.println("zuidazhi:"+best+",from��"+bestMove.getFrom()+",to:"+bestMove.getTo());
					//System.out.println(list.toString());
					//System.out.println("num:"+num);
				}
			}
		}
		//System.out.println("zuidazhi:"+best);
		return best;
	}
	
	int minSearch(int depth){//��С�������㷨
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
					System.out.println("zuidazhi:"+best+",from��"+bestMove.getFrom()+",to:"+bestMove.getTo());
				}
				
			}
		}
		return best;
	}
	//��-�����������Ǳ�����
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
			if(value>beta) return beta;      //ע��˴�û�еȺ�
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
	//��������ֵת�����ַ���ʾ
	public char IdToChar(int a)
	{
		if(a <32)
		{
			switch(a)
			{
			case 16:	return '˧';
			case 17:
			case 18:	return '��';
			case 19:
			case 20:	return '��';
			case 21:
			case 22:	return '��';
			case 23:
			case 24:	return '��';
			case 25:
			case 26:	return '��';
			case 27:
			case 28:
			case 29:
			case 30:
			case 31:	return '��';
			default:	return 0;
			}
		}
		else
		{
			a = a-16;
			switch(a)
			{
			case 16:	return '��';
			case 17:
			case 18:	return 'ʿ';
			case 19:
			case 20:	return '��';
			case 21:
			case 22:	return '��';
			case 23:
			case 24:	return '��';
			case 25:
			case 26:	return '��';
			case 27:
			case 28:
			case 29:
			case 30:
			case 31:	return '��';
			default:	return 0;
			}
		}
	}
	
	boolean makeMove(Move m){         //ִ���߷�,ִ�к�����Ѿ��������򷵻�true�����򷵻�false
		short from,dest,p;
		int sideTag=(side==0?32:16);//�˴���ʾ�Է���˧��ֵ
		from=m.getFrom();
		dest=m.getTo();
		p=board[dest];
		//System.out.println("jianzao...1");
		Move newMove=new Move(from,dest,p);
		//System.out.println("jianzao...2");
		moveStack.add(newMove);
		//System.out.println("makemove.."+ply);
		//������������
		if(p>0){
			piece[p]=0;
		}
		piece[board[from]]=dest;
		//������������
		board[dest]=board[from];
		board[from]=0;
		
		ply++;
		changeSide();
		return p==sideTag;
	}
	
	void unMakeMove(){//�����߷�
		short from,dest,p;
		ply--;
		changeSide();
		int last=moveStack.size();
		Move tmp=moveStack.get(last-1);
		from=tmp.getFrom();
		dest=tmp.getTo();
		p=tmp.getCapture();
		//������������
		board[from]=board[dest];
		board[dest]=p;
		//������������
		if(p>0) piece[p]=dest;
		piece[board[from]]=from;
		
		moveStack.remove(last-1);
		
	}
	void changeSide(){//�任���巽
		side=(short) (1-side);
	}
	//player�����ж�λ��pos�Ƿ�������߹���
	int posIsValid(int id,int from,int to){ //����-1���򲻺Ϸ�������0�Ϸ�������1��ʾ���м��һ������
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
	
	//ArrayList<Move> moveList;//�߷�����
	//int moveNum;//�߷�����
	//��İ˸����з���������ʾ������λ�ñ�ʾ
	final short maDir[]={+0x0e,-0x12,-0x21,-0x1f,-0x0e,+0x12,+0x1f,+0x21};
	final short maCheck[]={-0x01,-0x01,-0x10,-0x10,+0x01,+0x01,+0x10,+0x10};
	
	//��˧���ĸ����з��������ʾ
	final short kingDir[]={-0x10,+0x01,+0x10,-0x01};
	//ʿ���ĸ����з�������
	final short shiDir[]={-0x11,-0x0f,+0x11,+0x0f};
	final short xiangDir[]={-0x22,-0x1e,+0x22,+0x1e};
	final short xiangCheck[]={-0x11,-0x0f,+0x11,+0x0f};//����λ��
	final short juDir[]={-0x01,-0x10,+0x01,+0x10};
	final short paoDir[]={-0x01,+0x01,-0x10,+0x10};
	final short bingDir[][]={{-0x01,+0x01,-0x10},{-0x01,+0x01,+0x10}};
	//����˫��ÿһ�����ӵ�λ�ã�������ӱ��Ե�������ֵΪ0���±�16-31����췽���ӵ�λ�ã�
	//32-47����ڷ����ӵ�λ�ã��±�0-15û��ʵ�����壬��chessman���е�����ID����һ��
	static short piece[]={
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			199,198,200,197,201,196,202,195,203,164,170,147,149,151,153,155,
			55,54,56,53,57,52,58,51,59,84,90,99,101,103,105,107
	};
	//����ÿ�����ӵļ�ֵ
	final short pieceValue[]={
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			1000,20,20,20,20,40,40,90,90,45,45,10,10,10,10,10,
			1000,20,20,20,20,40,40,90,90,45,45,10,10,10,10,10
	};
	//��������
	final short pieceNumToType[]={
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,1,1,2,2,3,3,4,4,5,5,6,6,6,6,6,
			0,1,1,2,2,3,3,4,4,5,5,6,6,6,6,6,
	};
	//���治ͬ�����ڲ�ͬλ�õķ�ֵ����һά��ʾ�߷���0�췽1�ڷ����ڶ�ά��ʾ��������
	//0-6�ֱ��ʾ��ʿ�����ڱ�������λ��ʾλ��
	static final short positionValues[][][]={
		{
			{//��˧
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
			},{//��ʿ
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
	
	static short side=0;//side=0�ֵ����죬
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
	};//���������Ӱڷ�λ��
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
			{//����
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
			},{//����
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
	private short capture;//���汻�ò��Ե�������id
}