package fi.mamk.cloudfishare;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.IdGeneratorStrategy;

import com.google.appengine.api.datastore.Blob;

@PersistenceCapable
public class BinObject {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String name;
    
    @Persistent
    private String code;

    @Persistent
    Blob content;

    public BinObject() { }
    public BinObject(String name, String code, Blob content) {
        this.name = name; 
        this.code = code;
        this.content = content;
    }

    // JPA getters and setters and empty constructor
    // ...
    public Blob getContent()                { return content; }
    public void setContent(Blob content)    { this.content = content; }
    public String getName()                 { return name; }
    public String getCode()                 { return code; }
}
