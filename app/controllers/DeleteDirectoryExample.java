package controllers;

/**
 * Created by Aladinne on 16/05/2015.
 */


        import java.io.File;
        import java.io.IOException;

public class DeleteDirectoryExample
{
  //  private static final String SRC_FOLDER = "C:\\mkyong-new";

    public void deleteFolder( String SRC_FOLDER)
    {

        File directory = new File(SRC_FOLDER);

        //make sure directory exists
        if(!directory.exists()){

            System.out.println("Directory does not exist.");
            System.out.println(SRC_FOLDER);


        }else{

            try{

                delete(directory);

            }catch(IOException e){
                e.printStackTrace();

            }
        }

        System.out.println("Done");
    }

    public static void delete(File file)
            throws IOException{

        if(file.isDirectory()){

            //directory is empty, then delete it
            if(file.list().length==0){

                file.delete();
                System.out.println("Directory is deleted : "
                        + file.getAbsolutePath());

            }else{

                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    delete(fileDelete);
                }

                //check the directory again, if empty then delete it
                if(file.list().length==0){
                    file.delete();
                    System.out.println("Directory is deleted : "
                            + file.getAbsolutePath());
                }
            }

        }else{
            //if file, then delete it
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }
}