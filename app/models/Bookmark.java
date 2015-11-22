package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Aladinne on 07/04/2015.
 */
@Entity
public class Bookmark extends Model {
    @Id
    public int id ;
    public int membre ;
    public int manga ;
    public String titre_manga;
    public static Model.Finder<Integer,Bookmark > find = new Model.Finder<Integer, Bookmark>(Integer.class,Bookmark.class);
}
