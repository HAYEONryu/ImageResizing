package com.example.demo;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

	@RequestMapping("/")
	public String form() {
		return "form";
	}
	@GetMapping("/test")
	public String test() {
		return "test";
	}
	@PostMapping("/upload")
	// 업로드하는 파일들을 MultipartFile 형태의 파라미터로 전달된다.
	public String upload(@RequestParam(value = "width") int width, @RequestParam(value = "height") int height,
	@RequestParam MultipartFile[] uploadfile, Model model) throws IllegalStateException, IOException {
	    List<FileDto> list = new ArrayList<>();
        int newWidth = width;                                  // 변경 할 넓이
        int newHeight = height;                                 // 변경 할 높이
        String mainPosition = "W";                             // W:넓이중심, H:높이중심, X:설정한 수치로(비율무시)
 
        Image image;
        int imageWidth;
        int imageHeight;
        double ratio;
        int w;
        int h;
        long fileSize;
	   
	    for (MultipartFile file : uploadfile) {
	        if (!file.isEmpty()) {
	        	String imgFormat = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
	        	String fileName = null; 
	        	Calendar calendar = Calendar.getInstance(); 
	        	Date date = calendar.getTime(); 
	        	fileName = (new SimpleDateFormat("yyyyMMddHHmmss").format(date));
	        	fileName+="."+imgFormat;
	            String imgOriginalPath="/Users/hayeon/Documents/workspace-spring-tool-suite-4/filetest2/src/main/resources/static/direc/"+fileName;
	        	//yyyyMMddHHmmss 파일 이름을 만들어준다.
                File originalFile = new File(imgOriginalPath);
	            // 전달된 내용을 실제 물리적인 파일로 저장해준다.
	            File newFile = new File(fileName);
	            file.transferTo(newFile);
	           
	            try{
	                // 원본 이미지 가져오기
	                image = ImageIO.read(new File(imgOriginalPath));
	     
	                // 원본 이미지 사이즈 가져오기
	                imageWidth = image.getWidth(null);
	                imageHeight = image.getHeight(null);
	                //용량 확인 
	                fileSize = originalFile.length()/ 1024;
		            FileDto dto = new FileDto(Long.toString(fileSize)+ " kb", Integer.toString(imageWidth) +"/"+ Integer.toString(imageHeight), fileName, file.getContentType());
		            list.add(dto);
		            String imgTargetPath= "/Users/hayeon/Documents/workspace-spring-tool-suite-4/filetest2/src/main/resources/static/direc/"+"resized_"+ dto.getFileName();
	                File targetFile = new File(imgTargetPath);

	                if(mainPosition.equals("W")){    // 넓이기준
	     
	                    ratio = (double)newWidth/(double)imageWidth;
	                    w = (int)(imageWidth * ratio);
	                    h = (int)(imageHeight * ratio);
	     
	                }else if(mainPosition.equals("H")){ // 높이기준
	     
	                    ratio = (double)newHeight/(double)imageHeight;
	                    w = (int)(imageWidth * ratio);
	                    h = (int)(imageHeight * ratio);
	     
	                }else{ //설정값 (비율무시)
	     
	                    w = newWidth;
	                    h = newHeight;
	                }
	     
	                // 이미지 리사이즈
	                // Image.SCALE_DEFAULT : 기본 이미지 스케일링 알고리즘 사용
	                // Image.SCALE_FAST    : 이미지 부드러움보다 속도 우선
	                // Image.SCALE_REPLICATE : ReplicateScaleFilter 클래스로 구체화 된 이미지 크기 조절 알고리즘
	                // Image.SCALE_SMOOTH  : 속도보다 이미지 부드러움을 우선
	                // Image.SCALE_AREA_AVERAGING  : 평균 알고리즘 사용
	                Image resizeImage = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
	     
	                // 새 이미지  저장하기
	                BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	                Graphics g = newImage.getGraphics();
	                g.drawImage(resizeImage, 0, 0, null);
	                g.dispose();
	                ImageIO.write(newImage, imgFormat, targetFile);
	               //용량 확인 
	                fileSize = targetFile.length()/ 1024;
	                System.out.println("fileSize 확인 "+fileSize);
	                FileDto resizedDto = new FileDto(Long.toString(fileSize)+ " kb",w+"/"+h,"resized_"+fileName, file.getContentType());
		            list.add(resizedDto);
	     
	            }catch (Exception e){
	     
	                e.printStackTrace();
	     
	            }

	            
	        }          
	     }
	    model.addAttribute("files", list);
	    return "result";
	}
}


