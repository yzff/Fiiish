package com.manyanger.entries;


/**
 * @ClassName: DetailInfo
 * @Description: TODO
 * @author Zephan.Yu
 * @date 2014-6-19 下午11:17:00
 */
public class ChapterItem {
    
    private int id;
    
    private String title;
    
    private int position;
    
    private boolean isPayed;
    
    
//    private int imageCount;
//    
//    private List<String> images;
    
    public ChapterItem() {        

    }
    
    /** 
     * <p>Title: </p> 
     * <p>Description: </p> 
     * @param i
     * @param string 
     */
//    public ChapterItem(int id, String title) {        
//        this.id = id;
//        this.title = title;
//    }

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

	public boolean isPayed() {
		return isPayed;
	}

	public void setPayed(boolean isPayed) {
		this.isPayed = isPayed;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
    

    /**
     * @return the images
     */
//    public List<String> getImages() {
//        return images;
//    }

    /**
     * @param images the images to set
     */
//    public void setImages(List<String> images) {
//        this.images = images;
//    }

}
