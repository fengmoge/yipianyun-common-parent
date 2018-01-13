package yipianyun.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;





import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.SystemDefaultHttpClient;

import com.alibaba.fastjson.JSONObject;


 

public class ResFileUtils {
	public static final  int BYTE_SIEX=1024;
	public static final  int BYTE_SIEX2=2048;
//	public static final String RESOURCE_FS_URL = PropertiesUtil.getProperties("mail","resource.url");


	private static final Log log=LogFactory.getLog(ResFileUtils.class);
	public static boolean fileSrcDownloadFromRes(HttpServletResponse response, String filePath, String fileName)
			throws Exception {
		filePath += "&src=Y";
		return fileDownloadFromRes(response, filePath, fileName);
	}
	public static boolean fileDownloadFromRes(HttpServletResponse response, String filePath, String fileName)
			throws Exception {
		String url = PropertiesUtil.getProperties("mail", "resource.url")+filePath;
		//获取client
		/**
		 * 此处不用DefaultHttpClient类创建client,这个类有问题，可以用mailDefaultHttpClient类替代，但
		 * 建议使用4.3版本的包中HttpClients类
		 */
//		DefaultHttpClient httpClient = new mailDefaultHttpClient();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		//获取远程reqest1
		HttpGet httpGet = new HttpGet(url);
		
		//获取远程request1请求后的响应1
		CloseableHttpResponse response1 = httpClient.execute(httpGet);
		//获取远程响应1的实体
		 HttpEntity httpEntity = response1.getEntity();
		 //获取远程响应的输入流
		 InputStream is = httpEntity.getContent();
		 long contentLength = httpEntity.getContentLength();
		 log.info("contentLength="+contentLength);
//		 if(contentLength==-1){
//			 return false;
//		 }
		//设置response的相应类型为下载
		 response.setContentType("application/x-download");
		 response.setHeader("Content-disposition", "attachment;fileName="+URLEncoder.encode(fileName,"utf-8"));
		 BufferedInputStream buffin =null;
		 BufferedOutputStream buffout =null;
		 try{
			 //将远程输入流包装成缓存输入流
			 buffin= new BufferedInputStream(is);
			 //当前响应的缓存输出流
			 buffout = new BufferedOutputStream(response.getOutputStream());
			 byte[]  buff=new byte[BYTE_SIEX];
			 int length=-1;
				 while((length=buffin.read(buff))!=-1){
					 buffout.write(buff, 0, length);
					 buffout.flush();
				 }
				 return true;
		 }catch(Exception e){
			 log.error("下载文件失败",e);
			throw(e);
		 }finally{
			 if(response1!=null){
				 try {
					response1.close();
				} catch (Exception e) {
				}
			 }
			 if(is!=null){
				 try {
					is.close();
				} catch (Exception e) {
				}
			 }
			 if(buffout!=null){
				 try {
					buffout.close();
				} catch (Exception e) {
				}
			 }
		 }
		
	}
		 
	/**
	 * 文件上传到远程资源库中(文件格式不转换)
	 * @param file
	 * @param userId
	 * @param fileType
	 * @returnString
	 */
	public static String fileUpload2Res(File file,String fileType,String userId)throws Exception{
		//传递到资源服务器的请求参数
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> resourceParamsMap = new HashMap<String, Object>();
		resourceParamsMap.put("isSecret", "N");
		resourceParamsMap.put("creator", userId);
		resourceParamsMap.put("fileType", fileType);
		map.put("toPDF", "N");
		map.put("secret", "1");
		map.put("resourceParams", resourceParamsMap);
		String jsonStr = JSONObject.toJSONString(map);
		//IP，端口，应用名
		String url = PropertiesUtil.getProperties("mail", "resource.url");
		//上传文件路径
		String uploadpath = PropertiesUtil.getProperties("mail", "resource.uploadpath");
		String resourceUrl=url+uploadpath+URLEncoder.encode(jsonStr, "utf-8");
		
		HttpClient httpClient = new HttpClient();
		PostMethod method = new PostMethod(resourceUrl);
		NameValuePair valuePair = new NameValuePair("data","");
		NameValuePair[] params={valuePair};
		method.setRequestBody(params);
		HttpMethodParams param = method.getParams();

		
		Part[] parts = { new DesFilePart("file", file, null, "UTF-8") };
		method.setRequestEntity(new MultipartRequestEntity(parts, param));
		param.setContentCharset("UTF-8");
		httpClient.executeMethod(method);
		
		InputStream stream = null;
		BufferedReader reader = null;
		try {
			//上传文件后从响应中读返回数据
			stream = method.getResponseBodyAsStream();
			reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			String str = null;
			StringBuilder builder = new StringBuilder();
			while ((str = reader.readLine()) != null) {
				builder.append(str);
			}
			return builder.toString();
			
		}catch(Exception e){
			log.error("上传文件"+file.getName()+"失败"+e);
			throw e;
		} finally {
			if (stream != null) {
				stream.close();
			}
			if (reader != null) {
				reader.close();
			}
			// 释放连接
			method.releaseConnection();
		}
		
	}
	
	/**
	 * 
	 * @param message
	 *            资源服务器上传文件后返回的信息
	 * @return 文件上传后在资源文件服务器访问路径
	 */
	public static String getFilePath(String message) {
		if (null != message) {
			JSONObject jsonObject = JSONObject.parseObject(message);
			boolean success = jsonObject.getBoolean("success");
			if (success) {
				JSONObject data = jsonObject.getJSONObject("data");
				 String fileGroup = data.getString("fileGroup");
				 String filePath = data.getString("filePath");
				 log.info( "/" + fileGroup + "/" + filePath);
//				 return "/" + fileGroup + "/" + filePath;
				String id = data.getString("id");
				return "/fileView/resourceView?resId=" + id;
			}
		}

		log.error("文件上传失败。(" + message + ")");
		return null;
	}
}
