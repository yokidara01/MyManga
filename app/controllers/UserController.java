package controllers;


import com.avaje.ebean.Ebean;

import com.avaje.ebean.Expr;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import models.*;

import play.api.UsefulException;
import play.api.libs.iteratee.IterateeExeption;
import play.api.mvc.Security;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import scala.reflect.api.Exprs;
import views.html.*;
import views.html.home_message;

import java.beans.Expression;
import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Aladinne on 12/02/2015.
 */
public class UserController extends Controller{

    public static Result AddUser()
    {
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        String ps = values.get("username")[0];
        String mdp = values.get("password")[0];
        String email = values.get("email")[0];


     Membre m  = new Membre() ;

        m.pseudo=ps;
        m.email=email;
        m.mdp=mdp;
        m.manga_noted="";

        List<Membre> luser = Membre.find.all();
        for(Membre u:luser)
        {
            if(u.pseudo.equals(m.pseudo))
            {
                return ok(erreur.render("ce pseudo existe deja!"));
            }
            if(u.email.equals(m.email))
            {
                return ok(erreur.render("cette adresse E-mail existe deja!"));
            }
        }


        m.save();

        Membre user =new Membre();
        try {
            user=Membre.find.where().ilike("id",session("co")).findUnique();
        }catch (Exception e){}

        List<Manga> lm = Manga.find.all();

        List<Catfav> lcf = Catfav.find.where().ilike("id_user",m.id+"").findList();
        models.home_message msg = models.home_message.find.where().ilike("titre","%").findUnique();
        return ok(accueil.render(user,lm,lcf,msg));
    }







    @play.mvc.Security.Authenticated(Secured.class)
    public static Result logIn() {
        String mdp = Form.form().bindFromRequest().get("password");
        String un = Form.form().bindFromRequest().get("username");

        Membre m = new Membre();


        m = m.authenticate(un, mdp);

        if (m == null) {

            return ok(erreur.render("pseudo ou mot de pass incorrect"));
        } else {
            m=Membre.find.where().ilike("pseudo",un).ilike("mdp",mdp).findUnique();

            session("co",m.id+"");
            List<Manga> lm = Manga.find.where().ilike("temp","false").findList();
            List<Catfav> lcf = Catfav.find.where().ilike("id_user",m.id+"").findList();
            models.home_message msg = models.home_message.find.where().ilike("titre","%").findUnique();

            return ok(message.render(m,lcf,"Bienvenu "+m.pseudo)) ;
            //return ok(accueil.render(m,lm,lcf,msg));
        }
    }

    public static Result Disco()
    {
        session().clear();
        return redirect("/");
    }

    public static  Result submitComment()
    {  final Map<String, String[]> values = request().body().asFormUrlEncoded();
        String ch = values.get("cmnt")[0];
        int id_manga = Integer.parseInt(values.get("id")[0]);

        Membre user =new Membre();
        try {
            user= Membre.find.where().ilike("id",session("co")).findUnique();

        }catch (Exception e){return  ok(erreur.render("connecter vous")); };


        Avis cmnt = new Avis();
        cmnt.invisible="false";
        cmnt.cmnt=ch;
        cmnt.target=id_manga;
        cmnt.user=user.id;
        cmnt.user_name=user.pseudo;
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy.MM.dd" );
//récupération de la date courante
        Date currentTime_1 = new Date();
//on crée la chaîne à partir de la date
        String dateString = formatter.format(currentTime_1);
        cmnt.date= dateString;

        Ebean.save(cmnt);


           return redirect("/Manga/"+id_manga);


    }



    public static Result saveCatFav()
    {
       final Map<String, String[]> values = request().body().asFormUrlEncoded();
        String g ="";
        ArrayList<Catfav> lcf =new ArrayList<Catfav>();
        Membre user =new Membre();
        try {
            user= Membre.find.where().ilike("id",session("co")).findUnique();

        }catch (Exception e){return  ok(erreur.render("")); };
        for (int i=0;i<values.get("genre").length;i++)
        {
            Catfav cf = new Catfav();
            cf.genre=values.get("genre")[i];
            cf.id_user= user.id;
            lcf.add(cf);

        }
        List<Catfav> oldlcf = Catfav.find.where().ilike("id_user",user.id+"").findList();
        Ebean.delete(oldlcf);
        Ebean.save(lcf);
        List<Genre> lG = Genre.find.all();


//return ok(lnode.get(1));
        List<Bookmark> lbm = Bookmark.find.where().ilike("membre",user.id+"").findList();
        return ok(profil.render(user, lG,lcf,lbm));
}


    public static  Result UpdateProfil()
    {
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        String ps = values.get("Pseudo")[0];
        String mdp = values.get("mdp")[0];
        String email = values.get("email")[0];




        Membre user =new Membre();
        try {
            user=Membre.find.where().ilike("id",session("co")).findUnique();
            user.pseudo=ps;
            user.email=email;
            user.mdp=mdp;
            user.save();
        }catch (Exception e){}

        List<Genre> lG = Genre.find.all();
        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();

        List<Bookmark> lbm = Bookmark.find.where().ilike("membre",user.id+"").findList();
        return ok(profil.render(user, lG,lcf,lbm));
    }


    public static Result advSrch(){
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        String ch = values.get("ch")[0];
        String statue = values.get("statue")[0];
String g="";
        if (values.get("genre")!=null) {
            for (int i = 0; i < values.get("genre").length; i++) {
                g += values.get("genre")[i] + ",";
            }
        }

      //  List<Manga>lm = Manga.find.where().ilike("titre","%"+ch+"%").ilike("genre","%"+g+"%").ilike("auteur","%"+ch+"%").findList();
        Expr expr ;
        List<Manga>lm = Manga.find.where().or(
                com.avaje.ebean.Expr.like("titre","%"+ch + "%"),
                com.avaje.ebean.Expr.like("auteur", "%"+ch + "%")
              ).
                ilike("genre","%"+g+"%").ilike("statue",statue).
                ilike("temp","false")
                .findList();


        Membre user = new Membre();
        try {
            user = Membre.find.where().ilike("id",session("co")).findUnique();

       }catch (Exception e)
       {

       }
        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();
        return ok(search.render(user, lm,lcf));
    }
    public static Result renderUserPanel()
    { Membre user= new Membre();
        try {

            user = Membre.find.byId(Integer.parseInt(session("co")));

        }catch (Exception e){

        }

        if (session("co") ==null)
        {

            return  ok(erreur.render("veuillez connecter pour participer a l'alimentation de site"));
        }
        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();
        return ok(UserPanel.render(user,lcf));
    }


    public static Result renderUserAddManga()
    { Membre user= new Membre();
         List<Genre> lg = Genre.find.all() ;
        try {
           user = Membre.find.byId(Integer.parseInt(session("co")));
        }catch (Exception e){}
        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();
        return ok(userAddManga.render(user,lg,lcf)) ;


    }

    public static Result renderUserAddChapter()
    { Membre user= null;
        List<Manga> lm = Manga.find.all() ;
        try {
          user = Membre.find.byId(Integer.parseInt(session("co")));
        }catch (Exception e){}
        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();
        return ok(userAddChapter.render(user,lm,lcf)) ;


    }


    public static  Result userAddManga()
    {
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        Manga m = new Manga () ;
        m.temp = "true" ;
        m.id_user=Integer.parseInt(session("co"));
        m.setTitre(values.get("titre")[0]);
        m.setAuteur(values.get("auteur")[0]);
        m.setReleasedate(values.get("releasedate")[0]);
        m.setArtiste(values.get("artiste")[0]);
        String g="";
        for (int i=0;i<values.get("genre[]").length;i++)
        {
            g+=values.get("genre[]")[i]+",";
        }
        m.setGenre(g);


        m.setSommaire(values.get("sommaire")[0]);
        m.setStatue(values.get("statue")[0]);
        m.setNbrchap(Integer.parseInt(values.get("nbrchap")[0]));


/*
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart picture = body.getFile("picture");
        if (picture != null) {
            String fileName = picture.getFilename();
            String contentType = picture.getContentType();
            File file = picture.getFile();
            return ok("File uploaded");
        } else {
            flash("error", "Missing file");
            return redirect("fail");
        }

*/
        Ebean.save(m);
        File fb = new File("public\\images\\Mangas\\"+m.titre);
        fb.mkdirs();
        m=Manga.find.where().ilike("titre",m.getTitre()).findUnique();
        session().put("idManga",m.getId()+"");


        Membre user= new Membre();

        try {
            user = Membre.find.byId(Integer.parseInt(session("co")));
        }catch (Exception e){}
        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();
        return ok(tests.render(user,lcf)) ;
    }



    public  static  Result userAddChapter()
    {
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        Chapter chptr = new Chapter() ;

        chptr.user_id=Integer.parseInt(session("co")) ;
        chptr.temp="true" ;
        chptr.nbr_chapter=Integer.parseInt(values.get("nbr")[0]);
        chptr.titre=values.get("titre")[0];
        chptr.id_manga=Integer.parseInt(values.get("manga")[0]);
        String mangaName = Manga.find.byId(chptr.id_manga).titre ;

        File fb = new File("public/images/Mangas/"+mangaName+"/"+chptr.nbr_chapter);
        fb.mkdirs();
        Ebean.save(chptr);
        Membre user =new Membre();
        try {
            user= Membre.find.where().ilike("id",session("co")).findUnique();
        }catch (Exception e){ };
        Manga m= Manga.find.byId(Manga.find.byId(chptr.id_manga).id) ;
        session().remove("manga_id");
        session().remove("chapter_nbr");

        session().put("manga_id",chptr.id_manga+"");
        session().put("chapter_nbr",chptr.nbr_chapter+"");

        System.out.println(session().get("chapter_nbr")+"***********************"+session().get("manga_id"));
        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();
        return ok(UploadChapter.render(user,m,chptr.id,lcf)) ;
    }
    public static Result checkManga(int id)
    {
        Membre user= new Membre();
        Manga m = Manga.find.byId(id) ;
        try {
            user = Membre.find.byId(Integer.parseInt(session("co")));
        }catch (Exception e){}

        return ok(checkManga.render(user,m));
    }

    public static Result addCat()
    { List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
        List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();
        Membre user= new Membre();
        try {
            user = Membre.find.byId(Integer.parseInt(session("co")));
        }catch (Exception e){}


        return  ok(addCat.render(user,lam,lac));

    }


    public static  Result SaveCat()
    {
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        String cat = values.get("genre")[0];

        int i  = Genre.find.findRowCount();
        Genre g = new Genre() ;
        g.genre=cat ;
        g.node="node"+i;
        Ebean.save(g);











        int c , m , u , b ;
        b=0 ;
        c=Avis.find.all().size();
        m=Manga.find.all().size();
        u=Membre.find.all().size();
        Membre user =new Membre();
        try {
            user=Membre.find.where().ilike("id",session("co")).findUnique();
            if (user.rang==0)
            {
                return ok(erreur.render("connecter vous"));
            }
        }catch (Exception e){}

        List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
        List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();

        return  ok(ADMIN.render(user,c,m,u,b,lam,lac));
    }


    public static  Result renderdeleteCat()
    {







List<Genre> lg =Genre.find.all();





        List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
        List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();
        Membre user= new Membre();
        try {
            user = Membre.find.byId(Integer.parseInt(session("co")));
        }catch (Exception e){}


        return  ok(supprimerCategorie.render(user, lam, lac, lg));
    }

    public static  Result deleteCat()
    {
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        String cat = values.get("genre")[0];

        Genre g = Genre.find.where().ilike("genre",cat).findUnique();
        Ebean.delete(g);












        int c , m , u , b ;
        b=0 ;
        c=Avis.find.all().size();
        m=Manga.find.all().size();
        u=Membre.find.all().size();
        Membre user =new Membre();
        try {
            user=Membre.find.where().ilike("id",session("co")).findUnique();
            if (user.rang==0)
            {
                return ok(erreur.render("connecter vous"));
            }
        }catch (Exception e){}

        List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
        List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();

        return  ok(ADMIN.render(user,c,m,u,b,lam,lac));
    }



    public static Result setInvisibleAvis()
    {
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        String idmanga = values.get("id_manga")[0];
        String idavis = values.get("id_avis")[0];

        int id= Integer.parseInt(idmanga);


        Avis a = Avis.find.byId(Integer.parseInt(idavis));
        if (a.invisible.equals("false")) {
            a.invisible = "true";
        }else
        {
            a.invisible = "false";
        }

        Ebean.save(a);







        Manga m = new Manga();
        try {
            m= Manga.find.byId(id) ;
        }catch (UsefulException e){return ok("Manga not found");}

        Membre user =new Membre();
        try {
            user= Membre.find.where().ilike("id",session("co")).findUnique();
        }catch (Exception e){ };

        List <Chapter> lChapter ;
        lChapter=Chapter.find.where().ilike("id_manga",id+"").ilike("temp","false").findList();

        List<Avis> lAvis = Avis.find.where().ilike("target",id+"").ilike("invisible","false").findList();
        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();
        int k = m.isNoted(user,m);
        return ok(detailsManga.render(user,m,lChapter,lAvis,lcf,k));
    }



    public static  Result searchUser()

    { Membre user =new Membre();
        try {
            user= Membre.find.where().ilike("id",session("co")).findUnique();
        }catch (Exception e){ };
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        String ch = values.get("ch")[0];

        List<Membre> lm = Membre.find.where().ilike("pseudo","%"+ch+"%").findList();
        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();
        return ok(ResultSearchUser.render(user,lm,lcf)) ;
    }

    public static  Result mp(int id)
    {Membre user =new Membre();
        try {
            user= Membre.find.where().ilike("id",session("co")).findUnique();
        }catch (Exception e){ };
        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();
        return ok(mp.render(user,id,lcf)) ;

    }

    public static  Result sendMp()
    {Membre user =new Membre();
        try {
            user= Membre.find.where().ilike("id",session("co")).findUnique();
        }catch (Exception e){ };
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        String ch = values.get("ch")[0];
        String id = values.get("id")[0];

        Mp mp = new Mp();
        mp.ch=ch ;
        mp.from_id=user.id;
        mp.from_name=user.pseudo;
        mp.target=Integer.parseInt(id);
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy.MM.dd" );
//récupération de la date courante
        Date currentTime_1 = new Date();
//on crée la chaîne à partir de la date
        String dateString = formatter.format(currentTime_1);
        mp.date= dateString;

        Ebean.save(mp);

        List<Manga> lm = Manga.find.where().ilike("temp","false").findList();

        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();


        models.home_message msg = models.home_message.find.where().ilike("titre","%").findUnique();
        return ok(accueil.render(user,lm,lcf,msg));

    }

    public static  Result BR()
    {Membre user =new Membre();
        try {
            user= Membre.find.where().ilike("id",session("co")).findUnique();
        }catch (Exception e){ };

        List<Mp> lmp = Mp.find.where().ilike("target",user.id+"").findList();
        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();
        return ok(BoiteReception.render(user,lmp,lcf)) ;

    }


    public static  Result checkChapter (int id)
    {
        Membre user =new Membre();
        try {
            user= Membre.find.where().ilike("id",session("co")).findUnique();
        }catch (Exception e){ };

        Chapter c =Chapter.find.byId(id);

        List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
        List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();

        return ok(checkChapter.render(user,lam,lac,c));
    }
    public static  Result mdfChapter()
    {  final Map<String, String[]> values = request().body().asFormUrlEncoded();
        Chapter chptr  ;
        chptr = Chapter.find.byId(Integer.parseInt(values.get("id")[0])) ;
        chptr.nbr_chapter=Integer.parseInt(values.get("nbr_chapter")[0]);
        chptr.titre=values.get("titre")[0];
        chptr.temp="false";
        Ebean.save(chptr);


        int c , m , u , b ;
        b=0 ;
        c=Avis.find.all().size();
        m=Manga.find.all().size();
        u=Membre.find.all().size();
        Membre user =new Membre();
        try {
            user=Membre.find.where().ilike("id",session("co")).findUnique();
            if (user.rang==0)
            {
                return ok(erreur.render("connecter vous"));
            }
        }catch (Exception e){}

        List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
        List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();

        return  ok(ADMIN.render(user,c,m,u,b,lam,lac));


    }


    public static Result SelectTodeleteChapter()
    {
        List<Chapter> lc = Chapter.find.all();
        List<Manga> lm = Manga.find.all();


        Membre user =new Membre();
        try {
            user=Membre.find.where().ilike("id",session("co")).findUnique();
            if (user.rang==0)
            {
                return ok(erreur.render("connecter vous "));
            }
        }catch (Exception e){}

        List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
        List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();
        return ok(SelectTodeleteChapter.render(user,lam,lac,lc,lm)) ;
    }
    public static  Result deleteChapter(int id)
    {
        Chapter c = Chapter.find.byId(id);
        Manga mangatemp = Manga.find.byId(c.id_manga);
        String dir ="public\\images\\Mangas\\"+mangatemp.titre+"\\"+c.nbr_chapter ;
        DeleteDirectoryExample delt = new DeleteDirectoryExample() ;
        try {
            delt.deleteFolder(dir);

            Ebean.delete(c);

                }catch (Exception es)
        {

        }



        int cc , m , u , b ;
        b=0 ;
        cc=Avis.find.all().size();
        m=Manga.find.all().size();
        u=Membre.find.all().size();
        Membre user =new Membre();
        try {
            user=Membre.find.where().ilike("id",session("co")).findUnique();
            if (user.rang==0)
            {
                return ok(erreur.render("connecter vous"));
            }
        }catch (Exception e){}

        List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
        List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();

        return  ok(ADMIN.render(user,cc,m,u,b,lam,lac));

    }

    public static  Result afficheGenre(String genre)
    {

        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        List<Manga>lm = Manga.find.where().ilike("genre","%"+genre+"%").ilike("temp","%"+"false"+"%").findList();

        Membre user = new Membre();
        try {
            user = Membre.find.where().ilike("id",session("co")).findUnique();

        }catch (Exception e)
        {

        }
        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();
        return ok(AfficheGenre.render(user, lm,lcf));
    }



   public static  Result  suggestion()
   {String gu="" ;
       Manga m2 ;


       Membre user =new Membre();
       try {
           user= Membre.find.where().ilike("id",session("co")).findUnique();
       }catch (Exception e){ };

       List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();

       for (Catfav cf :lcf)
        {
            gu+=cf.genre+",";
            System.out.println("-------" + gu);}

      List <Manga> match_manga = Manga.find.where().like("genre","%"+gu+"%").findList();
      // List <Manga> match_manga = Manga.find.where().ilike("temp", "false").between("note", 50, 100).like("genre","%"+gu+"%").findList();
       System.out.println("-------" + match_manga.size());

    m2=match_manga.get(0);



       List <Chapter> lChapter ;
    lChapter=Chapter.find.where().ilike("id_manga",m2.id+"").findList();

   List<Avis> lAvis = Avis.find.where().ilike("target",m2.id+"").findList();

      return ok(suggestion.render(user, m2, lChapter, lAvis, lcf));
      // return  ok(match_manga.size()+"")  ;

   }

    public static  Result bookmark (int id )
    {
        Bookmark bm = new Bookmark() ;
        bm.titre_manga = Manga.find.byId(id).titre;
        bm.manga=id ;
        Membre user =new Membre();

        if(session("co")==null)
        {
            return ok(erreur.render("veuillez connecter pour marquer cette manga")) ;
        }


        try {
            user= Membre.find.where().ilike("id",session("co")).findUnique();
        }catch (Exception e){ };
        bm.membre= user.id ;

        Bookmark b =Bookmark.find.where().ilike("membre",user.id+"").ilike("manga",id+"").findUnique();
        if(b==null)
           Ebean.save(bm);

        List<Manga> lm = Manga.find.where().ilike("temp","false").findList();

        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();

        models.home_message msg = models.home_message.find.where().ilike("titre","%").findUnique();
        return ok(accueil.render(user,lm,lcf,msg));
    }

    public static  Result DeleteBookmark(int id)
    {Membre user =new Membre();
        try {
            user= Membre.find.where().ilike("id",session("co")).findUnique();
        }catch (Exception e){ };
        Bookmark bm = Bookmark.find.where().ilike("manga",id+"").ilike("membre",user.id+"").findUnique() ;
        Ebean.delete(bm);
        List<Genre> lG = Genre.find.all();
        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();
        List<Bookmark> lbm = Bookmark.find.where().ilike("membre",user.id+"").findList();
        return ok(profil.render(user, lG,lcf,lbm));

    }


    public static  Result homeMessageRender()
    {

        models.home_message m = models.home_message.find.where().ilike("titre","%").findUnique();
        List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
        List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();
        Membre user= new Membre();
        try {
            user = Membre.find.byId(Integer.parseInt(session("co")));
        }catch (Exception e){}


        return  ok(home_message.render(user, lam, lac,m));
    }


    public static  Result SaveHomeMessage()
    {
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        String message = values.get("message")[0];
        String titre = values.get("titre")[0];

        models.home_message m = models.home_message.find.byId(1+"");
        m.message=message;
        m.titre=titre;
        Ebean.save(m);











        int c , m2 , u , b ;
        b=0 ;
        c=Avis.find.all().size();
        m2=Manga.find.all().size();
        u=Membre.find.all().size();
        Membre user =new Membre();
        try {
            user=Membre.find.where().ilike("id",session("co")).findUnique();
            if (user.rang==0)
            {
                return ok(erreur.render("connecter vous"));
            }
        }catch (Exception e){}

        List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
        List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();

        return  ok(ADMIN.render(user,c,m2,u,b,lam,lac));
    }

    public static  Result promouvoir()
    {  Membre user =new Membre();
        try {
            user=Membre.find.where().ilike("id",session("co")).findUnique();
            if (user.rang==0)
            {
                return ok(erreur.render("connecter vous"));
            }
        }catch (Exception e){}
        List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
        List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();

        return  ok(promotion.render(user,lam,lac));


    }
    public  static  Result searchForPromot()
    { final Map<String, String[]> values = request().body().asFormUrlEncoded();
        String ch = values.get("ch")[0];

        List<Membre> lm = Membre.find.where().ilike("pseudo","%"+ch+"%").findList();
        Membre user =new Membre();
        try {
            user=Membre.find.where().ilike("id",session("co")).findUnique();
            if (user.rang==0)
            {
                return ok(erreur.render("connecter vous"));
            }
        }catch (Exception e){}
        List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
        List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();
        return  ok(selectUserToPromot.render(user,lam,lac,lm)) ;
    }

    public static  Result promot(int id)
    {

        Membre mm = Membre.find.byId(id);
        Membre user =new Membre();
        try {
            user=Membre.find.where().ilike("id",session("co")).findUnique();
            if (user.id!=1)
            {
                return ok(erreur.render("que le super admin peut faire ça"));




            }
        }catch (Exception e){}


        mm.rang=1;
        Ebean.save(mm);




        int c , m2 , u , b ;
        b=0 ;
        c=Avis.find.all().size();
        m2=Manga.find.all().size();
        u=Membre.find.all().size();



        List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
        List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();

        return  ok(ADMIN.render(user,c,m2,u,b,lam,lac));


    }

}
