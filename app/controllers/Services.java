package controllers;


import models.Manga;

import play.mvc.*;

import play.libs.Json;

/**
 * Created by Aladinne on 22/05/2015.
 */
public class Services extends Controller {


   public static Result renderManga(int id )
   {
        Manga m = Manga.find.byId(id);

       if(m!=null){
           return   ok(Json.toJson(m));
       }else
           return null;
   }
}
