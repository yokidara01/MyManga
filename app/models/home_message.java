package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Aladinne on 07/04/2015.
 */
@Entity
public class home_message extends Model {
@Id
public int id ;
    public String message;
    public String titre ;
    public static Model.Finder<String,home_message> find = new Model.Finder<String,home_message>(String.class,home_message.class);
}
