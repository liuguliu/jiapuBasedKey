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
		//�������
		File file = new File("data/FamilyBook");
		String[] filelist = file.list();
		People p[] = new People[5000];
		int personnum = 0;
		int Generation = -1;
		//ѭ����ȡ�����ļ�
		for(int l=0; l<filelist.length;l++) {
			//��������ֵ
			personnum=0;
			Generation = -1;
			for(int i=0;i<5000;i++) {
				p[i]=null;
			}
			//��ȡ�ļ�
			String[] saveFilename = filelist[l].split("\\.");
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream("data/FamilyBook/"+saveFilename[0]+".doc.txt"),"UTF-8"));
			String text = "";
			String line="";
			
			while((line=bufferedreader.readLine())!=null){
	            text+=line+"\r";
	        }
			String[] lines = text.split("\r");
			//1 ѭ������ÿһ�е�����
	        for(int i=0; i<lines.length; i++) {
	        	if(getGeneration(lines[i])!=-1) {
	        		Generation = getGeneration(lines[i]);
	        	}else if(lines[i].length()<5){
	        		continue;
	        	}else {
	        		p[personnum] = new People();	//�½�����
	            	p[personnum].id = personnum+1; 	//���
	            	p[personnum].generation = Generation;	//����
	            	p[personnum].description = lines[i];	//����
	            	p[personnum].getName(personnum, lines[i]);	//����
	            	p[personnum].getFatherNameAndRank(personnum, lines[i]);	//��ͥ���к͸�������
	            	p[personnum].getCourtesyName(personnum, lines[i]);	//��
	            	p[personnum].getpesudonym(personnum, lines[i]);	//��
	            	p[personnum].getBirthday(personnum, lines[i]);	//��������
	            	p[personnum].getDeathdate(personnum, lines[i]);	//��������
	            	p[personnum].getburied(personnum, lines[i]);	//����
	            	//System.out.println(p[personnum].toStringTest());
	            	personnum++;
	        	}
	        }
	        //2 ѭ��Ϊ�������ｨ�����ӹ�ϵ
	        for(int i=0; i<personnum; i++) {
	        	p[i].getFatherid(i,p);
	        }
	        //3 ѭ����������������ż����Ӧ��Ů��Ϣ
	        int TreeNUM = personnum;
	        for(int i=0; i<TreeNUM; i++) {	//ѭ����ȡ������ż��Ϣ����Ů��Ϣ
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
		String pattern = "[��]?[һ|��|إ|��|��|��|��|��|��|��|ʮ]{1,3}��";
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
				case "ʮһ��":num=11;break;
				case "��ʮ����":num=12;break;
				case "ʮ����":num=12;break;
				case "��ʮ����":num=13;break;
				case "ʮ����":num=13;break;
				case "��ʮ����":num=14;break;
				case "ʮ����":num=14;break;
				case "��ʮ����":num=15;break;
				case "ʮ����":num=15;break;
				case "��ʮ����":num=16;break;
				case "ʮ����":num=16;break;
				case "��ʮ����":num=17;break;
				case "ʮ����":num=17;break;
				case "��ʮ����":num=18;break;
				case "ʮ����":num=18;break;
				case "��ʮ����":num=19;break;	
				case "ʮ����":num=19;break;	
				case "�ڶ�ʮ��":num=20;break;
				case "��ʮ��":num=20;break;
				case "�ڶ�ʮһ��":num=21;break;
				case "��ʮһ��":num=21;break;
				case "إ����":num=21;break;
				case "�ڶ�ʮ����":num=22;break;
				case "��ʮ����":num=22;break;
				case "إһ��":num=21;break;
				case "�ڶ�ʮ����":num=23;break;
				case "��ʮ����":num=23;break;
				case "إ����":num=23;break;
				case "�ڶ�ʮ����":num=24;break;
				case "��ʮ����":num=24;break;
				case "إ����":num=24;break;
				case "�ڶ�ʮ����":num=25;break;
				case "��ʮ����":num=25;break;
				case "إ����":num=25;break;
				case "�ڶ�ʮ����":num=26;break;
				case "��ʮ����":num=26;break;
				case "إ����":num=26;break;
				case "�ڶ�ʮ����":num=27;break;
				case "��ʮ����":num=27;break;
				case "إ����":num=27;break;
				case "�ڶ�ʮ����":num=28;break;
				case "��ʮ����":num=28;break;
				case "إ����":num=28;break;
				case "�ڶ�ʮ����":num=29;break;				
				case "��ʮ����":num=29;break;				
				case "إ����":num=29;break;				
				case "����ʮ��":num=30;break;
				case "��ʮ��":num=30;break;
				case "����ʮһ��":num=31;break;
				case "��ʮһ��":num=31;break;
				case "����ʮ����":num=32;break;
				case "��ʮ����":num=32;break;
				case "����ʮ����":num=33;break;
				case "��ʮ����":num=33;break;
				case "����ʮ����":num=34;break;
				case "��ʮ����":num=34;break;
				case "����ʮ����":num=35;break;
				case "��ʮ����":num=35;break;
				case "����ʮ����":num=36;break;
				case "��ʮ����":num=36;break;
				case "����ʮ����":num=37;break;
				case "��ʮ����":num=37;break;
				case "����ʮ����":num=38;break;
				case "��ʮ����":num=38;break;
				case "����ʮ����":num=39;break;
				case "��ʮ����":num=39;break;
				case "����ʮ��":num=40;break;
				case "��ʮ��":num=40;break;
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
