package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Aladinne on 31/03/2015.
 */
@Entity
public class Mp extends Model {
@Id
    public int id ;
    public int target;
       public int from_id;
    public String ch ;
    public String from_name;
    public String date ;
    public static Model.Finder<Integer,Mp > find = new Model.Finder<Integer, Mp>(Integer.class,Mp.class);

    @Override
    public String toString() {
        return "Mp{" +
                "from_name='" + from_name + '\'' +
                ", id=" + id +
                ", target=" + target +
                ", from=" + from_id +
                ", ch='" + ch + '\'' +
                '}';
    }
}
