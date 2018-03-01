import java.io.*;											//��������Ҫ�İ�
import java.awt.image.BufferedImage;
import javax.imageio.*;


public class ImageProcessing {				
	
		 BufferedImage image;		//ͼƬ
		 int[]imagearr;				//ͼƬ���ص�����
		 
		public void reading(String ipath) throws IOException{		//��ͼƬ�ĺ���
			
			image=ImageIO.read(new File(ipath));	//����ͼƬ
			
			int width=image.getWidth();
			int height=image.getHeight();
			imagearr = new int[width*height];
			
			image.getRGB(0, 0, width, height, imagearr, 0, width);		//��ͼƬ������װ��imagearr
		}
		public void cutting(String ipath,int newx,int newy,int newwidth,int newheight) {	//�ü�ͼƬ�ĺ���
			try{
				reading(ipath);			//����ͼƬ
				
				if(newwidth>image.getWidth()||newheight>image.getHeight())			//�ж��Ƿ񳬳���Χ
					System.out.println("�ü�������Χ��");
				else{
		
				int [][]inputData=new int[image.getWidth()][image.getHeight()];
				
				BufferedImage newimage=new BufferedImage(newwidth,newheight,image.getType());		//�����µ�BufferedImage����
				
				for(int m=0;m<inputData.length;m++){
					for(int k=0;k<inputData[0].length;k++){
						inputData[m][k]=image.getRGB(m, k);				//��ȡGRB��װ������
					}
				}	
				for(int i=newy;i<newheight;i++){						//��ͼʵ��
					for(int j=newx;j<newwidth;j++){
					newimage.setRGB(i-newy, j-newx, inputData[i][j]);	
				}
			}
				
				File outimage=new File("newimage_cutting.jpg");		//�����µ�ͼƬ�ļ�
				ImageIO.write(newimage, "jpg", outimage);			//д���ü����ͼƬ�ļ�
				System.out.println("Success");
			}
		}
				catch(IOException e){
					e.printStackTrace();
			}
		}
		public void shrink(String ipath,double times){		//����ͼƬ�ĺ���
			
			try {
				
				reading(ipath);			//��ȡͼƬ
				int newheight=(int) (image.getHeight()*times);
				int newwidth=(int) (image.getWidth()*times);
				
				if(newheight>image.getHeight()||newwidth>image.getWidth())
					System.out.println("������ʹͼƬ��С�Ĳ�����");
				else{
				int [][][]outputThreeDeminsionData=new int [newwidth][newheight][4];		//������ά����
				//����processOntToThreeDeminsion����
				double [][][]input3DData=processOneToThreeDeminsion(imagearr,image.getWidth(),image.getHeight());		
				
				float rowratio=(float)image.getWidth()/(float)newwidth;		//�б���
				float colratio=(float)image.getHeight()/(float)newheight;	//�б���
				
				for(int row=0;row<newheight;row++){				//˫���Բ�ֵ�㷨
					
					double srcRow=(float)row*rowratio;
					double j=Math.floor(srcRow);
					double i=srcRow-j;
					
					for(int col=0;col<newwidth;col++){
						
						double srcCow=(float)col*colratio;
						double k=Math.floor(srcCow);
						double m=srcCow-k;
						
						double coffiecent1=(1.0d-i)*(1.0d-m);
						double coffiecent2=i*(1.0d-m);
						double coffiecent3=i*m;
						double coffiecent4=(1.0d-i)*m;
						
						for(int l=0;l<4;l++){
							outputThreeDeminsionData[row][col][l]=(int)(
									coffiecent1*input3DData[getClip((int)j,image.getHeight()-1,0)][getClip((int)k,image.getWidth()-1,0)][l]
									+coffiecent2*input3DData[getClip((int)j+1,image.getHeight()-1,0)][getClip((int)k,image.getWidth()-1,0)][l]
									+coffiecent3*input3DData[getClip((int)j+1,image.getHeight()-1,0)][getClip((int)k+1,image.getWidth()-1,0)][l]
									+coffiecent4*input3DData[getClip((int)j,image.getHeight()-1,0)][getClip((int)k+1,image.getWidth()-1,0)][l]);	
						}
					}
					
				}
				//����convertToDeminsionData����
				int[]newimagearr=convertToDeminsionData(outputThreeDeminsionData,newwidth,newheight);
				
				BufferedImage newimage=new BufferedImage(newwidth,newheight,image.getType());
				newimage.setRGB(0, 0, newwidth, newheight,newimagearr, 0, newwidth);
				
				File outimage=new File("newimage_shrinking.jpg");
				ImageIO.write(newimage, "jpg", outimage);
				
				System.out.println("Success");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void enlarging(String ipath,int times){			//�Ŵ�ͼƬ�ĺ���
			
			
			try {
				reading(ipath);			//��ȡͼƬ
				
				int newheight=image.getHeight()*times;
				int newwidth=image.getWidth()*times;
				
				if(newheight<=image.getHeight()||newwidth<=image.getWidth())
				{System.out.println("������ʹͼƬ�Ŵ�Ĳ�����");}
				else{
					
					int [][][]outputThreeDeminsionData=new int [newwidth][newheight][4];		//������ά����
					//����processOntToThreeDeminsion����
					double [][][]input3DData=processOneToThreeDeminsion(imagearr,image.getWidth(),image.getHeight());		
					
					float rowratio=(float)image.getWidth()/(float)newwidth;		//�б���
					float colratio=(float)image.getHeight()/(float)newheight;	//�б���
					
					for(int row=0;row<newheight;row++){				//˫���Բ�ֵ�㷨
						
						double srcRow=(float)row*rowratio;
						double j=Math.floor(srcRow);
						double i=srcRow-j;
						
						for(int col=0;col<newwidth;col++){
							
							double srcCow=(float)col*colratio;
							double k=Math.floor(srcCow);
							double m=srcCow-k;
							
							double coffiecent1=(1.0d-i)*(1.0d-m);
							double coffiecent2=i*(1.0d-m);
							double coffiecent3=i*m;
							double coffiecent4=(1.0d-i)*m;
							
							for(int l=0;l<4;l++){
								outputThreeDeminsionData[row][col][l]=(int)(
										coffiecent1*input3DData[getClip((int)j,image.getHeight()-1,0)][getClip((int)k,image.getWidth()-1,0)][l]
										+coffiecent2*input3DData[getClip((int)j+1,image.getHeight()-1,0)][getClip((int)k,image.getWidth()-1,0)][l]
										+coffiecent3*input3DData[getClip((int)j+1,image.getHeight()-1,0)][getClip((int)k+1,image.getWidth()-1,0)][l]
										+coffiecent4*input3DData[getClip((int)j,image.getHeight()-1,0)][getClip((int)k+1,image.getWidth()-1,0)][l]);	
							}
						}
						
					}
					//����convertToDeminsionData����
					int[]newimagearr=convertToDeminsionData(outputThreeDeminsionData,newwidth,newheight);
					
					BufferedImage newimage=new BufferedImage(newwidth,newheight,image.getType());
					newimage.setRGB(0, 0, newwidth, newheight,newimagearr, 0, newwidth);
					
					File outimage=new File("newimage_enlarging.jpg");
					ImageIO.write(newimage, "jpg", outimage);
					
					System.out.println("Success");
				}
				} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private int[]convertToDeminsionData(int[][][]data,int imgCols,int imgRows)			//ת����ά����Ϊһά����ĺ���
		{
			int[] oneDPix=new int[imgCols*imgRows*4];
			
			for(int row=0,cnt=0;row<imgRows;row++){
				for(int col=0;col<imgCols;col++){
					oneDPix[cnt]=((data[row][col][0]<<24)&0xFF000000)
							|((data[row][col][1]<<16)&0x00FF0000)
							|((data[row][col][2]<<8)&0x0000FF00)
							|((data[row][col][3]&0x000000FF));
					cnt++;
				}
			}
			return oneDPix;
			
		}
		private int getClip(int x,int max,int min){			
			return x>max?max:x<min?min:x;
		}
		private double [][][]processOneToThreeDeminsion(int[] oneDPix2,int imgrows,int imgcols){		//ת��һά����Ϊ��ά����ĺ���
			double[][][] tempData=new double[imgrows][imgcols][4];
			for(int row=0;row<imgrows;row++){
				int[]arow=new int[imgcols];
				for(int col=0;col<imgcols;col++){
					int element=row*imgcols+col;
					arow[col]=oneDPix2[element];
				}
				for(int col=0;col<imgcols;col++){
					tempData[row][col][0]=(arow[col]>>24)&0xFF;			//alpha
					tempData[row][col][1]=(arow[col]>>16)&0xFF;			//red
					tempData[row][col][2]=(arow[col>>8])&0xFF;			//green
					tempData[row][col][3]=(arow[col])&0xFF;				//blue
				}
			}
			return tempData;
		}
		public static void main(String[]args) throws IOException{
			String imagepath_cut="cow.jpg";
			String imagepath_changesize="orange.jpg";
			
			ImageProcessing process=new ImageProcessing();
			
			process.shrink(imagepath_changesize, 0.5);
			process.enlarging(imagepath_changesize, 2);
			process.cutting(imagepath_cut, 50, 17, 199, 200);
		}
}



