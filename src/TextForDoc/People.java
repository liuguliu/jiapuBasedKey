package TextForDoc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class People {
	int id;	//编号
	String name;	//姓名
	int fatherid;	//父亲编号
	int motherid;	//母亲编号
	String partnerid="";	//配偶编号
	String CourtesyName="";	//字
	String pesudonym="";	//号
	String gender="男";	//性别
	String ChineseBirthday;	//农历出生日期
	String Chinesedeathdate;	//农历过世日期
	String buried;	//葬于
	int familyrank;	//家庭排行
	int generation;	//辈分
	String family="汪";	//所属姓氏
	String description;	//其他描述
	//辅助处理变量
	int buriedindex = 0;	//判断妻子描述信息结束位置
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
	//提取姓名
	public void getName(int i, String personinfo) {
		int indexSpace = personinfo.indexOf(' ');	//名字中的字数不确定(以第一个空格为结束)
		name = personinfo.substring(0, indexSpace);
	}
	//提取家庭排行和父亲姓名
	public void getFatherNameAndRank(int i, String personinfo) {
		int indexStart = personinfo.indexOf(' ');
		int indexEnd = personinfo.indexOf("子 ");
		if(indexStart<indexEnd) {
			String fatherAndRank = personinfo.substring(indexStart, indexEnd+1);		
			int indexofRank = getFamilyRank(i, fatherAndRank);
			fathername = fatherAndRank.substring(1,indexofRank);
		}
		else {
			//为始祖或格式错误
		}
	}
	//提取字
	public void getCourtesyName(int i, String personinfo) {
		String pattern = "字[\\u4E00-\\u9FA5][\\u4E00-\\u9FA5]";
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
	//提取号
	public void getpesudonym(int i, String personinfo) {
		String pattern = "号[\\u4E00-\\u9FA5][\\u4E00-\\u9FA5]";
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
	//提取农历出生日期
	public void getBirthday(int i, String personinfo) {
		String pattern = "\\S{0,5}年\\S{1,10}月\\S{1,10}生";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(personinfo);
		if(m.find()) {
			String birthdayinfo=m.group(0).trim();
			//System.out.println(birthdayinfo);
			ChineseBirthday = birthdayinfo.substring(0, birthdayinfo.length()-1);
		}
	}
	//提取农历过世日期
	public void getDeathdate(int i, String personinfo) {
		String pattern = "\\S{1,4}年\\S{1,10}月\\S{1,10}公故";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(personinfo);
		if(m.find()) {
			buriedindex=m.start();
			String deathdateinfo=m.group(0).trim();
			Chinesedeathdate = deathdateinfo.substring(0, deathdateinfo.length()-2);
		}
	} 
	//提取葬于
	public void getburied(int i, String personinfo) {
		if(personinfo.contains("公故葬")||personinfo.contains("公故 葬")) {
			String pattern = "公故 *葬\\S{1,200}[ | |。]";
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
			String pattern = "公\\S{0,5}葬\\S{1,200} ";
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
	//建立父子关系
	public void getFatherid(int i,People[] p) {
		for(int j=i-1;j>=0;j--) {
			if(p[j].name.contains(p[i].fathername)) {
				//System.out.println(p[j].toString());
				p[i].fatherid = p[j].id;
			}
		}
	}
	//提取家庭排行被getFatherNameAndRank()函数调用
	public int getFamilyRank(int i, String fartherAndRank) {
		int indexofRank = -1;
		if(fartherAndRank.contains("嗣子")) {
			familyrank=1;
			indexofRank = fartherAndRank.indexOf("嗣子");
		}
		else if(fartherAndRank.contains("公长子")) {
			familyrank=1;
			indexofRank = fartherAndRank.indexOf("公长子");
		}
		else if(fartherAndRank.contains("公次子")) {
			familyrank=2;
			indexofRank = fartherAndRank.indexOf("公次子");
		}
		else if(fartherAndRank.contains("公三子")) {
			familyrank=3;
			indexofRank = fartherAndRank.indexOf("公三子");
		}
		else if(fartherAndRank.contains("公四子")) {
			familyrank=4;
			indexofRank = fartherAndRank.indexOf("公四子");
		}
		else if(fartherAndRank.contains("公五子")) {
			familyrank=5;
			indexofRank = fartherAndRank.indexOf("公五子");
		}
		else if(fartherAndRank.contains("公六子")) {
			familyrank=6;
			indexofRank = fartherAndRank.indexOf("公六子");
		}
		else if(fartherAndRank.contains("公七子")) {
			familyrank=7;
			indexofRank = fartherAndRank.indexOf("公七子");
		}
		else if(fartherAndRank.contains("公八子")) {
			familyrank=8;
			indexofRank = fartherAndRank.indexOf("公八子");
		}
		else if(fartherAndRank.contains("之子")||fartherAndRank.contains("长子")||fartherAndRank.contains("公子")) {
			familyrank=1;
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
			familyrank=2;
			indexofRank = fartherAndRank.indexOf("次子");
		}
		else if(fartherAndRank.contains("三子")) {
			familyrank=3;
			indexofRank = fartherAndRank.indexOf("三子");
		}
		else if(fartherAndRank.contains("四子")) {
			familyrank=4;
			indexofRank = fartherAndRank.indexOf("四子");
		}
		else if(fartherAndRank.contains("五子")) {
			familyrank=5;
			indexofRank = fartherAndRank.indexOf("五子");
		}
		else if(fartherAndRank.contains("六子")) {
			familyrank=6;
			indexofRank = fartherAndRank.indexOf("六子");
		}
		else if(fartherAndRank.contains("七子")) {
			familyrank=7;
			indexofRank = fartherAndRank.indexOf("七子");
		}
		else if(fartherAndRank.contains("八子")) {
			familyrank=8;
			indexofRank = fartherAndRank.indexOf("八子");
		}
		else if(fartherAndRank.contains("九子")) {
			familyrank=9;
			indexofRank = fartherAndRank.indexOf("九子");
		}
		else if(fartherAndRank.contains("十子")) {
			familyrank=10;
			indexofRank = fartherAndRank.indexOf("十子");
		}else {
			familyrank=1;
			indexofRank = fartherAndRank.length()-1;
		}
		return indexofRank;
	}
	
	public int getWifeAndChildrenInfo(int TreeNUM, int personnum, People[] p) {
		
		String wifeinfo = description;		
		int k =0;	//记录妻子数
		int[] wifes = {0,0,0,0,0,0,0,0,0,0};	//记录第i个妻子描述信息出现在字符串中的位置
		int[] wifesid = {0,0,0,0,0,0,0,0,0,0};	//记录第i个妻子的编号
		String pattern = "[续|又|再]{0,1}[娶|配|妣][^故|葬|合冢|生故未详|享寿].*?[女|氏|孺人]";
		Pattern rPattern = Pattern.compile(pattern);
		Matcher m = rPattern.matcher(wifeinfo);
		
		while(m.find()) {
			wifes[k] = m.start();			
			if(k>0) {	//获取上一个妻子的属性信息及对应子女信息(若只有一位妻子这部分不执行)
				String wifeAndChildren = wifeinfo.substring(wifes[k-1], wifes[k]);
				getWifeBirthday(wifesid[k-1], wifeAndChildren, p);
				getWifeDeathdate(wifesid[k-1], wifeAndChildren, p);
				getwifeburied(wifesid[k-1], wifeAndChildren, p);
				personnum = getChildrenInfo(TreeNUM, personnum, p[wifesid[k-1]].id, wifeAndChildren,p);
			}
			p[personnum] = new People();
			p[personnum].id = personnum+1;
			p[personnum].gender = "女";
			p[personnum].generation = generation;
			int indexpei=-1;
			if(m.group(0).contains("配")) {
				indexpei= m.group(0).indexOf('配');							
			}else if(m.group(0).contains("娶")){
				indexpei= m.group(0).indexOf('娶');
			}else {
				indexpei= m.group(0).indexOf('妣');
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
		//获取最后一位妻子或唯一的这位妻子及其儿女信息
		if(k>0) {
			if(wifes[k-1]<buriedindex) {
				String wifeAndChildren = wifeinfo.substring(wifes[k-1],buriedindex);
				getWifeBirthday(wifesid[k-1], wifeAndChildren, p);
				getWifeDeathdate(wifesid[k-1], wifeAndChildren, p);
				getwifeburied(wifesid[k-1], wifeAndChildren, p);
				
				personnum = getChildrenInfo(TreeNUM, personnum, p[wifesid[k-1]].id, wifeAndChildren, p); 
			}else {
				//这里有一个错误
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
			//无妻子信息
			personnum = getChildrenInfo(TreeNUM, personnum, -1, wifeinfo, p);
		}
		return personnum;
	}
	//提取子女信息
	public int getChildrenInfo(int TreeNUM, int personnum, int imotherid, String childreninfo, People[] p) {
		
		String patterns = "子[一|二|三|四|五|六|七|八|九|十][^一|二|三|四|五|六|七|八|九|十]";
		Pattern rs = Pattern.compile(patterns);
		Matcher ms = rs.matcher(childreninfo);
		while(ms.find()) {
			int indexofson = ms.start();
			String sonsinfo = childreninfo.substring(indexofson+2).trim();
			personnum = getSon(TreeNUM, personnum,imotherid, ms.group(0), sonsinfo, p);
		}
		
		String patternd = "女[一|二|三|四|五|六|七|八|九|十]";
		Pattern rd = Pattern.compile(patternd);
		Matcher md = rd.matcher(childreninfo);
		while(md.find()) {
			int indexofdaughter = md.start();
			String daughtersinfo = childreninfo.substring(indexofdaughter+2).trim();
			personnum = getDaughter(personnum, imotherid, md.group(0),daughtersinfo, p);
		}
		return personnum;
	}
	//提取儿子信息
	public int getSon(int TreeNUM, int personnum,int imotherid,String ms, String sonsinfo, People[] p) {
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
    				nameson=sons[j].substring(0, sons[j].indexOf("（")-1);
    				description = sons[j].substring(sons[j].indexOf("（")+1,sons[j].length()-1);
    			}else if(sons[j].length()>2){
    				nameson = sons[j].substring(0, 2);
    				description = sons[j].substring(2);
    			}else {
    				nameson = sons[j];
    			}
    			//判断是否存在
    			boolean flag=false;
    			for(int k=0;k<TreeNUM;k++) {
    				if((p[k].name.equals(nameson)||p[k].name.equals(GlobalVar.generationRank[generation]+nameson))
    						&&name.contains(p[k].fathername)) {
    					p[k].motherid = imotherid;
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
	//提取女儿及女婿信息
	public int getDaughter(int personnum,int imotherid, String ms, String daughtersinfo, People[] p) {
		int childrennum= getchildrenNum(ms);
		if(!daughtersinfo.contains(" ")) {
			for(int j=0;j<childrennum;j++) {
				p[personnum] = new People();
				p[personnum].id = personnum+1;
				p[personnum].name = "女"+GlobalVar.cNum[j];
				p[personnum].gender="女";
				p[personnum].familyrank=j+1;
				p[personnum].generation=generation+1;
				p[personnum].fatherid = id;
				p[personnum].motherid = imotherid;
				if(daughtersinfo.contains(")")){
					p[personnum].description = "夭";
				}
				//System.out.println(p[num].toString());
				personnum++;
			}
		}else {
			String[] daughters = daughtersinfo.split(" ");
			if(childrennum<daughters.length) {
				for(int j=0;j<childrennum;j++) {
					if(daughters[j].contains(GlobalVar.rankNum[j]+"适")||daughters[j].charAt(0)=='适') {
						p[personnum]=new People();
						p[personnum].id=personnum+1;
						p[personnum].name="女"+GlobalVar.cNum[j];
						p[personnum].gender="女";
						p[personnum].familyrank=j+1;
						p[personnum].generation=generation+1;
						p[personnum].fatherid = id;
	        			p[personnum].motherid = imotherid;       				
	    				if(daughters[j].contains(GlobalVar.rankNum[j]+"适")) {
	    					p[personnum].description = daughters[j].substring(1);
	    				}else {
	    					p[personnum].description = daughters[j];
	    				}
	    				p[personnum].partnerid = String.valueOf(personnum+2);
	    				//System.out.println(p[num].toString());
	    				personnum++;
	    				//嫁
	    				if(daughters[j].length()>1) {
	    					p[personnum]=new People();
	    					p[personnum].id=personnum+1;
	    					if(daughters[j].contains(GlobalVar.rankNum[j]+"适")) {
	    						//System.out.println(daughters[j]);
	    						p[personnum].name=daughters[j].substring(2);
	        				}else {
	        					p[personnum].name=daughters[j].substring(1);
	        				}
	    					p[personnum].partnerid = String.valueOf(personnum);
	    					personnum++;
	    				}else {
	    					//独一适字，无其他信息
	    				}
	    				
					}else  {
						p[personnum]=new People();
						p[personnum].id=personnum+1;
						p[personnum].name="女"+GlobalVar.cNum[j];
						p[personnum].gender="女";
						p[personnum].familyrank=j+1;
						p[personnum].generation=generation+1;
						p[personnum].fatherid = id;   				
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
		return personnum;
	}
	//获取子女个数（处理字符串如生子一、子一、女二）
	public int getchildrenNum(String ms) {
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
	//妻子出生日期
	public void getWifeBirthday(int i, String wifeinfo, People[] p) {
		String pattern = "[同公年|\\S{0,5}年]\\S{1,10}月\\S{1,10}生";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(wifeinfo);
		if(m.find()) {
			String birthdayinfo=m.group(0).trim();
			p[i].ChineseBirthday = birthdayinfo.substring(0, birthdayinfo.length()-1);
		}
	}
	//妻子过世日期
	public void getWifeDeathdate(int i, String wifeinfo, People[] p) {
		String pattern = "\\S{1,4}年\\S{1,15}妣故";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(wifeinfo);
		if(m.find()) {
			String deathdateinfo=m.group(0).trim();
			p[i].Chinesedeathdate = deathdateinfo.substring(0, deathdateinfo.length()-2);
		}
	} 
	//妻子葬于
	public void getwifeburied(int i, String wifeinfo, People[] p) {
		String pattern = "公\\S{0,5}妣\\S{0,5}葬\\S{1,200} ";
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
