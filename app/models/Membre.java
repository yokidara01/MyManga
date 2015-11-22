package models;

import play.db.ebean.Model;

/**
 * Created by Aladinne on 16/02/2015.
 */

import java.util.List;

import javax.persistence.Entity;
import  javax.persistence.Id;


import play.db.ebean.Model;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
@Entity
public class Membre extends Model{

    @Id

    public int id ;
    public String avatar;
    public String genrefavorie;
    public  String nom ;
    public  String pseudo ;
    public  String email ;
    public  String mdp ;
    public  int rang ;
    public String manga_noted ;
    public static Model.Finder<Integer,Membre > find = new Model.Finder<Integer, Membre>(Integer.class,Membre.class);
    public Membre() {
    }


    public static Membre authenticate(String ps, String password) {
        return find.where()
                .eq("pseudo", ps)
                .eq("mdp", password)
                .findUnique();
    }



}
