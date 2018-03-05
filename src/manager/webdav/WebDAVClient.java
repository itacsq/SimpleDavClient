package manager.webdav;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import manager.logging.Logger;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

public class WebDAVClient{
	private String DAV_USER;
	private String DAV_PSWD;
	private String DAV_ADDR;
	private String DAV_TMP;
	private Logger logger;

	public WebDAVClient(String user, String pswd, String addr, String tmpPath, Logger logger){
		this.DAV_USER = user;
		this.DAV_PSWD = pswd;
		if(!addr.endsWith("/"))
			addr = addr + "/";
		this.DAV_ADDR = addr;
		
		this.DAV_TMP = tmpPath;
		this.logger = logger;
	}
	
	public boolean Delete(String fileName){
		try{
			HttpClient client = new HttpClient();
			Credentials creds = new UsernamePasswordCredentials(this.DAV_USER, this.DAV_PSWD);
			client.getState().setCredentials(AuthScope.ANY, creds);

			String deleteFile = this.DAV_ADDR + fileName;
			this.logger.debug("[WEBDAV]: downloading file <" + deleteFile + ">");
			deleteFile = deleteFile.replaceAll(" ", "%20");
		
			System.out.println(deleteFile);
			
			DeleteMethod delMethod = new DeleteMethod(deleteFile);
			client.executeMethod(delMethod);
			
			int rc = delMethod.getStatusCode();
			if(rc == 204){
				String mex = "[DAV CLIENT] Delete successfull!";
				System.out.println(mex);
				this.logger.info(mex);
			}else{
				String err = "[DAV CLIENT] Error! Cant Delete: " + fileName;
				this.logger.error(err);
				System.out.println(err);
			}
		}
		catch (HttpException ex){
			String err = "ERROR: " + ex.getLocalizedMessage();
			this.logger.error(err);
			System.out.println(err);
		}
		catch (IOException ex) {
			String err = "ERROR: " + ex.getLocalizedMessage();
			this.logger.error(err);
			System.out.println(err);
		}
		return false;
	}
	
	
	
	public boolean DownloadFromDav(String fileName){
		try{
			HttpClient client = new HttpClient();
			Credentials creds = new UsernamePasswordCredentials(this.DAV_USER, this.DAV_PSWD);
			client.getState().setCredentials(AuthScope.ANY, creds);

			String downloadFile = this.DAV_ADDR + "/" + fileName;
			this.logger.debug("[WEBDAV]: downloading file <" + downloadFile + ">");
			downloadFile = downloadFile.replaceAll(" ", "%20");
			
			GetMethod httpMethod = new GetMethod(downloadFile);
			client.executeMethod(httpMethod);
			
			if (httpMethod.getResponseContentLength() > 0L) {
			this.logger.debug("[WEBDAV]: DOWNLOAD Location: " + this.DAV_TMP + "/" + fileName);
			InputStream inputStream = httpMethod.getResponseBodyAsStream();
			
			File responseFile = new File(this.DAV_TMP + "/" + fileName);
			OutputStream outputStream = new FileOutputStream(responseFile);
			byte[] buf = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0){
				outputStream.write(buf, 0, len);
			}
			outputStream.close();
			inputStream.close();
			if (responseFile.exists())
				return true;
			return false;
			}

			httpMethod.releaseConnection();
			String mex = "[DAV CLIENT] Download successfull!";
			System.out.println(mex);
			this.logger.info(mex);
		}
		catch (HttpException ex){
			String err = "ERROR: " + ex.getLocalizedMessage();
			this.logger.error(err);
			System.out.println(err);
		}
		catch (IOException ex) {
			String err = "ERROR: " + ex.getLocalizedMessage();
			this.logger.error(err);
			System.out.println(err);
		}
		return false;
	}
	
	/**
	* This example retrieves a list of the repositories that are registered
	* on the SAS Metadata Server.
	* @param pathFrom physical file path 
	* @param fileFrom physical file name 
	* @param pathTo dav file path
	* @param fileTo dav file name 
	* @return true if ok
	*/
	public boolean upload(String pathFrom, String fileFrom, String pathTo, String fileTo){
		try{
			File from = new File(pathFrom + "/" + fileFrom);
			if(!from.exists())
				return false;
			HttpClient client = new HttpClient();
		    Credentials creds = new UsernamePasswordCredentials(DAV_USER, DAV_PSWD);
		    client.getState().setCredentials(AuthScope.ANY, creds);
		    
		    String uploadFile = pathTo + "/" + fileTo + "(DocumentMSExcel)";
		    uploadFile = uploadFile.replaceAll(" ","%20");
		    
		    PutMethod method = new PutMethod(uploadFile);
		    RequestEntity requestEntity = new InputStreamRequestEntity(
		    		new FileInputStream(from));
		    method.setRequestEntity(requestEntity);
		    client.executeMethod(method);
		    System.out.println(method.getStatusCode() + " " + method.getStatusText());
		    if(method.getStatusCode()==201)
		    	return true;
		    return false;
		}
		catch(HttpException ex){
			ex.printStackTrace();
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		return false;
	}
}