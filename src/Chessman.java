import javax.swing.ImageIcon;
 


public class Chessman {
	public Chessman(){
		id=0;
		pos=0;
	}
	public Chessman(short id,short pos){
		this.id=id;
		this.pos=pos;
//		this.x=x;
//		this.y=y;
		switch(id){
		//�췽������ʿ���࣬�������ڣ�����˳��
			case 16:pic=new ImageIcon("image/red-jiang.gif");break;
			case 17:
			case 18:pic=new ImageIcon("image/red-shi.gif");break;
			case 19:
			case 20:pic=new ImageIcon("image/red-xiang.gif");break;
			case 21:
			case 22:pic =new ImageIcon("image/red-ma.gif");break;
			case 23:
			case 24:pic=new ImageIcon("image/red-ju.gif");break;
			case 25:
			case 26:pic=new ImageIcon("image/red-pao.gif");break;
			case 27:
			case 28:
			case 29:
			case 30:
			case 31:pic=new ImageIcon("image/red-bing.gif");break;
			 
		//�ڷ�
			case 32:pic=new ImageIcon("image/black-jiang.gif");break;
			case 33:
			case 34:pic=new ImageIcon("image/black-shi.gif");break;
			case 35:
			case 36:pic=new ImageIcon("image/black-xiang.gif");break;
			case 37:
			case 38:pic =new ImageIcon("image/black-ma.gif");break;
			case 39:
			case 40:pic=new ImageIcon("image/black-ju.gif");break;
			case 41:
			case 42:pic=new ImageIcon("image/black-pao.gif");break;
			case 43:
			case 44:
			case 45:
			case 46:
			case 47:pic=new ImageIcon("image/black-bing.gif");break;
			     
			
		}
			
	}
	//�����ӷ��ڵ�a��b��
//	public void drawAtPos(short a,short b){
//		JLabel tmp=new JLabel(pic);
//		
//	}
	
	
	public ImageIcon getPic(){
		return pic;
	}
	public short getPos(){
		return pos;
	}
	
	public short getId(){
		return id;
	}
	private short id;//����id,
	private ImageIcon pic;//����ͼ��
	private short pos;//�Ե����ӵ�λ��

}
