package aregmi.ramapo.edu.storyshare;

public class SingleAvailableStoryOption {

    private String author_name;
    private String story_summary;

    public SingleAvailableStoryOption(String author_name, String story_summary){
        this.author_name = author_name;
        this.story_summary = story_summary;
    }

    public String getStorySummary(){
        return story_summary;
    }

    public void setStorySummary(String story_summary){
        this.story_summary = story_summary;
    }

    public String getAuthorName(){
        return author_name;
    }

    public void setAuthorName(String author_name){
        this.author_name = author_name;
    }

    @Override
    public String toString(){
        return "Author Name: "+ author_name+ "\n" + "Story Summary: "+story_summary;
    }



}
