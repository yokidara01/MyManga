package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Aladinne on 21/03/2015.
 */
@Entity
public class Avis extends Model {
    @Id
    public int id ;
    public String cmnt ;
    public int target ;
    public int  user ;
    public String  user_name ;
    public String date ;
    public String invisible ;
    public static Model.Finder<Integer,Avis > find = new Model.Finder<Integer, Avis>(Integer.class,Avis.class);

}
