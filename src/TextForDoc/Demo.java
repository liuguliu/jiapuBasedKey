package TextForDoc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Demo {
	static String generationRank[] = {"һ","��","��","��","��","��","��","��","��","ʮ","ʮһ","ʮ��","��","��","��","��","��","ʢ","ʱ"};
	static String cNum[] = {"һ","��","��","��","��","��","��","��","��","ʮ"};
	static String rankNum[] = {"","��","��","��","��","��","��","��","��","��","ʮ"};
	static int Generation = -1;
	static int personnum = 0;
	static People p[] = new People[1500];
	
	public static void main(String[] args) throws IOException {
		
		String text = "";
		File file = new File("data/01��һ�������ٹ�121.doc.txt");
		FileReader filereader = new FileReader(file);
		BufferedReader bufferedreader = new BufferedReader(filereader);
		String line="";
		while((line=bufferedreader.readLine())!=null){
            text+=line+"\r";
        }
		String[] lines = text.split("\r");
        for(int i=0; i<lines.length; i++) {	//1 ѭ����ȡÿһ�е�����
        	//System.out.println(lines[i]);
        	if(getGeneration(lines[i])!=-1) {
        		Generation = getGeneration(lines[i]);
        		//System.out.println(Generation);
        	}else {
        		p[personnum] = new People();	//�½�����
            	p[personnum].id = personnum+1; 
            	p[personnum].generation = Generation;
            	p[personnum].description = lines[i];
            	getName(personnum, lines[i]);
            	getFatherNameAndRank(personnum, lines[i]);
            	getCourtesyName(personnum, lines[i]);
            	getpesudonym(personnum, lines[i]);
            	getBirthday(personnum, lines[i]);
            	getDeathdate(personnum, lines[i]);
            	getburied(personnum, lines[i]);
            	//System.out.println(p[num].toStringTest());
            	personnum++;
        	}
        }
        for(int i=0; i<personnum; i++) {
        	getFatherid(i);
        }
        int wang = personnum;
        for(int i=0; i<wang; i++) {	//ѭ����ȡ������ż��Ϣ����Ů��Ϣ
        	getWifeInfo(i, p[i].description, wang);
        	//System.out.println(p[i].toString());
        }
//        for(int i=0; i<wang; i++) {	//ѭ����ȡ������ż��Ϣ����Ů��Ϣ
//        	getChildrenInfo(i, wang, p[i].description);
//        	//System.out.println(p[i].toString());
//        }
        for(int i=0; i<personnum; i++) {
        	System.out.println(p[i].toString());
        }
	}
	public static void getWifeInfo(int ipartner, String wifeinfo, int wang) {
		//System.out.println();
		//System.out.println(p[ipartner].name+":");
		String pattern = "[��|��|��]{0,1}[Ȣ|��].*?[Ů|��]";
		Pattern rPattern = Pattern.compile(pattern);
		Matcher m = rPattern.matcher(wifeinfo);
		int k =0;
		int[] wifes = {0,0,0,0,0,0,0,0,0,0};
		int[] wifesid = {0,0,0,0,0,0,0,0,0,0};
		while(m.find()) {
			//System.out.println(m.group(0));
			wifes[k] = m.start();			
			if(k>0) {	//��ȡ��һ�����ӵ�������Ϣ����Ӧ��Ů��Ϣ(��ֻ��һλ�����ⲿ�ֲ�ִ��)
				String wifeAndChildren = wifeinfo.substring(wifes[k-1], wifes[k]);
				getChildrenInfo(ipartner,wifesid[k-1], wang, wifeAndChildren);
			}
			p[personnum] = new People();
			p[personnum].id = personnum+1;
			p[personnum].gender = "Ů";
			int indexpei=-1;
			if(m.group(0).contains("��")) {
				indexpei= m.group(0).indexOf('��');							
			}else {
				indexpei= m.group(0).indexOf('Ȣ');
			}
			p[personnum].name = m.group(0).substring(indexpei+1);	
			if(indexpei>0) {
				p[personnum].description=m.group(0).substring(0,indexpei+1);
			}
			p[personnum].partnerid = String.valueOf(ipartner);
			if(p[ipartner].partnerid!="") {
				p[ipartner].partnerid=p[ipartner].partnerid+"/"+String.valueOf(p[personnum].id);
			}else {
				p[ipartner].partnerid=String.valueOf(p[personnum].id);
			}
			wifesid[k] = p[personnum].id;
			
			personnum++;
			k++;			
		}	//while
		//��ȡ���һλ���ӻ�Ψһ����λ���Ӽ����Ů��Ϣ
		if(k>1) {
			String wifeAndChildren = wifeinfo.substring(wifes[k-1]);
			getChildrenInfo(ipartner,wifesid[k-1], wang, wifeAndChildren); 
		}else  if(k==1){
			String wifeAndChildren = wifeinfo.substring(wifes[k-1]);
			getChildrenInfo(ipartner,wifesid[k-1], wang, wifeAndChildren); 
		}else {
			//��������Ϣ
			getChildrenInfo(ipartner,-1, wang, wifeinfo);
		}
	}
	public static void getChildrenInfo(int ifatherid,int imotherid, int wang, String childreninfo) {
		//System.out.println();
		//System.out.println(p[ifatherid].name+":");
		String patterns = "��[һ|��|��|��|��|��|��|��|��|ʮ][^һ|��|��|��|��|��|��|��|��|ʮ]";
		Pattern rs = Pattern.compile(patterns);
		Matcher ms = rs.matcher(childreninfo);
		while(ms.find()) {
			int indexofson = ms.start();
			String sonsinfo = childreninfo.substring(indexofson+2).trim();
			getSon(ifatherid,imotherid,wang, ms.group(0),sonsinfo);
		}
		
		String patternd = "Ů[һ|��|��|��|��|��|��|��|��|ʮ]";
		Pattern rd = Pattern.compile(patternd);
		Matcher md = rd.matcher(childreninfo);
		while(md.find()) {
			int indexofdaughter = md.start();
			String daughtersinfo = childreninfo.substring(indexofdaughter+2).trim();
			getDaughter(ifatherid, imotherid, md.group(0),daughtersinfo);
		}
		
	}
	public static void getSon(int ifatherid,int imotherid,int wang, String ms, String sonsinfo) {
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
    				nameson=sons[j].substring(0, sons[j].indexOf("��"));
    				description = sons[j].substring(sons[j].indexOf("��")+1,sons[j].length()-1);
    			}else if(sons[j].length()>2){
    				nameson = sons[j].substring(0, 2);
    				description = sons[j].substring(2);
    			}else {
    				nameson = sons[j];
    			}
    			//�ж��Ƿ����
    			boolean flag=false;
    			for(int k=0;k<wang;k++) {
    				if((p[k].name.equals(nameson)||p[k].name.equals(generationRank[p[ifatherid].generation]+nameson))
    						&&p[k].fathername.equals(p[ifatherid].name)) {
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
    				p[personnum].generation=p[ifatherid].generation+1;
    				p[personnum].familyrank = j+1;
    				p[personnum].fatherid = p[ifatherid].id;
    				p[personnum].motherid = imotherid; 
    				p[personnum].description = description;
    				//System.out.println(p[personnum].toString());
    				personnum++;
    			}else {
					continue;
				}
    		}	//for
		}	//if		
	}
	public static void getDaughter(int ifatherid,int imotherid, String ms, String daughtersinfo) {
		int childrennum= getchildrenNum(ms);
		//System.out.println("num:"+childrennum);
		String[] daughters = daughtersinfo.split(" ");
		if(childrennum<daughters.length) {
			for(int j=0;j<childrennum;j++) {
			//System.out.print(daughters[j]+" ");
				if(daughters[j].contains(rankNum[j]+"��")||daughters[j].charAt(0)=='��') {
					p[personnum]=new People();
					p[personnum].id=personnum+1;
					p[personnum].name="Ů"+cNum[j];
					p[personnum].gender="Ů";
					p[personnum].familyrank=j+1;
					p[personnum].generation=p[ifatherid].generation+1;
					p[personnum].fatherid = p[ifatherid].id;
        			p[personnum].motherid = imotherid;       				
    				if(daughters[j].contains(rankNum[j]+"��")) {
    					p[personnum].description = daughters[j].substring(1);
    				}else {
    					p[personnum].description = daughters[j];
    				}
    				p[personnum].partnerid = String.valueOf(personnum+2);
    				//System.out.println(p[num].toString());
    				personnum++;
    				//��
    				p[personnum]=new People();
					p[personnum].id=personnum+1;
					if(daughters[j].contains(rankNum[j]+"��")) {
						//System.out.println(daughters[j]);
						p[personnum].name=daughters[j].substring(2);
    				}else {
    					p[personnum].name=daughters[j].substring(1);
    				}
					p[personnum].partnerid = String.valueOf(personnum);
					personnum++;
				}else  {
					p[personnum]=new People();
					p[personnum].id=personnum+1;
					p[personnum].name="Ů"+cNum[j];
					p[personnum].gender="Ů";
					p[personnum].familyrank=j+1;
					p[personnum].generation=p[ifatherid].generation+1;
					p[personnum].fatherid = p[ifatherid].id;   				
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
	public static int getchildrenNum(String ms) {
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
	public static void getBirthday(int i, String personinfo) {
		String pattern = "\\S{1,5}��\\S{1,10}��\\S{1,10}��";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(personinfo);
		if(m.find()) {
			String birthdayinfo=m.group(0).trim();
			p[i].ChineseBirthday = birthdayinfo.substring(0, birthdayinfo.length()-1);
			//System.out.println(birthdayinfo);
		}
	}
	public static void getDeathdate(int i, String personinfo) {
		String pattern = "\\S{1,4}��\\S{1,10}��\\S{1,10}����";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(personinfo);
		if(m.find()) {
			String deathdateinfo=m.group(0).trim();
			p[i].Chinesedeathdate = deathdateinfo.substring(0, deathdateinfo.length()-2);
		}
	} 
	public static void getburied(int i, String personinfo) {
		if(personinfo.contains("������")||personinfo.contains("���� ��")) {
			String pattern = "���� *��\\S{1,200} ";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(personinfo);
			if(m.find()) {
				p[i].buried = m.group(0).substring(2).trim();
			}
		}else {
			String pattern = "��\\S{1,5}��\\S{1,200} ";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(personinfo);
			if(m.find()) {
				p[i].buried = m.group(0).trim();
			}
		}
	}
	public static void getCourtesyName(int i, String personinfo) {
		String pattern = "��[\\u4E00-\\u9FA5][\\u4E00-\\u9FA5]";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(personinfo);
		if(m.find()) {
			String courtesyinfo =m.group(0).trim();
			p[i].CourtesyName = courtesyinfo.substring(1);
//			if(courtesyinfo.contains("����")) {
//				int indexofYou = courtesyinfo.indexOf("����");
//				p[i].CourtesyName = courtesyinfo.substring(1, indexofYou);
//				p[i].CourtesyName = p[i].CourtesyName + "/"+courtesyinfo.substring(indexofYou+2);
//			}
//			else {
//				p[i].CourtesyName = courtesyinfo.substring(1);
//			}
		}
	}
	public static void getpesudonym(int i, String personinfo) {
		String pattern = "��[\\u4E00-\\u9FA5][\\u4E00-\\u9FA5]";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(personinfo);
		if(m.find()) {
			p[i].pesudonym=m.group(0).trim().substring(1);
		}
	}
	public static void getFatherid(int i) {
		for(int j=i-1;j>=0;j--) {
			if(p[j].name.equals(p[i].fathername)) {
				//System.out.println(p[j].toString());
				p[i].fatherid = p[j].id;		
			}
		}
	}
	public static int getGeneration(String line) {
		int num = -1;
		String pattern = "��.��";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		if(m.find()) {
			switch (m.group(0)) {
				case "��һ��":num=1;break;
				case "�ڶ���":num=2;break;
				case "������":num=3;break;
				case "������":num=4;break;
				case "������":num=5;break;
				case "������":num=6;break;
				case "������":num=7;break;
				case "�ڰ���":num=8;break;
				case "�ھ���":num=9;break;
				case "��ʮ��":num=10;break;
				case "��ʮһ��":num=11;break;
				case "��ʮ����":num=12;break;
				case "��ʮ����":num=13;break;
				case "��ʮ����":num=14;break;
				case "��ʮ����":num=15;break;
				case "��ʮ����":num=16;break;
				case "��ʮ����":num=17;break;
				case "��ʮ����":num=18;break;
				case "��ʮ����":num=19;break;	
				case "�ڶ�ʮ��":num=20;break;
				case "�ڶ�ʮһ��":num=21;break;
				case "�ڶ�ʮ����":num=22;break;
				case "�ڶ�ʮ����":num=23;break;
				case "�ڶ�ʮ����":num=24;break;
				case "�ڶ�ʮ����":num=25;break;
				case "�ڶ�ʮ����":num=26;break;
				case "�ڶ�ʮ����":num=27;break;
				case "�ڶ�ʮ����":num=28;break;
				case "�ڶ�ʮ����":num=29;break;				
				case "����ʮ��":num=30;break;
				case "����ʮһ��":num=31;break;
				case "����ʮ����":num=32;break;
				case "����ʮ����":num=33;break;
				case "����ʮ����":num=34;break;
				case "����ʮ����":num=35;break;
				case "����ʮ����":num=36;break;
				case "����ʮ����":num=37;break;
				case "����ʮ����":num=38;break;
				case "����ʮ����":num=39;break;
				case "����ʮ��":num=40;break;
				default:num=-1;break;
			}
		}		
		return num;
	}
	public static void getName(int i, String personinfo) {
		int indexSpace = personinfo.indexOf(' ');	//�����е�������ȷ��
		p[i].name = personinfo.substring(0, indexSpace);
	}
	public static void getFatherNameAndRank(int i, String personinfo) {
		int indexStart = personinfo.indexOf(' ');
		int indexEnd = personinfo.indexOf("�� ");
		String fatherAndRank = personinfo.substring(indexStart, indexEnd+1);		
		int indexofRank = getFamilyRank(i, fatherAndRank);
		//System.out.println(indexofRank);
		p[i].fathername = fatherAndRank.substring(1,indexofRank);

	}
	public static int getFamilyRank(int i, String fartherAndRank) {
		int indexofRank = -1;
		if(fartherAndRank.contains("������")) {
			p[i].familyrank=1;
			indexofRank = fartherAndRank.indexOf("������");
		}
		else if(fartherAndRank.contains("������")) {
			p[i].familyrank=2;
			indexofRank = fartherAndRank.indexOf("������");
		}
		else if(fartherAndRank.contains("������")) {
			p[i].familyrank=3;
			indexofRank = fartherAndRank.indexOf("������");
		}
		else if(fartherAndRank.contains("������")) {
			p[i].familyrank=4;
			indexofRank = fartherAndRank.indexOf("������");
		}
		else if(fartherAndRank.contains("������")) {
			p[i].familyrank=5;
			indexofRank = fartherAndRank.indexOf("������");
		}
		else if(fartherAndRank.contains("������")) {
			p[i].familyrank=6;
			indexofRank = fartherAndRank.indexOf("������");
		}
		else if(fartherAndRank.contains("������")) {
			p[i].familyrank=7;
			indexofRank = fartherAndRank.indexOf("������");
		}
		else if(fartherAndRank.contains("������")) {
			p[i].familyrank=8;
			indexofRank = fartherAndRank.indexOf("������");
		}
		else if(fartherAndRank.contains("֮��")||fartherAndRank.contains("����")||fartherAndRank.contains("����")) {
			p[i].familyrank=1;
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
			p[i].familyrank=2;
			indexofRank = fartherAndRank.indexOf("����");
		}
		else if(fartherAndRank.contains("����")) {
			p[i].familyrank=3;
			indexofRank = fartherAndRank.indexOf("����");
		}
		else if(fartherAndRank.contains("����")) {
			p[i].familyrank=4;
			indexofRank = fartherAndRank.indexOf("����");
		}
		else if(fartherAndRank.contains("����")) {
			p[i].familyrank=5;
			indexofRank = fartherAndRank.indexOf("����");
		}
		else if(fartherAndRank.contains("����")) {
			p[i].familyrank=6;
			indexofRank = fartherAndRank.indexOf("����");
		}
		else if(fartherAndRank.contains("����")) {
			p[i].familyrank=7;
			indexofRank = fartherAndRank.indexOf("����");
		}
		else if(fartherAndRank.contains("����")) {
			p[i].familyrank=8;
			indexofRank = fartherAndRank.indexOf("����");
		}
		else if(fartherAndRank.contains("����")) {
			p[i].familyrank=9;
			indexofRank = fartherAndRank.indexOf("����");
		}
		else if(fartherAndRank.contains("ʮ��")) {
			p[i].familyrank=10;
			indexofRank = fartherAndRank.indexOf("ʮ��");
		}else {
			p[i].familyrank=1;
			indexofRank = fartherAndRank.length()-1;
		}
		return indexofRank;
	}
	public static void addExcel(String path, List<People> list) {		
		//id+","+name+","+fatherid+","+motherid+","+partnerid+","+CourtesyName+","+pesudonym+","+gender+","+ChineseBirthday+","+Chinesedeathdate+","
				//+buried+","+familyrank+","+generition+","+family+","+description;
		try {
			WritableWorkbook wb = null;		
			// ������д���Excel������		
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			// ��fileNameΪ�ļ���������һ��Workbook
			wb = Workbook.createWorkbook(file);
			// ����������
			WritableSheet ws = wb.createSheet("Sheet0", 0);
			// Ҫ���뵽��Excel�����кţ�Ĭ�ϴ�0��ʼ
			Label labelId = new Label(0, 0, "���");
			Label labelName = new Label(1, 0, "����");
			Label labelfatherid = new Label(2,0,"���ױ��");
			Label labelmotherid = new Label(3,0,"ĸ�ױ��");
			Label labelpartnerid = new Label(4,0,"��ż���");
			Label labelcourtesyName = new Label(5,0,"��");
			Label labelpesudonym = new Label(6,0,"��");
			Label labelgender = new Label(7,0,"�Ա�");
			Label labelChineseBirthday = new Label(8,0,"ũ����������");
			Label labelChinesedeathdate = new Label(9,0,"ũ����������");
			Label labelburied = new Label(10,0,"����");
			Label labelfamilyrank = new Label(11,0,"��ͥ����");
			Label labelgenerition = new Label(12,0,"����");
			Label labelfamily = new Label(13,0,"��������");
			Label labeldescription = new Label(14,0,"��������");
			ws.addCell(labelId);
			ws.addCell(labelName);
			ws.addCell(labelfatherid);
			ws.addCell(labelmotherid);
			ws.addCell(labelpartnerid);
			ws.addCell(labelcourtesyName);
			ws.addCell(labelpesudonym);
			ws.addCell(labelgender);
			ws.addCell(labelChineseBirthday);
			ws.addCell(labelChinesedeathdate);
			ws.addCell(labelburied);
			ws.addCell(labelfamilyrank);
			ws.addCell(labelgenerition);
			ws.addCell(labelfamily);
			ws.addCell(labeldescription);
			for (int i = 0; i < list.size(); i++) {
				Label labelId_i = new Label(0, i + 1, list.get(i).id + "");
				Label labelName_i = new Label(1, i + 1, list.get(i).name);
				Label labelfatherid_i = new Label(2, i + 1, String.valueOf(list.get(i).fatherid));
				Label labelmotherid_i = new Label(3, i + 1, String.valueOf(list.get(i).motherid));
				Label labelpartnerid_i = new Label(4, i + 1, list.get(i).partnerid);
				Label labelcourtesyName_i = new Label(5, i + 1, list.get(i).CourtesyName);
				Label labelpesudonym_i = new Label(6, i + 1, list.get(i).pesudonym);
				Label labelgender_i = new Label(7, i + 1, list.get(i).gender);
				Label labelChineseBirthday_i = new Label(8, i + 1, list.get(i).ChineseBirthday);
				Label labelChinesedeathdate_i = new Label(9, i + 1, list.get(i).Chinesedeathdate);
				Label labelburied_i = new Label(10, i + 1, list.get(i).buried);
				Label labelfamilyrank_i = new Label(11, i + 1, String.valueOf(list.get(i).familyrank));
				Label labelgenerition_i = new Label(12, i + 1, String.valueOf(list.get(i).generation));
				Label labelfamily_i = new Label(13, i + 1, list.get(i).family);
				Label labeldescription_i = new Label(14, i + 1, list.get(i).description);
				ws.addCell(labelId_i);
				ws.addCell(labelName_i);
				ws.addCell(labelfatherid_i);
				ws.addCell(labelmotherid_i);
				ws.addCell(labelpartnerid_i);
				ws.addCell(labelcourtesyName_i);
				ws.addCell(labelpesudonym_i);
				ws.addCell(labelgender_i);
				ws.addCell(labelChineseBirthday_i);
				ws.addCell(labelChinesedeathdate_i);
				ws.addCell(labelburied_i);
				ws.addCell(labelfamilyrank_i);
				ws.addCell(labelgenerition_i);
				ws.addCell(labelfamily_i);
				ws.addCell(labeldescription_i);
			}
			// д���ĵ�
			wb.write();
			// �ر�Excel����������
			wb.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
