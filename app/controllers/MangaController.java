package controllers;

import com.avaje.ebean.Ebean;
import com.ning.http.multipart.FilePart;
import models.*;
import org.apache.commons.io.FileUtils;
import play.api.Logger;
import play.api.UsefulException;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.*;

import  org.apache.commons.*;
import views.html.defaultpages.error;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Aladinne on 19/02/2015.
 */
public class MangaController extends Controller {

    public static Result addManga()
    {
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        Manga m = new Manga () ;
        m.temp = "false" ;
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
        File fb = new File("public/Mangas/"+m.titre);
        fb.mkdirs();
            m=Manga.find.where().ilike("titre",m.getTitre()).findUnique();
            session().put("idManga",m.getId()+"");
        return redirect("/ajoutManga/AjoutCover" ) ;


    }
    public static  Result addCover()
    {

        Membre user =null;   List<Catfav> lcf =null;
        try {


        user=Membre.find.byId(Integer.parseInt(session("co")));
      lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();
    }catch (Exception e){}

        int i =Integer.parseInt(session().get("idManga"));
        session().remove("idManga");
        System.out.println("******"+i);

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart picture = body.getFile("pic");
        if (picture != null) {
            String fileName = picture.getFilename();
            String contentType = picture.getContentType();
            File file = picture.getFile();

            String UPLOAD_DIRECTORY = "public\\Mangas";


            try {
               FileUtils.moveFile(file, new File("public/images/Mangas", fileName));

            } catch (IOException ioe) {
                System.out.println("Problem operating on filesystem");
           }

                Manga m =Manga.find.byId(i);
                m.setAvatar(fileName);
            Ebean.save(m);
            response().setContentType("text/html");
            return ok(message.render(user, lcf, "Coverture charger ")) ;
        } else {
            flash("error", "Missing file");
            return redirect("fail");
        }

    }

    public static  Result showDetails(Integer id )
    {  Manga m = new Manga();
        try {
        m= Manga.find.byId(id) ;
    }catch (UsefulException e){return ok("Manga not found");}
        int k =0;
        Membre user =new Membre();
        try {
            user= Membre.find.where().ilike("id",session("co")).findUnique();
           k= m.isNoted(user,m);
        }catch (Exception e){ };

        List <Chapter> lChapter ;
        lChapter=Chapter.find.where().ilike("id_manga",id+"").ilike("temp","false").findList();

        List<Avis> lAvis = Avis.find.where().ilike("target",id+"").ilike("invisible","false").findList();
        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();

        System.out.println("isnoted return"+k);
        return ok(detailsManga.render(user,m,lChapter,lAvis,lcf,k));


    }

    public static Result  addChapter(){
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        Chapter chptr = new Chapter() ;

        chptr.user_id=Integer.parseInt(session("co")) ;
        chptr.temp="false" ;
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

        //List<Chapter> lc = Chapter.find.all();
        List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
        List<Manga> lm = Manga.find.all();
        List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();
        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();

      //  return ok(UploadChapter.render(user,m,chptr.id,lcf)) ;
        return  ok(AdminAddChapter.render(user,lam,lac));

    }



    public  static  Result UploadChapter() {




       Http.MultipartFormData md=request().body().asMultipartFormData();

        List<Http.MultipartFormData.FilePart> file;

        file=md.getFiles();
String ch ="";
        Page page ;
        int chapterNbr , manga_id;
        chapterNbr=Integer.parseInt(session().get("chapter_nbr"));
        manga_id=Integer.parseInt(session().get("manga_id"));
        Manga m = Manga .find.byId(manga_id);
        for(Http.MultipartFormData.FilePart p: file){
           ch+= p.getFilename();

            File f = p.getFile();
            String UPLOAD_DIRECTORY = "public\\Mangas\\";


            try {

                FileUtils.moveFile(f, new File("public/images/Mangas/"+m.titre+"/"+chapterNbr+"/", p.getFilename()));

                page= new Page();
                page.nbr_chapter=chapterNbr;
                page.id_manga=manga_id;
                page.path=p.getFilename();
                Ebean.save(page);

            } catch (IOException ioe) {
                System.out.println("Problem operating on filesystem");
                ioe.printStackTrace();
                session().remove("manga_id");
                session().remove("chapter_nbr");
            }
        }
        session().remove("manga_id");
        session().remove("chapter_nbr");
        response().setContentType("text/html");
        return redirect("/index");
    }


    public static Result ReadChapter(int idManga,int nbrChapter)
    {
        List<Page>LPage =Page.find.where().ilike("id_manga",idManga+"").ilike("nbr_chapter",nbrChapter+"").orderBy("path asc").findList();


      //  for (int i = 0; i < LPage.size(); i++) {
      //      Object o = LPage.get(i);
       //     if (LPage.contains(o))
        //        LPage.remove(i);
       // }


        Membre user =new Membre();
        try {
            user= Membre.find.where().ilike("id",session("co")).findUnique();
        }catch (Exception e){ };
        Manga m = Manga.find.byId(idManga) ;
        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();
        return ok(ReadChapter.render(user,m,LPage,lcf));
       // return ok(idManga+""+nbrChapter+"");
    }

    public static Result AlterManga()
    {
        List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
        List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();








        Membre user =new Membre();
        try {
            user= Membre.find.where().ilike("id",session("co")).findUnique();
        }catch (Exception e){ };
        List<Manga> lm = Manga.find.all();

        return  ok(AlterManga.render(user,lam,lac,lm)) ;
    }



    public static Result MangaSelectedToAlter( )
    {
        List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
        List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();


        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        int id = Integer.parseInt(values.get("id")[0]) ;

        Membre user =new Membre();
        try {
            user= Membre.find.where().ilike("id",session("co")).findUnique();
        }catch (Exception e){ };
        Manga m = Manga.find.byId(id);
        return  ok(ModifieManga.render(user,m,lam,lac));
    }

    public static Result MangaModifie()
    {
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        Manga m = new Manga () ;
m=Manga.find.byId(Integer.parseInt(values.get("id")[0])) ;
        m.setTitre(values.get("titre")[0]);
        m.setAuteur(values.get("auteur")[0]);
        m.temp="false";
        m.setArtiste(values.get("statue")[0]);
        m.setNbrchap(Integer.parseInt(values.get("nbrchap")[0]));




    Ebean.save(m);





        int c , m1 , u , b ;
        b=0 ;
        c=Avis.find.all().size();
        m1=Manga.find.all().size();
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

        return  ok(ADMIN.render(user,c,m1,u,b,lam,lac));
    }

    public static Result AlterMangaToDelete()
    {
        List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
        List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();









        Membre user =new Membre();
        try {
            user= Membre.find.where().ilike("id",session("co")).findUnique();
        }catch (Exception e){ };
        List<Manga> lm = Manga.find.all();
        return ok(SupprimerManga.render(user,lam,lac,lm));
    }

    public static Result DeleteManga()
    {
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        Manga m1 = new Manga () ;
        m1=Manga.find.byId(Integer.parseInt(values.get("id")[0])) ;


        String dir ="public\\images\\Mangas\\"+m1.titre ;
        DeleteDirectoryExample delt = new DeleteDirectoryExample() ;

        Ebean.delete(m1);
        delt.deleteFolder(dir);






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
                return ok(erreur.render("connecter vous en mode admin"));
            }
        }catch (Exception e){}

        List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
        List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();

        return  ok(ADMIN.render(user,c,m,u,b,lam,lac));
    }


    public static Result SelectChapterToAlter()
    {    Membre user =new Membre();
        try {
            user= Membre.find.where().ilike("id",session("co")).findUnique();
        }catch (Exception e){ };
        List<Manga> lm = Manga.find.all();

        List<Chapter> lc = Chapter.find.all();
        List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
        List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();

        return ok(SelectChapterToAlter.render(user,lam,lac,lm,lc)) ;
    }



public static  Result RendermodifieChapter(int id)
{Membre user =new Membre();

   // final Map<String, String[]> values = request().body().asFormUrlEncoded();


    Chapter c = Chapter.find.byId(id) ;
    //c.titre=values.get("titre")[0];
    //c.nbr_chapter=Integer.parseInt(values.get("nbr_chapter")[0]);
    //Ebean.save(c) ;
    try {
        user= Membre.find.where().ilike("id",session("co")).findUnique();
    }catch (Exception e){ };
    List<Manga> lam = Manga.find.where().ilike("temp","true").findList();
    List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();
    return  ok(modifieChapter.render(user,lam,lac,c));
}

    public static Result aleatoire()
    {   int id ;
        List<Manga> lm ;
        Manga m ;
        try {
            lm= Manga.find.all() ;
            Collections.shuffle(lm);
            m=lm.get(0);
            id=m.id;
        }catch (UsefulException e){return ok("Manga not found");}
        int k=0;
        Membre user =new Membre();
        try {
            user= Membre.find.where().ilike("id",session("co")).findUnique();
           k = m.isNoted(user, m);
        }catch (Exception e){ };

        List <Chapter> lChapter ;
        lChapter=Chapter.find.where().ilike("id_manga",id+"").findList();

        List<Avis> lAvis = Avis.find.where().ilike("target",id+"").findList();
        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();




       return ok(detailsManga.render(user,m,lChapter,lAvis,lcf,k));
        //return ok("");
    }

    public static  Result search()
    {
        String ch="";
        final Map<String, String[]> values = request().body().asFormUrlEncoded();

        ch=values.get("ch")[0] ;
        Membre user =new Membre();
        try {
            user=Membre.find.where().ilike("id",session("co")).findUnique();
        }catch (Exception e){}

        List<Manga> lm = Manga.find.where().ilike("titre","%"+ch+"%").ilike("temp","false").findList();
        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();
        return ok(search.render(user, lm,lcf));
    }


    public static Result note()
    {   final Map<String, String[]> values = request().body().asFormUrlEncoded();

       double  n=Double.parseDouble(values.get("hidden1")[0] );

        Manga m  = Manga.find.byId(Integer.parseInt(values.get("id")[0])) ;
       m.nbr_note++;
       m.somme_note+=n ;


        m.note=m.somme_note/m.nbr_note;

        m.note=round(m.note, 2) ;
        Ebean.save(m);

        Manga m2 = new Manga();
        try {
            m2= Manga.find.byId(Integer.parseInt(values.get("id")[0])) ;
        }catch (UsefulException e){return ok("Manga not found");}

        Membre user =new Membre();
        try {
            user= Membre.find.where().ilike("id",session("co")).findUnique();

        }catch (Exception e){ };
        user.manga_noted+=m2.id+",";
        Ebean.save(user);

        List <Chapter> lChapter ;
        lChapter=Chapter.find.where().ilike("id_manga",m2.id+"").findList();

        List<Avis> lAvis = Avis.find.where().ilike("target",m2.id+"").findList();
        List<Catfav> lcf = Catfav.find.where().ilike("id_user",user.id+"").findList();

        return ok(detailsManga.render(user,m2,lChapter,lAvis,lcf,1));

    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }



    public static Result deleteSuggestion(int id)
    {


        Manga m1  ;
        m1=Manga.find.byId(id) ;


        String dir ="public\\images\\Mangas\\"+m1.titre ;
        DeleteDirectoryExample delt = new DeleteDirectoryExample() ;

        Ebean.delete(m1);
        delt.deleteFolder(dir);
        return redirect("/ADMIN");
    }
public static Result renderAllPage(int idManga,int nbrChapter)
{
    List<Page> lp = Page.find.where().ilike("id_manga",idManga+"").ilike("nbr_chapter",nbrChapter+"").findList();
    Manga m = Manga.find.where().ilike("id",idManga+"").findUnique();




    List<Manga> lam = Manga.find.where().ilike("temp","true").findList();

    List<Chapter> lac = Chapter.find.where().ilike("temp","true").findList();
    Membre user =new Membre();
    try {
        user= Membre.find.where().ilike("id",session("co")).findUnique();

    }catch (Exception e){ };
    return ok(detailsChapitreCheck.render(user,lam,lac,lp,m));

}
}
