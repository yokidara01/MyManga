package controllers;

import com.avaje.ebean.PagingList;
import models.*;
import play.*;
import play.api.db.*;

import play.cache.Cache;
import play.cache.Cached;
import play.mvc.*;

import views.html.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.*;


public class Application extends Controller {


    public static Result accueilRender() {

        Membre user =new Membre();
        try {
            user=Membre.find.where().ilike("id",session("co")).findUnique();
        }catch (Exception e){}

        List<Manga> lm = Manga.find.where().ilike("temp","false").findList();

        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();

        models.home_message msg = models.home_message.find.where().ilike("titre","%").findUnique();
        return ok(accueil.render(user,lm,lcf,msg));


    }

    public static Result renderLogIn()
    {

        Membre user = new Membre();
        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();
        return ok(login.render(user,lcf)) ;
    }

    public static Result renderInsc()
    {Membre user = new Membre();
        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();
        return ok(inscription.render(user,lcf)) ;

    }

    public static  Result RenderProfile(){
        Membre user ;
        user=Membre.find.where().ilike("id",session("co")).findUnique();

        List<Genre> lG = Genre.find.all();

        List<String> lnode= new List<String>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<String> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(String s) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends String> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, Collection<? extends String> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public String get(int index) {
                return null;
            }

            @Override
            public String set(int index, String element) {
                return null;
            }

            @Override
            public void add(int index, String element) {

            }

            @Override
            public String remove(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<String> listIterator() {
                return null;
            }

            @Override
            public ListIterator<String> listIterator(int index) {
                return null;
            }

            @Override
            public List<String> subList(int fromIndex, int toIndex) {
                return null;
            }
        };


        List<Catfav> lgu = Catfav.find.where().ilike("id_user",user.id+"").findList();
        System.out.println(lG.size()+" G size");
        System.out.println("*****************************************************");
        for(Genre gx:lG)
        {
            System.out.println(gx.genre);




        }
        System.out.println("*****************************************************");
        for(Catfav gux:lgu)
        {
            System.out.println(gux.genre);




        }


        boolean k;

      /*  for(Genre g:lG) {
            System.out.println(g.genre+"**********");
            for(Catfav gu:lgu) {


                if ((g.genre).equals(gu.genre))
                {
                   k= lG.remove(g);
                    System.out.println(k  +"k");
                }
            }
        }

*/




        System.out.println(lG.size()+" G size after");
        for(int j = 1; j <= lG.size(); j++)
       {
        lnode.add("node"+j);
       }

//return ok(lnode.get(1));





        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();
        List<Bookmark> lbm = Bookmark.find.where().ilike("membre",user.id+"").findList();
        return ok(profil.render(user, lG,lcf,lbm));
    }




    public static Result RenderAdvSearch()
    {
        Membre user = new Membre();
        return ok(AdvSearch.render("aze"));
    }


    public static Result renderAddManga(){
        List<Genre> l = Genre.find.all();


        List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
        List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();
        Membre user= new Membre();
        try {
            user = Membre.find.byId(Integer.parseInt(session("co")));
        }catch (Exception e){}


        return  ok(addManga.render(user, lam, lac,l));


    }

public static Result renderAddCoverManga(){

    List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
    List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();
    Membre user= new Membre();
    try {
        user = Membre.find.byId(Integer.parseInt(session("co")));
    }catch (Exception e){}


    return  ok(addAvatarManga.render(user, lam, lac));





}

    public static Result RenderAddChapter(){


        List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
        List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();


        Membre user =new Membre();
        try {
            user=Membre.find.where().ilike("id",session("co")).findUnique();
        }catch (Exception e){}

        List<Manga> lm = Manga.find.all();
        return ok(AjouterChapitre.render(user, lam, lac,lm)) ;
    }

public static Result Rendererreur(){

        return ok(erreur.render(""));
    }

 public static Result   renderAdmin()
 {
     int c , m , u , b ;
     b=0 ;
     c=Avis.find.all().size();
     m=Manga.find.all().size();
    u=Membre.find.all().size();
     Membre user =null;
     try {
         user=Membre.find.where().ilike("id",session("co")).findUnique();
         if (user.rang==0)
         {
             return ok(erreur.render("il faut etre un administrateur"));
         }
     }catch (Exception e){}

     if (user==null)
     {
         return ok(erreur.render("il faut etre un administrateur"));
     }
     List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
     List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();

     return  ok(ADMIN.render(user,c,m,u,b,lam,lac));
 }


    public  static Result RenderRechercheAvancee()
    {   List<Genre> lG = Genre.find.all();
        Membre user = new Membre()   ;

        try {
            user=Membre.find.where().ilike("id",session("co")).findUnique();

        }catch (Exception e){}

        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();
     return ok(RechercheAvancee.render(user,lG,lcf));
    }


    public static  Result searchUser()
    { Membre user = new Membre()   ;

        try {
            user=Membre.find.where().ilike("id",session("co")).findUnique();

        }catch (Exception e){}
        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();
        return ok(searchUser.render(user,lcf));
    }



        public static  Result erreur (String ch  )
        {
            return ok(erreur.render(ch+" "+"introuvable vous ete perdu ? ")) ;
        }





}
