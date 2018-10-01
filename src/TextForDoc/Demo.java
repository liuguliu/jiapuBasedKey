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
	static String generationRank[] = {"一","二","三","四","五","六","七","八","九","十","十一","十二","延","祚","昌","克","相","盛","时"};
	static String cNum[] = {"一","二","三","四","五","六","七","八","九","十"};
	static String rankNum[] = {"","长","次","三","四","五","六","七","八","九","十"};
	static int Generation = -1;
	static int personnum = 0;
	static People p[] = new People[1500];
	
	public static void main(String[] args) throws IOException {
		
		String text = "";
		File file = new File("data/01卷一　　庙荣公121.doc.txt");
		FileReader filereader = new FileReader(file);
		BufferedReader bufferedreader = new BufferedReader(filereader);
		String line="";
		while((line=bufferedreader.readLine())!=null){
            text+=line+"\r";
        }
		String[] lines = text.split("\r");
        for(int i=0; i<lines.length; i++) {	//1 循环读取每一行的人物
        	//System.out.println(lines[i]);
        	if(getGeneration(lines[i])!=-1) {
        		Generation = getGeneration(lines[i]);
        		//System.out.println(Generation);
        	}else {
        		p[personnum] = new People();	//新建人物
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
        for(int i=0; i<wang; i++) {	//循环提取人物配偶信息及子女信息
        	getWifeInfo(i, p[i].description, wang);
        	//System.out.println(p[i].toString());
        }
//        for(int i=0; i<wang; i++) {	//循环提取人物配偶信息及子女信息
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
		String pattern = "[续|又|再]{0,1}[娶|配].*?[女|氏]";
		Pattern rPattern = Pattern.compile(pattern);
		Matcher m = rPattern.matcher(wifeinfo);
		int k =0;
		int[] wifes = {0,0,0,0,0,0,0,0,0,0};
		int[] wifesid = {0,0,0,0,0,0,0,0,0,0};
		while(m.find()) {
			//System.out.println(m.group(0));
			wifes[k] = m.start();			
			if(k>0) {	//获取上一个妻子的属性信息及对应子女信息(若只有一位妻子这部分不执行)
				String wifeAndChildren = wifeinfo.substring(wifes[k-1], wifes[k]);
				getChildrenInfo(ipartner,wifesid[k-1], wang, wifeAndChildren);
			}
			p[personnum] = new People();
			p[personnum].id = personnum+1;
			p[personnum].gender = "女";
			int indexpei=-1;
			if(m.group(0).contains("配")) {
				indexpei= m.group(0).indexOf('配');							
			}else {
				indexpei= m.group(0).indexOf('娶');
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
		//获取最后一位妻子或唯一的这位妻子及其儿女信息
		if(k>1) {
			String wifeAndChildren = wifeinfo.substring(wifes[k-1]);
			getChildrenInfo(ipartner,wifesid[k-1], wang, wifeAndChildren); 
		}else  if(k==1){
			String wifeAndChildren = wifeinfo.substring(wifes[k-1]);
			getChildrenInfo(ipartner,wifesid[k-1], wang, wifeAndChildren); 
		}else {
			//无妻子信息
			getChildrenInfo(ipartner,-1, wang, wifeinfo);
		}
	}
	public static void getChildrenInfo(int ifatherid,int imotherid, int wang, String childreninfo) {
		//System.out.println();
		//System.out.println(p[ifatherid].name+":");
		String patterns = "子[一|二|三|四|五|六|七|八|九|十][^一|二|三|四|五|六|七|八|九|十]";
		Pattern rs = Pattern.compile(patterns);
		Matcher ms = rs.matcher(childreninfo);
		while(ms.find()) {
			int indexofson = ms.start();
			String sonsinfo = childreninfo.substring(indexofson+2).trim();
			getSon(ifatherid,imotherid,wang, ms.group(0),sonsinfo);
		}
		
		String patternd = "女[一|二|三|四|五|六|七|八|九|十]";
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
    			//提取姓名和描述
    			if(sons[j].contains("（")) {
    				nameson=sons[j].substring(0, sons[j].indexOf("（"));
    				description = sons[j].substring(sons[j].indexOf("（")+1,sons[j].length()-1);
    			}else if(sons[j].length()>2){
    				nameson = sons[j].substring(0, 2);
    				description = sons[j].substring(2);
    			}else {
    				nameson = sons[j];
    			}
    			//判断是否存在
    			boolean flag=false;
    			for(int k=0;k<wang;k++) {
    				if((p[k].name.equals(nameson)||p[k].name.equals(generationRank[p[ifatherid].generation]+nameson))
    						&&p[k].fathername.equals(p[ifatherid].name)) {
    					flag=true;
    					break;
    				}
    			}
    			if(!flag) {	//若不存在
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
				if(daughters[j].contains(rankNum[j]+"适")||daughters[j].charAt(0)=='适') {
					p[personnum]=new People();
					p[personnum].id=personnum+1;
					p[personnum].name="女"+cNum[j];
					p[personnum].gender="女";
					p[personnum].familyrank=j+1;
					p[personnum].generation=p[ifatherid].generation+1;
					p[personnum].fatherid = p[ifatherid].id;
        			p[personnum].motherid = imotherid;       				
    				if(daughters[j].contains(rankNum[j]+"适")) {
    					p[personnum].description = daughters[j].substring(1);
    				}else {
    					p[personnum].description = daughters[j];
    				}
    				p[personnum].partnerid = String.valueOf(personnum+2);
    				//System.out.println(p[num].toString());
    				personnum++;
    				//嫁
    				p[personnum]=new People();
					p[personnum].id=personnum+1;
					if(daughters[j].contains(rankNum[j]+"适")) {
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
					p[personnum].name="女"+cNum[j];
					p[personnum].gender="女";
					p[personnum].familyrank=j+1;
					p[personnum].generation=p[ifatherid].generation+1;
					p[personnum].fatherid = p[ifatherid].id;   				
					p[personnum].motherid = imotherid; 
    				if(daughters[j].contains("(殀)")||daughters[j].contains("(夭)")) {
    					p[personnum].description = "夭";
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
			case '一':num=1;break;
			case '二':num=2;break;
			case '三':num=3;break;
			case '四':num=4;break;
			case '五':num=5;break;
			case '六':num=6;break;
			case '七':num=7;break;
			case '八':num=8;break;
			case '九':num=9;break;
			case '十':num=10;break;
			default:num=0;break;
		}
		return num;
	}
	public static void getBirthday(int i, String personinfo) {
		String pattern = "\\S{1,5}年\\S{1,10}月\\S{1,10}生";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(personinfo);
		if(m.find()) {
			String birthdayinfo=m.group(0).trim();
			p[i].ChineseBirthday = birthdayinfo.substring(0, birthdayinfo.length()-1);
			//System.out.println(birthdayinfo);
		}
	}
	public static void getDeathdate(int i, String personinfo) {
		String pattern = "\\S{1,4}年\\S{1,10}月\\S{1,10}公故";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(personinfo);
		if(m.find()) {
			String deathdateinfo=m.group(0).trim();
			p[i].Chinesedeathdate = deathdateinfo.substring(0, deathdateinfo.length()-2);
		}
	} 
	public static void getburied(int i, String personinfo) {
		if(personinfo.contains("公故葬")||personinfo.contains("公故 葬")) {
			String pattern = "公故 *葬\\S{1,200} ";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(personinfo);
			if(m.find()) {
				p[i].buried = m.group(0).substring(2).trim();
			}
		}else {
			String pattern = "公\\S{1,5}葬\\S{1,200} ";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(personinfo);
			if(m.find()) {
				p[i].buried = m.group(0).trim();
			}
		}
	}
	public static void getCourtesyName(int i, String personinfo) {
		String pattern = "字[\\u4E00-\\u9FA5][\\u4E00-\\u9FA5]";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(personinfo);
		if(m.find()) {
			String courtesyinfo =m.group(0).trim();
			p[i].CourtesyName = courtesyinfo.substring(1);
//			if(courtesyinfo.contains("又字")) {
//				int indexofYou = courtesyinfo.indexOf("又字");
//				p[i].CourtesyName = courtesyinfo.substring(1, indexofYou);
//				p[i].CourtesyName = p[i].CourtesyName + "/"+courtesyinfo.substring(indexofYou+2);
//			}
//			else {
//				p[i].CourtesyName = courtesyinfo.substring(1);
//			}
		}
	}
	public static void getpesudonym(int i, String personinfo) {
		String pattern = "号[\\u4E00-\\u9FA5][\\u4E00-\\u9FA5]";
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
		String pattern = "第.世";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		if(m.find()) {
			switch (m.group(0)) {
				case "第一世":num=1;break;
				case "第二世":num=2;break;
				case "第三世":num=3;break;
				case "第四世":num=4;break;
				case "第五世":num=5;break;
				case "第六世":num=6;break;
				case "第七世":num=7;break;
				case "第八世":num=8;break;
				case "第九世":num=9;break;
				case "第十世":num=10;break;
				case "第十一世":num=11;break;
				case "第十二世":num=12;break;
				case "第十三世":num=13;break;
				case "第十四世":num=14;break;
				case "第十五世":num=15;break;
				case "第十六世":num=16;break;
				case "第十七世":num=17;break;
				case "第十八世":num=18;break;
				case "第十九世":num=19;break;	
				case "第二十世":num=20;break;
				case "第二十一世":num=21;break;
				case "第二十二世":num=22;break;
				case "第二十三世":num=23;break;
				case "第二十四世":num=24;break;
				case "第二十五世":num=25;break;
				case "第二十六世":num=26;break;
				case "第二十七世":num=27;break;
				case "第二十八世":num=28;break;
				case "第二十九世":num=29;break;				
				case "第三十世":num=30;break;
				case "第三十一世":num=31;break;
				case "第三十二世":num=32;break;
				case "第三十三世":num=33;break;
				case "第三十四世":num=34;break;
				case "第三十五世":num=35;break;
				case "第三十六世":num=36;break;
				case "第三十七世":num=37;break;
				case "第三十八世":num=38;break;
				case "第三十九世":num=39;break;
				case "第四十世":num=40;break;
				default:num=-1;break;
			}
		}		
		return num;
	}
	public static void getName(int i, String personinfo) {
		int indexSpace = personinfo.indexOf(' ');	//名字中的字数不确定
		p[i].name = personinfo.substring(0, indexSpace);
	}
	public static void getFatherNameAndRank(int i, String personinfo) {
		int indexStart = personinfo.indexOf(' ');
		int indexEnd = personinfo.indexOf("子 ");
		String fatherAndRank = personinfo.substring(indexStart, indexEnd+1);		
		int indexofRank = getFamilyRank(i, fatherAndRank);
		//System.out.println(indexofRank);
		p[i].fathername = fatherAndRank.substring(1,indexofRank);

	}
	public static int getFamilyRank(int i, String fartherAndRank) {
		int indexofRank = -1;
		if(fartherAndRank.contains("公长子")) {
			p[i].familyrank=1;
			indexofRank = fartherAndRank.indexOf("公长子");
		}
		else if(fartherAndRank.contains("公次子")) {
			p[i].familyrank=2;
			indexofRank = fartherAndRank.indexOf("公次子");
		}
		else if(fartherAndRank.contains("公三子")) {
			p[i].familyrank=3;
			indexofRank = fartherAndRank.indexOf("公三子");
		}
		else if(fartherAndRank.contains("公四子")) {
			p[i].familyrank=4;
			indexofRank = fartherAndRank.indexOf("公四子");
		}
		else if(fartherAndRank.contains("公五子")) {
			p[i].familyrank=5;
			indexofRank = fartherAndRank.indexOf("公五子");
		}
		else if(fartherAndRank.contains("公六子")) {
			p[i].familyrank=6;
			indexofRank = fartherAndRank.indexOf("公六子");
		}
		else if(fartherAndRank.contains("公七子")) {
			p[i].familyrank=7;
			indexofRank = fartherAndRank.indexOf("公七子");
		}
		else if(fartherAndRank.contains("公八子")) {
			p[i].familyrank=8;
			indexofRank = fartherAndRank.indexOf("公八子");
		}
		else if(fartherAndRank.contains("之子")||fartherAndRank.contains("长子")||fartherAndRank.contains("公子")) {
			p[i].familyrank=1;
			if(fartherAndRank.indexOf("之子")!=-1) {
				indexofRank = fartherAndRank.indexOf("之子");
			}
			else if(fartherAndRank.indexOf("长子")!=-1) {
				indexofRank = fartherAndRank.indexOf("长子");
			}
			else {
				indexofRank = fartherAndRank.indexOf("公子");
			}
		}
		else if(fartherAndRank.contains("次子")) {
			p[i].familyrank=2;
			indexofRank = fartherAndRank.indexOf("次子");
		}
		else if(fartherAndRank.contains("三子")) {
			p[i].familyrank=3;
			indexofRank = fartherAndRank.indexOf("三子");
		}
		else if(fartherAndRank.contains("四子")) {
			p[i].familyrank=4;
			indexofRank = fartherAndRank.indexOf("四子");
		}
		else if(fartherAndRank.contains("五子")) {
			p[i].familyrank=5;
			indexofRank = fartherAndRank.indexOf("五子");
		}
		else if(fartherAndRank.contains("六子")) {
			p[i].familyrank=6;
			indexofRank = fartherAndRank.indexOf("六子");
		}
		else if(fartherAndRank.contains("七子")) {
			p[i].familyrank=7;
			indexofRank = fartherAndRank.indexOf("七子");
		}
		else if(fartherAndRank.contains("八子")) {
			p[i].familyrank=8;
			indexofRank = fartherAndRank.indexOf("八子");
		}
		else if(fartherAndRank.contains("九子")) {
			p[i].familyrank=9;
			indexofRank = fartherAndRank.indexOf("九子");
		}
		else if(fartherAndRank.contains("十子")) {
			p[i].familyrank=10;
			indexofRank = fartherAndRank.indexOf("十子");
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
			// 创建可写入的Excel工作簿		
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			// 以fileName为文件名来创建一个Workbook
			wb = Workbook.createWorkbook(file);
			// 创建工作表
			WritableSheet ws = wb.createSheet("Sheet0", 0);
			// 要插入到的Excel表格的行号，默认从0开始
			Label labelId = new Label(0, 0, "编号");
			Label labelName = new Label(1, 0, "姓名");
			Label labelfatherid = new Label(2,0,"父亲编号");
			Label labelmotherid = new Label(3,0,"母亲编号");
			Label labelpartnerid = new Label(4,0,"配偶编号");
			Label labelcourtesyName = new Label(5,0,"字");
			Label labelpesudonym = new Label(6,0,"号");
			Label labelgender = new Label(7,0,"性别");
			Label labelChineseBirthday = new Label(8,0,"农历出生日期");
			Label labelChinesedeathdate = new Label(9,0,"农历过世日期");
			Label labelburied = new Label(10,0,"葬于");
			Label labelfamilyrank = new Label(11,0,"家庭排行");
			Label labelgenerition = new Label(12,0,"辈份");
			Label labelfamily = new Label(13,0,"所属姓氏");
			Label labeldescription = new Label(14,0,"其他描述");
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
			// 写进文档
			wb.write();
			// 关闭Excel工作簿对象
			wb.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
