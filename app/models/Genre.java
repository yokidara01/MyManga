package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Aladinne on 19/02/2015.
 */
@Entity
public class Genre extends Model {
    @Id
     public String genre ;
    public String node ;

    public static Model.Finder<String,Genre > find = new Model.Finder<String, Genre>(String.class,Genre.class);
}
