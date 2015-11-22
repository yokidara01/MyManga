package models;

/**
 * Created by Aladinne on 19/02/2015.
 */


import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
@Entity

public class Manga {

    @Id
    public int id ;
    public  String titre;

    public String auteur;

    public  String releasedate;

    public  String artiste;

    public String genre;

    public String sommaire;

    public String statue;
    public int id_user ;
    public String temp ;
    public int nbrchap;
    public int nbr_note;
    public int somme_note;
    public double note;
    public String avatar ;
    public static Model.Finder<Integer,Manga > find = new Model.Finder<Integer, Manga>(Integer.class,Manga.class);
    @Override
    public String toString() {
        return "Manga{" +
                "titre='" + titre + '\'' +
                ", auteur='" + auteur + '\'' +
                ", releasedate='" + releasedate + '\'' +
                ", artiste='" + artiste + '\'' +
                ", genre='" + genre + '\'' +
                ", sommaire='" + sommaire + '\'' +
                ", statue='" + statue + '\'' +
                ", nbrchap=" + nbrchap +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getArtiste() {
        return artiste;
    }

    public void setArtiste(String artiste) {
        this.artiste = artiste;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSommaire() {
        return sommaire;
    }

    public void setSommaire(String sommaire) {
        this.sommaire = sommaire;
    }

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }

    public int getNbrchap() {
        return nbrchap;
    }

    public void setNbrchap(int nbrchap) {
        this.nbrchap = nbrchap;
    }

    public Double getNote() {
        return note;
    }

    public void setNote(Double note) {
        this.note = note;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }



    public int isNoted (Membre user,Manga m)
    {int i =0;
        int k =-1;
        if (user==null)
        {
            return -1;
        }

        if(user.manga_noted.length()==0)
        {
            k=-1;
        }else
        {
            String noted[] = user.manga_noted.split(",");
            for (String n :noted)
            {
                if(n.equals(m.id+""))
                {System.out.println("there is match");
                    i++ ;
                    return 1;
                }
            }
        }


    System.out.println("i==" + i);
        if(i>0)
        {
            return 1;
        }else
            return -1;
    }
}