package ua.deti.bulletjounal;

public class Item {
    private int mImageType;
    private String description;
    private String title;
    public Item(int type, String description, String title){
        this.description=description;
        this.mImageType=type;
        this.title=title;
    }

    public int getType() {
        return mImageType;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }


}
