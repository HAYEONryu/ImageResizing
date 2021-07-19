package com.example.demo;


public class FileDto {
	private String fileSize;	//이미지 용량 
	private String fileWH;
	private String fileName;	// 실제 파일 이름
	private String contentType;

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileWH() {
		return fileWH;
	}

	public void setFileWH(String fileWH) {
		this.fileWH = fileWH;
	}

    
    public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

    
    public FileDto() {}

    public FileDto(String fileSize,String fileWH,String fileName, String contentType) {
        this.fileSize = fileSize;
        this.fileWH = fileWH;
        this.fileName = fileName;
        this.contentType = contentType;
    }


 	// setter/getter는 생략
}
