package TextForDoc;

import java.io.File;

public class TestForFileRead {
	public static void main(String[] args) {
		File file = new File("data/FamilyBook");
		String[] filelist = file.list();
		for(int i=8; i<filelist.length;i++) {
			String[] saveFilename = filelist[i].split("\\.");
			System.out.println(saveFilename[0]);
		}
	}
}
