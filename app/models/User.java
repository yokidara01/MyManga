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
public class User extends Model{

@Id

    public int id ;
    public String avatar;
    public String genreFav;
    public  String nom ;
    public  String psudeo ;
    public  String email ;
    public  String mdp ;

    public static Model.Finder<Integer, User> find = new Model.Finder<Integer, User>(Integer.class,User.class);
    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGenreFav() {
        return genreFav;
    }

    public void setGenreFav(String genreFav) {
        this.genreFav = genreFav;
    }

    public String getPsudeo() {
        return psudeo;
    }

    public void setPsudeo(String psudeo) {
        this.psudeo = psudeo;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }
}
