function verifMail(champ)
{
	var str = champ.value;
var regexp = new RegExp("^[a-zA-Z0-9_\\-\\.]{3,}@[a-zA-Z0-9\\-_]{2,}\\.[a-zA-Z]{2,4}$", "g");

if
(!regexp.test(str)) {


return false;
}
return true;
}

function verifInscription()
{
    var nom = document.form.username.value;
    var email = document.form.email.value;
    var mdp = document.form.password.value;
    var c_mdp = document.form.password_confirm.value;

    if(nom=="")
    {
    alert("nom invalide ou vide")
    return false;
    }
    if ((email=="") && (verifMail(email) )   )
        {
        alert("E-mail invalide ou vide")
        return false;
        }
    if(mdp=="")
        {
        alert("mot de pass invalide ou vide")
        return false;
        }
    if(mdp!=c_mdp)
        {
        alert("mot de pass non identique")
        return false;
        }


return true ;
}

function testAddManga()
{


         var t = document.formManga.titre.value;
            var a = document.formManga.auteur.value;
            var d = document.formManga.releasedate.value;
            var art = document.formManga.artiste.value;
            var somm = document.formManga.sommaire.value;
            var statue= document.formManga.statue.value;
            var nbrchap  = document.formManga.nbrchap.value;

     if  (t=="")
        {
        alert("champ titre  vide")
        return false;
        }
         if(a=="")
            {
            alert("champ auteur vide")
            return false;
            }
             if(d=="")
                {
                alert("champ date vide")
                return false;
                }
                 if(art=="")
                    {
                    alert("champ artiste vide")
                    return false;
                    }
                     if(somm=="")
                        {
                        alert("champ sommaire vide")
                        return false;
                        }
                         if(statue=="")
                            {
                            alert("champ statue vide")
                            return false;
                            }
                             if(nbrchap=="")
                                {
                                alert("champ nombre de chapitre vide")
                                return false;
                                }
                                 if(isNaN(nbrchap))
                                {
                                alert("nombre invalide au champ : nombre des chapitres")
                                return false;
                                }

var genre = document.getElementsByName('genre[]');
var hasChecked = false;
for (var i = 0; i < genre.length; i++)
{
if (genre[i].checked)
{
hasChecked = true;
break;
}
}
if (hasChecked == false)
{
	alert("un ou plusieur genre doit etre selectionné");
	return false;
}






 return true

}

function testAddChapter()
{
  var nbr = document.addchapter.nbr.value;
            var titre = document.addchapter.titre.value;
            var manga = document.addchapter.manga.value;

              if(nbr=="")
                            {
                            alert("champ nombre de chapitre vide")
                            return false;
                            }

                             if(isNaN(nbr))
                                                            {
                                                            alert("nombre invalide au champ : nombre de chapitre")
                                                            return false;
                                                            }
                              if(titre=="")
                                            {
                                            alert("champ titre de chapitre vide")
                                            return false;
                                            }
                                              if(manga=="")
                                                            {
                                                            alert("selectionné un manga")
                                                            return false;
                                                            }

}