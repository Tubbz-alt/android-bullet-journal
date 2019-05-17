package ua.deti.bulletjounal;

public class Item {
    private int mImageType;
    private String description;
    private String title;
    private  String type;
    private boolean show;
    public Item(int type, String description, String title,String type_str,boolean show){
        this.description=description;
        this.mImageType=type;
        this.title=title;
        this.type=type_str;
        this.show=show;
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

    public boolean getShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    @Override
    public String toString() {
        return getStringType()+"-"+getTitle()+"-"+getDescription()+"\n";
    }
}
