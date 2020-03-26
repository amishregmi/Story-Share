package aregmi.ramapo.edu.storyshare;

public class Single_Available_Story_Item {
    private String author_name;
    private String author_description;

    public Single_Available_Story_Item(String author_name, String author_description){
        this.author_name = author_name;
        this.author_description = author_description;
    }


    public String getName() {
        return author_name;
    }

    public void setName(String author_name) {
        this.author_name = author_name;
    }

    public String getDescription() {
        return author_description;
    }

    public void setDescription(String author_description) {
        this.author_description = author_description;
    }

    @Override
    public String toString() {
        return "Name: "+author_name+" Description: "+author_description;
    }
}
