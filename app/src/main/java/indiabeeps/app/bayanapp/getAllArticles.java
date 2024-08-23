package indiabeeps.app.bayanapp;

public class getAllArticles {

    public String description;
    public String name;
    public String id;
    public String modified;
    public String category;
    public String fav;
    public String catid;



    public getAllArticles(){}

    public getAllArticles(String id, String name, String description, String fav, String category, String modified,String catid) {
        super();

        this.catid = catid;
        this.id = id;
        this.name = name;
        this.description= description;
        this.fav = fav;
        this.category = category;
        this.modified= modified;
    }

    /**
     * @return the Article Name
     */
    public String getArticleName() {
        return this.name;
    }


}