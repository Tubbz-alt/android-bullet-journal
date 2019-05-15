package ua.deti.bulletjounal;

public class Item_Yearly {

    private String day;
    private String title;
    private Boolean show;

    public Item_Yearly( String day, String title,boolean show){


        this.title=title;
        this.day=day;
        this.show=show;
    }



    public String getTitle() {
        return title;
    }

    public String getDay() {
        return day;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }
}
