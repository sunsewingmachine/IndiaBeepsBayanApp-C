package indiabeeps.app.bayanapp;

public class getAllCategory {

    public String name;
    public String modified;
    public String id;
    public String slug;



    public getAllCategory(){}

    public getAllCategory(String id, String name,String modified,String slug) {
        super();

        this.id = id;
        this.name = name;
        this.modified = modified;
        this.slug = slug;

    }


}