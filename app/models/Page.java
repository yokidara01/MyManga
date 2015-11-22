package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Aladinne on 02/03/2015.
 */
@Entity
public class Page {
    @Id
    public int id ;
    public int id_manga;

    public int nbr_chapter;
    public String path ;
    public static Model.Finder<Integer,Page > find = new Model.Finder<Integer,Page>(Integer.class,Page.class);
}
