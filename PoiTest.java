import java.io.BufferedInputStream;							//导入所需要的包
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;


public class PoiTest {
	InputStream in;						//输入流
	XWPFDocument docx;					//docx的word文件
	XWPFWordExtractor extractor;			//用来抽取word内容信息
	List<XWPFParagraph>paras;				//段落列表
	
	public void read(String path)					//读文件的函数
	{
		try {
			in=new BufferedInputStream(new FileInputStream(path));			//读入文件     
			docx=new XWPFDocument(in);				
			extractor=new XWPFWordExtractor(docx);
			paras=docx.getParagraphs();			//获取文件段落列表
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void getwordTitles(String path) throws IOException{			//提取标题及副标题框架的函数
		
		read(path);			//读取文件
		
	    System.out.println("文档标题和副标题框架为：");
		for(int i=0;i<paras.size();i++){
			String text=paras.get(i).getParagraphText();		//获取每段文字内容
			String style=paras.get(i).getStyle();				//获取文字的格式style
			
			if("1".equals(style)){
				System.out.println(text+"--["+style+"]");
				
			}else if("2".equals(style)){
				System.out.println(text+"--["+style+"]");
			}else if("3".equals(style)){
				System.out.println(text+"--["+style+"]");
			}else{
				continue;
			}
		}	
	}
	public void cncharacters(String path){				//计算文档字数的函数
		
		read(path);			//读取文件
		
		
			int length=0;
			
			for(int i=0;i<paras.size();i++)     	//计算文档字数
			{
				length+=paras.get(i).getText().length();
			
			}
			
			System.out.println("文档总字数为："+length);
		
	}
	public void cnparagraphs(String path){				//计算文档段落数的函数
		
		read(path);			
		
		System.out.println("文档总段数为："+paras.size());
	}
	public void replacetext(String path,String original,String newone){    //替换字符串的函数
		
		read(path);
		
		System.out.println(extractor.getText().replace(original, newone));
	}
	public void getarticle(String path){			//获取全文的函数
		
		read(path);
		
		System.out.println(extractor.getText());
	}
	public static void main(String[] args) throws IOException {
		
		String path="makesi.docx";
		
		PoiTest test=new PoiTest();			//实例化对象
		
		test.getarticle(path);			//获取全文		
		
		System.out.println();
		
		test.cncharacters(path);		//计算字数
		
		System.out.println();
		
		test.cnparagraphs(path);		//计算段落数
		
		System.out.println();
		
		test.getwordTitles(path);		//获取标题副标题框架
		
		System.out.println();
		
		test.replacetext(path, "在", "是");		//替换字符串

	}
}

