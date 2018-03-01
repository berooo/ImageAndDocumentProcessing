import java.io.BufferedInputStream;							//��������Ҫ�İ�
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;


public class PoiTest {
	InputStream in;						//������
	XWPFDocument docx;					//docx��word�ļ�
	XWPFWordExtractor extractor;			//������ȡword������Ϣ
	List<XWPFParagraph>paras;				//�����б�
	
	public void read(String path)					//���ļ��ĺ���
	{
		try {
			in=new BufferedInputStream(new FileInputStream(path));			//�����ļ�     
			docx=new XWPFDocument(in);				
			extractor=new XWPFWordExtractor(docx);
			paras=docx.getParagraphs();			//��ȡ�ļ������б�
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void getwordTitles(String path) throws IOException{			//��ȡ���⼰�������ܵĺ���
		
		read(path);			//��ȡ�ļ�
		
	    System.out.println("�ĵ�����͸�������Ϊ��");
		for(int i=0;i<paras.size();i++){
			String text=paras.get(i).getParagraphText();		//��ȡÿ����������
			String style=paras.get(i).getStyle();				//��ȡ���ֵĸ�ʽstyle
			
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
	public void cncharacters(String path){				//�����ĵ������ĺ���
		
		read(path);			//��ȡ�ļ�
		
		
			int length=0;
			
			for(int i=0;i<paras.size();i++)     	//�����ĵ�����
			{
				length+=paras.get(i).getText().length();
			
			}
			
			System.out.println("�ĵ�������Ϊ��"+length);
		
	}
	public void cnparagraphs(String path){				//�����ĵ��������ĺ���
		
		read(path);			
		
		System.out.println("�ĵ��ܶ���Ϊ��"+paras.size());
	}
	public void replacetext(String path,String original,String newone){    //�滻�ַ����ĺ���
		
		read(path);
		
		System.out.println(extractor.getText().replace(original, newone));
	}
	public void getarticle(String path){			//��ȡȫ�ĵĺ���
		
		read(path);
		
		System.out.println(extractor.getText());
	}
	public static void main(String[] args) throws IOException {
		
		String path="makesi.docx";
		
		PoiTest test=new PoiTest();			//ʵ��������
		
		test.getarticle(path);			//��ȡȫ��		
		
		System.out.println();
		
		test.cncharacters(path);		//��������
		
		System.out.println();
		
		test.cnparagraphs(path);		//���������
		
		System.out.println();
		
		test.getwordTitles(path);		//��ȡ���⸱������
		
		System.out.println();
		
		test.replacetext(path, "��", "��");		//�滻�ַ���

	}
}

