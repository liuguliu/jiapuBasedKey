package TextForDoc;

public class People {
	int id;	
	String name;
	int fatherid;
	int motherid;
	String partnerid="";
	String CourtesyName;
	String pesudonym;
	String gender="ÄÐ";
	String ChineseBirthday;
	String Chinesedeathdate;
	String buried; 
	int familyrank;
	int generation;
	String family="Íô";
	String description;
	int buriedindex = 0;
	String fathername="";
	String daughterinfo="";
	String soninfo="";
	String spouseinfo="";
	String personInfo="";
	
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
}
