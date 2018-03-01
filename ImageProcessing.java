import java.io.*;											//导入所需要的包
import java.awt.image.BufferedImage;
import javax.imageio.*;


public class ImageProcessing {				
	
		 BufferedImage image;		//图片
		 int[]imagearr;				//图片像素的数组
		 
		public void reading(String ipath) throws IOException{		//读图片的函数
			
			image=ImageIO.read(new File(ipath));	//读入图片
			
			int width=image.getWidth();
			int height=image.getHeight();
			imagearr = new int[width*height];
			
			image.getRGB(0, 0, width, height, imagearr, 0, width);		//将图片的像素装入imagearr
		}
		public void cutting(String ipath,int newx,int newy,int newwidth,int newheight) {	//裁剪图片的函数
			try{
				reading(ipath);			//读入图片
				
				if(newwidth>image.getWidth()||newheight>image.getHeight())			//判断是否超出范围
					System.out.println("裁剪超出范围！");
				else{
		
				int [][]inputData=new int[image.getWidth()][image.getHeight()];
				
				BufferedImage newimage=new BufferedImage(newwidth,newheight,image.getType());		//建立新的BufferedImage对象
				
				for(int m=0;m<inputData.length;m++){
					for(int k=0;k<inputData[0].length;k++){
						inputData[m][k]=image.getRGB(m, k);				//获取GRB并装入数组
					}
				}	
				for(int i=newy;i<newheight;i++){						//截图实现
					for(int j=newx;j<newwidth;j++){
					newimage.setRGB(i-newy, j-newx, inputData[i][j]);	
				}
			}
				
				File outimage=new File("newimage_cutting.jpg");		//创建新的图片文件
				ImageIO.write(newimage, "jpg", outimage);			//写出裁剪后的图片文件
				System.out.println("Success");
			}
		}
				catch(IOException e){
					e.printStackTrace();
			}
		}
		public void shrink(String ipath,double times){		//收缩图片的函数
			
			try {
				
				reading(ipath);			//读取图片
				int newheight=(int) (image.getHeight()*times);
				int newwidth=(int) (image.getWidth()*times);
				
				if(newheight>image.getHeight()||newwidth>image.getWidth())
					System.out.println("请输入使图片缩小的参数！");
				else{
				int [][][]outputThreeDeminsionData=new int [newwidth][newheight][4];		//创建三维数组
				//调用processOntToThreeDeminsion函数
				double [][][]input3DData=processOneToThreeDeminsion(imagearr,image.getWidth(),image.getHeight());		
				
				float rowratio=(float)image.getWidth()/(float)newwidth;		//行比例
				float colratio=(float)image.getHeight()/(float)newheight;	//列比例
				
				for(int row=0;row<newheight;row++){				//双线性插值算法
					
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
				//调用convertToDeminsionData函数
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

		public void enlarging(String ipath,int times){			//放大图片的函数
			
			
			try {
				reading(ipath);			//读取图片
				
				int newheight=image.getHeight()*times;
				int newwidth=image.getWidth()*times;
				
				if(newheight<=image.getHeight()||newwidth<=image.getWidth())
				{System.out.println("请输入使图片放大的参数！");}
				else{
					
					int [][][]outputThreeDeminsionData=new int [newwidth][newheight][4];		//创建三维数组
					//调用processOntToThreeDeminsion函数
					double [][][]input3DData=processOneToThreeDeminsion(imagearr,image.getWidth(),image.getHeight());		
					
					float rowratio=(float)image.getWidth()/(float)newwidth;		//行比例
					float colratio=(float)image.getHeight()/(float)newheight;	//列比例
					
					for(int row=0;row<newheight;row++){				//双线性插值算法
						
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
					//调用convertToDeminsionData函数
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

		private int[]convertToDeminsionData(int[][][]data,int imgCols,int imgRows)			//转换三维数组为一维数组的函数
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
		private double [][][]processOneToThreeDeminsion(int[] oneDPix2,int imgrows,int imgcols){		//转换一维数组为三维数组的函数
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



