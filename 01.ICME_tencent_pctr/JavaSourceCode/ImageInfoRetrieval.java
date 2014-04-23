
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class ImageInfoRetrieval {
	private static String webFolder = "http://students.engr.scu.edu/~she/icme_image/";
	private static String rgb = "variable rgb statistic avg value ";
	private static String hsv = "variable hsv statistic avg value ";
	
    private static CloseableHttpClient httpclient = null;
    private static ResponseHandler<String> responseHandler = null;
	
    public static void main(String[] args) {
        BufferedReader br = null, br1 = null;
        BufferedWriter bw = null;
        try {
    	// images name list
    	Path path = FileSystems.getDefault().getPath("flist");
    	if (! Files.isReadable(path)) {
    		System.out.println("File \"flist\" does not exist!");
    		return;
    	}
    	
    	// file for write
    	Path path1 = FileSystems.getDefault().getPath("img_attributes");
    	// Files.deleteIfExists(path1);
    	// Files.createFile(path1);
    	
        // create http client
    	httpclient = HttpClients.createDefault();
        // Create a custom response handler
        responseHandler = new ResponseHandler<String>() {

            public String handleResponse(
                    final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return (entity != null) ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }

        };
        
    	// Create read and write buffers
        br = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        br1 = Files.newBufferedReader(path1, StandardCharsets.UTF_8);

        // Skip the processed lines.
        while (br1.readLine() != null) {
            br.readLine();
        }
        br1.close();

        bw = Files.newBufferedWriter(path1, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
    	
        String imgFile;
        int pos;
        int count = 0;
        while ((imgFile = br.readLine()) != null) {
        	String info = getImgInfo(imgFile);
        	bw.write(info, 0, info.length());
        	bw.newLine();
        	
        	if (++count % 20 == 0) {
        		bw.flush();
                }
        }
        bw.flush();

        httpclient.close();
        br.close();
        bw.close();

        } catch (Exception e) {
             e.printStackTrace();
        }
    }
    
    
    private static String getImgInfo(String image) {
        String imageName = image.substring(0, image.indexOf(' '));

        String imgInfo = null;
        
        String imgUrl = new StringBuffer("http://mkweb.bcgsc.ca/color_summarizer/?text=1&url=")
        					.append(webFolder).append(imageName).append("&precision=extreme").toString();
        // System.out.println("imgURL = " + imgUrl);
    	try {
            HttpGet httpget = new HttpGet(imgUrl);

            imgInfo = httpclient.execute(httpget, responseHandler);
    	} catch (Exception e) {
    	}
    	
    	// parse Image Info
    	if (imgInfo == null)
    		return null;
    	
        // System.out.print(imgInfo);
       
    	StringBuffer f = new StringBuffer(image.substring(0, image.indexOf('.'))).append('\t').append(image.substring(imageName.length() + 1));
    	
    	int pos1 = imgInfo.indexOf(rgb) + 33;
    	int pos2 = imgInfo.indexOf(',', pos1);
    	f.append('\t').append(imgInfo.substring(pos1, pos2)); // r
    	pos1 = pos2 + 1;
    	pos2 = imgInfo.indexOf(',', pos1);
    	f.append('\t').append(imgInfo.substring(pos1, pos2)); // g
    	pos1 = pos2 + 1;
    	pos2 = imgInfo.indexOf(' ', pos1);
    	f.append('\t').append(imgInfo.substring(pos1, pos2)); // b

    	pos1 = imgInfo.indexOf(hsv) + 33;
    	pos2 = imgInfo.indexOf(',', pos1);
    	f.append('\t').append(imgInfo.substring(pos1, pos2)); // h
    	pos1 = pos2 + 1;
    	pos2 = imgInfo.indexOf(',', pos1);
    	f.append('\t').append(imgInfo.substring(pos1, pos2)); // s
    	pos1 = pos2 + 1;
    	pos2 = imgInfo.indexOf(' ', pos1);
    	f.append('\t').append(imgInfo.substring(pos1, pos2)); // v
    	
    	return f.toString();
    }

}
