package ua.deti.bulletjounal;

public class Item_Yearly {
    private int mImageType;
    private String description;
    private String title;
    private  String type;
    public Item_Yearly(int type, String description, String title,String type_str){
        this.description=description;
        this.mImageType=type;
        this.title=title;
        this.type=type_str;
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

    public String getStringType() {
        return type;
    }

    @Override
    public String toString() {
        return getStringType()+"-"+getTitle()+"-"+getDescription()+"\n";
    }
}
