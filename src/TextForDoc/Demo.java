package TextForDoc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Demo {
	
	public static void main(String[] args) throws IOException {
		//定义变量
		File file = new File("data/FamilyBook");
		String[] filelist = file.list();
		People p[] = new People[5000];
		int personnum = 0;
		int Generation = -1;
		//循环读取所有文件
		for(int l=0; l<filelist.length;l++) {
			//变量赋初值
			personnum=0;
			Generation = -1;
			for(int i=0;i<5000;i++) {
				p[i]=null;
			}
			//读取文件
			String[] saveFilename = filelist[l].split("\\.");
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream("data/FamilyBook/"+saveFilename[0]+".doc.txt"),"UTF-8"));
			String text = "";
			String line="";
			
			while((line=bufferedreader.readLine())!=null){
	            text+=line+"\r";
	        }
			String[] lines = text.split("\r");
			//1 循环处理每一行的人物
	        for(int i=0; i<lines.length; i++) {
	        	if(getGeneration(lines[i])!=-1) {
	        		Generation = getGeneration(lines[i]);
	        	}else if(lines[i].length()<5){
	        		continue;
	        	}else {
	        		p[personnum] = new People();	//新建人物
	            	p[personnum].id = personnum+1; 	//编号
	            	p[personnum].generation = Generation;	//辈分
	            	p[personnum].description = lines[i];	//描述
	            	p[personnum].getName(personnum, lines[i]);	//姓名
	            	p[personnum].getFatherNameAndRank(personnum, lines[i]);	//家庭排行和父亲姓名
	            	p[personnum].getCourtesyName(personnum, lines[i]);	//字
	            	p[personnum].getpesudonym(personnum, lines[i]);	//号
	            	p[personnum].getBirthday(personnum, lines[i]);	//出生日期
	            	p[personnum].getDeathdate(personnum, lines[i]);	//过世日期
	            	p[personnum].getburied(personnum, lines[i]);	//葬于
	            	//System.out.println(p[personnum].toStringTest());
	            	personnum++;
	        	}
	        }
	        //2 循环为家谱人物建立父子关系
	        for(int i=0; i<personnum; i++) {
	        	p[i].getFatherid(i,p);
	        }
	        //3 循环处理家谱人物的配偶及对应子女信息
	        int TreeNUM = personnum;
	        for(int i=0; i<TreeNUM; i++) {	//循环提取人物配偶信息及子女信息
	        	personnum = p[i].getWifeAndChildrenInfo(TreeNUM, personnum, p);
	        }
			String path = "data/Formatting1/"+saveFilename[0]+".xls";
			List<People> list = new ArrayList<People>();
			for(int i=0;i<personnum;i++) {
				list.add(p[i]);
				System.out.println(p[i].toString());
			}
			addExcel(path,list);
		}
	}
	
	public static int getGeneration(String line) {
		int num = -1;
		String pattern = "[第]?[一|二|廿|三|四|五|六|七|八|九|十]{1,3}世";
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
				case "十一世":num=11;break;
				case "第十二世":num=12;break;
				case "十二世":num=12;break;
				case "第十三世":num=13;break;
				case "十三世":num=13;break;
				case "第十四世":num=14;break;
				case "十四世":num=14;break;
				case "第十五世":num=15;break;
				case "十五世":num=15;break;
				case "第十六世":num=16;break;
				case "十六世":num=16;break;
				case "第十七世":num=17;break;
				case "十七世":num=17;break;
				case "第十八世":num=18;break;
				case "十八世":num=18;break;
				case "第十九世":num=19;break;	
				case "十九世":num=19;break;	
				case "第二十世":num=20;break;
				case "二十世":num=20;break;
				case "第二十一世":num=21;break;
				case "二十一世":num=21;break;
				case "廿二世":num=21;break;
				case "第二十二世":num=22;break;
				case "二十二世":num=22;break;
				case "廿一世":num=21;break;
				case "第二十三世":num=23;break;
				case "二十三世":num=23;break;
				case "廿三世":num=23;break;
				case "第二十四世":num=24;break;
				case "二十四世":num=24;break;
				case "廿四世":num=24;break;
				case "第二十五世":num=25;break;
				case "二十五世":num=25;break;
				case "廿五世":num=25;break;
				case "第二十六世":num=26;break;
				case "二十六世":num=26;break;
				case "廿六世":num=26;break;
				case "第二十七世":num=27;break;
				case "二十七世":num=27;break;
				case "廿七世":num=27;break;
				case "第二十八世":num=28;break;
				case "二十八世":num=28;break;
				case "廿八世":num=28;break;
				case "第二十九世":num=29;break;				
				case "二十九世":num=29;break;				
				case "廿九世":num=29;break;				
				case "第三十世":num=30;break;
				case "三十世":num=30;break;
				case "第三十一世":num=31;break;
				case "三十一世":num=31;break;
				case "第三十二世":num=32;break;
				case "三十二世":num=32;break;
				case "第三十三世":num=33;break;
				case "三十三世":num=33;break;
				case "第三十四世":num=34;break;
				case "三十四世":num=34;break;
				case "第三十五世":num=35;break;
				case "三十五世":num=35;break;
				case "第三十六世":num=36;break;
				case "三十六世":num=36;break;
				case "第三十七世":num=37;break;
				case "三十七世":num=37;break;
				case "第三十八世":num=38;break;
				case "三十八世":num=38;break;
				case "第三十九世":num=39;break;
				case "三十九世":num=39;break;
				case "第四十世":num=40;break;
				case "四十世":num=40;break;
				default:num=-1;break;
			}
		}		
		return num;
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
