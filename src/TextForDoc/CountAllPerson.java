package TextForDoc;

import java.io.File;
import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class CountAllPerson {
	static int num = 0;
	public static void main(String[] args){
		File file = new File("data/Formatting");
		String[] filelist = file.list();
		for(int i=0;i<filelist.length;i++) {
			Workbook workbook;
			try {
				workbook = Workbook.getWorkbook(new File("data/Formatting1/"+filelist[i]));
				Sheet sheet = workbook.getSheet(0);
				int rows = sheet.getRows();
				num+=rows;
			} catch (BiffException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	//for
		System.out.println("num:"+num);
	}
}
