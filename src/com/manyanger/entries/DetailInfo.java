package com.manyanger.entries;

import java.util.List;

/**
 * @ClassName: DetailInfo
 * @Description: TODO
 * @author Zephan.Yu
 * @date 2014-6-20 下午10:36:00
 */
public class DetailInfo {
    
    private int id;
    
    private String title; //
    
    private String author; // 作者
    
    private int chapters; // 
    
    private String theme; // 题材
    
    private String process; // 连载状态
     
    private String depict; // 描述
    
    private String coverUrl; // 
    
    private List<ChapterItem> chapterList;
    
    private int bestFlag;
    
    private int processType;
    
    private int payedCount;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the chapters
     */
    public int getChapters() {
        return chapters;
    }

    /**
     * @param chapters the chapters to set
     */
    public void setChapters(int chapters) {
        this.chapters = chapters;
    }

//    public String getChapterString() {
//        StringBuilder s = new StringBuilder();
//        s.append(AppUtil.getString(R.string.update_to));
//        s.append(chapters);
//        s.append(AppUtil.getString(R.string.hua));
//        
//        return s.toString();
//    }
    /**
     * @return the theme
     */
    public String getTheme() {
        return theme;
    }

    /**
     * @param theme the theme to set
     */
    public void setTheme(String theme) {
        this.theme = theme;
    }

    /**
     * @return the process
     */
    public String getProcess() {
        return process;
    }

    /**
     * @param process the process to set
     */
    public void setProcess(String process) {
        this.process = process;
    }

    /**
     * @return the depict
     */
    public String getDepict() {
        return depict;
    }

    /**
     * @param depict the depict to set
     */
    public void setDepict(String depict) {
        this.depict = depict;
    }

    /**
     * @return the coverUrl
     */
    public String getCoverUrl() {
        return coverUrl;
    }
    
    public String getCoverIconKey() {
    	// todo: always has http://
       if(coverUrl != null && coverUrl.length() > "http://".length()){
    	   String keystr = coverUrl.substring("http://".length()+1);
    	   int pos = keystr.indexOf('/');
    	   if(pos > 0){
    		   keystr = keystr.substring(pos+1);
    	   }
    	   keystr = keystr.replace("/", "_");
    	   return keystr;
       }
        return "";
    }
    /**
     * @param coverUrl the coverUrl to set
     */
    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    /**
     * @return the chapterList
     */
    public List<ChapterItem> getChapterList() {
        return chapterList;
    }

    /**
     * @param chapterList the chapterList to set
     */
    public void setChapterList(List<ChapterItem> chapterList) {
        this.chapterList = chapterList;
    }

    /**
     * @return the isBest
     */
    public boolean isBest() {
        return this.bestFlag == 2;
    }

    /**
     * @param isBest the isBest to set
     */
    public void setBest(int flag) {
        this.bestFlag = flag;
    }

    public boolean isProcess(){
//        if(this.process == null){
//            return false;
//        }
//        if(this.process.equals("连载中")){
//            return true;
//        }
//        return false;
    	return this.processType == 1;
    }
    
    public boolean isLong(){
        return chapters > 6;
    }

	public void setProcessType(int processType) {
		this.processType = processType;
		
	}

	public int getPayedCount() {
		return payedCount;
	}

	public void setPayedCount(int payedCount) {
		this.payedCount = payedCount;
	}
	
	public void incPayedCount() {
		this.payedCount = this.payedCount + 1;
	}
	
	public boolean isAllChapterPayed() {
		return this.payedCount == this.chapters;
	}
}
