package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Aladinne on 30/03/2015.
 */
@Entity
public class Catfav extends Model {
    @Id
    public int id ;
    public int id_user ;

    public String genre ;
    public static Model.Finder<Integer, Catfav> find = new Model.Finder<Integer, Catfav>(Integer.class, Catfav.class);

}
