package manager.logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger
{
  private PrintWriter log;

  public Logger()
  {
    try
    {
      String timestamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
      String logPath = timestamp + "_davver.log";
      this.log = new PrintWriter(new BufferedWriter(new FileWriter(logPath, true)));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void close() { this.log.close(); }


  public void info(String txt)
  {
    String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'h:m:ssZ").format(new Date());
    String info = "";
    info = info + "[" + timestamp + "] INFO :: " + txt;
    toFile(info);
  }
  public void debug(String txt) {
    String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'h:m:ssZ").format(new Date());
    String info = "";
    info = info + "[" + timestamp + "] DEBUG :: " + txt;
    toFile(info);
  }
  public void error(String txt) {
    String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'h:m:ssZ").format(new Date());
    String info = "";
    info = info + "[" + timestamp + "] !!! ERROR :: " + txt;
    toFile(info);
  }

  public void toFile(String txt)
  {
    try
    {
      this.log.println(txt);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}