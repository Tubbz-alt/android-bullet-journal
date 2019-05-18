package ua.deti.bulletjounal;

public class HubItem {
    private String itemName;

    public HubItem(String itemName)
    {
        this.itemName = itemName;
    }

    public String getItemName() { return itemName; }

    public void changeName(String s)
    {
        itemName = s;
    }

    @Override
    public String toString() { return itemName + "\n"; }


}
