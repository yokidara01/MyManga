package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Aladinne on 27/02/2015.
 */
@Entity
public class Chapter {
    @Id
    public int id;
    public int id_manga ;
    public int nbr_chapter ;
    public int user_id ;
    public String temp ;
    public String titre ;

    public static Model.Finder<Integer,Chapter > find = new Model.Finder<Integer, Chapter>(Integer.class,Chapter.class);
}
