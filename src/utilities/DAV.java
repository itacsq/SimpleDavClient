package utilities;

public enum DAV
{
  USER("sasadm@saspw"), 
  PASS("$adm!2010"), 
  ADDRSS("");

  private String txt;

  private DAV(String txt)
  {
    this.txt = txt;
  }
  public String getTxt() {
    return this.txt;
  }
  public void setTxt(String txt) {
    this.txt = txt;
  }
  public String toString() {
    return this.txt;
  }
}