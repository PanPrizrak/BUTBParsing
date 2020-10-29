import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.asynchttpclient.*;

import java.io.*;
import java.util.*;

public class Application {

    public static void main(String[] args) {
        //String searchQuery = "iphone 6s" ;
        String baseUrl = "https://www.butb.by/tsifry-i-analitika/birzhevye-kotirovki/" ;
        WebClient clientFN = new WebClient();
        clientFN.getOptions().setCssEnabled(false);
        clientFN.getOptions().setJavaScriptEnabled(false);
        String DirName = "/home/panprizrak/Documents/butb/";
        File  myFolder = new File(DirName);
        File[] files = myFolder.listFiles();
        StringBuilder bufMyFilesInFolder = new StringBuilder();
        for (File myFile : files){
            bufMyFilesInFolder.append(myFile.toString().substring((myFile.toString().lastIndexOf('/')+1),myFile.toString().length()));
        }
        System.out.println(bufMyFilesInFolder.toString());
        try {
            String searchUrl = baseUrl;// + "search/sss?sort=rel&query=" + URLEncoder.encode(searchQuery, "UTF-8");
            HtmlPage page = clientFN.getPage(searchUrl);
            HtmlElement Cotirovky = page.getHtmlElementById("acc1");
            List<HtmlElement> items = (List<HtmlElement>) Cotirovky.getHtmlElementsByTagName("li") ;
            if(items.isEmpty()){
                System.out.println("No items found !");
            }else{
                for(HtmlElement htmlItem : items){
                    HtmlAnchor itemAnchor = ((HtmlAnchor) htmlItem.getFirstByXPath(".//a"));
                    //HtmlElement spanPrice = ((HtmlElement) htmlItem.getFirstByXPath(".//a/span[@class='result-price']")) ;

                    // It is possible that an item doesn't have any price, we set the price to 0.0 in this case
                    String cotirovkiText = Cotirovky.asText() ;
                    if (!itemAnchor.getHrefAttribute().isEmpty()){
                    String attributeURL = itemAnchor.getHrefAttribute();

                   /* Item item = new Item();
                    item.setTitle(itemAnchor.asText());
                    item.setUrl( baseUrl + itemAnchor.getHrefAttribute());

                    item.setPrice(new BigDecimal(itemPrice.replace("$", "")));*/


                    /*ObjectMapper mapper = new ObjectMapper();
                    String jsonString = mapper.writeValueAsString(item) ;*/

                   // System.out.println(cotirovkiText);
                    System.out.println(attributeURL);
                        AsyncHttpClient client = Dsl.asyncHttpClient();
                        String[] bufFileName = attributeURL.split("/");
                        int lengthFileNameURL = bufFileName.length;
                        String fileName = bufFileName[bufFileName.length-1];
                        System.out.println(fileName);
                        fileName = fileName.substring(0,fileName.lastIndexOf('.'));

                        fileName = fileName.replace('.','_');
                        if (fileName.indexOf('%')!=0)
                            fileName = fileName.replace('%','_');

                        final FileOutputStream stream = new FileOutputStream(DirName+fileName+".xls");
                        client.prepareGet("https://www.butb.by" + attributeURL).execute(new AsyncCompletionHandler<FileOutputStream>() {

                            @Override
                            public State onBodyPartReceived(HttpResponseBodyPart bodyPart)
                                    throws Exception {
                                stream.getChannel().write(bodyPart.getBodyByteBuffer());
                                return State.CONTINUE;
                            }

                            @Override
                            public FileOutputStream onCompleted(Response response)
                                    throws Exception {
                                return stream;
                            }
                        });
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
