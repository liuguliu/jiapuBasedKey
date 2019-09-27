package TextForDoc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class People {
	int id;	//���
	String name;	//����
	int fatherid;	//���ױ��
	int motherid;	//ĸ�ױ��
	String partnerid="";	//��ż���
	String CourtesyName="";	//��
	String pesudonym="";	//��
	String gender="��";	//�Ա�
	String ChineseBirthday;	//ũ����������
	String Chinesedeathdate;	//ũ����������
	String buried;	//����
	int familyrank;	//��ͥ����
	int generation;	//����
	String family="��";	//��������
	String description;	//��������
	//�����������
	int buriedindex = 0;	//�ж�����������Ϣ����λ��
	String fathername="";	
	String personInfo="";
	String spouseinfo="";
	String daughterinfo="";
	String soninfo="";
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return id+","+name+","+fatherid+","+motherid+","+partnerid+","+CourtesyName+","+pesudonym+","+gender+","+ChineseBirthday+","+Chinesedeathdate+","
				+buried+","+familyrank+","+generation+","+family+","+description;
	}
	
	public String toStringTest() {
		// TODO Auto-generated method stub
		return id+","+name+","+fatherid+","+partnerid+","+CourtesyName+","+pesudonym+","+ChineseBirthday+","+Chinesedeathdate+","
				+buried+","+familyrank+","+generation+","+fathername+","+daughterinfo+","+soninfo+","+description;
	}
	//��ȡ����
	public void getName(int i, String personinfo) {
		int indexSpace = personinfo.indexOf(' ');	//�����е�������ȷ��(�Ե�һ���ո�Ϊ����)
		name = personinfo.substring(0, indexSpace);
	}
	//��ȡ��ͥ���к͸�������
	public void getFatherNameAndRank(int i, String personinfo) {
		int indexStart = personinfo.indexOf(' ');
		int indexEnd = personinfo.indexOf("�� ");
		if(indexStart<indexEnd) {
			String fatherAndRank = personinfo.substring(indexStart, indexEnd+1);		
			int indexofRank = getFamilyRank(i, fatherAndRank);
			fathername = fatherAndRank.substring(1,indexofRank);
		}
		else {
			//Ϊʼ����ʽ����
		}
	}
	//��ȡ��
	public void getCourtesyName(int i, String personinfo) {
		String pattern = "��[\\u4E00-\\u9FA5][\\u4E00-\\u9FA5]";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(personinfo);
		while(m.find()) {
			String courtesyinfo =m.group(0).trim();
			CourtesyName =CourtesyName + courtesyinfo.substring(1)+"/";
		}
		if(CourtesyName!="") {
			CourtesyName = CourtesyName.substring(0,CourtesyName.length()-1);
		}		
	}
	//��ȡ��
	public void getpesudonym(int i, String personinfo) {
		String pattern = "��[\\u4E00-\\u9FA5][\\u4E00-\\u9FA5]";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(personinfo);
		while(m.find()) {
			String pesudonyminfo =m.group(0).trim();
			pesudonym =pesudonym + pesudonyminfo.substring(1)+"/";
		}
		if(pesudonym!="") {
			pesudonym = pesudonym.substring(0,pesudonym.length()-1);
		}
	}
	//��ȡũ����������
	public void getBirthday(int i, String personinfo) {
		String pattern = "\\S{0,5}��\\S{1,10}��\\S{1,10}��";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(personinfo);
		if(m.find()) {
			String birthdayinfo=m.group(0).trim();
			//System.out.println(birthdayinfo);
			ChineseBirthday = birthdayinfo.substring(0, birthdayinfo.length()-1);
		}
	}
	//��ȡũ����������
	public void getDeathdate(int i, String personinfo) {
		String pattern = "\\S{1,4}��\\S{1,10}��\\S{1,10}����";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(personinfo);
		if(m.find()) {
			buriedindex=m.start();
			String deathdateinfo=m.group(0).trim();
			Chinesedeathdate = deathdateinfo.substring(0, deathdateinfo.length()-2);
		}
	} 
	//��ȡ����
	public void getburied(int i, String personinfo) {
		if(personinfo.contains("������")||personinfo.contains("���� ��")) {
			String pattern = "���� *��\\S{1,200}[ | |��]";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(personinfo);
			if(m.find()) {
				if(buriedindex==0) {
					buriedindex=m.start();
				}
				buried = m.group(0).substring(2).trim();
			}else {
				buriedindex=personinfo.length();
			}
		}else {
			String pattern = "��\\S{0,5}��\\S{1,200} ";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(personinfo);
			if(m.find()) {
				if(buriedindex==0) {
					buriedindex=m.start();
				}
				buried = m.group(0).trim();
			}else {
				buriedindex=personinfo.length();
			}
		}
	}
	//�������ӹ�ϵ
	public void getFatherid(int i,People[] p) {
		for(int j=i-1;j>=0;j--) {
			if(p[j].name.contains(p[i].fathername)) {
				//System.out.println(p[j].toString());
				p[i].fatherid = p[j].id;
			}
		}
	}
	//��ȡ��ͥ���б�getFatherNameAndRank()��������
	public int getFamilyRank(int i, String fartherAndRank) {
		int indexofRank = -1;
		if(fartherAndRank.contains("����")) {
			familyrank=1;
			indexofRank = fartherAndRank.indexOf("����");
		}
		else if(fartherAndRank.contains("������")) {
			familyrank=1;
			indexofRank = fartherAndRank.indexOf("������");
		}
		else if(fartherAndRank.contains("������")) {
			familyrank=2;
			indexofRank = fartherAndRank.indexOf("������");
		}
		else if(fartherAndRank.contains("������")) {
			familyrank=3;
			indexofRank = fartherAndRank.indexOf("������");
		}
		else if(fartherAndRank.contains("������")) {
			familyrank=4;
			indexofRank = fartherAndRank.indexOf("������");
		}
		else if(fartherAndRank.contains("������")) {
			familyrank=5;
			indexofRank = fartherAndRank.indexOf("������");
		}
		else if(fartherAndRank.contains("������")) {
			familyrank=6;
			indexofRank = fartherAndRank.indexOf("������");
		}
		else if(fartherAndRank.contains("������")) {
			familyrank=7;
			indexofRank = fartherAndRank.indexOf("������");
		}
		else if(fartherAndRank.contains("������")) {
			familyrank=8;
			indexofRank = fartherAndRank.indexOf("������");
		}
		else if(fartherAndRank.contains("֮��")||fartherAndRank.contains("����")||fartherAndRank.contains("����")) {
			familyrank=1;
			if(fartherAndRank.indexOf("֮��")!=-1) {
				indexofRank = fartherAndRank.indexOf("֮��");
			}
			else if(fartherAndRank.indexOf("����")!=-1) {
				indexofRank = fartherAndRank.indexOf("����");
			}
			else {
				indexofRank = fartherAndRank.indexOf("����");
			}
		}
		else if(fartherAndRank.contains("����")) {
			familyrank=2;
			indexofRank = fartherAndRank.indexOf("����");
		}
		else if(fartherAndRank.contains("����")) {
			familyrank=3;
			indexofRank = fartherAndRank.indexOf("����");
		}
		else if(fartherAndRank.contains("����")) {
			familyrank=4;
			indexofRank = fartherAndRank.indexOf("����");
		}
		else if(fartherAndRank.contains("����")) {
			familyrank=5;
			indexofRank = fartherAndRank.indexOf("����");
		}
		else if(fartherAndRank.contains("����")) {
			familyrank=6;
			indexofRank = fartherAndRank.indexOf("����");
		}
		else if(fartherAndRank.contains("����")) {
			familyrank=7;
			indexofRank = fartherAndRank.indexOf("����");
		}
		else if(fartherAndRank.contains("����")) {
			familyrank=8;
			indexofRank = fartherAndRank.indexOf("����");
		}
		else if(fartherAndRank.contains("����")) {
			familyrank=9;
			indexofRank = fartherAndRank.indexOf("����");
		}
		else if(fartherAndRank.contains("ʮ��")) {
			familyrank=10;
			indexofRank = fartherAndRank.indexOf("ʮ��");
		}else {
			familyrank=1;
			indexofRank = fartherAndRank.length()-1;
		}
		return indexofRank;
	}
	
	public int getWifeAndChildrenInfo(int TreeNUM, int personnum, People[] p) {
		
		String wifeinfo = description;		
		int k =0;	//��¼������
		int[] wifes = {0,0,0,0,0,0,0,0,0,0};	//��¼��i������������Ϣ�������ַ����е�λ��
		int[] wifesid = {0,0,0,0,0,0,0,0,0,0};	//��¼��i�����ӵı��
		String pattern = "[��|��|��]{0,1}[Ȣ|��|��][^��|��|��ڣ|����δ��|����].*?[Ů|��|����]";
		Pattern rPattern = Pattern.compile(pattern);
		Matcher m = rPattern.matcher(wifeinfo);
		
		while(m.find()) {
			wifes[k] = m.start();			
			if(k>0) {	//��ȡ��һ�����ӵ�������Ϣ����Ӧ��Ů��Ϣ(��ֻ��һλ�����ⲿ�ֲ�ִ��)
				String wifeAndChildren = wifeinfo.substring(wifes[k-1], wifes[k]);
				getWifeBirthday(wifesid[k-1], wifeAndChildren, p);
				getWifeDeathdate(wifesid[k-1], wifeAndChildren, p);
				getwifeburied(wifesid[k-1], wifeAndChildren, p);
				personnum = getChildrenInfo(TreeNUM, personnum, p[wifesid[k-1]].id, wifeAndChildren,p);
			}
			p[personnum] = new People();
			p[personnum].id = personnum+1;
			p[personnum].gender = "Ů";
			p[personnum].generation = generation;
			int indexpei=-1;
			if(m.group(0).contains("��")) {
				indexpei= m.group(0).indexOf('��');							
			}else if(m.group(0).contains("Ȣ")){
				indexpei= m.group(0).indexOf('Ȣ');
			}else {
				indexpei= m.group(0).indexOf('��');
			}
			p[personnum].name = m.group(0).substring(indexpei+1);	
			if(indexpei>0) {
				p[personnum].description=m.group(0).substring(0,indexpei+1);
			}
			p[personnum].partnerid = String.valueOf(id);
			if(partnerid!="") {
				partnerid=partnerid+"/"+String.valueOf(p[personnum].id);
			}else {
				partnerid=String.valueOf(p[personnum].id);
			}
			wifesid[k] = personnum;
			
			personnum++;
			k++;			
		}	//while
		//��ȡ���һλ���ӻ�Ψһ����λ���Ӽ����Ů��Ϣ
		if(k>0) {
			if(wifes[k-1]<buriedindex) {
				String wifeAndChildren = wifeinfo.substring(wifes[k-1],buriedindex);
				getWifeBirthday(wifesid[k-1], wifeAndChildren, p);
				getWifeDeathdate(wifesid[k-1], wifeAndChildren, p);
				getwifeburied(wifesid[k-1], wifeAndChildren, p);
				
				personnum = getChildrenInfo(TreeNUM, personnum, p[wifesid[k-1]].id, wifeAndChildren, p); 
			}else {
				//������һ������
			}
		}
//		else  if(k==1){
//			//System.out.println(wifes[k-1]+" "+p[ipartner].buriedindex);
//			String wifeAndChildren = wifeinfo.substring(wifes[k-1],p[ipartner].buriedindex);			
//			System.out.println(wifeAndChildren);
//			getBirthday(wifesid[k-1], wifeAndChildren);
//			getWifeDeathdate(wifesid[k-1], wifeAndChildren);
//			getwifeburied(wifesid[k-1], wifeAndChildren);
//			getChildrenInfo(ipartner,wifesid[k-1], wang, wifeAndChildren); 
//		}
		else {
			//��������Ϣ
			personnum = getChildrenInfo(TreeNUM, personnum, -1, wifeinfo, p);
		}
		return personnum;
	}
	//��ȡ��Ů��Ϣ
	public int getChildrenInfo(int TreeNUM, int personnum, int imotherid, String childreninfo, People[] p) {
		
		String patterns = "��[һ|��|��|��|��|��|��|��|��|ʮ][^һ|��|��|��|��|��|��|��|��|ʮ]";
		Pattern rs = Pattern.compile(patterns);
		Matcher ms = rs.matcher(childreninfo);
		while(ms.find()) {
			int indexofson = ms.start();
			String sonsinfo = childreninfo.substring(indexofson+2).trim();
			personnum = getSon(TreeNUM, personnum,imotherid, ms.group(0), sonsinfo, p);
		}
		
		String patternd = "Ů[һ|��|��|��|��|��|��|��|��|ʮ]";
		Pattern rd = Pattern.compile(patternd);
		Matcher md = rd.matcher(childreninfo);
		while(md.find()) {
			int indexofdaughter = md.start();
			String daughtersinfo = childreninfo.substring(indexofdaughter+2).trim();
			personnum = getDaughter(personnum, imotherid, md.group(0),daughtersinfo, p);
		}
		return personnum;
	}
	//��ȡ������Ϣ
	public int getSon(int TreeNUM, int personnum,int imotherid,String ms, String sonsinfo, People[] p) {
		int childrennum= getchildrenNum(ms);
		//System.out.println("childrennum:"+childrennum);
		String[] sons = sonsinfo.split(" ");
		if(childrennum<sons.length) {
			for(int j=0;j<childrennum;j++) {
				//System.out.print(sons[j]+" ");
				String nameson="";
    			String description = "";
    			//��ȡ����������
    			if(sons[j].contains("��")) {
    				nameson=sons[j].substring(0, sons[j].indexOf("��")-1);
    				description = sons[j].substring(sons[j].indexOf("��")+1,sons[j].length()-1);
    			}else if(sons[j].length()>2){
    				nameson = sons[j].substring(0, 2);
    				description = sons[j].substring(2);
    			}else {
    				nameson = sons[j];
    			}
    			//�ж��Ƿ����
    			boolean flag=false;
    			for(int k=0;k<TreeNUM;k++) {
    				if((p[k].name.equals(nameson)||p[k].name.equals(GlobalVar.generationRank[generation]+nameson))
    						&&name.contains(p[k].fathername)) {
    					p[k].motherid = imotherid;
    					flag=true;
    					break;
    				}
    			}
    			if(!flag) {	//��������
    				p[personnum] = new People();
    				p[personnum].id = personnum+1;
    				if(nameson.length()==1) {
    					p[personnum].name = nameson;
    				}else {
    					p[personnum].name = nameson;
    				}
    				p[personnum].generation=generation+1;
    				p[personnum].familyrank = j+1;
    				p[personnum].fatherid = id;
    				p[personnum].motherid = imotherid; 
    				p[personnum].description = description;
    				//System.out.println(p[personnum].toString());
    				personnum++;
    			}else {
					continue;
				}
    		}	//for
		}	//if		
		return personnum;
	}
	//��ȡŮ����Ů����Ϣ
	public int getDaughter(int personnum,int imotherid, String ms, String daughtersinfo, People[] p) {
		int childrennum= getchildrenNum(ms);
		if(!daughtersinfo.contains(" ")) {
			for(int j=0;j<childrennum;j++) {
				p[personnum] = new People();
				p[personnum].id = personnum+1;
				p[personnum].name = "Ů"+GlobalVar.cNum[j];
				p[personnum].gender="Ů";
				p[personnum].familyrank=j+1;
				p[personnum].generation=generation+1;
				p[personnum].fatherid = id;
				p[personnum].motherid = imotherid;
				if(daughtersinfo.contains(")")){
					p[personnum].description = "ز";
				}
				//System.out.println(p[num].toString());
				personnum++;
			}
		}else {
			String[] daughters = daughtersinfo.split(" ");
			if(childrennum<daughters.length) {
				for(int j=0;j<childrennum;j++) {
					if(daughters[j].contains(GlobalVar.rankNum[j]+"��")||daughters[j].charAt(0)=='��') {
						p[personnum]=new People();
						p[personnum].id=personnum+1;
						p[personnum].name="Ů"+GlobalVar.cNum[j];
						p[personnum].gender="Ů";
						p[personnum].familyrank=j+1;
						p[personnum].generation=generation+1;
						p[personnum].fatherid = id;
	        			p[personnum].motherid = imotherid;       				
	    				if(daughters[j].contains(GlobalVar.rankNum[j]+"��")) {
	    					p[personnum].description = daughters[j].substring(1);
	    				}else {
	    					p[personnum].description = daughters[j];
	    				}
	    				p[personnum].partnerid = String.valueOf(personnum+2);
	    				//System.out.println(p[num].toString());
	    				personnum++;
	    				//��
	    				if(daughters[j].length()>1) {
	    					p[personnum]=new People();
	    					p[personnum].id=personnum+1;
	    					if(daughters[j].contains(GlobalVar.rankNum[j]+"��")) {
	    						//System.out.println(daughters[j]);
	    						p[personnum].name=daughters[j].substring(2);
	        				}else {
	        					p[personnum].name=daughters[j].substring(1);
	        				}
	    					p[personnum].partnerid = String.valueOf(personnum);
	    					personnum++;
	    				}else {
	    					//��һ���֣���������Ϣ
	    				}
	    				
					}else  {
						p[personnum]=new People();
						p[personnum].id=personnum+1;
						p[personnum].name="Ů"+GlobalVar.cNum[j];
						p[personnum].gender="Ů";
						p[personnum].familyrank=j+1;
						p[personnum].generation=generation+1;
						p[personnum].fatherid = id;   				
						p[personnum].motherid = imotherid; 
	    				if(daughters[j].contains("(�|)")||daughters[j].contains("(ز)")) {
	    					p[personnum].description = "ز";
	    				}
	    				//System.out.println(p[num].toString());
	    				personnum++;
					}
				}
			}		
		}
		return personnum;
	}
	//��ȡ��Ů�����������ַ���������һ����һ��Ů����
	public int getchildrenNum(String ms) {
		int num=0;
		char NUM= ms.charAt(1);
		switch (NUM) {
			case 'һ':num=1;break;
			case '��':num=2;break;
			case '��':num=3;break;
			case '��':num=4;break;
			case '��':num=5;break;
			case '��':num=6;break;
			case '��':num=7;break;
			case '��':num=8;break;
			case '��':num=9;break;
			case 'ʮ':num=10;break;
			default:num=0;break;
		}
		return num;
	}
	//���ӳ�������
	public void getWifeBirthday(int i, String wifeinfo, People[] p) {
		String pattern = "[ͬ����|\\S{0,5}��]\\S{1,10}��\\S{1,10}��";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(wifeinfo);
		if(m.find()) {
			String birthdayinfo=m.group(0).trim();
			p[i].ChineseBirthday = birthdayinfo.substring(0, birthdayinfo.length()-1);
		}
	}
	//���ӹ�������
	public void getWifeDeathdate(int i, String wifeinfo, People[] p) {
		String pattern = "\\S{1,4}��\\S{1,15}����";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(wifeinfo);
		if(m.find()) {
			String deathdateinfo=m.group(0).trim();
			p[i].Chinesedeathdate = deathdateinfo.substring(0, deathdateinfo.length()-2);
		}
	} 
	//��������
	public void getwifeburied(int i, String wifeinfo, People[] p) {
		String pattern = "��\\S{0,5}��\\S{0,5}��\\S{1,200} ";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(wifeinfo);
		if(m.find()) {
			if(p[i].buriedindex==0) {
				p[i].buriedindex=m.start();
			}
			p[i].buried = m.group(0).trim();
		}
}
}
