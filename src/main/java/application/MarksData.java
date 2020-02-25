package application;

public class MarksData
{
	public String aat, assignment, cie1, cie2, cie3, total, sub;
	
	public MarksData() {}
	
    public String getAat() { return aat; }

    public String getAssignment() { return assignment; }
    
    public String getCie1() { return cie1; }
    
    public String getCie2() { return cie2; }
    public String getCie3() { return cie3; }
    public String getTotal() { return total; }
    public String getSub() { return sub; }
    public void setAat(String name) {
        this.aat = name;
    }
    
    public void setAssignment(String name) {
        this.assignment = name;
    }
    
    public void setCie1(String name) {
        this.cie1 = name;
    }
    
    public void setCie2(String name) {
        this.cie2 = name;
    }
    public void setCie3(String name) {
        this.cie3 = name;
    }
    public void setTotal(String name) {
        this.total = name;
    }
    public void setSub(String name) {
        this.sub = name;
    }
}