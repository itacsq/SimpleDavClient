package starter;

import utilities.DAV;
import manager.logging.Logger;
import manager.webdav.WebDAVClient;

public class Feynman
{
  private Logger logger;

  public Feynman()
  {
    this.logger = new Logger();
    this.logger.info("[DAVVER] Dav Initializing ... ");
  }

  public void init(String[] args)
  {
    String msg = "";

    if (args.length == 0)
    {
      msg = msg + "Use: java -jar davver.jar \"action\" \"filename\" \"fspath\" \"davpath\" \"user\" \"pswd\" [logging_policy]";
      msg = msg + "\n";
      msg = msg + "\n- action: download, delete;";
      msg = msg + "\n- filename: the name of file, with extension, which you want to download (ex. test.xls);";
      msg = msg + "\n- fspath: the path of file system where you want to save the file;";
      msg = msg + "\n- davpath: the address of the content server (ex. http://hostname:port/davfolder);";
      msg = msg + "\n- user: repository username;";
      msg = msg + "\n- pswd: repository password;";

      System.out.println(msg);
    }
    else if (args.length == 6) {
    	
      String action = args[0];
      String filename = args[1];
      String fspath = args[2];
      String webaddr = args[3];
      String user = args[4];
      String pswd = args[5];

      if(user.equals("SAS_SADM")){
    	  user = DAV.USER.getTxt();
    	  pswd = DAV.PASS.getTxt();
      }
      
      
      String lg = "";
      lg = lg + "[DAVVER] Args: ";
      lg = lg + " - Action <" + action + "> ";
      lg = lg + " - File <" + filename + "> ";
      lg = lg + " - Folder <" + fspath + "> ";
      lg = lg + " - DavAddress <" + webaddr + "> ";
      lg = lg + " - User <" + user + "> ";
      this.logger.info(lg);
      System.out.println(lg);
      WebDAVClient wdav = new WebDAVClient(user, pswd, webaddr, fspath, this.logger);
      if(action.equals("download"))
    	  wdav.DownloadFromDav(filename);
      else if(action.equals("delete"))
    	  wdav.Delete(filename);
    }
  }

  public void destroy()
  {
    this.logger.info("[DAVVER] ... Dav Completed");
    this.logger.close();
  }

  public static void main(String[] args){
	  /* *** TEST 
	  	String [] args2 = new String[6];
	  	args2[0] = "delete";
	  	args2[1] = "CDR1_OS001_OP000_01_100_F_20130320_1150.xls";
	  	args2[2] = "";
	  	args2[3] = "http://censrvvtpaws001.ita.sas.com:8080/SASContentServer/repository/default/sasdav/";
	  	args2[4] = "SAS_SADM";
	  	args2[5] = "";
	  	*/
		
	  Feynman f = new Feynman();
	  f.init(args);
	  f.destroy();
  }
}